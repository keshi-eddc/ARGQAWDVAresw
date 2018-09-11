package com.edmi.service.serviceImp.icodrops;

import com.alibaba.fastjson.JSONObject;
import com.edmi.dao.icodrops.ICO_icodrops_detailRepository;
import com.edmi.dao.icodrops.ICO_icodrops_detail_socialLinkRepository;
import com.edmi.dao.icodrops.ICO_icodrops_detail_tokenInfoRepository;
import com.edmi.dao.icodrops.ICO_icodrops_listRepository;
import com.edmi.dto.icodrops.ICO_icodrops_detailDto;
import com.edmi.dto.icodrops.ICO_icodrops_detail_socialLinkDto;
import com.edmi.dto.icodrops.ICO_icodrops_detail_tokenInfoDto;
import com.edmi.dto.icodrops.ICO_icodrops_listDto;
import com.edmi.entity.icodrops.ICO_icodrops_detail;
import com.edmi.entity.icodrops.ICO_icodrops_detail_socialLink;
import com.edmi.entity.icodrops.ICO_icodrops_detail_tokenInfo;
import com.edmi.entity.icodrops.ICO_icodrops_list;
import com.edmi.service.service.IcodropsService;
import com.edmi.utils.http.HttpClientUtil;
import com.edmi.utils.http.request.Request;
import com.edmi.utils.http.request.RequestMethod;
import com.edmi.utils.http.response.Response;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private JdbcTemplate jdbcTemplate;

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

                                            List<ICO_icodrops_list> oldlist = icodropsListDao.getICO_icodrops_listByIco_url(listModel.getIco_url());
                                            if (CollectionUtils.isEmpty(oldlist)) {
                                                log.info("--- insert into ICO_icodrops_list object.");
                                                icodropsListDao.save(listModel);
                                            } else {
                                                for (ICO_icodrops_list old : oldlist) {
                                                    String oldInputType = old.getInput_type();
                                                    String oldTableType = old.getTable_type();
                                                    String oldIcoUrl = old.getIco_url();
                                                    if (listModel.getInput_type().equals(oldInputType)) {
                                                        if (listModel.getTable_type().equals(oldTableType)) {
                                                            if (!listModel.getIco_url().equals(oldIcoUrl)) {
                                                                log.info("---- in /" + oldInputType + "/" + oldTableType + " find new one");
                                                                icodropsListDao.save(listModel);
                                                            } else {
                                                                log.info("---- this item is already existed, do not insert into icodrops_list table");
                                                            }
                                                        }
                                                    }
                                                }
                                            }
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
            String crawledStatu = item.getCrawledStatu();
            if (crawledStatu.equals("ini")) {
                log.info("--- and update it crawledStatu from ini to 200");
                item.setCrawledStatu("200");
                icodropsListDao.save(item);
            }
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

    @Override
    public JSONObject getIco_icodrops_index(String dataSourceNameLevel2) {
        JSONObject result = new JSONObject();//最后返回数据集合

        /*查询基本列表信息*/
        String indexes_sql = "select\n" +
                "  li.ico_url,\n" +
                "  li.ico_name,\n" +
                "  tf.token_key,\n" +
                "  tf.token_value\n" +
                "from ico_icodrops_list li left join ico_icodrops_detail_tokeninfo tf on li.ico_url = tf.link\n" +
                "where tf.token_key = 'Ticker'";
        /*查询social以及token、website的信息*/
        String indexes_social_sql = "select\n" +
                "      li.ico_url,\n" +
                "      so.social_link_key,\n" +
                "      so.social_link_value\n" +
                "from ico_icodrops_list li left join ico_icodrops_detail_tokeninfo tf on li.ico_url = tf.link\n" +
                "      LEFT JOIN ico_icodrops_detail detail ON li.pk_id = detail.fk_id\n" +
                "      LEFT JOIN ico_icodrops_detail_sociallink so ON detail.pk_id = so.fk_id\n" +
                "where tf.token_key = 'Ticker'";

        List<Map<String, Object>> indexes = jdbcTemplate.queryForList(indexes_sql);
        List<Map<String, Object>> socials = jdbcTemplate.queryForList(indexes_social_sql);

        JSONObject socials_json = new JSONObject();//组装social信息
        for (Map<String, Object> social : socials) {
            Object itemUrl = social.get("ico_url");
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

        JSONObject solution_data = new JSONObject();//组装solution_data信息
        for (Map<String, Object> index : indexes) {

            JSONObject block = new JSONObject();
            block.put("name", index.get("ico_name").toString());
            block.put("token_name", index.get("token_value").toString());
            if (socials_json.containsKey(index.get("ico_url").toString())) {
                JSONObject social_json = socials_json.getJSONObject(index.get("ico_url").toString());
                JSONObject standardSocials = new JSONObject();
                for (Map.Entry<String, Object> entry : social_json.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().toString();
                    if (StringUtils.equalsIgnoreCase("WEBSITE", key)) {
                        block.put("website", value);
                    } else if (StringUtils.equalsIgnoreCase("WHITEPAPER", key)) {
                        block.put("white_paper", value);
                    } else if (StringUtils.equalsIgnoreCase("facebook", key)) {
                        standardSocials.put("facebook", value);
                    } else if (StringUtils.equalsIgnoreCase("reddit", key)) {
                        standardSocials.put("reddit", value);
                    } else if (StringUtils.equalsIgnoreCase("twitter", key)) {
                        standardSocials.put("twitter", value);
                    } else if (StringUtils.equalsIgnoreCase("telegram", key)) {
                        standardSocials.put("telegram", value);
                    } else if (StringUtils.equalsIgnoreCase("linkedin", key)) {
                        standardSocials.put("linkedin", value);
                    } else if (StringUtils.equalsIgnoreCase("medium", key)) {
                        standardSocials.put("medium", value);
                    } else if (StringUtils.equalsIgnoreCase("slack", key)) {
                        standardSocials.put("slack", value);
                    } else if (StringUtils.equalsIgnoreCase("btc", key)) {
                        standardSocials.put("btc", value);
                    } else if (StringUtils.equalsIgnoreCase("youtube", key)) {
                        standardSocials.put("youtube", value);
                    } else if (StringUtils.equalsIgnoreCase("github", key)) {
                        standardSocials.put("github", value);
                    } else if (StringUtils.equalsIgnoreCase("weixin", key)) {
                        standardSocials.put("weixin", value);
                    } else if (StringUtils.equalsIgnoreCase("fa", key)) {
                        standardSocials.put("fa", value);
                    }
                }
                block.put("social", standardSocials);
            }
            solution_data.put(index.get("ico_url").toString(), block);
        }
        result.put("number", indexes.size());
        result.put("solution_data", solution_data);
        result.put("source", "icodrops.com");
        return result;
    }

    @Override
    public JSONObject getICO_icodrops_detailByItemUrl(String url) {
        JSONObject about_json = new JSONObject();
        JSONObject detail_json = new JSONObject();
        List<ICO_icodrops_detail> details = icodropsDetailDao.getICO_icodrops_detailByLinkOrOrderByInsert_Time(url);
        if(CollectionUtils.isNotEmpty(details)){
            /*按照状态顺序取其中的一个ended-ico、active-ico、upcoming-ico*/
            ICO_icodrops_detail detail_recent = null;
            for(ICO_icodrops_detail detail:details){
                String input_type = detail.getIco_icodrops_list().getInput_type();
                if(StringUtils.equalsIgnoreCase("ended-ico",input_type)){
                    detail_recent = detail;
                    break;
                }else if(StringUtils.equalsIgnoreCase("active-ico",input_type)){
                    detail_recent = detail;
                    break;
                }else if(StringUtils.equalsIgnoreCase("upcoming-ico",input_type)){
                    detail_recent = detail;
                    break;
                }
            }
            List<ICO_icodrops_detail_socialLink> socialLinks = socialLinkDao.getICO_icodrops_detail_socialLinksByFkid(detail_recent.getPk_id());
            List<ICO_icodrops_detail_tokenInfo> tokenInfos = tokenDao.getICO_icodrops_detail_tokenInfosByFkid(detail_recent.getPk_id());

            /*开始组装数据*/
            ICO_icodrops_detailDto detailDto = new ICO_icodrops_detailDto();
            try {
                BeanUtils.copyProperties(detailDto,detail_recent);
                detail_json.putAll(BeanUtils.describe(detailDto));

                if(CollectionUtils.isNotEmpty(socialLinks)){
                    List<ICO_icodrops_detail_socialLinkDto> socialLinkDtos = new ArrayList<>();
                    for(ICO_icodrops_detail_socialLink socialLink:socialLinks){
                        ICO_icodrops_detail_socialLinkDto socialLinkDto = new ICO_icodrops_detail_socialLinkDto();
                        BeanUtils.copyProperties(socialLinkDto,socialLink);
                        socialLinkDtos.add(socialLinkDto);
                    }
                    /*添加属于detail本身的链接，如website、whitePaper*/
                    if(CollectionUtils.isNotEmpty(socialLinkDtos)){
                        for(ICO_icodrops_detail_socialLinkDto socialLinkDto:socialLinkDtos){
                            String key = socialLinkDto.getSocial_link_key();
                            if(StringUtils.equalsIgnoreCase("WEBSITE",key)||StringUtils.equalsIgnoreCase("WHITEPAPER",key)){
                                detail_json.put(key,socialLinkDto.getSocial_link_value());
                            }
                        }
                    }
                }
                if(CollectionUtils.isNotEmpty(tokenInfos)){
                    List<ICO_icodrops_detail_tokenInfoDto> tokenInfoDtos = new ArrayList<>();
                    for(ICO_icodrops_detail_tokenInfo tokenInfo:tokenInfos){
                        ICO_icodrops_detail_tokenInfoDto tokenInfoDto = new ICO_icodrops_detail_tokenInfoDto();
                        BeanUtils.copyProperties(tokenInfoDto,tokenInfo);
                        tokenInfoDtos.add(tokenInfoDto);
                    }
                    /*添加更多detail的信息*/
                    if(CollectionUtils.isNotEmpty(tokenInfoDtos)){
                        for(ICO_icodrops_detail_tokenInfoDto tokenInfoDto:tokenInfoDtos){
                            String key = tokenInfoDto.getToken_key();
                            if(StringUtils.isNotEmpty(key)){
                                detail_json.put(key,tokenInfoDto.getToken_value());
                            }
                        }
                    }
                }
                /*从list里面提取信息*/
                ICO_icodrops_list list = detail_recent.getIco_icodrops_list();
                ICO_icodrops_listDto listDto = new ICO_icodrops_listDto();

                BeanUtils.copyProperties(listDto,list);
                detail_json.putAll(BeanUtils.describe(listDto));
                /*处理Block的logo*/
                detail_json.put("solution_photo_url",detail_json.getString("ico_photo_url"));
                detail_json.remove("ico_photo_url");

                /*从list里面取bounty  BOUNTY: NO INFORMATION*/
                String bounty = detail_json.getString("tag_four");
                String[] bounty_ky = StringUtils.split(bounty, ":");
                if(null!=bounty_ky&&bounty_ky.length==2){
                    detail_json.put(bounty_ky[0],bounty_ky[1]);
                }
                detail_json.remove("tag_four");

                /*提取kyc*/
                String tag_one = detail_json.getString("tag_one");
                String[] tag_one_ky = StringUtils.split(tag_one, ":");
                if(null!=tag_one_ky&&tag_one_ky.length==2){
                    detail_json.put(tag_one_ky[0],tag_one_ky[1]);
                }
                detail_json.remove("tag_one");
                detail_json.remove("Know Your Customer (KYC)");

                /*处理时间*/
                String end_date = detail_json.getString("end_date");
                String end_date_time = detail_json.getString("end_date_time");
                detail_json.put("ico_end",end_date);
                detail_json.remove("end_date");
                detail_json.remove("end_date_time");

                String start_date = detail_json.getString("start_date");
                detail_json.put("ico_start",start_date);
                detail_json.remove("start_date");

                /*处理participate*/
                String participate_not = detail_json.getString("Сan't participate");
                detail_json.put("can not participate",participate_not);
                detail_json.remove("Сan't participate");

                /*处理Whitelist*/
                String whitelist = detail_json.getString("Whitelist");
                whitelist = StringUtils.substringBefore(whitelist,"(");
                detail_json.put("Whitelist",whitelist);

                /*添加ico状态*/
                detail_json.put("ico_status",detail_recent.getIco_icodrops_list().getInput_type());

                /*处理title Token Sale*/
                if(detail_json.containsKey("title Token Sale")){
                    String token_sale = detail_json.getString("title Token Sale");
                    String[] token_sales = StringUtils.split(token_sale, "–");
                    if(null!=token_sales&&token_sales.length==2){
                        detail_json.put("icoStart",token_sales[0]);
                        detail_json.put("icoEnd",token_sales[1]);
                    }
                }
                /*处理Total Tokens 、 Available for Token Sale*/
                Number total = null;
                if(detail_json.containsKey("Total Tokens")){
                    //每三位以逗号进行分隔。
                    DecimalFormat format = new DecimalFormat(",###");//299,792,458
                    String total_tokens = detail_json.getString("Total Tokens");
                    if(StringUtils.isEmpty(total_tokens)){
                        total_tokens = "0";
                    }
                    total = format.parse(total_tokens);
                }
                Number available = null;
                if(detail_json.containsKey("Available for Token Sale")){
                    //以百分比方式计数，并取两位小数
                    DecimalFormat format = new DecimalFormat("#.##%");
                    String available_for_token_sale = detail_json.getString("Available for Token Sale");
                    if(StringUtils.isEmpty(available_for_token_sale)){
                        available_for_token_sale = "0";
                    }
                    available = format.parse(available_for_token_sale);
                }
                BigDecimal token_for_sale = BigDecimal.valueOf(total.longValue()).multiply(BigDecimal.valueOf(available.doubleValue()));
                detail_json.put("Token for sale",token_for_sale.toString());


                /*从ico_detail中提取出概况:name,whitePaperURL,tag,about,brief,description,prototype*/
                if(detail_json.containsKey("ico_name")){
                    about_json.put("name",detail_json.getString("ico_name"));
                    detail_json.remove("ico_name");
                }else{
                    about_json.put("name","");
                }

                if(detail_json.containsKey("WHITEPAPER")){
                    about_json.put("whitePaperURL",detail_json.getString("WHITEPAPER"));
                    detail_json.remove("WHITEPAPER");
                }else{
                    about_json.put("whitePaperURL","");
                }

                if(detail_json.containsKey("categ_type")){
                    about_json.put("tag",detail_json.getString("categ_type"));
                    detail_json.remove("categ_type");
                }else{
                    about_json.put("tag","");
                }

                about_json.put("about","");

                if(detail_json.containsKey("ico_description")){
                    about_json.put("description",detail_json.getString("ico_description"));
                    detail_json.remove("ico_description");
                }else{
                    about_json.put("description","");
                }

                about_json.put("prototype","");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        detail_json.remove("class");
        about_json.remove("class");
        about_json.put("ico",detail_json);
        return about_json;
    }
}
