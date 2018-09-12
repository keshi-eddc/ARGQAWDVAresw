package com.edmi;

import com.edmi.dao.icorating.ICO_icorating_funds_listRepository;
import com.edmi.dao.icorating.ICO_icorating_listRepository;
import com.edmi.entity.icorating.ICO_icorating_funds_list;
import com.edmi.entity.icorating.ICO_icorating_list;
import com.edmi.service.service.IcoratingService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IcoratingTest {
    Logger log = Logger.getLogger(IcoratingTest.class);
    @Autowired
    private IcoratingService icoratingService;
    @Autowired
    private ICO_icorating_listRepository listDao;
    @Autowired
    private ICO_icorating_funds_listRepository foundsListDao;

    @Test
    public void listTest() {
        log.info("************** start Test");
        icoratingService.getIcotatingList();

        Integer t = listDao.getMaxCrawledTimes();
        log.info("getMaxCrawledTimes========" + t);
        List<ICO_icorating_list> li = listDao.getMaxCurrentPageWithMaxCrawledTimes(t);
        ICO_icorating_list item = li.get(0);
        log.info("name:" + item.getName());
        log.info("CurrentPage:" + item.getCurrentPage());
    }

    @Test
    public void icotatingDetailManager() {
        log.info("******** start icotatingDetailManager test ");
        List<ICO_icorating_list> listItems = listDao.findTop10ByCrawledStatu("ini");
        log.info("get items num : " + listItems.size() + "  ,from list table");
        if (CollectionUtils.isNotEmpty(listItems)) {
            for (int i = 0; i < listItems.size(); i++) {
                ICO_icorating_list item = listItems.get(i);
//                String name = item.getName();
//                log.info("name:" + name);
                icoratingService.getIcoratingDetail(item);
            }
        } else {
            log.info("get null from list table");
        }
    }

    @Test
    public void getIcoratingFoundsList() {
        //268
        icoratingService.getIcoratingFundsList();
    }

    @Test
    public void icoratingFoundsDetailManager() {
        //查出所有的item，因为列表页已经判断，此处不会有重复
        List<ICO_icorating_funds_list> foundslist = new ArrayList<>();
        foundslist = foundsListDao.getAllByCrawledStatus("ini");
        //测试
//        ICO_icorating_funds_list one = foundsListDao.findICO_icorating_funds_listByLink("https://icorating.com/funds/iconiq-lab-fund/");
//        foundslist.add(one);
        log.info("********** Start icorating founds detail task **********");
        log.info("--- get from icorating_funds_list ,items num:" + foundslist.size());
        if (CollectionUtils.isNotEmpty(foundslist)) {
            for (int i = 0; i < foundslist.size(); i++) {
                ICO_icorating_funds_list foundsitem = foundslist.get(i);
//                log.info(foundsitem.getLink());
                if (StringUtils.isNotEmpty(foundsitem.getLink())) {
                    log.info("--- will extra  :" + i);
                    icoratingService.getIcoratingFoundDetail(foundsitem);
                }
            }
            log.info("********** icorating founds detail task over **********");
        } else {
            log.info("get null from list table");
        }

    }

}
