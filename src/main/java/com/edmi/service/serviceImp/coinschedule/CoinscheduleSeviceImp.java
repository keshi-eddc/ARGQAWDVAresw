package com.edmi.service.serviceImp.coinschedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.edmi.dao.coinschedule.*;
import com.edmi.dto.coinschedule.ICO_coinschedule_detailDto;
import com.edmi.entity.coinschedule.*;
import com.edmi.service.service.CoinscheduleService;
import com.edmi.utils.http.HttpClientUtil;
import com.edmi.utils.http.exception.MethodNotSupportException;
import com.edmi.utils.http.request.Request;
import com.edmi.utils.http.request.RequestMethod;
import com.edmi.utils.http.response.Response;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
    @Autowired
    private ICO_coinschedule_detail_milestoneDao ico_coinschedule_detail_milestoneDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
                        org.springframework.beans.BeanUtils.copyProperties(ico_coinschedule_list, list, new String[]{"pkId", "insertTime"});
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
                                    Elements names = colum.getElementsByAttributeValue("target", "_self");//获取block的名字
                                    if (CollectionUtils.isNotEmpty(names)) {
                                        Element name = names.first();
                                        String icoName = StringUtils.defaultIfEmpty(name.ownText(), "");
                                        String icoCoinscheduleUrl = StringUtils.substringBeforeLast(StringUtils.defaultIfEmpty(name.attr("href"), ""), "#event");
                                        list.setIcoName(icoName);
                                        list.setIcoCoinscheduleUrl(icoCoinscheduleUrl);
                                    }
                                    Elements sp_logos = colum.getElementsByClass("sp-logo");
                                    if (CollectionUtils.isNotEmpty(sp_logos)) {
                                        Element sp_logo = sp_logos.first();
                                        Elements imgs = sp_logo.getElementsByTag("img");
                                        if (CollectionUtils.isNotEmpty(imgs)) {
                                            Element img = imgs.first();
                                            String src = img.attr("data-src");
                                            list.setBlockLogo(src);
                                        }
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
                                    Elements sp_logos = colum.getElementsByClass("sp-logo");
                                    if (CollectionUtils.isNotEmpty(sp_logos)) {
                                        Element sp_logo = sp_logos.first();
                                        Elements imgs = sp_logo.getElementsByTag("img");
                                        if (CollectionUtils.isNotEmpty(imgs)) {
                                            Element img = imgs.first();
                                            String src = img.attr("data-src");
                                            list.setBlockLogo(src);
                                        }
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


    @Override
    public void getIco_coinschedule_detail(Ico_coinschedule_List item) {
        String url = item.getIcoCoinscheduleUrl();
        if (StringUtils.isNotEmpty(url)) {
            ICO_coinschedule_detail oldDetail = ico_coinschedule_detailDao.findICO_coinschedule_detailByLink(url);
            if (oldDetail == null) {
                try {
                    Request request = new Request(url, RequestMethod.GET);
                    Response response = HttpClientUtil.doRequest(request);
                    int code = response.getCode();
                    //验证请求
                    log.info("- request: " + url + " ,code:" + code);
                    if (code == 200) {
                        String content = response.getResponseText();
                        // 验证页面
                        if (StringUtils.isNotBlank(content)) {
                            // 验证是否是正常页面
                            if (content.contains("prj-title")) {
                                Document doc = Jsoup.parse(content);
                                ICO_coinschedule_detail detailModel = extraDetails(doc, item);

                            }
                        }
                    } else {
                        log.error("!!! bad request," + code + " - " + url);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                log.info("- this Ico_coinschedule_List item has already extra.do not extra.");
            }
        }
    }

    /**
     * 解析详情
     *
     * @param doc
     * @param item
     */
    public ICO_coinschedule_detail extraDetails(Document doc, Ico_coinschedule_List item) {
        log.info("- extraDetails");
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
            //logo
            Elements logoeles = doc.select("div.company-info >div.logo-container >img");
            if (logoeles != null && logoeles.size() > 0) {
                String logo = logoeles.attr("src");
//                log.info("logo :" + logo);
                detailModel.setLogo_url(logo);
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
                //解析ico信息
                extraIcoInfo(doc, detailModel);
                //解析社交链接
                extraSocialLink(doc, detailModel);
                //解析人员信息
                extraMember(doc, detailModel);
                //解析milestone
                extraMilestone(doc, detailModel);

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
        log.info("- extraIcoInfo");
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
        log.info("- extraSocialLink");
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
     * 2018年9月11日11:54:19
     * 增加解析milestone
     *
     * @param doc
     * @param detailModel
     */
    public void extraMilestone(Document doc, ICO_coinschedule_detail detailModel) {
        log.info("- extraMilestone");
        try {
            Elements sectionseles = doc.select("div.container>div.content-section >div.widget");
            if (sectionseles != null && sectionseles.size() > 0) {
                for (Element sectionele : sectionseles) {
                    Elements titleles = sectionele.select("h3");
                    if (titleles != null && titleles.size() > 0) {
                        String title = titleles.text().trim();
                        if (title.equals("Milestones") || title.equals("Milestone")) {
                            List<ICO_coinschedule_detail_milestone> milestonesList = new ArrayList<>(50);
                            //解析 Milestones
                            Elements lineseles = sectionele.select("ul.ul-milestones > li");
                            if (lineseles != null && lineseles.size() > 0) {
                                String key = "";
                                String val = "";
                                for (Element linele : lineseles) {
                                    Elements keyeles = linele.select("span.title");
                                    if (keyeles != null && keyeles.size() > 0) {
                                        key = keyeles.text().trim();
                                    }
                                    Elements valeles = linele.select("p.text");
                                    if (valeles != null && valeles.size() > 0) {
                                        val = valeles.text().trim();
                                    }
//                                    log.info(key + " = " + val);
                                    if (StringUtils.isNotEmpty(key)) {
                                        ICO_coinschedule_detail_milestone milestoneModel = new ICO_coinschedule_detail_milestone();
                                        milestoneModel.setIco_coinschedule_detail(detailModel);
                                        milestoneModel.setMilestone_key(key);
                                        milestoneModel.setMilestone_value(val);
                                        milestoneModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                        milestoneModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));

                                        milestonesList.add(milestoneModel);
                                    }
                                }
                            }
                            ico_coinschedule_detail_milestoneDao.saveAll(milestonesList);
                        }
                    }
                }
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
        log.info("- extraMember");
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
                                        } else {
                                            log.info("- this ICO_coinschedule_icos_list is alreadt existed.do not insert.");
                                            //更新的功能
//                                            log.info("----- update old date");
//                                            oldicolistModel.setCategory(category);
//                                            oldicolistModel.setEnded_on(endedOn);
//                                            oldicolistModel.setTotal_raised(totalRaised);
//                                            oldicolistModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
//                                            icosLists.add(oldicolistModel);
                                        }
                                    }
                                }
                            }
                            ico_coinschedule_icos_listDao.saveAll(icosLists);
//                            log.info("***** getIcoCoinscheduleICOsList task over");
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
        String url = member.getMember_url();
        if (StringUtils.isNotEmpty(url)) {
            List<ICO_coinschedule_detail_member_sociallink> oldMemberSocialLink = ico_coinschedule_detail_member_sociallinkDao.findICO_coinschedule_detail_member_sociallinksByMemberUrl(url);
            if (CollectionUtils.isEmpty(oldMemberSocialLink)) {
                try {
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
//                                                        log.info("socialele:" + socialele.toString());
                                                        String social_link_key = "";
                                                        String social_link_value = socialele.attr("href").trim();
                                                        Elements keyeles = socialele.select("img");
                                                        if (keyeles != null && keyeles.size() > 0) {
                                                            social_link_key = keyeles.attr("alt").trim();
                                                        } else {
                                                            log.info("----- social_link_key is null : " + url);
                                                        }
//                                                        log.info(social_link_key + " = " + social_link_value);
//                                                        if (StringUtils.isNotEmpty(social_link_key) && StringUtils.isNotEmpty(social_link_value)) {}
                                                        ICO_coinschedule_detail_member_sociallink sociallinkModel = new ICO_coinschedule_detail_member_sociallink();
                                                        sociallinkModel.setIco_coinschedule_detail_member(member);
                                                        sociallinkModel.setMemberUrl(member.getMember_url());
                                                        sociallinkModel.setSocial_link_key(social_link_key);
                                                        sociallinkModel.setSocial_link_value(social_link_value);
                                                        sociallinkModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                                        sociallinkModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                                        sociallinkList.add(sociallinkModel);
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
                        log.info("!!!bad request, " + code + " - " + url);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                log.info("- this member has already extra,do not extra.");
            }
        } else {
            log.info("!!! ICO_coinschedule_detail_member getMember_url is null.");
        }
    }

    @Override
    public JSONObject getIco_coinschedule_index(String dataSourceNameLevel2) {
        JSONObject json = new JSONObject();
        if (StringUtils.equalsIgnoreCase("all", dataSourceNameLevel2)) {
            String indexes_sql = "select ifnull(ico_name,'') as ico_name," +
                    "ifnull(pk_id,'') as pk_id," +
                    "ifnull(ico_tag,'') as ico_tag," +
                    "ifnull(website,'') as website," +
                    "ifnull(link,'') as link from ico_coinschedule_detail";
            String socials_sql = "select ifnull(social_link_key,'') as social_link_key," +
                    "ifnull(fk_id,'') as fk_id," +
                    "ifnull(social_link_value,'') as social_link_value from ico_coinschedule_detail_sociallink ";

            List<Map<String, Object>> details = jdbcTemplate.queryForList(indexes_sql);
            List<Map<String, Object>> socials = jdbcTemplate.queryForList(socials_sql);

            JSONObject socials_json = new JSONObject();//组装social信息
            for (Map<String, Object> social : socials) {
                Object itemUrl = social.get("fk_id");
                Object block_lable_name = social.get("social_link_key");
                Object block_lable_url = social.get("social_link_value");
                if (null != itemUrl && null != block_lable_name && null != block_lable_url) {
                    if (socials_json.containsKey(itemUrl.toString())) {
                        socials_json.getJSONObject(itemUrl.toString()).put(block_lable_name.toString(), block_lable_url.toString());
                    } else {
                        JSONObject social_json = new JSONObject();
                        social_json.put(block_lable_name.toString(), block_lable_url.toString());
                        socials_json.put(itemUrl.toString(), social_json);
                    }
                }
            }
            json.put("number", details.size());
            JSONObject solution_data = new JSONObject();
            for (Map<String, Object> detail : details) {

                JSONObject solution_data_url = new JSONObject();
                solution_data_url.put("name", detail.get("ico_name").toString());
                solution_data_url.put("token_name", detail.get("ico_tag").toString());
                solution_data_url.put("website", detail.get("website").toString());

                String pk_id = detail.get("pk_id").toString();

                /*添加socials*/
                if (socials_json.containsKey(pk_id)) {
                    JSONObject social_json = socials_json.getJSONObject(pk_id);
                    JSONObject standardSocials = new JSONObject();
                    for (Map.Entry<String, Object> entry : social_json.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue().toString();
                        if (StringUtils.equalsIgnoreCase("Paper", key)) {
                            solution_data_url.put("white_paper", value);
                        } else if (StringUtils.equalsIgnoreCase("Website", key)) {
                            solution_data_url.put("website", value);
                        } else {
                            standardSocials.put(StringUtils.lowerCase(key), value);
                        }

                    }
                    solution_data_url.put("social", standardSocials);
                }
                solution_data.put(detail.get("link").toString(), solution_data_url);
            }
            json.put("solution_data", solution_data);
            json.put("source", "coinschedule.com." + dataSourceNameLevel2);
        } else if (StringUtils.equalsIgnoreCase("icos", dataSourceNameLevel2)) {
            String indexes_sql = "select ifnull(name,'') as name," +
                    "ifnull(category,'') as category," +
                    "ifnull(ended_on,'') as ended_on," +
                    "ifnull(total_raised,'') as total_raised from ico_coinschedule_icos_list";
            List<Map<String, Object>> details = jdbcTemplate.queryForList(indexes_sql);
            json.put("number", details.size());
            JSONObject solution_data = new JSONObject();
            for (Map<String, Object> detail : details) {

                JSONObject solution_data_url = new JSONObject();
                solution_data_url.put("name", JSON.toJSON(detail));

                solution_data.put(detail.get("name").toString(), solution_data_url);
            }
            json.put("solution_data", solution_data);
            json.put("source", "icorating.com." + dataSourceNameLevel2);
        }
        return json;
    }

    @Override
    public JSONObject getICO_coinschedule_detailByItemUrl(String url) {

        JSONObject json = new JSONObject();
        ICO_coinschedule_detail detail = ico_coinschedule_detailDao.findICO_coinschedule_detailByLink(url);
        if (null != detail) {
            List<ICO_coinschedule_detail_icoinfo> icoinfos = ico_coinschedule_detail_icoinfoDao.getICO_coinschedule_detail_icoinfosByFkid(detail.getPk_id());
            List<ICO_coinschedule_detail_member> members = ico_coinschedule_detail_memberDao.getICO_coinschedule_detail_membersByFkid(detail.getPk_id());
            /*开始组装ICO详细数据*/
            ICO_coinschedule_detailDto detailDto = new ICO_coinschedule_detailDto();
            JSONObject ico_detail = new JSONObject();//ico的所有详细信息
            try {
                BeanUtils.copyProperties(detailDto, detail);
                ico_detail.putAll(BeanUtils.describe(detailDto));

                if (CollectionUtils.isNotEmpty(icoinfos)) {
                    for (ICO_coinschedule_detail_icoinfo icoinfo : icoinfos) {
                        ico_detail.put(icoinfo.getIco_key(), icoinfo.getIco_value());
                    }
                }
                /*从ico_detail中提取出概况:name,whitePaperURL,tag,about,brief,description,prototype*/
                JSONObject ico_about = new JSONObject();
                if (ico_detail.containsKey("ico_name")) {
                    ico_about.put("name", ico_detail.getString("ico_name"));
                    ico_detail.remove("ico_name");
                }
                if (ico_detail.containsKey("tags")) {
                    ico_about.put("tag", ico_detail.getString("tags"));
                    ico_detail.remove("tags");
                }
                if (ico_detail.containsKey("ico_description")) {
                    ico_about.put("description", ico_detail.getString("ico_description"));
                    ico_detail.remove("ico_description");
                }
                if (ico_detail.containsKey("White Paper")) {
                    ico_about.put("whitePaperURL", ico_detail.getString("White Paper"));
                    ico_detail.remove("White Paper");
                }
                ico_about.put("about", "");
                ico_about.put("brief", "");
                ico_about.put("prototype", "");
                /*下面处理Block的logo*/
                if (ico_detail.containsKey("logo_url")) {
                    ico_detail.put("solution_photo_url", detail.getIco_coinschedule_list().getBlockLogo());
                    ico_detail.remove("logo_url");
                } else {
                    ico_detail.put("solution_photo_url", "");
                }
                if (ico_detail.containsKey("Bitcoin Talk")) {
                    ico_detail.remove("Bitcoin Talk");
                }
                ico_detail.remove("class");
                json.putAll(ico_about);//把提取出来的概况添加进去
                json.put("ico", ico_detail);//剩下的详细信息添加到ico里面


                /*组装member信息*/
                JSONArray members_json = new JSONArray();
                if (CollectionUtils.isNotEmpty(members)) {
                    for (ICO_coinschedule_detail_member member : members) {
                        JSONObject member_json = new JSONObject();
                        member_json.put("memberURL", member.getMember_url());
                        member_json.put("memberName", member.getMember_name());
                        member_json.put("memberResponsibility", member.getMember_position());
                        member_json.put("memberRole", member.getMember_type());
                        List<ICO_coinschedule_detail_member_sociallink> member_sociallinks = member.getMemberSociallinkList();
                        JSONObject member_social = new JSONObject();
                        for (ICO_coinschedule_detail_member_sociallink sociallink : member_sociallinks) {
                            String key = sociallink.getSocial_link_key();
                            String value = sociallink.getSocial_link_value();
                            if (StringUtils.equalsIgnoreCase("LinkedIn", key)) {
                                member_social.put("linkedin", value);
                            } else if (StringUtils.equalsIgnoreCase("Facebook", key)) {
                                member_social.put("facebook", value);
                            } else if (StringUtils.equalsIgnoreCase("Twitter", key)) {
                                member_social.put("twitter", value);
                            }
                        }
                        member_json.put("member_social", member_social);
                        members_json.add(member_json);
                    }
                }
                json.put("member", members_json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            json.remove("class");


        }
        return json;
    }

    public static void main(String[] args) {
        CoinscheduleSeviceImp t = new CoinscheduleSeviceImp();
        Ico_coinschedule_List item = new Ico_coinschedule_List();
        item.setIcoCoinscheduleUrl("https://www.coinschedule.com/ico/mibcoin");
//        item.setIcoCoinscheduleUrl("https://www.coinschedule.com/ico/kimex-token#event4542");
//        t.getIco_coinschedule_detail(item);
//        t.getIcoCoinscheduleICOsList();
//        ICO_coinschedule_detail_member member = new ICO_coinschedule_detail_member();
//        member.setMember_url("https://www.coinschedule.com/p/13194/tuomo-tiito");
//        t.getIcoCoinscheduleMemberSocialLink(member);

        String url = "https://www.coinschedule.com/ico/b66#event4859";
        try {
            Request request = new Request(url, RequestMethod.GET);
            Response response = HttpClientUtil.doRequest(request);
            int code = response.getCode();
            System.out.println("code:" + code);
            String content = response.getResponseText();
            Document doc = Jsoup.parse(content);
            t.extraMilestone(doc, null);
        } catch (MethodNotSupportException e) {
            e.printStackTrace();
        }

    }
}
