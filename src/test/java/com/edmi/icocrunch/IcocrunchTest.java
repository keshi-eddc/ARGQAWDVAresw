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
        String[] shows = new String[]{"ICO","PreICO"};
        for(String show:shows){
            log.info("正在抓取show："+show+"类型的block数据");
            Long serialNumber =  icocrunchSevice.getIco_icocrunch_listMaxSerialNumber(show);
            if(null==serialNumber){//第一次抓取icocrunch数据
                log.info("第一次抓取icocrunch数据");
                serialNumber = Calendar.getInstance().getTime().getTime();
                icocrunchSevice.getIco_icocrunch_list(show,1,serialNumber);
            }
            Ico_icocrunch_list ico_icocrunch_list = icocrunchSevice.getNextPageIco_icocrunch_list(show,serialNumber);
            while(ico_icocrunch_list.getCurrentPage()<=ico_icocrunch_list.getTotalPage()){
                icocrunchSevice.getIco_icocrunch_list(show,ico_icocrunch_list.getCurrentPage(),serialNumber);
                ico_icocrunch_list = icocrunchSevice.getNextPageIco_icocrunch_list(show,serialNumber);
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
