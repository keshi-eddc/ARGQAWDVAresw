package com.edmi.service.service;

import com.alibaba.fastjson.JSONObject;

public interface FetchICODataService {

    public JSONObject getICODataBySourceName(String dataSourceNameLevel1,String dataSourceNameLevel2,int page_number,int pageSize);
    public JSONObject getICODataByICOUrl(JSONObject solution_data,String dataSourceNameLevel1,String dataSourceNameLevel2);
}
