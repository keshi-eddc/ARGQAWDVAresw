package com.edmi.coinschedule;

import com.edmi.service.service.CoinscheduleService;
import com.edmi.utils.http.exception.MethodNotSupportException;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CoinscheduleTest {

    @Autowired
    private CoinscheduleService coinscheduleService;

    Logger log = Logger.getLogger(CoinscheduleTest.class);

    @Test
    public void getList() throws MethodNotSupportException {
        coinscheduleService.getIco_coinschedule_List();

    }
    @Test
    public void getDetail() throws MethodNotSupportException {

    }

}
