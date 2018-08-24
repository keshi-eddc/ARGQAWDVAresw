package com.edmi.service.serviceImp.icodrops;

import com.edmi.dao.icodrops.ICO_icodrops_listRepository;
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
import java.util.Calendar;

/**
 * 实现接口
 */
@Service("IcodropsService")
public class IcodropsServiceImp implements IcodropsService {
    Logger log = Logger.getLogger(IcodropsServiceImp.class);
    @Autowired
    ICO_icodrops_listRepository icodropsListDao;

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
}
