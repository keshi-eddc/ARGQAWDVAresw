package com.edmi.service.serviceImp.icodrops;

import com.edmi.dao.icodrops.ICO_icodrops_detailRepository;
import com.edmi.dao.icodrops.ICO_icodrops_detail_socialLinkRepository;
import com.edmi.dao.icodrops.ICO_icodrops_detail_tokenInfoRepository;
import com.edmi.dao.icodrops.ICO_icodrops_listRepository;
import com.edmi.entity.icodrops.ICO_icodrops_detail;
import com.edmi.entity.icodrops.ICO_icodrops_detail_socialLink;
import com.edmi.entity.icodrops.ICO_icodrops_detail_tokenInfo;
import com.edmi.entity.icodrops.ICO_icodrops_list;
import com.edmi.service.service.IcodropsService;
import com.edmi.utils.http.HttpClientUtil;
import com.edmi.utils.http.request.Request;
import com.edmi.utils.http.request.RequestMethod;
import com.edmi.utils.http.response.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 实现接口
 */
@Service("IcodropsService")
public class IcodropsServiceImp implements IcodropsService {
    Logger log = Logger.getLogger(IcodropsServiceImp.class);
    @Autowired
    ICO_icodrops_listRepository icodropsListDao;
    @Autowired
    ICO_icodrops_detailRepository icodropsDetailDao;
    @Autowired
    private ICO_icodrops_detail_socialLinkRepository socialLinkDao;
    @Autowired
    private ICO_icodrops_detail_tokenInfoRepository tokenDao;

