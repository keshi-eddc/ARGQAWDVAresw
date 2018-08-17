package com.edmi;

import com.edmi.dao.icorating.ICO_icorating_listRepository;
import com.edmi.entity.icorating.ICO_icorating_list;
import com.edmi.service.service.IcoratingService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IcoratingTest {
    Logger log = Logger.getLogger(IcoratingTest.class);
    @Autowired
    private IcoratingService icoratingService;
    @Autowired
    private ICO_icorating_listRepository listDao;

    @Test
    public void listTest() {
        log.info("start Test");
        icoratingService.getIcotatingList();

//        Integer t = listDao.getMaxCrawledTimes();
//        log.info("getMaxCrawledTimes========" + t);
//        List<ICO_icorating_list> li = listDao.getMaxCurrentPageWithMaxCrawledTimes(t);
//        ICO_icorating_list item = li.get(0);
//        log.info("name:"+item.getName());
//        log.info("CurrentPage:"+item.getCurrentPage());


    }

}
