package com.edmi.service.service;

import com.alibaba.fastjson.JSONObject;
import com.edmi.entity.coinschedule.ICO_coinschedule_detail_member;
import com.edmi.entity.coinschedule.Ico_coinschedule_List;
import com.edmi.utils.http.exception.MethodNotSupportException;

public interface CoinscheduleService {

    public void getIco_coinschedule_List() throws MethodNotSupportException;

    public void getIco_coinschedule_detail(Ico_coinschedule_List item);

    public void getIcoCoinscheduleICOsList();

    public void getIcoCoinscheduleMemberSocialLink(ICO_coinschedule_detail_member member);

    public JSONObject getIco_coinschedule_index(String dataSourceNameLevel2);

    public JSONObject getICO_coinschedule_detailByItemUrl(String url);

}
