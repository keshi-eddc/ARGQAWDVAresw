package com.edmi.service.serviceImp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.edmi.entity.icocrunch.Ico_icocrunch_detail;
import com.edmi.service.service.FetchICODataService;
import com.edmi.service.service.IcocrunchSevice;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FetchICODataServiceImp implements FetchICODataService {

    @Autowired
    private IcocrunchSevice icocrunchSevice;

    @Override
    public JSONObject getICODataBySourceName(String dataSourceName,int page_number,int pageSize) {
        if(StringUtils.equalsIgnoreCase("icocrunch.io",dataSourceName)){
            return icocrunchSevice.getIco_icocrunch_detailPageable(page_number,pageSize);
        }else{
            return null;
        }

    }

    @Override
    public JSONObject getICODataByICOCrunchUrl(JSONObject solution_data) {
        String dataSourceName = "";
        JSONObject json = new JSONObject();
        int number = 0;

        JSONObject solution_id = new JSONObject();
        for(Map.Entry<String, Object> entry:solution_data.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue().toString();
            if(StringUtils.isEmpty(dataSourceName)){//根据url的域名判断是哪个网站的Block URL
                dataSourceName = StringUtils.substringBetween(key,"//","/");
            }
            if(StringUtils.equalsIgnoreCase("icocrunch.io",dataSourceName)){
                Ico_icocrunch_detail detail = icocrunchSevice.getIco_icocrunch_detailByICOCrunchUrl(key);
                if(null!=detail){
                    number+=1;

                    JSONObject solution_url = new JSONObject();
                    solution_url.put(detail.getIcoCrunchUrl(),JSON.toJSON(detail));

                    solution_id.put(value,solution_url);
                }
            }else{
                return null;
            }
        }
        json.put("number",number);
        json.put("source",dataSourceName);
        json.put("solution_data",solution_id);
        return json;
    }
}
