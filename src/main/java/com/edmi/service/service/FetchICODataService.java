package com.edmi.service.service;

import com.alibaba.fastjson.JSONObject;

public interface FetchICODataService {

    public JSONObject getICODataBySourceName(String dataSourceName,int page_number,int pageSize);
    public JSONObject getICODataByICOCrunchUrl(JSONObject solution_data);
}