    @Override
    public void getIcodropsListWithInput(String inputUrl) {
        log.info(" ***** Strart get icodrops list with input url:" + inputUrl);
        String input_type = StringUtils.substringBeforeLast(inputUrl, "/");
        input_type = StringUtils.substringAfterLast(input_type, "/");
        log.info("input_type:" + input_type);
        try {
            Request request = new Request(inputUrl, RequestMethod.GET);
            Response response = HttpClientUtil.doRequest(request);
            int code = response.getCode();
            //验证请求
            if (code == 200) {
                String content = response.getResponseText();
                // 验证页面
                if (StringUtils.isNotBlank(content)) {
                    // 验证是否是正常页面
                    if (content.contains("container")) {
                        Document doc = Jsoup.parse(content);
                        Elements tabseles = doc.select("div.container > div.tabs__content");
                        if (tabseles != null && tabseles.size() > 0) {
                            log.info("-含有：" + tabseles.size() + "个表");
                            for (Element tabeles : tabseles) {
                                Elements table_categoryeles = tabeles.select("h3.col-md-12.col-12.not_rated");
                                if (table_categoryeles != null && table_categoryeles.size() > 0) {
                                    String table_category = table_categoryeles.text().trim();
                                    Elements linesles = tabeles.select("div.col-md-12.col-12.a_ico");
                                    log.info("----表 " + table_category + " ，有 : " + linesles.size() + " 行");
                                    if (linesles != null && linesles.size() > 0) {
                                        for (int i = 0; i < linesles.size(); i++) {
                                            ICO_icodrops_list listModel = new ICO_icodrops_list();
                                            listModel.setInsertTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                            listModel.setUpdateTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                            listModel.setCrawledStatu("ini");
                                            listModel.setInput_type(input_type);
                                            listModel.setTable_type(table_category);

                                            Element linele = linesles.get(i);
                                            log.info("----------------------- " + i);
                                            //Project
                                            Elements projecteles = linele.select("div.ico-row > div.ico-main-info > h3 > a");
                                            if (projecteles != null && projecteles.size() > 0) {
                                                String ico_name = projecteles.text().trim();
                                                String ico_url = projecteles.attr("href").trim();
//                                                log.info("ico_name:" + ico_name);
//                                                log.info("ico_url:" + ico_url);
                                                listModel.setIco_name(ico_name);
                                                listModel.setIco_url(ico_url);
                                            }
                                            //ico_photo_url
                                            Elements photoeles = linele.select("div.ico-row > div.ico-icon > a > img");
                                            if (photoeles != null && photoeles.size() > 0) {
                                                String ico_photo_url = photoeles.attr("data-src");
                                                if (StringUtils.isNotEmpty(ico_photo_url)) {
                                                    if (!ico_photo_url.contains("icodrops.com")) {
                                                        ico_photo_url = "icodrops.com" + ico_photo_url;
                                                    }
                                                    if (!ico_photo_url.contains("https:")) {
                                                        ico_photo_url = "https:" + ico_photo_url;
                                                    }
//                                                    log.info("----- >>>>> ico_photo_url:" + ico_photo_url);
                                                    listModel.setIco_photo_url(ico_photo_url);
                                                }
                                            }
                                            //Interest
                                            Elements interesteles = linele.select("div.interest");
                                            if (interesteles != null && interesteles.size() > 0) {
                                                String interest = interesteles.text().trim();
//                                                log.info("interest:" + interest);
                                                listModel.setInterest(interest);
                                            }
                                            //Category
                                            Elements categ_typeles = linele.select("div.categ_type");
                                            if (categ_typeles != null && categ_typeles.size() > 0) {
                                                String categ_type = categ_typeles.text().trim();
//                                                log.info("categ_type:" + categ_type);
                                                listModel.setCateg_type(categ_type);
                                            }
                                            //Received
                                            Elements receivedeles = linele.select("div#new_column_categ_invisted > span");
                                            if (receivedeles != null && receivedeles.size() > 0) {
                                                int size = receivedeles.size();
                                                String received = "";
                                                String received_percent = "";
                                                if (size == 2) {
                                                    Element rele = receivedeles.first();
                                                    Element rpele = receivedeles.last();
                                                    received = rele.text().trim();
                                                    received_percent = rpele.text().trim();
                                                } else if (size == 1) {
                                                    received = receivedeles.text().trim();
                                                }
//                                                log.info("received:" + received);
//                                                log.info("received_percent:" + received_percent);
                                                listModel.setReceived(received);
                                                listModel.setReceived_percent(received_percent);
                                            }
                                            //Goal
                                            Elements goaleles = linele.select("div#categ_desctop");
                                            if (goaleles != null && goaleles.size() > 0) {
                                                String goal = goaleles.text().trim();
//                                                log.info("goal:" + goal);
                                                listModel.setGoal(goal);
                                            }
                                            //End Date
                                            Elements dateles = linele.select("div.date");
                                            if (dateles != null && dateles.size() > 0) {
                                                String end_date = dateles.text().trim();
                                                String end_date_time = dateles.attr("data-date");

                                                end_date = end_date.replaceAll("Ended", "").replaceAll(":", "").trim();
//                                                log.info("end_date:" + end_date);
//                                                log.info("end_date_time:" + end_date_time);
                                                if (inputUrl.contains("active") || inputUrl.contains("ended")) {
                                                    //end time only
                                                    listModel.setEnd_date(end_date);
                                                    listModel.setEnd_date_time(end_date_time);
                                                }
                                                if (inputUrl.contains("upcoming")) {
                                                    //start time only
                                                    listModel.setStart_date(end_date);
                                                }
                                            }


                                            //Tags
                                            Elements tagseles = linele.select("div.meta_icon > div.tooltip");
                                            if (tagseles != null && tagseles.size() > 0) {
                                                for (Element tagele : tagseles) {
                                                    String key = tagele.attr("class");
                                                    String val = tagele.attr("title");
//                                                    log.info("- " + key + " = " + val);
                                                    if (inputUrl.contains("active") || inputUrl.contains("upcoming")) {
                                                        //set tags
                                                        if (key.contains("categ_one")) {
                                                            listModel.setTag_one(val);
                                                        } else if (key.contains("categ_two")) {
                                                            listModel.setTag_two(val);
                                                        } else if (key.contains("categ_three")) {
                                                            listModel.setTag_three(val);
                                                        } else if (key.contains("categ_four")) {
                                                            listModel.setTag_four(val);
                                                        } else if (key.contains("categ_five")) {
                                                            listModel.setTag_five(val);
                                                        }
                                                    } else if (inputUrl.contains("ended")) {
                                                        //set Market only
                                                        String market = tagseles.text().trim();
                                                        market = market.replaceAll("Ticker", "").replaceAll(":", "");
                                                        listModel.setMarket(market);
                                                    }
                                                }
                                            }
                                            icodropsListDao.save(listModel);

//                                            ICO_icodrops_list oldlist = icodropsListDao.getICO_icodrops_listByIco_url(listModel.getIco_url());
//                                            if (null == oldlist) {
//                                                icodropsListDao.save(listModel);
//                                            } else {
//                                                log.info("---- this item is already existed, do not insert into icodrops_list table");
//                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        log.error(" !!! page is not usually");
                    }
                } else {
                    log.error("!!! page null :" + inputUrl);
                }
            } else {
                log.error(" !!! bad request ,url:" + inputUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    @Async("myTaskAsyncPool")注解实现多线程
//    @Async("myTaskAsyncPool")
    @Override
    public void getIcodropsDetail(ICO_icodrops_list item) {
        log.info("--- extra icodrops detail");
        String url = item.getIco_url();
        String inputtype = item.getInput_type();
        ICO_icodrops_detail oldDetailModel = icodropsDetailDao.getICO_icodrops_detailByLink(url);
        if (null == oldDetailModel) {
            log.info("-- will extra :" + inputtype + " / " + url);
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
                        if (content.contains("ico-row")) {
                            Document doc = Jsoup.parse(content);
                            //解析详情
                            ICO_icodrops_detail detailModel = extraDetail(doc, item);
                            //解析社交链接
                            extraSocialLink(doc, detailModel);
                            //解析token信息
                            extraTokenInfo(doc, detailModel);
                            //解析完成
                            item.setCrawledStatu("200");
                            icodropsListDao.save(item);
                        } else {
                            log.info("--- this is unnormal page");
                        }
                    }
                } else {
                    log.error("!!! bad request,the code is :" + code);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.info("--- this icodrops item has extraed already .");
        }
    }

    /**
     * 解析详情
     *
     * @param doc
     * @param item
     */
    public ICO_icodrops_detail extraDetail(Document doc, ICO_icodrops_list item) {
        ICO_icodrops_detail detailModel = new ICO_icodrops_detail();
        detailModel.setIco_icodrops_list(item);
        detailModel.setLink(item.getIco_url());
        detailModel.setIco_name(item.getIco_name());
        detailModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
        detailModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));

        Elements descriptioneles = doc.select("div.white-desk.ico-desk > div.ico-row > div.ico-main-info div.ico-description");
        if (descriptioneles != null && descriptioneles.size() > 0) {
            String ico_description = descriptioneles.text().trim();
            if (StringUtils.isNotEmpty(ico_description)) {
//                log.info("--- ico_description:" + ico_description);
                detailModel.setIco_description(ico_description);
            }
        }
        icodropsDetailDao.save(detailModel);
        return detailModel;
    }

    public void extraSocialLink(Document doc, ICO_icodrops_detail detailModel) {
        List<ICO_icodrops_detail_socialLink> socialLinkList = new ArrayList<>(20);
        Elements socialLinkeles = doc.select("div.ico-right-col > div.soc_links > a");
        if (socialLinkeles != null && socialLinkeles.size() > 0) {
            for (Element ele : socialLinkeles) {
                String url = ele.attr("href");
                Elements keyeles = ele.select("i");
                String keystr = "";
                if (keyeles != null && keyeles.size() > 0) {
                    keystr = keyeles.attr("class");
                    if (keystr.contains("fa-")) {
                        keystr = StringUtils.substringAfter(keystr, "fa-");
                        if (keystr.contains("-")) {
                            keystr = StringUtils.substringBefore(keystr, "-");
                        }
                    }
                }
//                log.info("--- " + keystr + " = " + url);
                if (StringUtils.isNotEmpty(keystr) && StringUtils.isNotEmpty(url)) {
                    ICO_icodrops_detail_socialLink socialLinkModel = new ICO_icodrops_detail_socialLink();
                    socialLinkModel.setIco_icodrops_detail(detailModel);
                    socialLinkModel.setLink(detailModel.getLink());
                    socialLinkModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    socialLinkModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    socialLinkModel.setSocial_link_key(keystr);
                    socialLinkModel.setSocial_link_value(url);
                    socialLinkList.add(socialLinkModel);
                }
            }
            //website
            Elements webeles = doc.select("div.ico-row > div.ico-right-col > a");
            if (webeles != null && webeles.size() > 0) {
                for (Element ele : webeles) {
                    ICO_icodrops_detail_socialLink socialLinkModel = new ICO_icodrops_detail_socialLink();
                    String key = ele.text();
                    String value = ele.attr("href");
                    if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
                        socialLinkModel.setIco_icodrops_detail(detailModel);
                        socialLinkModel.setLink(detailModel.getLink());
                        socialLinkModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        socialLinkModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        socialLinkModel.setSocial_link_key(key);
                        socialLinkModel.setSocial_link_value(value);
                        socialLinkList.add(socialLinkModel);
                    }
                }
            }
        }
        socialLinkDao.saveAll(socialLinkList);
    }

    public void extraTokenInfo(Document doc, ICO_icodrops_detail detailModel) {
        List<ICO_icodrops_detail_tokenInfo> tokenList = new ArrayList<>(50);
        Elements tokeneles = doc.select("div.white-desk.ico-desk > div.row.list");
        if (tokeneles != null && tokeneles.size() > 0) {
            for (Element sectionele : tokeneles) {
                Elements titleles = sectionele.select("h4");
                if (titleles != null && titleles.size() > 0) {
                    String titlestr = titleles.text().trim();
//                    log.info("-------- titlestr:" + titlestr);
                    if (titlestr.contains("Token Sale:")) {
                        String tokenSale = StringUtils.substringAfter(titlestr, ":").trim();
                        ICO_icodrops_detail_tokenInfo tokenInfoModel = new ICO_icodrops_detail_tokenInfo();
                        tokenInfoModel.setIco_icodrops_detail(detailModel);
                        tokenInfoModel.setLink(detailModel.getLink());
                        tokenInfoModel.setToken_key("title Token Sale");
                        tokenInfoModel.setToken_value(tokenSale);
                        tokenInfoModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        tokenInfoModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        tokenList.add(tokenInfoModel);

                        Elements valseles = sectionele.select("li");
                        if (valseles != null && valseles.size() > 0) {
                            for (Element valele : valseles) {
                                String val = valele.text().trim();
                                if (StringUtils.isNotEmpty(val)) {
                                    Elements keyeles = valele.select("span.grey");
                                    String key = keyeles.text().trim();
                                    if (StringUtils.isNotEmpty(key)) {
                                        String value = StringUtils.substringAfter(val, key).trim();
                                        key = key.replaceAll(":", "").trim();
//                                        log.info("--------- " + key + " = " + value);
                                        ICO_icodrops_detail_tokenInfo tokenModel = new ICO_icodrops_detail_tokenInfo();
                                        tokenModel.setIco_icodrops_detail(detailModel);
                                        tokenModel.setLink(detailModel.getLink());
                                        tokenModel.setToken_key(key);
                                        tokenModel.setToken_value(value);
                                        tokenModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                        tokenModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                        tokenList.add(tokenModel);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        tokenDao.saveAll(tokenList);
    }
}
