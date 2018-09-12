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
    @Autowired
    private CoinscheduleService coinscheduleService;

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
            return coinscheduleService.getIco_coinschedule_index(dataSourceNameLevel2);
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

                        JSONObject ico_detail = new JSONObject();
                        ico_detail.putAll(BeanUtils.describe(detailDto));

                        /*从ico_detail中提取出概况:name,whitePaperURL,tag,about,brief,description,prototype*/
                        JSONObject ico_about = new JSONObject();
                        if(ico_detail.containsKey("icoName")){
                            ico_about.put("name",ico_detail.getString("icoName"));
                            ico_detail.remove("ico_name");
                        }
                        if(ico_detail.containsKey("whitepaper")){
                            ico_about.put("whitePaperURL",ico_detail.getString("whitepaper"));
                            ico_detail.remove("whitepaper");
                        }
                        if(ico_detail.containsKey("whitepaper")){
                            ico_about.put("whitePaperURL",ico_detail.getString("whitepaper"));
                            ico_detail.remove("whitepaper");
                        }
                        ico_about.put("tag",ico_detail.getString("categories"));
                        ico_detail.remove("categories");
                        if(ico_detail.containsKey("shortDescription")){
                            ico_about.put("about",ico_detail.getString("shortDescription"));
                            ico_detail.remove("shortDescription");
                        }
                        ico_about.put("brief","");
                        if(ico_detail.containsKey("icoProjectDescription")){
                            ico_about.put("description",ico_detail.getString("icoProjectDescription"));
                            ico_detail.remove("icoProjectDescription");
                        }
                        ico_about.put("prototype","");

                        ico_detail.put("solution_photo_url",ico_detail.getString("logo"));
                        ico_detail.remove("logo");

                        /*处理时间，preicoDate拆分出开始、结束时间*/
                        String preicoDate = ico_detail.getString("preicoDate");
                        if(StringUtils.isNotEmpty(preicoDate)){
                            String[] preicoDates = StringUtils.split(preicoDate, "—");
                            if(ArrayUtils.isNotEmpty(preicoDates)&&preicoDates.length==2){
                                ico_detail.put("preicoStart",preicoDates[0]);
                                ico_detail.put("preicoEnd",preicoDates[1]);
                            }else{
                                ico_detail.put("preicoStart",preicoDates[0]);
                                ico_detail.put("preicoEnd",preicoDates[1]);
                            }
                        }else{
                            ico_detail.put("preicoStart","");
                            ico_detail.put("preicoEnd","");
                        }
                        ico_detail.remove("preicoDate");
                        /*处理时间，icoDate拆分出开始、结束时间*/
                        String icoDate = ico_detail.getString("icoDate");
                        if(StringUtils.isNotEmpty(icoDate)){
                            String[] icoDates = StringUtils.split(icoDate, "—");
                            if(ArrayUtils.isNotEmpty(icoDates)&&icoDates.length==2){
                                ico_detail.put("icoStart",icoDates[0]);
                                ico_detail.put("icoEnd",icoDates[1]);
                            }else{
                                ico_detail.put("icoStart",icoDates[0]);
                                ico_detail.put("icoEnd",icoDates[1]);
                            }
                        }else{
                            ico_detail.put("icoStart","");
                            ico_detail.put("icoEnd","");
                        }
                        ico_detail.remove("icoDate");
                        /*移除social信息*/
                        ico_detail.remove("telegram");
                        ico_detail.remove("twitter");
                        ico_detail.remove("bitcointalk");
                        ico_detail.remove("facebook");
                        ico_detail.remove("gitHub");
                        ico_detail.remove("medium");


                        ico_detail.remove("class");

                        ico_about.put("ico",ico_detail);

                        if(null!=ico_about){
                            number+=1;
                            if(solution_id.containsKey(value)){
                                solution_id.getJSONObject(value).put(key,ico_about);
                            }else{
                                JSONObject solution_url = new JSONObject();
                                solution_url.put(key,ico_about);
                                solution_id.put(value,solution_url);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else if(StringUtils.containsIgnoreCase("trackico.io", dataSourceNameLevel1)&&StringUtils.containsIgnoreCase(dataSourceName,dataSourceNameLevel1)){
                JSONObject detail = trackicoService.getICO_trackico_detailByItemUrl(key);
                if(null!=detail){
                    number+=1;
                    if(solution_id.containsKey(value)){
                        solution_id.getJSONObject(value).put(key,detail);
                    }else{
                        JSONObject solution_url = new JSONObject();
                        solution_url.put(key,detail);
                        solution_id.put(value,solution_url);
                    }
                }
            }else if(StringUtils.containsIgnoreCase("icorating.com",dataSourceNameLevel1)&&StringUtils.containsIgnoreCase(dataSourceName,dataSourceNameLevel1)){
                JSONObject detail = null;
                if(StringUtils.equalsIgnoreCase("all",dataSourceNameLevel2)){
                    detail = icoratingService.getICO_icorating_detailByItemUrl(key);

                }else if(StringUtils.equalsIgnoreCase("funds",dataSourceNameLevel2)){
                    detail = icoratingService.getICO_icorating_funds_detailByItemUrl(key);

                }
                if(null!=detail){
                    number+=1;
                    if(solution_id.containsKey(value)){
                        solution_id.getJSONObject(value).put(key,detail);
                    }else{
                        JSONObject solution_url = new JSONObject();
                        solution_url.put(key,detail);
                        solution_id.put(value,solution_url);
                    }
                }
            }else if(StringUtils.containsIgnoreCase("icodrops.com",dataSourceNameLevel1)&&StringUtils.containsIgnoreCase(dataSourceName,dataSourceNameLevel1)) {

                JSONObject detail = icodropsService.getICO_icodrops_detailByItemUrl(key);
                if(null!=detail){
                    number+=1;
                    if(solution_id.containsKey(value)){
                        solution_id.getJSONObject(value).put(key,detail);
                    }else{
                        JSONObject solution_url = new JSONObject();
                        solution_url.put(key,detail);
                        solution_id.put(value,solution_url);
                    }
                }

            }else if(StringUtils.containsIgnoreCase("coinschedule.com",dataSourceNameLevel1)&&StringUtils.containsIgnoreCase(dataSourceName,dataSourceNameLevel1)){
                JSONObject detail = null;
                if(StringUtils.equalsIgnoreCase("all",dataSourceNameLevel2)){
                     detail = coinscheduleService.getICO_coinschedule_detailByItemUrl(key);

                }else if(StringUtils.equalsIgnoreCase("funds",dataSourceNameLevel2)){
                     detail = icoratingService.getICO_icorating_funds_detailByItemUrl(key);
                }
                if(null!=detail){
                    number+=1;
                    if(solution_id.containsKey(value)){
                        solution_id.getJSONObject(value).put(key,detail);
                    }else{
                        JSONObject solution_url = new JSONObject();
                        solution_url.put(key,detail);
                        solution_id.put(value,solution_url);
                    }
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
