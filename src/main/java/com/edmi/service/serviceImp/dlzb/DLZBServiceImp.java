package com.edmi.service.serviceImp.dlzb;

import com.edmi.dao.dlzb.DLZB_Project_ListRepository;
import com.edmi.dao.dlzb.DLZB_Project_List_Basic_InfoRepository;
import com.edmi.entity.dlzb.DLZB_Project_List;
import com.edmi.entity.dlzb.DLZB_Project_List_Basic_Info;
import com.edmi.service.service.DLZBService;
import com.edmi.utils.http.HttpClientUtil;
import com.edmi.utils.http.exception.MethodNotSupportException;
import com.edmi.utils.http.request.Request;
import com.edmi.utils.http.request.RequestMethod;
import com.edmi.utils.http.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class DLZBServiceImp implements DLZBService {

    Logger log = Logger.getLogger(DLZBServiceImp.class);

    @Autowired
    private DLZB_Project_ListRepository listDao;

    @Autowired
    private DLZB_Project_List_Basic_InfoRepository infoDao;

    @Override
    public void getDLZB_Project_List(String keyword) throws MethodNotSupportException {

        boolean flag = true;
        int page = 1;
        do{
            log.info("正在抓取keyword="+keyword);
            String link = "http://www.dlzb.com/g/search-htm-page-"+page+"-kw-"+keyword+".html";
            Request request = new Request(link, RequestMethod.GET);
            request.addHeader("Cookie","__jsluid=a4bcce27239de7abaefafb24815729c7; Hm_lvt_c909c1510b4aebf2db610b8d191cbe91=1525935465,1526457378; UM_distinctid=16348d76450131-0a7b7f662413f08-17347840-100200-16348d76452813; CNZZDATA1271464492=1052051246-1525932105-%7C1526460761; Hm_lvt_c8a521bedd9b0f1bef75b751d4605570=1525935602,1526464128; D3z_vi-ds=53566d1c135c2d46fcf92ede6c7f2eca; Hm_lpvt_c909c1510b4aebf2db610b8d191cbe91=1526464540; PHPSESSID=s4dmr818ggd0u3rnvtbe59h0g0; Hm_lvt_d7682ab43891c68a00de46e9ce5b76aa=1526463891; Hm_lpvt_d7682ab43891c68a00de46e9ce5b76aa=1526464466; D3z_ipshow-35=35-1; Hm_lpvt_c8a521bedd9b0f1bef75b751d4605570=152646412");
            request.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0");
            Response response = HttpClientUtil.doRequest(request);
            int code = response.getCode(); //response code
            if(200 ==code){
                String content = response.getResponseText(); //response text
                Document doc = Jsoup.parse(content);
                Elements con_list = doc.getElementsByClass("con_list");
                if(null!=con_list&&con_list.size()>0){
                    Element con = con_list.first();
                    Elements list = con.getElementsByAttributeValue("class", "gclist_ul listnew");//这个元素为空代表页面数据为空
                    if(null!=list&&list.size()>0){
                        list = list.first().getElementsByTag("li");
                        List<DLZB_Project_List> projects = new ArrayList<DLZB_Project_List>();
                        for(Element li:list){
                            DLZB_Project_List project = new DLZB_Project_List();
                            Elements titles = li.getElementsByClass("gccon_title");
                            Elements dates = li.getElementsByClass("gc_date");

                            if(null!=titles&&titles.size()>0){
                                String href = titles.first().attr("href");
                                String title = titles.first().text();
                                project.setGc_link(StringUtils.defaultIfEmpty(href,""));
                                project.setGc_title(StringUtils.defaultIfBlank(title,""));
                            }
                            if(null!=dates&&dates.size()>0){
                                String date = dates.first().text();
                                project.setGc_date(date);
                            }
                            project.setKeyword(keyword);
                            project.setStatus("ini");
                            project.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            project.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            projects.add(project);
                        }
                        listDao.saveAll(projects);
                        log.info("keyword="+keyword+",第"+page+"页保存完毕！");
                        page++;
                    }else{
                        flag =false;
                    }
                }else{
                    log.info("keyword="+keyword+",第"+page+"页没有数据元素！");
                }

            }else{
                log.info("keyword="+keyword+",第"+page+"页请求错误，error code："+code);
            }
        }while (flag);




    }

    @Override
    @Async("myTaskAsyncPool")
    public void getDLZB_Project_List_Basic_Info(DLZB_Project_List project) throws MethodNotSupportException {
        Request request = new Request(project.getGc_link(), RequestMethod.GET);
        request.addHeader("Cookie","__jsluid=d69004aae0fcaa7360795ded7e697999; UM_distinctid=16368596ef367-06dacf77de1017-44410a2e-100200-16368596ef493d; D3z_vi-ds=53566d1c135c2d46fcf92ede6c7f2eca; Hm_lvt_c909c1510b4aebf2db610b8d191cbe91=1526464082,1526538496,1526539339; CNZZDATA1271464492=1598036264-1526460761-%7C1526538609; D3z_mobile=touch; Hm_lvt_d7682ab43891c68a00de46e9ce5b76aa=1526464228,1526538446,1526539898; Hm_lpvt_d7682ab43891c68a00de46e9ce5b76aa=1526539898; D3z_ipshow-35=35-3; Hm_lvt_c8a521bedd9b0f1bef75b751d4605570=1526464082,1526538495,1526541087; Hm_lpvt_c8a521bedd9b0f1bef75b751d4605570=1526541087; Hm_lpvt_c909c1510b4aebf2db610b8d191cbe91=1526541087");
        request.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode(); //response code
        if(200 ==code){
            DLZB_Project_List_Basic_Info basic_info = new DLZB_Project_List_Basic_Info();
            String content = response.getResponseText(); //response text
            Document doc = Jsoup.parse(content);
            Elements jibens = doc.getElementsByAttributeValueStarting("class", "m jiben");
            if(null!=jibens&&jibens.size()>0){
                Element jiben = jibens.first();
                String txt = jiben.text();
                String date = StringUtils.substringBetween(txt,"：","】");
                String trace = StringUtils.substringAfterLast(txt, "跟踪次数：");
                basic_info.setLast_update_time(StringUtils.defaultIfEmpty(date,""));
                if(NumberUtils.isDigits(trace)){
                    basic_info.setTrack_times(NumberUtils.createInteger(trace));
                }
            }
            Elements table_titles = doc.getElementsByClass("table_title");
            for(Element table_title:table_titles){
                String table_title_txt = table_title.text();
                Element table_title_value_ele = table_title.nextElementSibling();
                String  table_title_value = "";
                if(null!=table_title_value_ele){
                     table_title_value = StringUtils.defaultIfEmpty(table_title_value_ele.text(),"");
                }
                if(StringUtils.equals("所属地区",table_title_txt)){
                    basic_info.setRegion(table_title_value);
                }else if(StringUtils.equals("进展阶段",table_title_txt)){
                    basic_info.setProgress(table_title_value);
                }else if(StringUtils.equals("加入时间",table_title_txt)){
                    basic_info.setJoin_time(table_title_value);
                }else if(StringUtils.equals("投资总额",table_title_txt)){
                    basic_info.setInvest_total(table_title_value);
                }else if(StringUtils.equals("项目性质",table_title_txt)){
                    basic_info.setProject_nature(table_title_value);
                }else if(StringUtils.equals("资金来源",table_title_txt)){
                    basic_info.setFunds_source(table_title_value);
                }else if(StringUtils.equals("业主单位",table_title_txt)){
                    basic_info.setProprietor(table_title_value);
                }else if(StringUtils.equals("项目周期",table_title_txt)){
                    basic_info.setProject_cycle(table_title_value);
                }else if(StringUtils.equals("关键设备",table_title_txt)){
                    basic_info.setDevices(table_title_value);
                }
            }
            Element project_overview  = doc.getElementById("content");
            basic_info.setProject_overview(project_overview.text());
            basic_info.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            basic_info.setProject_list(project);
            infoDao.save(basic_info);
            project.setStatus(String.valueOf(code));
            project.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            listDao.save(project);
            log.info("项目详情抓取完毕："+project.getGc_link());
        }else{
            log.info("项目详情请求失败，error code:"+code+",link:"+project.getGc_link());
        }
    }
}
