package com.edmi;

import com.edmi.service.service.IcodropsService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

/**
 * 测试icodrops
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IcodropsTest {
    Logger log = Logger.getLogger(IcodropsTest.class);
    @Autowired
    private IcodropsService icodropsService;

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

}
