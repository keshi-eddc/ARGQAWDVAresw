package com.edmi.service.serviceImp.coinschedule;

import com.edmi.dao.coinschedule.*;
import com.edmi.entity.coinschedule.*;
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
    private ICO_coinschedule_detailDao ico_coinschedule_detailDao;
    @Autowired
    private ICO_coinschedule_detail_icoinfoDao ico_coinschedule_detail_icoinfoDao;
    @Autowired
    private ICO_coinschedule_detail_sociallinkDao ico_coinschedule_detail_sociallinkDao;
    @Autowired
    private ICO_coinschedule_detail_memberDao ico_coinschedule_detail_memberDao;
    @Autowired
    private ICO_coinschedule_icos_listDao ico_coinschedule_icos_listDao;
    @Autowired
    private ICO_coinschedule_detail_member_sociallinkDao ico_coinschedule_detail_member_sociallinkDao;

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
            log.info("----- request code:" + code);
            if (code == 200) {
                String content = response.getResponseText();
                // 验证页面
                if (StringUtils.isNotBlank(content)) {
                    // 验证是否是正常页面
                    if (content.contains("prj-title")) {
                        Document doc = Jsoup.parse(content);
                        ICO_coinschedule_detail detailModel = extraDetails(doc, item);
                        //解析ico信息
                        extraIcoInfo(doc, detailModel);
                        //解析社交链接
                        extraSocialLink(doc, detailModel);
                        //解析人员信息
                        extraMember(doc, detailModel);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析详情
     *
     * @param doc
     * @param item
     */
    public ICO_coinschedule_detail extraDetails(Document doc, Ico_coinschedule_List item) {
        ICO_coinschedule_detail detailModel = new ICO_coinschedule_detail();

        try {
            detailModel.setIco_coinschedule_list(item);
            detailModel.setLink(item.getIcoCoinscheduleUrl());
            detailModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            detailModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));

            Elements titleles = doc.select("div.info-container > h1.prj-title");
            if (titleles != null && titleles.size() > 0) {
                String titlestr = titleles.text().trim();
//                            log.info("titlestr:" + titlestr);
                String title = "";
                String tag = "";
                if (titlestr.contains("(") && titlestr.contains(")")) {
                    title = StringUtils.substringBefore(titlestr, "(").trim();
                    tag = StringUtils.substringBetween(titlestr, "(", ")").trim();
                }
//                            log.info("title :" + title);
//                            log.info("tag:" + tag);
                detailModel.setIco_name(title);
                detailModel.setIco_tag(tag);
            }
            //website
            Elements websiteles = doc.select("div.actions-bar > a.website-link");
            if (websiteles != null && websiteles.size() > 0) {
                String website = websiteles.attr("href").trim();
//                            log.info("website:" + website);
                detailModel.setWebsite(website);
            }
            //description
            Elements descriptioneles = doc.select("div.widget >div.project-description");
            if (descriptioneles != null && descriptioneles.size() > 0) {
                String description = descriptioneles.text().trim();
//                            log.info("description:" + description);
                detailModel.setIco_description(description);
            }
            //tags div.content-section > div.widget
            Elements tagseles = doc.select("div.content-section > div.widget");
            if (tagseles != null && tagseles.size() > 0) {
                for (Element ele : tagseles) {
                    Elements tageles = ele.select("h5");
                    if (tageles != null && tageles.size() > 0) {
                        String tagtitle = tageles.text().trim();
                        if (tagtitle.equals("Tags")) {
//                                        log.info("tagtitle:" + tagtitle);
                            Elements tagveles = ele.select("div.ui.label.ui-tag");
                            if (tagveles != null && tagveles.size() > 0) {
                                StringBuffer sbff = new StringBuffer();
                                for (Element tagvele : tagveles) {
                                    String tagvaltemp = tagvele.text().trim();
                                    sbff.append(tagvaltemp).append("#&#");
                                }
                                String tagval = sbff.toString();
                                if (tagval.endsWith("#&#")) {
                                    tagval = StringUtils.substringBeforeLast(tagval, "#&#");
                                }
//                                            log.info("tagval:" + tagval);
                                detailModel.setTags(tagval);
                            }
                        }
                    }
                }
            }

            //div.tab-pane div.content-item > div.timer-container.timewrapper1
            //判断有无 preico date
            Elements predateeles = doc.select("div.tab-content.widget > div.tab-pane");
            if (predateeles != null && predateeles.size() > 0) {
                //其他
                Elements sectionseles = predateeles.select("div.content-item > div.ui.label,h5.ui.header,div.ui.label");
                if (sectionseles != null && sectionseles.size() > 0) {
                    int sw = 0;
                    StringBuffer restrictedSB = new StringBuffer();
                    String tokens_for_sale = "";
                    for (Element ele : sectionseles) {
                        String seClassStr = ele.attr("class");
//                                    log.info("seClassStr:" + seClassStr);
                        String val = ele.text().trim();
//                                    log.info("val:" + val);
                        if (seClassStr.equals("ui header")) {
                            if (val.equals("Restricted Countries")) {
                                sw = 1;
                            } else if (val.equals("Tokens for sale")) {
                                sw = 2;
                            }
                        }
                        if (sw == 1) {
                            restrictedSB = restrictedSB.append(val).append("#&#");
                        } else if (sw == 2) {
                            tokens_for_sale = val;
                        }
                    }
                    String restricted_countries = restrictedSB.toString();
                    if (restricted_countries.endsWith("#&#")) {
                        restricted_countries = StringUtils.substringBeforeLast(restricted_countries, "#&#");
                    }
                    if (restricted_countries.contains("Restricted Countries#&#")) {
                        restricted_countries = restricted_countries.replace("Restricted Countries#&#", "");
                    }
//                                log.info("restricted_countries:" + restricted_countries);
//                                log.info("tokens_for_sale:" + tokens_for_sale);
                    detailModel.setRestricted_countries(restricted_countries);
                    detailModel.setTokens_for_sale(tokens_for_sale);
                }

                //时间
                if (predateeles.size() == 1) {
                    Elements strateles = predateeles.select("div.timer-container.timewrapper1.start-date.startclock span.date-text");
                    if (strateles != null && strateles.size() > 0) {
                        String start_date = strateles.text().trim();
//                                    log.info("start_date:" + start_date);
                        detailModel.setStart_date(start_date);
                    }
                    Elements endeles = predateeles.select("div.timer-container.timewrapper1.end-date.endclock span.date-text");
                    if (endeles != null && endeles.size() > 0) {
                        String end_date = endeles.text().trim();
//                                    log.info("end_date:" + end_date);
                        detailModel.setEnd_date(end_date);
                    }
                } else if (predateeles.size() == 2) {
                    //含有 pre date，两个时间
                    //0 pre
                    Element predateOnele = predateeles.get(0);
                    if (predateOnele != null) {
                        Elements strateles = predateOnele.select("div.timer-container.timewrapper1.start-date.startclock span.date-text");
                        if (strateles != null && strateles.size() > 0) {
                            String pre_start_date = strateles.text().trim();
//                                        log.info("pre_start_date:" + pre_start_date);
                            detailModel.setPre_start_date(pre_start_date);
                        }
                        Elements endeles = predateOnele.select("div.timer-container.timewrapper1.end-date.endclock span.date-text");
                        if (endeles != null && endeles.size() > 0) {
                            String pre_end_date = endeles.text().trim();
//                                        log.info("pre_end_date:" + pre_end_date);
                            detailModel.setPre_end_date(pre_end_date);
                        }
                    }

                    //1 normal
                    Element predateTwoles = predateeles.get(1);
                    if (predateTwoles != null) {
                        Elements strateles = predateTwoles.select("div.timer-container.timewrapper2.start-date.startclock span.date-text");
                        if (strateles != null && strateles.size() > 0) {
                            String start_date = strateles.text().trim();
//                                        log.info("start_date:" + start_date);
                            detailModel.setStart_date(start_date);
                        }
                        Elements endeles = predateTwoles.select("div.timer-container.timewrapper2.end-date.endclock span.date-text");
                        if (endeles != null && endeles.size() > 0) {
                            String end_date = endeles.text().trim();
//                                        log.info("end_date:" + end_date);
                            detailModel.setEnd_date(end_date);
                        }
                    }
                }
            }
            ICO_coinschedule_detail oldDetail = ico_coinschedule_detailDao.findICO_coinschedule_detailByLink(detailModel.getLink());
            if (null == oldDetail) {
                ico_coinschedule_detailDao.save(detailModel);
            } else {
                log.info("--- this coinschedule_detail is already existed ");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return detailModel;

    }

    /**
     * 解析ico信息
     *
     * @param doc
     * @param detailModel
     */
    //detail 模型存入数据库，才会有 pkid
    public void extraIcoInfo(Document doc, ICO_coinschedule_detail detailModel) {
        List<ICO_coinschedule_detail_icoinfo> infoList = new ArrayList<>(50);
        try {
            Elements infoeles = doc.select("div.container>div.content-section >div.widget:nth-child(1)>ul.characteristics-list>li");
            if (infoeles != null && infoeles.size() > 0) {
                for (Element linele : infoeles) {
                    ICO_coinschedule_detail_icoinfo infoModel = new ICO_coinschedule_detail_icoinfo();
                    infoModel.setIco_coinschedule_detail(detailModel);
                    infoModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    infoModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    Elements titleles = linele.select("span.title");
                    String title = "";
                    if (titleles != null && titleles.size() > 0) {
                        title = titleles.text();
                    }
                    Elements texteles = linele.select("span.text");
                    String text = "";
                    if (texteles != null && texteles.size() > 0) {
                        text = texteles.text();
                        Elements urleles = texteles.select("a.projectLink");
                        if (urleles != null && urleles.size() > 0) {
                            text = urleles.attr("href");
                        }
                    }
//                    log.info("-- " + title + " = " + text);
                    infoModel.setIco_key(title);
                    infoModel.setIco_value(text);
                    infoList.add(infoModel);
                }
                ico_coinschedule_detail_icoinfoDao.saveAll(infoList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * //解析社交链接
     */
    public void extraSocialLink(Document doc, ICO_coinschedule_detail detailModel) {
        try {
            Elements sociallinkeles = doc.select("div.container>div.content-section >div.widget");
            if (sociallinkeles != null && sociallinkeles.size() > 0) {
                List<ICO_coinschedule_detail_sociallink> sociallinkList = new ArrayList<>(50);
                for (Element sectionele : sociallinkeles) {
                    Elements titleles = sectionele.select("h3.section-title");
                    if (titleles != null && titleles.size() > 0) {
                        String title = titleles.text().trim();
//                        log.info("title:" + title);
                        if (title.equals("Links")) {
                            Elements sociallinkeseles = sectionele.select("div.socials-list-container li>a");
                            if (sociallinkeseles != null && sociallinkeseles.size() > 0) {
                                for (Element linkele : sociallinkeseles) {
                                    ICO_coinschedule_detail_sociallink sociallinkModel = new ICO_coinschedule_detail_sociallink();
                                    sociallinkModel.setIco_coinschedule_detail(detailModel);
                                    sociallinkModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                    sociallinkModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                    String key = linkele.text().trim();
                                    String link = linkele.attr("href").trim();

//                                    log.info(key + "=" + link);
                                    sociallinkModel.setSocial_link_key(key);
                                    sociallinkModel.setSocial_link_value(link);
                                    sociallinkList.add(sociallinkModel);
                                }
                            }
                        }
                    }
                }
                ico_coinschedule_detail_sociallinkDao.saveAll(sociallinkList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析人员信息
     *
     * @param doc
     * @param detailModel
     */
    public void extraMember(Document doc, ICO_coinschedule_detail detailModel) {
        try {
            Elements membersectioneles = doc.select("div.container>div.content-section >div.widget");
            if (membersectioneles != null && membersectioneles.size() > 0) {
                for (Element sectionele : membersectioneles) {
                    Elements titleles = sectionele.select("h4");
                    if (titleles != null && titleles.size() > 0) {
                        String title = titleles.text().trim();
//                        log.info("title:" + title);
                        if (title.contains("Team") || title.contains("Advisors")) {
                            List<ICO_coinschedule_detail_member> memberList = new ArrayList<>(50);
                            Elements memberparteles = sectionele.select("h4.header +div.stackable");
                            if (memberparteles != null && memberparteles.size() > 0) {
                                for (int i = 0; i < memberparteles.size(); i++) {
                                    String type = "";
                                    if (i == 0) {
                                        type = "Team";
                                    } else if (i == 1) {
                                        type = "Advisors";
                                    }
//                                    log.info("----------------" + type);
                                    Element mempartele = memberparteles.get(i);
                                    Elements Memberseles = mempartele.select("a.ui.card");
                                    if (Memberseles != null && Memberseles.size() > 0) {
                                        for (Element oneMemberele : Memberseles) {
                                            ICO_coinschedule_detail_member memberModel = new ICO_coinschedule_detail_member();
                                            memberModel.setIco_coinschedule_detail(detailModel);
                                            memberModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                            memberModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                            memberModel.setMember_type(type);
//                                            log.info("------");
                                            String member_url = oneMemberele.attr("href");
//                                            log.info("member_url:" + member_url);
                                            memberModel.setMember_url(member_url);
                                            Elements imgeles = oneMemberele.select("img");
                                            if (imgeles != null && imgeles.size() > 0) {
                                                String member_photo_url = imgeles.attr("src");
//                                                log.info("member_photo_url:" + member_photo_url);
                                                memberModel.setMember_photo_url(member_photo_url);
                                            }
                                            //name
                                            Elements nameles = oneMemberele.select("div.header");
                                            if (nameles != null && nameles.size() > 0) {
                                                String member_name = nameles.text().trim();
//                                                log.info("member_name:" + member_name);Andrew Sazama ✔
                                                member_name = member_name.replaceAll("✔", "");
                                                memberModel.setMember_name(member_name);
                                            }
                                            //position
                                            Elements positioneles = oneMemberele.select("div.meta > span");
                                            if (positioneles != null && positioneles.size() > 0) {
                                                String position = positioneles.text().trim();
//                                                log.info("position:" + position);
                                                memberModel.setMember_position(position);
                                            }
                                            //description
                                            Elements descriptioneles = oneMemberele.select("div.description.people-description");
                                            if (descriptioneles != null && descriptioneles.size() > 0) {
                                                String description = descriptioneles.text().trim();
//                                                log.info("description:" + description);
                                                memberModel.setMember_description(description);
                                            }
                                            memberList.add(memberModel);
                                        }
                                    }

                                }
                            }
                            ico_coinschedule_detail_memberDao.saveAll(memberList);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getIcoCoinscheduleICOsList() {
        log.info("***** strat getIcoCoinscheduleICOsList task");
        try {
            String url = "https://www.coinschedule.com/icos.html";
            Request request = new Request(url, RequestMethod.GET);
            Response response = HttpClientUtil.doRequest(request);
            int code = response.getCode();
            //验证请求
            if (code == 200) {
                String content = response.getResponseText();
                // 验证页面
                if (StringUtils.isNotBlank(content)) {
                    // 验证是否是正常页面
                    if (content.contains("tbody")) {
                        Document doc = Jsoup.parse(content);
                        Elements lineseles = doc.select("table.dataTable > tbody >tr");
                        if (lineseles != null && lineseles.size() > 0) {
                            List<ICO_coinschedule_icos_list> icosLists = new ArrayList<>();
                            for (Element linele : lineseles) {
                                ICO_coinschedule_icos_list listModel = new ICO_coinschedule_icos_list();
                                listModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                listModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                Elements tdseles = linele.select("td");
                                if (tdseles != null && tdseles.size() > 0) {
                                    if (tdseles.size() == 4) {
                                        String name = tdseles.get(0).text().trim();
                                        String category = tdseles.get(1).text().trim();
                                        String endedOn = tdseles.get(2).text().trim();
                                        String totalRaised = tdseles.get(3).text().trim();
//                                        log.info(name + " " + category + " " + endedOn + " " + totalRaised);
                                        listModel.setName(name);
                                        listModel.setCategory(category);
                                        listModel.setEnded_on(endedOn);
                                        listModel.setTotal_raised(totalRaised);
                                        ICO_coinschedule_icos_list oldicolistModel = ico_coinschedule_icos_listDao.findICO_coinschedule_icos_listByName(name);
                                        if (oldicolistModel == null) {
                                            log.info("----- insert new date");
                                            icosLists.add(listModel);
//                                            ico_coinschedule_icos_listDao.save(listModel);
                                        } else {
                                            log.info("----- update old date");
                                            oldicolistModel.setCategory(category);
                                            oldicolistModel.setEnded_on(endedOn);
                                            oldicolistModel.setTotal_raised(totalRaised);
                                            oldicolistModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                            icosLists.add(oldicolistModel);
//                                            ico_coinschedule_icos_listDao.save(oldicolistModel);
                                        }
                                    }
                                }
                            }
                            ico_coinschedule_icos_listDao.saveAll(icosLists);
                            log.info("***** getIcoCoinscheduleICOsList task over");
                        }
                    } else {
                        log.error("un normal");
                    }
                }
            } else {
                log.error("!!!bad request:" + url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    @Async("myTaskAsyncPool")
    @Override
    public void getIcoCoinscheduleMemberSocialLink(ICO_coinschedule_detail_member member) {
//        log.info("----- getIcoCoinscheduleMemberSocialLink");
        try {
            String url = member.getMember_url();
            Request request = new Request(url, RequestMethod.GET);
            Response response = HttpClientUtil.doRequest(request);
            int code = response.getCode();
//            log.info("url: " + url + " = " + code);
            //验证请求
            if (code == 200) {
                String content = response.getResponseText();
                // 验证页面
                if (StringUtils.isNotBlank(content)) {
                    // 验证是否是正常页面
                    if (content.contains("person-title")) {
                        List<ICO_coinschedule_detail_member_sociallink> sociallinkList = new ArrayList<>(10);
                        Document doc = Jsoup.parse(content);
                        Elements sectionseles = doc.select("div.container >div.content-section > div.widget");
                        if (sectionseles != null && sectionseles.size() > 0) {
                            for (Element sectionele : sectionseles) {
                                Elements titleles = sectionele.select("h3");
                                if (titleles != null && titleles.size() > 0) {
                                    String title = titleles.text().trim();
//                                    log.info("title:" + title);
                                    if (title.equals("Social")) {
                                        Elements socialseles = sectionele.select("a");
                                        if (socialseles != null && socialseles.size() > 0) {
                                            for (Element socialele : socialseles) {
                                                log.info("socialele:" + socialele.toString());
                                                String social_link_key = "";
                                                String social_link_value = socialele.attr("href").trim();
                                                Elements keyeles = socialele.select("img");
                                                if (keyeles != null && keyeles.size() > 0) {
                                                    social_link_key = keyeles.attr("alt").trim();
                                                } else {
                                                    log.info("----- social_link_key is null : " + url);
                                                }
                                                log.info(social_link_key + " = " + social_link_value);
                                                if (StringUtils.isNotEmpty(social_link_key) && StringUtils.isNotEmpty(social_link_value)) {
                                                    ICO_coinschedule_detail_member_sociallink sociallinkModel = new ICO_coinschedule_detail_member_sociallink();
                                                    sociallinkModel.setIco_coinschedule_detail_member(member);
                                                    sociallinkModel.setMember_url(member.getMember_url());
                                                    sociallinkModel.setSocial_link_key(social_link_key);
                                                    sociallinkModel.setSocial_link_value(social_link_value);
                                                    sociallinkModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                                    sociallinkModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                                    sociallinkList.add(sociallinkModel);
                                                }
                                            }
                                        } else {
                                            log.info("----- has no Social :" + url);
                                        }
                                    }
                                } else {
                                    log.info("----- has no title:" + url);
                                }
                            }
                            ico_coinschedule_detail_member_sociallinkDao.saveAll(sociallinkList);
                        } else {
                            log.info("----- has no sectionseles:" + url);
                        }
                    } else {
                        log.info("un normal page !!!  " + url);
                    }
                } else {
                    log.error("!!! page is null : " + url);
                }
            } else {
                log.info("!!!bad request");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CoinscheduleSeviceImp t = new CoinscheduleSeviceImp();
        Ico_coinschedule_List item = new Ico_coinschedule_List();
        item.setIcoCoinscheduleUrl("https://www.coinschedule.com/ico/mibcoin");
//        item.setIcoCoinscheduleUrl("https://www.coinschedule.com/ico/kimex-token#event4542");
//        t.getIco_coinschedule_detail(item);
//        t.getIcoCoinscheduleICOsList();
        ICO_coinschedule_detail_member member = new ICO_coinschedule_detail_member();
        member.setMember_url("https://www.coinschedule.com/p/13194/tuomo-tiito");
        t.getIcoCoinscheduleMemberSocialLink(member);
    }
}
