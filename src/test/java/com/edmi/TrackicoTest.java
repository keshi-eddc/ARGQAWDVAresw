package com.edmi;

import com.edmi.dao.trackico.ICO_trackico_itemRepository;
import com.edmi.entity.trackico.ICO_trackico_item;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.edmi.service.service.TrackicoService;
import com.edmi.utils.http.exception.MethodNotSupportException;

import java.util.List;

/**
 * @author keshi
 * @ClassName: TrackicoTest1
 * @Description: Junit测试 Trackico
 * @date 2018年8月3日 上午10:11:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TrackicoTest {
    Logger log = Logger.getLogger(TrackicoTest.class);

    // 注入实例 相当于自动new对象
    @Autowired
    private TrackicoService trackicoService;
    @Autowired
    private ICO_trackico_itemRepository ico_trackico_itemDao;

    @Test
    public void getICO_Trackico_detail() throws MethodNotSupportException {

        //all
//        List<ICO_trackico_item> items = ico_trackico_itemDao.findAllByStatus("ini");

        List<ICO_trackico_item> items = ico_trackico_itemDao.findTop10ByStatus("ini");

        // List<ICO_trackico_item> items =
        // ico_trackico_itemDao.findOneByItemUrl("https://www.trackico.io/ico/w12/");
        log.info("get items num ：" + items.size());
        if (items.size() != 0) {
            // 获取开始时间
            long startTime = System.currentTimeMillis();

            for (ICO_trackico_item item : items) {
                trackicoService.getICO_trackico_detail(item);
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 获取结束时间
            long endTime = System.currentTimeMillis();
            long mss = endTime - startTime;
            long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (mss % (1000 * 60)) / 1000;
            String timestr = hours + " hours " + minutes + " minutes " + seconds + " seconds ";
            log.info("this time crawled，" + "items num：" + items.size() + ".cost：" + timestr);
        } else {
            log.info("get item from databash ,item num is 0");
        }

    }
}
