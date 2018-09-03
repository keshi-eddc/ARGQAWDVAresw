package com.edmi.coinschedule;

import com.edmi.dao.coinschedule.ICO_coinschedule_detail_memberDao;
import com.edmi.dao.coinschedule.Ico_coinschedule_ListDao;
import com.edmi.entity.coinschedule.ICO_coinschedule_detail_member;
import com.edmi.entity.coinschedule.Ico_coinschedule_List;
import com.edmi.service.service.CoinscheduleService;
import com.edmi.utils.http.exception.MethodNotSupportException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CoinscheduleTest {

    @Autowired
    private CoinscheduleService coinscheduleService;
    @Autowired
    private Ico_coinschedule_ListDao ico_coinschedule_listDao;
    @Autowired
    private ICO_coinschedule_detail_memberDao ico_coinschedule_detail_memberDao;

    Logger log = Logger.getLogger(CoinscheduleTest.class);

    @Test
    public void getIco_coinschedule_List() throws MethodNotSupportException {
        coinscheduleService.getIco_coinschedule_List();
    }

    @Test
    public void getList() throws MethodNotSupportException {
//        coinscheduleService.getIco_coinschedule_List();
        coinscheduleService.getIcoCoinscheduleICOsList();
        //650
    }

    @Test
    public void getDetail() throws MethodNotSupportException {
        //216
        log.info("***** start Coinschedule detail task *****");
        List<Ico_coinschedule_List> itemlist = ico_coinschedule_listDao.findIco_coinschedule_ListWithNotIn();
        log.info("--- get coinschedule items num is :" + itemlist.size());
        for (int i = 0; i < itemlist.size(); i++) {
            log.info("----- will extra :" + i);
            Ico_coinschedule_List item = itemlist.get(i);
            coinscheduleService.getIco_coinschedule_detail(item);
//            break;
        }
        log.info("***** Coinschedule detail task  over *****");
    }

    @Test
    public void getMemberSocialLinks() {
        log.info("***** start getMemberSocialLinks task *****");
        List<ICO_coinschedule_detail_member> memberList = ico_coinschedule_detail_memberDao.findICO_coinschedule_detail_memberWithNotIn();
        log.info("get members from detail_member,num is :" + memberList.size());
        if (CollectionUtils.isNotEmpty(memberList)) {
            for (int i = 0; i < memberList.size(); i++) {
                ICO_coinschedule_detail_member member = memberList.get(i);
                log.info("----- will extra member :" + i);
                coinscheduleService.getIcoCoinscheduleMemberSocialLink(member);
//                break;
            }
            log.info("***** getMemberSocialLinks task over  *****");
        }
    }
}
