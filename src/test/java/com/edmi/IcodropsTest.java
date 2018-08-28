package com.edmi;

import com.edmi.dao.icodrops.ICO_icodrops_listRepository;
import com.edmi.entity.icodrops.ICO_icodrops_list;
import com.edmi.service.service.IcodropsService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试icodrops
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IcodropsTest {
    Logger log = Logger.getLogger(IcodropsTest.class);
    @Autowired
    private IcodropsService icodropsService;
    @Autowired
    private ICO_icodrops_listRepository itemDao;

    @Test
    public void icodropsListManager() {
        log.info("***** getIcodropsListWithInput task start");
        ArrayList<String> urlList = new ArrayList<>(10);
        urlList.add("https://icodrops.com/category/active-ico/");
        urlList.add("https://icodrops.com/category/upcoming-ico/");
        urlList.add("https://icodrops.com/category/ended-ico/");
        for (String url : urlList) {
            icodropsService.getIcodropsListWithInput(url);
        }
        log.info("***** getIcodropsListWithInput task over");
    }

    @Test
    public void icodropsDetailManager() {
        log.info("***** start icodropsDetailManager test *****");
        //1106
        List<ICO_icodrops_list> itemList = new ArrayList<>();
        itemList = itemDao.getAllByCrawledStatu("ini");
//        itemList = itemDao.getICO_icodrops_listByIco_url("https://icodrops.com/hashgraph/");
        if (CollectionUtils.isNotEmpty(itemList)) {
            log.info("--- total items is : " + itemList.size());
            for (int i = 0; i < itemList.size(); i++) {
                log.info("----- will extra item:" + i);
                ICO_icodrops_list item = itemList.get(i);
                icodropsService.getIcodropsDetail(item);
            }
            log.info("***** icodropsDetailManager task over *****");

        } else {
            log.info("--- get null ,from icodrops list tabel");
        }
    }

}
