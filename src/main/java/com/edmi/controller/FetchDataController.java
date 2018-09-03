package com.edmi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.edmi.service.service.FeixiaohaoService;
import com.edmi.service.service.FetchICODataService;
import com.edmi.service.service.LinkedinService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class FetchDataController {

    @Autowired
    private FeixiaohaoService feixiaohaoService;
    @Autowired
    private LinkedinService linkedinService;
    @Autowired
    private FetchICODataService fetchICODataService;


    @RequestMapping(value = "/v1/api/ico_api_list/get",method = RequestMethod.GET)
    public JSONObject getICO_Data_Process(@RequestParam("api_category")String api_category) {
        JSONObject exchanges =  feixiaohaoService.getICO_Data_Process(api_category);
        return exchanges;
    }
    /*===========exchange============================*/
    @RequestMapping(value = "/v1/exchange/ico_feixiaohao_exchange/get",method = RequestMethod.GET)
    public JSONObject get_ico_feixiaohao_exchange(@RequestParam("page_number")int page_number) {
        JSONObject exchanges =  feixiaohaoService.getICO_Feixiaohao_Exchange_Pageable(page_number-1, 500);
        return exchanges;
    }

    @RequestMapping(value = "/v1/exchange/ico_feixiaohao_exchange_details/get",method = RequestMethod.GET)
    public JSONObject get_ico_feixiaohao_exchange_details(@RequestParam("exchange_pk_id")int exchange_pk_id) {
        JSONObject exchanges =  feixiaohaoService.getICO_Feixiaohao_Exchange_details(exchange_pk_id);
        return exchanges;
    }

    @RequestMapping(value = "/v1/exchange/ico_feixiaohao_exchange_counter_party_details/get",method = RequestMethod.GET)
    public JSONObject get_ico_feixiaohao_exchange_counter_party_details(@RequestParam("page_number")int page_number) {
        JSONObject exchanges =  feixiaohaoService.getICO_Feixiaohao_Exchange_Counter_Party_Details(page_number-1, 500);
        return exchanges;
    }

    @RequestMapping(value = "/v1/exchange/ico_feixiaohao_exchange_currencies/get",method = RequestMethod.GET)
    public JSONObject get_ico_feixiaohao_exchange_currencies(@RequestParam("page_number")int page_number) {
        JSONObject exchanges =  feixiaohaoService.getICO_Feixiaohao_Exchange_Currencies(page_number-1, 100);
        return exchanges;
    }
    @RequestMapping(value = "/v1/exchange/ico_feixiaohao_exchange_currencies_page_link/get",method = RequestMethod.GET)
    public JSONObject get_ico_feixiaohao_exchange_currencies_page_link(@RequestParam("currencies_pk_id")long currencies_pk_id) {
        JSONObject exchanges =  feixiaohaoService.getICO_Feixiaohao_Exchange_Currencies_Page_Link(currencies_pk_id);
        return exchanges;
    }
    @RequestMapping(value = "/v1/exchange/ico_feixiaohao_exchange_currenciesdtl/get",method = RequestMethod.GET)
    public JSONObject get_ico_feixiaohao_exchange_currenciesdtl(@RequestParam("currencies_pk_id")long currencies_pk_id) {
        JSONObject exchanges =  feixiaohaoService.getICO_Feixiaohao_Exchange_Currenciesdtl(currencies_pk_id);
        return exchanges;
    }
    /*===========linkedin============================*/
    @RequestMapping(value = "/v1/linkedin/ico_linkedin_member_info/get",method = RequestMethod.GET)
    public JSONObject get_ico_linkedin_member_info(@RequestParam("link")String link) {
        JSONObject member_info =  linkedinService.get_ico_linkedin_member_info(link);
        return member_info;
    }

    @RequestMapping(value ={
            "/blocktest/api/v1/{dataSourceNameLevel1}/ico/index",
            "/blocktest/api/v1/{dataSourceNameLevel1}/{dataSourceNameLevel2}/ico/index",
    },method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json;charset=UTF-8")
    public JSONObject getListData(@PathVariable String dataSourceNameLevel1,@PathVariable(required = false) String dataSourceNameLevel2) {

        if(StringUtils.isEmpty(dataSourceNameLevel1)){
            JSONObject result = new JSONObject();
            result.put("errorCode","001");
            result.put("msg","dataSourceName must be not null!");
            return result;
        }else {
            return fetchICODataService.getICODataBySourceName(dataSourceNameLevel1,dataSourceNameLevel2,0,10000);
        }
    }
    @RequestMapping(value = {
            "/blocktest/api/v1/{dataSourceNameLevel1}/ico/detail",
            "/blocktest/api/v1/{dataSourceNameLevel1}/{dataSourceNameLevel2}/ico/detail"},method = {RequestMethod.GET,RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public JSONObject getListDataDetail(@RequestParam("solution_data")String  solution_data,@PathVariable String dataSourceNameLevel1,@PathVariable(required = false) String dataSourceNameLevel2) {

        if(StringUtils.isEmpty(solution_data)||StringUtils.isEmpty(dataSourceNameLevel1)){
            JSONObject result = new JSONObject();
            result.put("errorCode","001");
            result.put("msg","dataSource or dataSourceName must be not null!");
            return result;
        }else {
            JSONObject data = JSONObject.parseObject(solution_data);
            return fetchICODataService.getICODataByICOUrl(data,dataSourceNameLevel1,dataSourceNameLevel2);
        }
    }
}
