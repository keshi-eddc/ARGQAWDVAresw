package com.edmi.service.serviceImp.icocrunch;

import com.alibaba.fastjson.JSONObject;
import com.edmi.dao.icocrunch.Ico_icocrunch_detailDao;
import com.edmi.dao.icocrunch.Ico_icocrunch_listDao;
import com.edmi.entity.icocrunch.Ico_icocrunch_detail;
import com.edmi.entity.icocrunch.Ico_icocrunch_list;
import com.edmi.service.service.IcocrunchSevice;
import com.edmi.utils.http.HttpClientUtil;
import com.edmi.utils.http.exception.MethodNotSupportException;
import com.edmi.utils.http.request.Request;
import com.edmi.utils.http.request.RequestMethod;
import com.edmi.utils.http.response.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class IcocrunchSeviceImp implements IcocrunchSevice {

    Logger log = Logger.getLogger(IcocrunchSeviceImp.class);

    @Autowired
    private Ico_icocrunch_listDao ico_icocrunch_listDao;
    @Autowired
    private Ico_icocrunch_detailDao ico_icocrunch_detailDao;


    /*
     * show值分别为 ICO、 PreICO
     * */
    public void getIco_icocrunch_list(String show,int currentPage) throws MethodNotSupportException {
        String url = "https://icocrunch.io/page/"+currentPage+"/";
        Request request = new Request(url, RequestMethod.GET);
        request.addUrlParam("reviewed","no");
        request.addUrlParam("show",show);
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode(); //response code
        log.info(url);

        int totalPage = 0;
        if(200 ==code) {
            String content = response.getResponseText(); //response text
            Document doc = Jsoup.parse(content);
            /*分页信息*/
            Elements navigation = doc.getElementsByClass("navigation");
            if(null!=navigation&&navigation.size()>0){
                Element nav = navigation.first();
                Elements pages = nav.getElementsByClass("pages");//获取当前页和总页数
                if(null!=pages&&pages.size()>0){
                    Element page = pages.first();
                    String page_text = page.text();
                    String currentPage_text = StringUtils.trim(StringUtils.substringBetween(page_text,"Page","of"));
                    String totalPage_text = StringUtils.trim(StringUtils.substringAfterLast(page_text,"of"));
                    if(NumberUtils.isDigits(currentPage_text)){
                        log.info("当前页解析成功："+currentPage_text);
                        currentPage = NumberUtils.toInt(currentPage_text);
                    }
                    if(NumberUtils.isDigits(totalPage_text)){
                        log.info("总页数解析成功："+totalPage_text);
                        totalPage = NumberUtils.toInt(totalPage_text);
                    }
                }
            }
            /*block数据列表*/
            Elements tables = doc.getElementsByAttributeValueContaining("class", "row box-shadow table p-3");
            if(null!=tables&&tables.size()>0){
                Element table = tables.first();
                Elements list = table.getElementsByAttributeValue("class", "d-flex my-2");
                if(null!=list&&list.size()>0){
                    List ico_icocrunch_lists = new ArrayList<Ico_icocrunch_list>();
                    for(Element li:list){
                        Ico_icocrunch_list ico_icocrunch_list = new Ico_icocrunch_list();

                        String blockUrl = li.attr("href");

                        ico_icocrunch_list.setBlockUrl(blockUrl);
                        ico_icocrunch_list.setBlockType(show);
                        ico_icocrunch_list.setInsertTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        ico_icocrunch_list.setModifyTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        ico_icocrunch_list.setDetailsStatus("ini");
                        ico_icocrunch_list.setCurrentPage(currentPage);
                        ico_icocrunch_list.setTotalPage(totalPage);

                        Ico_icocrunch_list flag = ico_icocrunch_listDao.findIco_icocrunch_listByBlockUrlAndBlockType(ico_icocrunch_list.getBlockUrl(), ico_icocrunch_list.getBlockType());
                        if(null==flag){
                            ico_icocrunch_lists.add(ico_icocrunch_list);
                        }else{
                            log.info("该block已经存在"+ico_icocrunch_list.getBlockType()+":"+ico_icocrunch_list.getBlockUrl());
                        }
                    }
                    List<Ico_icocrunch_list> ico = ico_icocrunch_listDao.saveAll(ico_icocrunch_lists);
                    log.info("icocrunch,show:"+show+",第"+totalPage+"-"+currentPage+"页保存成功，本次共计保存："+ico.size());
                }
            }
        }else{
            log.info("icocrunch,show:"+show+",第"+currentPage+"请求失败，errorCode："+code);
        }
    }
    @Transactional
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
            /*获取soclink*/
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
            /*获取Funding*/
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
            /*获取dates*/
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
            /*获取block介绍*/
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

            /*获取Block网址*/
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
            /*组装指定格式的Json数据*/
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
    public int getIco_icocrunch_list_total_pages(String show) throws MethodNotSupportException {
        String url = "https://icocrunch.io/page/"+1+"/";
        Request request = new Request(url, RequestMethod.GET);
        request.addUrlParam("reviewed","no");
        request.addUrlParam("show",show);
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode(); //response code
        log.info(url);

        int totalPage = 0;
        if(200 ==code) {
            String content = response.getResponseText(); //response text
            Document doc = Jsoup.parse(content);
            /*分页信息*/
            Elements navigation = doc.getElementsByClass("navigation");
            if (null != navigation && navigation.size() > 0) {
                Element nav = navigation.first();
                Elements pages = nav.getElementsByClass("pages");//获取当前页和总页数
                if (null != pages && pages.size() > 0) {
                    Element page = pages.first();
                    String page_text = page.text();
                    String totalPage_text = StringUtils.trim(StringUtils.substringAfterLast(page_text, "of"));
                    if (NumberUtils.isDigits(totalPage_text)) {
                        log.info("总页数解析成功：" + totalPage_text);
                        totalPage = NumberUtils.toInt(totalPage_text);
                    }
                }
            }
        }
        return totalPage;
    }

    @Override
    public Ico_icocrunch_detail getIco_icocrunch_detailByICOCrunchUrl(String icoCrunchUrl) {
       return  ico_icocrunch_detailDao.getIco_icocrunch_detailByICOCrunchUrl(icoCrunchUrl);
    }
}
