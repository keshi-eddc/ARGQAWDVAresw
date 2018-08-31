package com.edmi.service.serviceImp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.edmi.dto.icocrunch.Ico_icocrunch_detailDto;
import com.edmi.entity.icocrunch.Ico_icocrunch_detail;
import com.edmi.service.service.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
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
    @Autowired
    private IcoratingService icoratingService;
    @Autowired
    private IcodropsService icodropsService;

    @Override
    public JSONObject getICODataBySourceName(String dataSourceNameLevel1,String dataSourceNameLevel2,int page_number,int pageSize) {
        if(StringUtils.equalsIgnoreCase("icocrunch.io",dataSourceNameLevel1)){
            return icocrunchSevice.getIco_icocrunch_detailPageable(page_number,pageSize);
        }else if(StringUtils.equalsIgnoreCase("trackico.io",dataSourceNameLevel1)){
            return trackicoService.getIco_trackico_detail_index();
        }else if(StringUtils.equalsIgnoreCase("icorating.com",dataSourceNameLevel1)){
            return icoratingService.getIco_icorating_all_index(dataSourceNameLevel2);
        }else if(StringUtils.equalsIgnoreCase("icodrops.com",dataSourceNameLevel1)){
            return icodropsService.getIco_icodrops_index(dataSourceNameLevel2);
        }else if(StringUtils.equalsIgnoreCase("coinschedule.com",dataSourceNameLevel1)){
            return icodropsService.getIco_icodrops_index(dataSourceNameLevel2);
        }else{
            return null;
        }

    }

    @Override
    public JSONObject getICODataByICOUrl(JSONObject solution_data,String dataSourceNameLevel1,String dataSourceNameLevel2) {
        JSONObject json = new JSONObject();
        int number = 0;

        JSONObject solution_id = new JSONObject();
        for(Map.Entry<String, Object> entry:solution_data.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue().toString();
            //根据url的域名判断是哪个网站的Block URL
            String dataSourceName = StringUtils.substringBetween(key,"//","/");

            if(StringUtils.containsIgnoreCase("icocrunch.io",dataSourceNameLevel1)&&StringUtils.containsIgnoreCase(dataSourceName,dataSourceNameLevel1)){
                Ico_icocrunch_detail detail = icocrunchSevice.getIco_icocrunch_detailByICOCrunchUrl(key);
                if(null!=detail){
                    Ico_icocrunch_detailDto detailDto = new Ico_icocrunch_detailDto();
                    try {
                        BeanUtils.copyProperties(detailDto,detail);
                        Map<String, String> detailDtoMap = BeanUtils.describe(detailDto);
                        detailDtoMap.put("solution_photo_url",detailDtoMap.get("logo"));
                        detailDtoMap.remove("logo");

                        /*处理时间，preicoDate拆分出开始、结束时间*/
                        String preicoDate = detailDtoMap.get("preicoDate");
                        if(StringUtils.isNotEmpty(preicoDate)){
                            String[] preicoDates = StringUtils.split(preicoDate, "—");
                            if(ArrayUtils.isNotEmpty(preicoDates)&&preicoDates.length==2){
                                detailDtoMap.put("preicoStart",preicoDates[0]);
                                detailDtoMap.put("preicoEnd",preicoDates[1]);
                            }else{
                                detailDtoMap.put("preicoStart",preicoDates[0]);
                                detailDtoMap.put("preicoEnd",preicoDates[1]);
                            }
                        }else{
                            detailDtoMap.put("preicoStart","");
                            detailDtoMap.put("preicoEnd","");
                        }
                        detailDtoMap.remove("preicoDate");
                        /*处理时间，icoDate拆分出开始、结束时间*/
                        String icoDate = detailDtoMap.get("icoDate");
                        if(StringUtils.isNotEmpty(icoDate)){
                            String[] icoDates = StringUtils.split(icoDate, "—");
                            if(ArrayUtils.isNotEmpty(icoDates)&&icoDates.length==2){
                                detailDtoMap.put("icoStart",icoDates[0]);
                                detailDtoMap.put("icoEnd",icoDates[1]);
                            }else{
                                detailDtoMap.put("icoStart",icoDates[0]);
                                detailDtoMap.put("icoEnd",icoDates[1]);
                            }
                        }else{
                            detailDtoMap.put("icoStart","");
                            detailDtoMap.put("icoEnd","");
                        }
                        detailDtoMap.remove("icoDate");


                        detailDtoMap.remove("class");
                        JSONObject solution_url = new JSONObject();
                        solution_url.put(key,detailDtoMap);
                        number+=1;
                        solution_id.put(value,solution_url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else if(StringUtils.containsIgnoreCase("trackico.io", dataSourceNameLevel1)&&StringUtils.containsIgnoreCase(dataSourceName,dataSourceNameLevel1)){
                JSONObject detail = trackicoService.getICO_trackico_detailByItemUrl(key);
                if(null!=detail){
                    number+=1;
                    JSONObject solution_url = new JSONObject();
                    solution_url.put(key,detail);
                    solution_id.put(value,solution_url);
                }
            }else if(StringUtils.containsIgnoreCase("icorating.com",dataSourceNameLevel1)&&StringUtils.containsIgnoreCase(dataSourceName,dataSourceNameLevel1)){

                if(StringUtils.equalsIgnoreCase("all",dataSourceNameLevel2)){
                    JSONObject detail = icoratingService.getICO_icorating_detailByItemUrl(key);
                    if(null!=detail){
                        number+=1;
                        JSONObject solution_url = new JSONObject();
                        solution_url.put(key,detail);
                        solution_id.put(value,solution_url);
                    }
                }else if(StringUtils.equalsIgnoreCase("funds",dataSourceNameLevel2)){
                    JSONObject detail = icoratingService.getICO_icorating_funds_detailByItemUrl(key);
                    if(null!=detail){
                        number+=1;
                        JSONObject solution_url = new JSONObject();
                        solution_url.put(key,detail);
                        solution_id.put(value,solution_url);
                    }
                }
            }else if(StringUtils.containsIgnoreCase("icodrops.com",dataSourceNameLevel1)&&StringUtils.containsIgnoreCase(dataSourceName,dataSourceNameLevel1)) {

                    JSONObject detail = icodropsService.getICO_icodrops_detailByItemUrl(key);
                    if (null != detail) {
                        number += 1;
                        JSONObject solution_url = new JSONObject();
                        solution_url.put(key, detail);
                        solution_id.put(value, solution_url);
                    }

            }else{
                continue;
            }
        }
        if(StringUtils.isNotEmpty(dataSourceNameLevel1)&&StringUtils.isNotEmpty(dataSourceNameLevel2)){
            json.put("source",dataSourceNameLevel1+"."+dataSourceNameLevel2);
        }else{
            json.put("source",dataSourceNameLevel1);
        }
        json.put("number",number);
        json.put("solution_data",solution_id);
        return json;
    }
}
