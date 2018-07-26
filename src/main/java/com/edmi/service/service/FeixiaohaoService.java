package com.edmi.service.service;

import com.alibaba.fastjson.JSONObject;
import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange;
import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange_Currencies;
import com.edmi.utils.http.exception.MethodNotSupportException;


public interface FeixiaohaoService {

    public void getICO_Feixiaohao_Exchange() throws MethodNotSupportException;
    public void getICO_Feixiaohao_Exchange_Details(ICO_Feixiaohao_Exchange exchange) throws MethodNotSupportException;
    public void getICO_Feixiaohao_Exchange_Counter_Party_Details(String link) throws MethodNotSupportException;
    public void importICO_Feixiaohao_Exchange_Currencies();
    public void getICO_Feixiaohao_Exchange_Currenciesdtl(ICO_Feixiaohao_Exchange_Currencies currency) throws MethodNotSupportException;

    public JSONObject getICO_Feixiaohao_Exchange_Pageable(int pageNum, int pageSize);
    public JSONObject getICO_Feixiaohao_Exchange_details(long exchange_pk_id);
    public JSONObject getICO_Feixiaohao_Exchange_Counter_Party_Details(int pageNum, int pageSize);
    public JSONObject getICO_Feixiaohao_Exchange_Currencies(int pageNum, int pageSize);
    public JSONObject getICO_Feixiaohao_Exchange_Currencies_Page_Link(long currencies_pk_id);
    public JSONObject getICO_Feixiaohao_Exchange_Currenciesdtl(long currencies_pk_id);
    public JSONObject getICO_Data_Process(String api_category);

}
