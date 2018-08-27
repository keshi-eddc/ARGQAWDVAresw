package com.edmi.service.serviceImp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.edmi.dto.icocrunch.Ico_icocrunch_detailDto;
import com.edmi.entity.icocrunch.Ico_icocrunch_detail;
import com.edmi.service.service.FetchICODataService;
import com.edmi.service.service.IcocrunchSevice;
import com.edmi.service.service.TrackicoService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Service
public class FetchICODataServiceImp implements FetchICODataService {

    @Autowired
    private IcocrunchSevice icocrunchSevice;
    @Autowired
    private TrackicoService trackicoService;

    @Override
    public JSONObject getICODataBySourceName(String dataSourceName,int page_number,int pageSize) {
        if(StringUtils.equalsIgnoreCase("icocrunch.io",dataSourceName)){
            return icocrunchSevice.getIco_icocrunch_detailPageable(page_number,pageSize);
        }else if(StringUtils.equalsIgnoreCase("trackico.io",dataSourceName)){
            return trackicoService.getIco_trackico_detail_index();
        }else{
            return null;
        }

    }

    @Override
    public JSONObject getICODataByICOUrl(JSONObject solution_data) {
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
            if(StringUtils.containsIgnoreCase(dataSourceName,"icocrunch.io")){
                dataSourceName = "icocrunch.io";
                Ico_icocrunch_detail detail = icocrunchSevice.getIco_icocrunch_detailByICOCrunchUrl(key);
                if(null!=detail){
                    Ico_icocrunch_detailDto detailDto = new Ico_icocrunch_detailDto();
                    try {
                        BeanUtils.copyProperties(detailDto,detail);
                        Map<String, String> detailDtoMap = BeanUtils.describe(detailDto);
                        detailDtoMap.put("solution_photo_url",detailDtoMap.get("logo"));
                        detailDtoMap.remove("logo");
                        detailDtoMap.remove("class");
                        JSONObject solution_url = new JSONObject();
                        solution_url.put(key,detailDtoMap);

                        number+=1;
                        solution_id.put(value,solution_url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else if(StringUtils.containsIgnoreCase(dataSourceName,"trackico.io")){
                dataSourceName = "trackico.io";
                JSONObject detail = trackicoService.getICO_trackico_detailByItemUrl(key);
                if(null!=detail){
                    number+=1;

                    JSONObject solution_url = new JSONObject();
                    solution_url.put(key,detail);
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
