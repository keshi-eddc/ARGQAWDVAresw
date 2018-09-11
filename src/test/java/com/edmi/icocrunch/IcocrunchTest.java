package com.edmi.icocrunch;

import com.edmi.entity.icocrunch.Ico_icocrunch_list;
import com.edmi.service.service.IcocrunchSevice;
import com.edmi.utils.http.exception.MethodNotSupportException;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IcocrunchTest {

    @Autowired
    private IcocrunchSevice icocrunchSevice;

    Logger log = Logger.getLogger(IcocrunchTest.class);

    @Test
    public void getIco_icocrunch_list() throws MethodNotSupportException {
        String[] shows = new String[]{"PreICO","ICO"};
        for(String show:shows){
            log.info("正在抓取show："+show+"类型的block数据");
            int totalPages = icocrunchSevice.getIco_icocrunch_list_total_pages(show);
            for(int i=1;i<=totalPages;i++){
                icocrunchSevice.getIco_icocrunch_list(show,i);
            }
        }
    }
    @Test
    public void getIco_icocrunch_detail() throws MethodNotSupportException {
        List<String> blockUrls = icocrunchSevice.getIco_icocrunch_listByDetailStatus("ini");
        for(String blockUrl:blockUrls){
            icocrunchSevice.getIco_icocrunch_detail(blockUrl);
            try {
                Thread.sleep(3*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
