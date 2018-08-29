package com.edmi.service.serviceImp.coinschedule;

import com.edmi.dao.coinschedule.Ico_coinschedule_ListDao;
import com.edmi.dao.icocrunch.Ico_icocrunch_detailDao;
import com.edmi.entity.coinschedule.Ico_coinschedule_List;
import com.edmi.service.service.CoinscheduleService;
import com.edmi.utils.http.HttpClientUtil;
import com.edmi.utils.http.exception.MethodNotSupportException;
import com.edmi.utils.http.request.Request;
import com.edmi.utils.http.request.RequestMethod;
import com.edmi.utils.http.response.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class CoinscheduleSeviceImp implements CoinscheduleService {

    Logger log = Logger.getLogger(CoinscheduleSeviceImp.class);

    @Autowired
    private Ico_coinschedule_ListDao listDao;
    @Autowired
    private Ico_icocrunch_detailDao ico_icocrunch_detailDao;


    /*
     * 获取列表
     * */
    public void getIco_coinschedule_List() throws MethodNotSupportException {
        String url = "https://www.coinschedule.com/";
        Request request = new Request(url, RequestMethod.GET);
        request.addUrlParam("live_view", 2);
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode(); //response code
        log.info(url);

        if (200 == code) {
            String content = response.getResponseText(); //response text
            Document doc = Jsoup.parse(content);
            /*分页信息*/
            Elements lives = doc.getElementsByAttributeValue("class", "live upcoming list-table div-upcoming");
            Elements upcomings = doc.getElementsByAttributeValue("class", "upcoming list-table list-div");
            List<Ico_coinschedule_List> ico_coinschedule_lists = new ArrayList<>();
            List<Ico_coinschedule_List> ico_coinschedule_lists_lives = this.getIco_coinschedule_List(lives, "lives");
            List<Ico_coinschedule_List> ico_coinschedule_lists_upcomings = this.getIco_coinschedule_List(upcomings, "upcomings");
            if (CollectionUtils.isNotEmpty(ico_coinschedule_lists_lives)) {
                ico_coinschedule_lists.addAll(ico_coinschedule_lists_lives);
            }
            if (CollectionUtils.isNotEmpty(ico_coinschedule_lists_upcomings)) {
                ico_coinschedule_lists.addAll(ico_coinschedule_lists_upcomings);
            }
            if (CollectionUtils.isNotEmpty(ico_coinschedule_lists)) {
                for (Ico_coinschedule_List ico_coinschedule_list : ico_coinschedule_lists) {//逐个判断是否已经抓取过
                    Ico_coinschedule_List list = listDao.findIco_icocrunch_listByIcoCoinscheduleUrlAndBlockType(ico_coinschedule_list.getIcoCoinscheduleUrl(), ico_coinschedule_list.getBlockType());
                    if (null != list) {
                        BeanUtils.copyProperties(ico_coinschedule_list, list, new String[]{"pkId", "insertTime"});
                        listDao.save(list);
                        log.info("该coinschedule已更新,type:" + ico_coinschedule_list.getBlockType() + ",url:" + ico_coinschedule_list.getIcoCoinscheduleUrl());
                    } else {
                        listDao.save(ico_coinschedule_list);
                        log.info("coinschedule保存成功,type:" + ico_coinschedule_list.getBlockType() + ",url:" + ico_coinschedule_list.getIcoCoinscheduleUrl());
                    }
                }
            }
        } else {
            log.info("coinschedule获取列表请求失败，errorCode：" + code);
        }
    }

    public List<Ico_coinschedule_List> getIco_coinschedule_List(Elements blocks, String type) {
        List<Ico_coinschedule_List> ico_coinschedule_lists = new ArrayList<Ico_coinschedule_List>();
        if ("lives".equals(type)) {
            if (CollectionUtils.isNotEmpty(blocks)) {
                Element live = blocks.first();
                Elements list_containers = live.getElementsByClass("list-container");
                if (CollectionUtils.isNotEmpty(list_containers)) {
                    Element list_container = list_containers.first();

                    Elements divTable_dtLives = list_container.getElementsByAttributeValue("class", "divTable dtLive");
                    if (CollectionUtils.isNotEmpty(divTable_dtLives)) {
                        Element divTable_dtLive = divTable_dtLives.first();

                        Elements divTableBodys = divTable_dtLive.getElementsByClass("divTableBody");//获取头信息
                        Elements divTableRows = divTable_dtLive.select("> div[class^=divTableRow]");//获取列表

                        Map<Integer, String> heads_map = new HashMap<>();
                        if (CollectionUtils.isNotEmpty(divTableBodys)) {
                            Element divTableBody = divTableBodys.first();
                            Elements heads = divTableBody.getElementsByAttributeValueStarting("class", "divTableCellHead");
                            for (int i = 0; i < heads.size(); i++) {
                                Element head = heads.get(i);
                                heads_map.put(i, head.ownText());
                            }
                        }
                        for (Element row : divTableRows) {

                            Ico_coinschedule_List list = new Ico_coinschedule_List();

                            Elements colums = row.select("> div");
                            for (int i = 0; i < colums.size(); i++) {
                                Element colum = colums.get(i);
                                String colum_name = heads_map.get(i);
                                if (StringUtils.equalsIgnoreCase("Name", colum_name)) {
                                    Elements names = colum.getElementsByAttributeValue("target", "_self");
                                    if (CollectionUtils.isNotEmpty(names)) {
                                        Element name = names.first();
                                        String icoName = StringUtils.defaultIfEmpty(name.ownText(), "");
                                        String icoCoinscheduleUrl = StringUtils.substringBeforeLast(StringUtils.defaultIfEmpty(name.attr("href"), ""), "#event");
                                        list.setIcoName(icoName);
                                        list.setIcoCoinscheduleUrl(icoCoinscheduleUrl);
                                    }
                                } else if (StringUtils.equalsIgnoreCase("Category", colum_name)) {
                                    String category = StringUtils.defaultIfEmpty(colum.ownText(), "");
                                    list.setCategory(category);
                                } else if (StringUtils.equalsIgnoreCase("End Date", colum_name)) {
                                    String endDate = StringUtils.defaultIfEmpty(colum.ownText(), "");
                                    list.setEndDate(endDate);
                                } else if (StringUtils.equalsIgnoreCase("Ends In", colum_name)) {
                                    String endsIn = StringUtils.defaultIfEmpty(colum.ownText(), "");
                                    list.setEndsIn(endsIn);
                                } else if (StringUtils.equalsIgnoreCase("Trust", colum_name)) {
                                    String trust = StringUtils.defaultIfEmpty(colum.text(), "");
                                    list.setTrust(trust);
                                }
                            }
                            list.setBlockType(type);
                            list.setInsertTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            list.setModifyTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            ico_coinschedule_lists.add(list);
                        }
                    }
                }
            }
        } else if ("upcomings".equals(type)) {
            if (CollectionUtils.isNotEmpty(blocks)) {
                Element block = blocks.first();
                Elements divTable_dtUpcomming = block.select("> div[class=divTable dtUpcomming]");//thead
                Elements list_container = block.select("> div[class=list-container]");//tbody

                Map<Integer, String> heads_map = new HashMap<>();
                if (CollectionUtils.isNotEmpty(divTable_dtUpcomming)) {
                    Elements divTableBody = divTable_dtUpcomming.first().getElementsByClass("divTableBody");
                    if (CollectionUtils.isNotEmpty(divTableBody)) {
                        Elements divTableRowHead = divTableBody.first().getElementsByAttributeValue("class", "divTableRow divTableRowHead");
                        if (CollectionUtils.isNotEmpty(divTableRowHead)) {
                            Elements heads = divTableRowHead.first().getElementsByAttributeValueStarting("class", "divTableCellHead");
                            for (int i = 0; i < heads.size(); i++) {
                                Element head = heads.get(i);
                                heads_map.put(i, head.ownText());
                            }
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(list_container)) {
                    Elements tables = list_container.first().getElementsByAttributeValue("class", "divTable dtUpcomming");
                    if (CollectionUtils.isNotEmpty(tables)) {
                        Elements divTableRows = tables.first().getElementsByAttributeValueStarting("class", "divTableRow ");
                        for (Element row : divTableRows) {

                            Ico_coinschedule_List list = new Ico_coinschedule_List();

                            Elements colums = row.select("> div");
                            for (int i = 0; i < colums.size(); i++) {
                                Element colum = colums.get(i);
                                String colum_name = heads_map.get(i);
                                if (StringUtils.equalsIgnoreCase("Name", colum_name)) {
                                    Elements names = colum.getElementsByAttributeValue("target", "_self");
                                    if (CollectionUtils.isNotEmpty(names)) {
                                        Element name = names.first();
                                        String icoName = StringUtils.defaultIfEmpty(name.ownText(), "");
                                        String icoCoinscheduleUrl = StringUtils.substringBeforeLast(StringUtils.defaultIfEmpty(name.attr("href"), ""), "#event");
                                        list.setIcoName(icoName);
                                        list.setIcoCoinscheduleUrl(icoCoinscheduleUrl);
                                    }
                                } else if (StringUtils.equalsIgnoreCase("Category", colum_name)) {
                                    String category = StringUtils.defaultIfEmpty(colum.ownText(), "");
                                    list.setCategory(category);
                                } else if (StringUtils.equalsIgnoreCase("Start Date", colum_name)) {
                                    String startDate = StringUtils.defaultIfEmpty(colum.ownText(), "");
                                    list.setStartDate(startDate);
                                } else if (StringUtils.equalsIgnoreCase("Starts In", colum_name)) {
                                    String startsIn = StringUtils.defaultIfEmpty(colum.ownText(), "");
                                    list.setStartsIn(startsIn);
                                } else if (StringUtils.equalsIgnoreCase("Trust", colum_name)) {
                                    String trust = StringUtils.defaultIfEmpty(colum.text(), "");
                                    list.setTrust(trust);
                                }
                            }
                            list.setBlockType(type);
                            list.setInsertTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            list.setModifyTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            ico_coinschedule_lists.add(list);
                        }
                    }
                }
            }
        }

        return ico_coinschedule_lists;
    }
  /*  @Transactional
    //@Async("myTaskAsyncPool")
    public void getIco_icocrunch_detail(String blockUrl) throws MethodNotSupportException {
        log.info(blockUrl);
        Request request = new Request(blockUrl, RequestMethod.GET);
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode(); //response code
        if(code == 200){
            Ico_icocrunch_detail ico_icocrunch_detail = new Ico_icocrunch_detail();
            String content = response.getResponseText(); //response text
            Document doc = Jsoup.parse(content);

            //获取block的图片地址
            Elements logos = doc.getElementsByAttributeValue("class", "attachment-ICOlogo size-ICOlogo wp-post-image");
            if(CollectionUtils.isNotEmpty(logos)){
                Element logo = logos.first();
                String logo_src = logo.attr("src");
                ico_icocrunch_detail.setLogo(StringUtils.defaultIfEmpty(logo_src,""));
            }
            //获取block名称、类别、简介
            Elements iconames = doc.getElementsByAttributeValue("class", "iconame media-body");
            if (CollectionUtils.isNotEmpty(iconames)) {
                Element iconame = iconames.first();

                String brief = iconame.ownText();//简介
                ico_icocrunch_detail.setShortDescription(StringUtils.defaultIfEmpty(brief,""));

                Elements iconamers = iconame.getElementsByClass("iconamer");
                if(CollectionUtils.isNotEmpty(iconamers)){
                    Element iconamer = iconamers.first();
                    Elements names = iconamer.getElementsByTag("h1");//名称
                    Elements categories = iconamer.getElementsByTag("a");//类别

                    if (CollectionUtils.isNotEmpty(names)){
                        Element name = names.first();
                        ico_icocrunch_detail.setIcoName(StringUtils.defaultIfEmpty(name.text(),""));
                    }
                    if(CollectionUtils.isNotEmpty(categories)){
                        StringBuffer category_names = new StringBuffer();
                        for(Element category:categories){
                            String href = category.attr("href");
                            String category_name = StringUtils.substringBetween(href,"https://icocrunch.io/","/");
                            category_names.append(category_name+" ");
                        }
                        ico_icocrunch_detail.setCategories(category_names.toString());
                    }
                }
            }
            *//*获取soclink*//*
            Elements soclinks = doc.getElementsByClass("soclink");
            if(CollectionUtils.isNotEmpty(soclinks)){
                for(Element soclink:soclinks){
                    String href = StringUtils.defaultIfEmpty(soclink.attr("href"),"");//link
                    Elements imgs = soclink.getElementsByTag("img");//更具图片的后缀名判断social的类型
                    if(CollectionUtils.isNotEmpty(imgs)){
                        String src = imgs.first().attr("src");
                        String type = StringUtils.substring(src,src.lastIndexOf("/")+1,src.lastIndexOf(".svg"));

                        if("tg".equals(type)){
                            ico_icocrunch_detail.setTelegram(href);
                        }else if("fb".equals(type)){
                            ico_icocrunch_detail.setFacebook(href);
                        }else if("bit".equals(type)){
                            ico_icocrunch_detail.setBitcointalk(href);
                        }else if("tw".equals(type)){
                            ico_icocrunch_detail.setTwitter(href);
                        }else if("git".equals(type)){
                            ico_icocrunch_detail.setGitHub(href);
                        }else if("wp".equals(type)){
                            ico_icocrunch_detail.setWhitepaper(href);
                        }else if ("med".equals(type)){
                            ico_icocrunch_detail.setMedium(href);
                        }else{
                           log.info("未知的social类型");
                        }
                    }else{
                        continue;
                    }
                }
            }
            *//*获取Funding*//*
            Elements internals = doc.getElementsByAttributeValue("class","tds fg1");
            for(Element internal:internals){
                Element key = internal.previousElementSibling();
                Element value = internal.nextElementSibling();
                String key_text = key.text();
                String value_text = StringUtils.defaultIfEmpty(value.text(),"");
                if("Token".equals(key_text)){
                    ico_icocrunch_detail.setTokenNameOrTicker(value_text);
                }else if("Hard cap".equals(key_text)){
                    ico_icocrunch_detail.setHardCapUsd(value_text);
                }else if("Price on ICO, eth".equals(key_text)){
                    ico_icocrunch_detail.setPriceEth(value_text);
                }else if("Price on ICO, usd".equals(key_text)){
                    ico_icocrunch_detail.setPriceUsd(value_text);
                }else if("Max bonus, %".equals(key_text)){
                    ico_icocrunch_detail.setMaxBonus(value_text);
                }else if("Rised".equals(key_text)){
                    ico_icocrunch_detail.setRised(value_text);
                }
            }
            *//*获取dates*//*
            Elements datecontainers = doc.getElementsByClass("datecontainer");
            if(CollectionUtils.isNotEmpty(datecontainers)){
                Element datecontainer = datecontainers.first();
                Elements tds = datecontainer.getElementsByClass("tds");
                for(Element td:tds){
                    Element key = td.previousElementSibling();
                    Element value = td.nextElementSibling();
                    String key_text = key.text();
                    String value_text = StringUtils.defaultIfEmpty(value.text(),"");
                    if("Whitelisting".equals(key_text)){
                        ico_icocrunch_detail.setWhitelistDate(value_text);
                    }else if("KYC".equals(key_text)){
                        ico_icocrunch_detail.setKycDate(value_text);
                    }else if("PreICO".equals(key_text)){
                        ico_icocrunch_detail.setPreicoDate(value_text);
                    }else if("ICO".equals(key_text)){
                        ico_icocrunch_detail.setIcoDate(value_text);
                    }
                }
            }
            *//*获取block介绍*//*
            Elements abouts = doc.getElementsByAttributeValue("class","row mt-4");
            for(Element about:abouts){
                 String about_text = about.text();
                 if(StringUtils.contains(about_text,"About")){
                     Element description  = about.nextElementSibling();
                     String description_text = StringUtils.defaultIfEmpty(description.text(),"") ;
                     ico_icocrunch_detail.setIcoProjectDescription(description_text);
                     break;
                 }
            }

            *//*获取Block网址*//*
            Elements websites = doc.getElementsByAttributeValue("class", "box-shadow ws text-white text-center btn");
            if(CollectionUtils.isNotEmpty(websites)){
                Element website = websites.first();
                String website_url = website.attr("onclick");
                website_url = StringUtils.substringBetween(website_url,"onLinkClick('","')");
                ico_icocrunch_detail.setIcoWebsite(website_url);
            }
            ico_icocrunch_detail.setIcoCrunchUrl(blockUrl);
            ico_icocrunch_detail.setInsertTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
            ico_icocrunch_detail.setModifyTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
            ico_icocrunch_listDao.updateIco_icocrunch_listByBlockUrl(String.valueOf(code),blockUrl);
            ico_icocrunch_detailDao.save(ico_icocrunch_detail);
            log.info("Block详情抓取成功："+blockUrl);
        }else{
            ico_icocrunch_listDao.updateIco_icocrunch_listByBlockUrl(String.valueOf(code),blockUrl);
            log.info("Block详情抓取失败："+blockUrl+",errorCode:"+code);
        }

    }

    @Override
    public Long getIco_icocrunch_listMaxSerialNumber(String show) {
        return ico_icocrunch_listDao.getIco_icocrunch_listMaxSerialNumber(show);
    }

    @Override
    public Ico_icocrunch_list getNextPageIco_icocrunch_list(String show,long serialNumber) {
        Ico_icocrunch_list ico_icocrunch_list = ico_icocrunch_listDao.findTop1ByBlockTypeAndSerialNumberOrderByCurrentPageDesc(show,serialNumber);
        ico_icocrunch_list.setCurrentPage(ico_icocrunch_list.getCurrentPage()+1);
        return ico_icocrunch_list;
    }

    @Override
    public List<String> getIco_icocrunch_listByDetailStatus(String detaiStatus) {
        return ico_icocrunch_listDao.getIco_icocrunch_listByDetailsStatus(detaiStatus);
    }

    @Override
    public JSONObject getIco_icocrunch_detailPageable(int page_number,int pageSize) {
        Pageable pageable = PageRequest.of(page_number,pageSize);
        Page<Ico_icocrunch_detail> page = ico_icocrunch_detailDao.getIco_icocrunch_detailPageable(pageable);
        JSONObject result = new JSONObject();
        result.put("totalPages",page.getTotalPages());
        result.put("number",page.getTotalElements());

        JSONObject solution_data = new JSONObject();
        for(Ico_icocrunch_detail detail:page.getContent()){
            *//*组装指定格式的Json数据*//*
            JSONObject block = new JSONObject();
            block.put("name",detail.getIcoName());
            block.put("token_name",detail.getTokenNameOrTicker());
            block.put("website",detail.getIcoWebsite());
            block.put("white_paper",detail.getWhitepaper());

            JSONObject social = new JSONObject();
            social.put("bitcointalk",detail.getBitcointalk());
            social.put("github",detail.getGitHub());
            social.put("medium",detail.getMedium());
            social.put("telegram",detail.getTelegram());
            social.put("twitter",detail.getTwitter());

            block.put("social",social);

            solution_data.put(detail.getIcoCrunchUrl(),block);
        }
        result.put("solution_data",solution_data);
        result.put("source","icocrunch.io");
        return result;
    }

    @Override
    public Ico_icocrunch_detail getIco_icocrunch_detailByICOCrunchUrl(String icoCrunchUrl) {
       return  ico_icocrunch_detailDao.getIco_icocrunch_detailByICOCrunchUrl(icoCrunchUrl);
    }*/

    @Override
    public void getIco_coinschedule_detail(Ico_coinschedule_List item) {
        String url = item.getIcoCoinscheduleUrl();
        try {
            Request request = new Request(url, RequestMethod.GET);
            Response response = HttpClientUtil.doRequest(request);
            int code = response.getCode();
            //验证请求
            if (code == 200) {
                String content = response.getResponseText();
                // 验证页面
                if (StringUtils.isNotBlank(content)) {
                    // 验证是否是正常页面
                    if (content.contains("prj-title")) {
                        Document doc = Jsoup.parse(content);
                        Elements titleles = doc.select("div.info-container > h1.prj-title");
                        if (titleles != null && titleles.size() > 0) {
                            String titlestr = titleles.text().trim();
                            log.info("titlestr:" + titlestr);
                            String title = "";
                            String tag = "";
                            if (titlestr.contains("(") && titleles.contains(")")) {
                                title = StringUtils.substringBefore(titlestr, "(");
                                tag = StringUtils.substringBetween(titlestr, "(", ")");
                            }
                            log.info("title :" + title);
                            log.info("tag:" + tag);
                        }
                        Elements photoeles = doc.select("div.logo-container > img");
                        if (photoeles != null && photoeles.size() > 0) {
                            String photourl = photoeles.attr("src");
                            log.info("photourl:" + photourl);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
