package com.edmi.service.serviceImp.icorating;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.edmi.dao.icorating.*;
import com.edmi.dto.icorating.*;
import com.edmi.entity.icorating.*;
import com.edmi.service.service.IcoratingService;
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
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 实现接口
 */
@Service("IcoratingService")
public class IcoratingServiceImp implements IcoratingService {
    static Logger log = Logger.getLogger(IcoratingServiceImp.class);
    // 列表页，断点抓
    static Boolean ListPageBreakPointCrawl = true;
    @Autowired
    private ICO_icorating_listRepository listDao;
    @Autowired
    private ICO_icorating_detailRepository detailDao;
    @Autowired
    private ICO_icorating_detail_block_teamRepository teamDao;
    @Autowired
    private ICO_icorating_detail_block_fundsRepository fundDao;
    @Autowired
    private ICO_icorating_detail_block_developmentRepository developmentDao;
    @Autowired
    private ICO_icorating_funds_listRepository foundsListDao;
    @Autowired
    private ICO_icorating_funds_detailRepository foundsDetailDao;
    @Autowired
    private ICO_icorating_funds_detail_memberRepository foundsMemberDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void getIcotatingList() {
        log.info("Start getIcotatingList ");
        /**
         * https://icorating.com/ico/latest/load/?page=2&all=false&search=&sort=added&direction=desc
         * 按添加时间排序，但不显示全部
         */
        /**
         * https://icorating.com/ico/all/load/?page=1&all=false&search=
         * 默认排序 ，显示全部
         */
        /**
         * https://icorating.com/ico/all/load/?page=11&all=false&search=&sort=added&direction=desc
         * 按添加时间排序，显示全部。自己拼的。
         */
        // 按默认排序抓，全量
        Boolean isNotLast = true;
        int page = 1;
        /**
         * 可实现中断抓取，从数据库查询到，上次抓取批次（最大批次），最大页数。
         *
         */
        Integer maxCrawledTimes = 0;
        if (ListPageBreakPointCrawl) {
            log.info("=== start list task, ListPageBreakPointCrawl ===");
            List<ICO_icorating_list> lastList = null;
            ICO_icorating_list maxCurrentPage = null;
            try {
                maxCrawledTimes = listDao.getMaxCrawledTimes();
                lastList = listDao.getMaxCurrentPageWithMaxCrawledTimes(maxCrawledTimes);
                if (lastList != null && lastList.size() > 0) {
                    maxCurrentPage = lastList.get(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.info("!!! have no maxCrawledTimes or  maxCurrentPage .");
            }
            if (maxCurrentPage != null) {
                int cpage = maxCurrentPage.getCurrentPage();
                int totalPage = maxCurrentPage.getLastPage();
                if (cpage == totalPage) {
                    log.info("---last crawl task is normal over, this time crawl  new");
                } else {
                    log.info("---last crawl task is not normal over ,this time crawl last time 's  maxCurrentPage");
                    log.info("---Continue to the last task , start page:" + cpage);
                    page = cpage;
                }
            }
        }
        if (maxCrawledTimes == null) {
            maxCrawledTimes = 0;
        }
        // page = 16;
        while (isNotLast) {
            String url = "https://icorating.com/ico/all/load/?page=" + page + "&all=false&search=";
            log.info("===Upcoming visit to:" + page + " : " + url);
            try {
                Request request = new Request(url, RequestMethod.GET);
                Response response = HttpClientUtil.doRequest(request);
                int code = response.getCode();
                // 严重请求
                if (code == 200) {
                    String content = response.getResponseText();
                    // 验证页面
                    if (StringUtils.isNotBlank(content)) {
                        // 验证是否是正常页面
                        if (content.contains("id") && content.contains("current_page") && content.contains("last_page")) {
                            // 解析json返回对象
                            List<ICO_icorating_list> itemList = extraListPageJson(content);
                            // 插入数据库前验证是否已经存在
                            // 插入数据库
                            for (ICO_icorating_list item : itemList) {
                                item.setCrawledTimes(maxCrawledTimes + 1);
                                item.setCrawledStatu("ini");
                                ICO_icorating_list itemOld = listDao.getICO_icorating_listByLink(item.getLink());
                                // 在数据库是否已经存在
                                if (itemOld == null) {
                                    log.info("insert into list table");
                                    listDao.save(item);
                                } else {
                                    log.info("the item is already existed.delete and insert new one");
                                    deleteICO_icorating_listByLink(item.getLink());
                                    listDao.save(item);
                                }
                            }
                            // 判断是否是最后一页
                            int current_page = getListPageCurrentPageNum(content);
                            int total_page = getListPageTotalPageNum(content);
                            if (current_page != 0 && total_page != 0) {
                                if (current_page == total_page) {
                                    isNotLast = false;
                                } else {
                                    // 设置下一页url
                                    page = current_page + 1;
                                    // 翻页间隔
                                    try {
                                        Thread.sleep(1 * 1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } else {
                        log.error(" page null :" + url);
                    }
                }
            } catch (MethodNotSupportException e) {
                e.printStackTrace();
            }
            // break;
        }
        log.info("--- icorating all pages has turned .task over.");
    }

    /**
     * 解析一页列表页的json
     * 返回 列表对象
     *
     * @param content
     */
    public List<ICO_icorating_list> extraListPageJson(String content) {
        List<ICO_icorating_list> itemList = new ArrayList<>();
        try {
            JSONObject jo = JSONObject.parseObject(content);
            JSONObject icosjo = jo.getJSONObject("icos");
            int current_page = icosjo.getInteger("current_page");
            int last_page = icosjo.getInteger("last_page");

            JSONArray icojos = icosjo.getJSONArray("data");
            log.info("----------current_page has : " + icojos.size() + " items");

            for (int i = 0; i < icojos.size(); i++) {
                ICO_icorating_list item = new ICO_icorating_list();

                JSONObject linejo = icojos.getJSONObject(i);
                String name = "";
                if (linejo.containsKey("name")) {
                    name = linejo.getString("name");
                    item.setName(name);
                }
                String ticker = "";
                if (linejo.containsKey("ticker")) {
                    ticker = linejo.getString("ticker");
                    item.setTicker(ticker);
                }
                String name_short = "";
                if (linejo.containsKey("name_short")) {
                    name_short = linejo.getString("name_short");
                    item.setNameShort(name_short);
                }
                String link = "";
                if (linejo.containsKey("link")) {
                    link = linejo.getString("link");
                    item.setLink(link);
                }
                String logo = "";
                if (linejo.containsKey("logo")) {
                    logo = linejo.getString("logo");
                    if (StringUtils.isNotBlank(logo)) {
                        if (!logo.contains("https://icorating.com")) {
                            logo = "https://icorating.com" + logo;
                        }
                        item.setLogo(logo);
                    }
                }
                String status = "";
                if (linejo.containsKey("status")) {
                    status = linejo.getString("status");
                    item.setStatus(status);
                }
                String hype_score_text = "";
                if (linejo.containsKey("hype_score_text")) {
                    hype_score_text = linejo.getString("hype_score_text");
                    item.setHypeScoreText(hype_score_text);
                }
                String risk_score_text = "";
                if (linejo.containsKey("risk_score_text")) {
                    risk_score_text = linejo.getString("risk_score_text");
                    item.setRiskScoreText(risk_score_text);

                }
                String basic_review_link = "";
                if (linejo.containsKey("basic_review_link")) {
                    basic_review_link = linejo.getString("basic_review_link");
                    item.setBasicReviewLink(basic_review_link);
                }
                String investment_rating_text = "";
                if (linejo.containsKey("investment_rating_text")) {
                    investment_rating_text = linejo.getString("investment_rating_text");
                    item.setInvestmentRatingText(investment_rating_text);
                }
                String investment_rating_link = "";
                if (linejo.containsKey("investment_rating_link")) {
                    investment_rating_link = linejo.getString("investment_rating_link");
                    item.setInvestmentRatingLink(investment_rating_link);
                }
                String post_ico_rating = "";
                if (linejo.containsKey("post_ico_rating")) {
                    post_ico_rating = linejo.getString("post_ico_rating");
                    item.setPostIcoRating(post_ico_rating);
                }
                String raised = "";
                if (linejo.containsKey("raised")) {
                    try {
                        raised = linejo.getString("raised");
                        if (StringUtils.isNotBlank(raised)) {
                            raised = raised.replaceAll(",", "").trim();
                            item.setRaised(Float.parseFloat(raised));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(0);
                    }
                }
                String raised_percent = "";
                if (linejo.containsKey("raised_percent")) {
                    raised_percent = linejo.getString("raised_percent").trim();
                    if (StringUtils.isNotBlank(raised_percent)) {
                        raised_percent = raised_percent.replaceAll(" ", "").trim();
                        item.setRaisedPercent(Float.parseFloat(raised_percent));
                    }
                }
                item.setCurrentPage(current_page);
                item.setLastPage(last_page);
                item.setInsertTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
                item.setUpdateTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
                itemList.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("extraListPageJson erro !");
        }
        return itemList;
    }

    /**
     * 获得当前页数
     *
     * @param content
     * @return
     */
    public int getListPageCurrentPageNum(String content) {
        int current_page = 0;
        try {
            JSONObject jo = JSONObject.parseObject(content);
            JSONObject icosjo = jo.getJSONObject("icos");
            current_page = icosjo.getInteger("current_page");
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("=====current_page:" + current_page);
        return current_page;
    }

    public int getListPageTotalPageNum(String content) {
        int totalPage = 0;
        try {
            JSONObject jo = JSONObject.parseObject(content);
            JSONObject icosjo = jo.getJSONObject("icos");
            totalPage = icosjo.getInteger("last_page");
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("=====totalPage:" + totalPage);
        return totalPage;
    }

    @Override
    @Transactional
    public int deleteICO_icorating_listByLink(String link) {
        return listDao.deleteICO_icorating_listByLink(link);
    }

    // =================detail=======================
    @Override
    public void getIcoratingDetail(ICO_icorating_list item) {
        // 查数据库有没有该详情
        String itemUrl = item.getLink();
        if (StringUtils.isNotBlank(itemUrl)) {
            ICO_icorating_detail oldDetai = detailDao.getICO_icorating_detailByLink(itemUrl);
            if (oldDetai == null) {
                log.info("extra detail");
                Request request = null;
                try {
                    request = new Request(itemUrl, RequestMethod.GET);
                } catch (MethodNotSupportException e) {
                    e.printStackTrace();
                }
                Response response = HttpClientUtil.doRequest(request);
                int code = response.getCode();
                if (code == 200) {
                    // 验证页面
                    String content = response.getResponseText();
                    if (StringUtils.isNotBlank(content)) {
                        // 验证页面是否正常
                        if (content.contains("heading") && content.contains("ico-card")) {
                            // 开始页面解析
                            //解析详情页面
                            ICO_icorating_detail detail = extraDetails(content, item);
                            if (detail != null) {
                                //解析详情页的人员
                                extraTeam(content, detail);
                                //解析金融
                                extraFund(content, detail);
                                //解析发展
                                extraDevelopment(content, detail);
                            } else {
                                log.info("--- extra detail ,return null . do not extra others");
                            }

                        } else {
                            log.info("page is not usually");
                        }
                    } else {
                        log.info(" page is null ");
                    }
                } else {
                    log.info("requst code is not 200 ,code:" + code);
                }

            } else {
                //已经存在，如果状态还是ini，改200
                log.info("---------- allread exist the detail URL ：" + itemUrl);
                String crwledstatus = item.getCrawledStatu();
                if (crwledstatus.equals("ini")) {
                    //更改item status
                    item.setCrawledStatu("200");
                    listDao.save(item);
                    log.info("---------- and change it's  item status 200");
                }
            }
        } else {
            log.error("--- item url is null");
        }
    }

    // 解析详情
    public ICO_icorating_detail extraDetails(String content, ICO_icorating_list item) {
        Document doc = Jsoup.parse(content);
        ICO_icorating_detail detailModel = new ICO_icorating_detail();
        try {
            detailModel.setLink(item.getLink());
            // fkid
            detailModel.setIco_icorating_list(item);
            // block_name
            // block_tag
            Elements blockeles = doc.select("div.o-grid__cell > div.o-media > div.o-media__body");
            if (blockeles != null && blockeles.size() > 0) {
                Elements nameles = blockeles.select("h1");
                String name = nameles.text().trim();
                Elements tageles = blockeles.select("p");
                String tag = tageles.text().trim();
                detailModel.setBlock_name(name);
                detailModel.setBlock_tag(tag);
            }
            // block_overview 注意此处的css
            Elements block_overvieweles = doc.select("#ico-card > div:nth-child(2) > div.o-grid__cell.o-grid__cell--width-100.o-grid__cell:nth-child(1) > *:not(.mb40)");
            if (block_overvieweles != null && block_overvieweles.size() > 0) {
                String str = block_overvieweles.text();
                if (str.startsWith("Overview Share")) {
                    str = StringUtils.substringAfter(str, "Overview Share").trim();
                }
                detailModel.setBlock_overview(str);
            }
            // contacts_facebook
            // contacts_twitter
            // contacts_reddit_alien
            // contacts_medium
            // contacts_github
            // contacts_instagram
            // contacts_telegram_plane
            // contacts_youtube
            // contacts_website
            Elements righteles = doc.select("#ico-card > div:nth-child(2) > div.o-grid__cell.o-grid__cell--width-100:nth-child(2) > div.mb20");
            if (righteles != null && righteles.size() > 0) {
                for (Element ele : righteles) {
                    // System.out.println(ele.toString());
                    Elements sectiontitle = ele.select("div.c-heading.c-heading--xsmall");
                    if (sectiontitle != null && sectiontitle.size() > 0) {
                        String title = sectiontitle.text();
                        if (title.contains("Contacts")) {
                            // System.out.println(ele.toString());
                            Elements sectioneles = ele.select("div.c-social-icons > a");
                            if (sectioneles != null && sectioneles.size() > 0) {
                                for (Element sele : sectioneles) {
                                    String sectionurl = sele.attr("href");

                                    Elements keyeles = sele.select("i");
                                    String keystr = "";
                                    if (keyeles != null && keyeles.size() > 0) {
                                        keystr = keyeles.attr("class");
                                    }
                                    if (keystr.contains("facebook")) {
                                        detailModel.setContacts_facebook(sectionurl);
                                    } else if (keystr.contains("twitter")) {
                                        detailModel.setContacts_twitter(sectionurl);
                                    } else if (keystr.contains("reddit")) {
                                        detailModel.setContacts_reddit_alien(sectionurl);
                                    } else if (keystr.contains("medium")) {
                                        detailModel.setContacts_medium(sectionurl);
                                    } else if (keystr.contains("github")) {
                                        detailModel.setContacts_github(sectionurl);
                                    } else if (keystr.contains("instagram")) {
                                        detailModel.setContacts_instagram(sectionurl);
                                    } else if (keystr.contains("telegram")) {
                                        detailModel.setContacts_telegram_plane(sectionurl);
                                    } else if (keystr.contains("youtube")) {
                                        detailModel.setContacts_youtube(sectionurl);
                                    }
                                    // System.out.println("key:" + keystr);
                                    // System.out.println(sectionurl);
                                }

                            }
                            // website
                            Elements webeles = ele.select("a.c-button.c-button--teal.c-button--block.mt15.cp");
                            if (webeles != null && webeles.size() > 0) {
                                String websiteurl = webeles.attr("href");
                                detailModel.setContacts_website(websiteurl);
                                // System.out.println("websiteurl:" + websiteurl);
                            }
                        }
                    }

                    // trading_start_ico
                    // trading_end_ico
                    // trading_token
                    // trading_price
                    // trading_MVP
                    // trading_registration
                    // trading_whitepaper
                    // trading_basicReview
                    Elements tradeles = ele.select("table.c-card-info__table > tbody > tr");
                    if (tradeles != null && tradeles.size() > 0) {
                        for (Element linele : tradeles) {
                            String key = linele.select("th").text().trim();
                            String val = linele.select("td").text().trim();
                            // System.out.println(key + " = " + val);
                            if (key.contains("Start ICO")) {
                                detailModel.setTrading_start_ico(val);
                            } else if (key.contains("End ICO")) {
                                detailModel.setTrading_end_ico(val);
                            } else if (key.contains("Token")) {
                                detailModel.setTrading_token(val);
                            } else if (key.contains("Price")) {
                                detailModel.setTrading_price(val);
                            } else if (key.contains("MVP")) {
                                detailModel.setTrading_MVP(val);
                            } else if (key.contains("Registration Country")) {
                                detailModel.setDetail_legal_registrationCountry(val);
                            } else if (key.contains("Whitepaper")) {
                                Elements wheles = linele.select("td > a");
                                if (wheles != null && wheles.size() > 0) {
                                    String url = wheles.attr("href");
                                    if (!url.contains("https://icorating.com")) {
                                        url = "https://icorating.com" + url;
                                    }
                                    detailModel.setTrading_whitepaper(url);
                                }
                            } else if (key.contains("Basic Review")) {
                                Elements wheles = linele.select("td > a");
                                if (wheles != null && wheles.size() > 0) {
                                    String url = wheles.attr("href");
                                    if (!url.contains("https://icorating.com")) {
                                        url = "https://icorating.com" + url;
                                    }
                                    detailModel.setTrading_basicReview(url);
                                }
                            }
                        }
                    }
                }
            }
            // detail_tokenSale_icoStartDate
            // detail_tokenSale_icoEndDate
            // detail_tokenSale_raised
            // detail_legal_IcoPlatform
            // detail_legal_registrationCountry
            // detail_tokenDetails_ticker
            // detail_tokenDetails_AdditionalTokenEmission
            // detail_tokenDetails_AcceptedCurrencies
            // detail_tokenDetails_TokenDistribution
            Elements detaileles = doc.select("div.c-tabs.tabs.mb-brand.mb40 > div.c-tabs__content >div#details");
            if (detaileles != null && detaileles.size() > 0) {
                Elements tableles = detaileles.select("table.c-info-table.c-info-table--va-top");
                if (tableles != null && tableles.size() > 0) {
                    for (Element tele : tableles) {
                        String tabletitle = tele.select("caption").text().trim();
//                        System.out.println("tabletitle:" + tabletitle);
                        Elements tabdetaileles = tele.select("tbody > tr");
                        if (tabdetaileles != null && tabdetaileles.size() > 0) {
                            if (tabletitle.contains("Token Sale")) {
                                // Token Sale表
                                for (Element linele : tabdetaileles) {
                                    String key = linele.select("th").text().trim();
                                    String val = linele.select("td").text().trim();
                                    // System.out.println(key + " = " + val);
                                    if (key.contains("ICO start date")) {
                                        detailModel.setDetail_tokenSale_icoStartDate(val);
                                    } else if (key.contains("ICO end date")) {
                                        detailModel.setDetail_tokenSale_icoEndDate(val);
                                    } else if (key.contains("Raised")) {
                                        detailModel.setDetail_tokenSale_raised(val);
                                    } else if (key.contains("ICO token supply")) {
                                        detailModel.setDetail_tokenSale_ICOTokenSupply(val);
                                    } else if (key.contains("Soft cap")) {
                                        detailModel.setDetail_tokenSale_softCap(val);
                                    }
                                }

                            } else if (tabletitle.contains("Legal")) {
                                // Legal 表
                                for (Element linele : tabdetaileles) {
                                    String key = linele.select("th").text().trim();
                                    String val = linele.select("td").text().trim();
                                    // System.out.println(key + " = " + val);
                                    if (key.contains("ICO Platform")) {
                                        detailModel.setDetail_legal_IcoPlatform(val);
                                    } else if (key.contains("Registration Country")) {
                                        detailModel.setDetail_legal_registrationCountry(val);
                                    } else if (key.contains("Country Limitations")) {
                                        detailModel.setDetail_legal_countryLimitations(val);
                                    } else if (key.contains("Registration Year")) {
                                        detailModel.setDetail_legal_registrationYear(val);
                                    }
                                }

                            } else if (tabletitle.contains("Token details")) {
                                // Token details 表
                                for (Element linele : tabdetaileles) {
                                    String key = linele.select("th").text().trim();
                                    String val = linele.select("td").text().trim();
//                                    System.out.println(key + " = " + val);
                                    if (key.contains("Ticker")) {
                                        detailModel.setDetail_tokenDetails_ticker(val);
                                    } else if (key.contains("Additional Token Emission")) {
                                        detailModel.setDetail_tokenDetails_AdditionalTokenEmissionv(val);
                                    } else if (key.contains("Accepted Currencies")) {
                                        detailModel.setDetail_tokenDetails_AcceptedCurrencies(val);
                                    } else if (key.contains("Token distribution")) {
                                        detailModel.setDetail_tokenDetails_TokenDistribution(val);
                                    } else if (key.contains("Type")) {
                                        detailModel.setDetail_tokenDetails_type(val);
                                    } else if (key.contains("Bonus Program")) {
                                        detailModel.setDetail_tokenDetails_bonusProgram(val);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            detailModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            detailModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            detailDao.save(detailModel);
            //更改item status
            item.setCrawledStatu("200");
            listDao.save(item);
        } catch (Exception e) {
            //更改item status
            item.setCrawledStatu("extra details exception");
            listDao.save(item);
            e.printStackTrace();
            log.info("extra details exception");
        }
        return detailModel;
    }


    /**
     * 解析公司人员
     *
     * @param content
     * @param detail
     */
    public void extraTeam(String content, ICO_icorating_detail detail) {
        log.info("-extraTeam");
        try {
            Document doc = Jsoup.parse(content);
            Elements detaileles = doc.select("div.c-tabs.tabs.mb-brand.mb40 > div.c-tabs__content >div#team");
            if (detaileles != null && detaileles.size() > 0) {
                List<ICO_icorating_detail_block_team> teamList = new ArrayList<>(100);
                Elements teameles = detaileles.select("div.c-table-container");
                if (teameles != null && teameles.size() > 0) {
                    for (int i = 0; i < teameles.size(); i++) {
                        String member_type = "";
                        if (i == 0) {
                            member_type = "Team members";
                        } else if (i == 1) {
                            member_type = "Advisors";
                        }
//                    System.out.println("member_type:" + member_type);
                        Element sectionele = teameles.get(i);
                        Elements linesles = sectionele.select("tbody > tr.c-table-custom__row");
                        for (Element linele : linesles) {
                            ICO_icorating_detail_block_team team = new ICO_icorating_detail_block_team();
                            //2
                            team.setIco_icorating_detail(detail);
                            //3
                            team.setLink(detail.getLink());
                            team.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            team.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            Elements nameles = linele.select("div.c-card-media__heading > a");
                            if (nameles != null && nameles.size() > 0) {
                                String name = nameles.text().trim();
                                String memberurl = nameles.attr("href");
//                            System.out.println("name:" + name);
//                            System.out.println("memberurl:" + memberurl);
                                //4
                                team.setMember_name(name);
                                //5
                                team.setMember_url(memberurl);
                            }
                            //人头像
                            Elements menberImageles = linele.select("div.o-media__image.visible-medium > a > img");
                            if (menberImageles != null && menberImageles.size() > 0) {
                                String member_photo_url = menberImageles.attr("src").trim();
                                if (StringUtils.isNotBlank(member_photo_url)) {
                                    if (!member_photo_url.contains("https://icorating.com")) {
                                        member_photo_url = "https://icorating.com" + member_photo_url;
                                        team.setMember_photo_url(member_photo_url);
                                    }
                                }
                            }
                            Elements tdeles = linele.select("td.c-table-custom__cell");
                            // System.out.println("tdeles size :" + tdeles.size());
                            if (tdeles != null && tdeles.size() > 0) {
                                int tdsize = tdeles.size();
                                if (tdsize == 4) {
                                    // 第二列
                                    Element positionele = tdeles.get(1);
                                    String position = positionele.text().trim();
//                                System.out.println("position:" + position);
                                    //6
                                    team.setMember_position(position);
                                    //7
                                    team.setMember_type(member_type);
                                    // 第三列
                                    Element scorele = tdeles.get(2);
                                    String starstr = scorele.text().trim();
                                    //8
                                    team.setMember_score(starstr);
//                                System.out.println("starstr:" + starstr);
                                    // 第四列
                                    Element socialele = tdeles.get(3);
                                    Elements socialsele = socialele.select("div.c-social-icons > a");
                                    if (socialsele != null && socialsele.size() > 0) {
                                        for (Element soele : socialsele) {
                                            String sotitle = soele.attr("title");
                                            String sourl = soele.absUrl("href");
//                                            System.out.println(sotitle + " = " + sourl);
                                            if (sotitle.contains("Linkedin")) {
                                                //9
                                                team.setMember_social_linkedin(sourl);
                                            } else if (sotitle.contains("Facebook")) {
                                                //10
                                                team.setMember_social_facebook(sourl);
                                            } else if (sotitle.contains("Twitter")) {
                                                //11
                                                team.setMember_social_twitter(sourl);
                                            }
                                        }
                                    }
                                }
                            }
                            teamList.add(team);
                        }
                    }
                }
                teamDao.saveAll(teamList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("extra team exception");
        }
    }

    /**
     * @Title: extraFund
     * @Description: 解析公司金融
     */
    public void extraFund(String content, ICO_icorating_detail detail) {
        log.info("-extraFund");
        try {
            Document doc = Jsoup.parse(content);
            Elements detaileles = doc.select("div.c-tabs.tabs.mb-brand.mb40 > div.c-tabs__content >div#funds");
            if (detaileles != null && detaileles.size() > 0) {
                List<ICO_icorating_detail_block_funds> fundsList = new ArrayList<>(10);
                Elements lineseles = detaileles.select("tbody > tr.c-table-custom__row");
                if (lineseles != null && lineseles.size() > 0) {
                    for (Element linele : lineseles) {
                        ICO_icorating_detail_block_funds fundModel = new ICO_icorating_detail_block_funds();
                        fundModel.setIco_icorating_detail(detail);
                        fundModel.setLink(detail.getLink());
                        fundModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        fundModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        Elements tdseles = linele.select("td");
                        if (tdseles != null && tdseles.size() > 0) {
                            for (Element tdele : tdseles) {
                                String tdkey = tdele.attr("data-label");
                                // System.out.println(tdkey);
                                if (tdkey.contains("FOUND")) {
                                    Elements eles = tdele.select("div.c-card-media__heading > a");
                                    if (eles != null && eles.size() > 0) {
                                        String fund = eles.text().trim();
                                        String fundurl = eles.attr("href").trim();
//                                        System.out.println("fund:" + fund);
//                                        System.out.println("fundurl:" + fundurl);
                                        fundModel.setFund(fund);
                                        fundModel.setFund_url(fundurl);
                                    }
                                } else if (tdkey.contains("STATUS")) {
                                    String status = tdele.text().trim();
//                                    System.out.println("status:" + status);
                                    fundModel.setStatus(status);
                                } else if (tdkey.contains("AUM")) {
                                    String aum = tdele.text().trim();
//                                    System.out.println("aum:" + aum);
                                    fundModel.setAum(aum);
                                } else if (tdkey.contains("STRATEGY")) {
                                    String stratgey = tdele.text().trim();
//                                    System.out.println("stratgey:" + stratgey);
                                    fundModel.setStratgey(stratgey);
                                }
                            }
                        }
                        fundsList.add(fundModel);
                    }
                }
                fundDao.saveAll(fundsList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("extra found exception");
        }
    }

    /**
     * @Title: extraDevelopment
     * @Description: 解析公司发展
     */
    public void extraDevelopment(String content, ICO_icorating_detail detail) {
        log.info("-extraDevelopment");
        try {
            Document doc = Jsoup.parse(content);
            Elements detaileles = doc.select("div.c-tabs.tabs.mb-brand.mb40 > div.c-tabs__content >div#development");
            if (detaileles != null && detaileles.size() > 0) {
                List<ICO_icorating_detail_block_development> developmentList = new ArrayList<>(10);
                Elements lineseles = detaileles.select("tbody > tr.c-table-custom__row");
                if (lineseles != null && lineseles.size() > 0) {
                    for (Element linele : lineseles) {
                        ICO_icorating_detail_block_development developmentModel = new ICO_icorating_detail_block_development();
                        developmentModel.setIco_icorating_detail(detail);
                        developmentModel.setLink(detail.getLink());
                        developmentModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        developmentModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));

                        Elements tdseles = linele.select("td");
                        if (tdseles != null && tdseles.size() > 0) {
                            for (Element tdele : tdseles) {
                                String tdkey = tdele.attr("data-label");
                                String tdvalue = tdele.text().trim();
//                                System.out.println(tdkey + " = " + tdvalue);
                                if (tdkey.contains("PRELAUNC")) {
                                    developmentModel.setPre_launch(tdvalue);
                                } else if (tdkey.contains("lunch")) {
                                    developmentModel.setLaunch(tdvalue);
                                } else if (tdkey.contains("CUSTOM_BLOCKCHAIN")) {
                                    developmentModel.setCustom(tdvalue);
                                } else if (tdkey.contains("TESTNET")) {
                                    developmentModel.setTestnet(tdvalue);
                                } else if (tdkey.contains("MAINNET")) {
                                    developmentModel.setMiannet(tdvalue);
                                }
                            }
                        }
                        developmentList.add(developmentModel);
                    }
                }
                developmentDao.saveAll(developmentList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("extraDevelopment Exception");
        }
    }

    //=================funds=====================

    @Override
    public void getIcoratingFundsList() {
        log.info("********** start icorating funds list task **********");
        int page = 1;
        Boolean isNotLast = true;
        while (isNotLast) {
            String url = "https://icorating.com/funds/load/?sort=avg_roi_eth&direction=desc&page=" + page;
            try {
//                System.out.println(url);
                Request request = new Request(url, RequestMethod.GET);
                Response response = HttpClientUtil.doRequest(request);
                int code = response.getCode();
                // 验证请求
                if (code == 200) {
                    String content = response.getResponseText();
                    // 验证页面
                    if (StringUtils.isNotBlank(content)) {
                        // 验证是否是正常页面
                        if (content.contains("funds") && content.contains("lastPage")) {
                            JSONObject jo = JSONObject.parseObject(content);
                            if (jo != null) {
                                List<ICO_icorating_funds_list> fundsList = new ArrayList<>(100);
                                String lastPagestr = jo.getString("lastPage");
                                if (lastPagestr.equalsIgnoreCase("false")) {
                                    log.info("----------current page is :" + page);
                                } else {
                                    isNotLast = false;
                                    log.info("----- last page:" + page);
                                }
                                // 解析json
                                JSONArray fundsarrjo = jo.getJSONArray("funds");
                                if (fundsarrjo != null && fundsarrjo.size() > 0) {
                                    for (int i = 0; i < fundsarrjo.size(); i++) {
                                        ICO_icorating_funds_list foundsModel = new ICO_icorating_funds_list();

                                        JSONObject linejo = fundsarrjo.getJSONObject(i);
                                        String fund = linejo.getString("nameByLanguage");
                                        foundsModel.setFund(fund);
                                        String status = linejo.getString("currentStatus");
                                        foundsModel.setStatus(status);
                                        long aum = linejo.getLongValue("aum");
                                        if (aum != 0) {
                                            foundsModel.setAum(aum);
                                        }
                                        JSONArray strategiesjoarr = linejo.getJSONArray("strategies");
                                        StringBuffer stbu = new StringBuffer();
                                        for (int tempi = 0; tempi < strategiesjoarr.size(); tempi++) {
                                            JSONObject tempjo = strategiesjoarr.getJSONObject(tempi);
                                            String stratestr = tempjo.getString("nameByLanguage");
                                            stbu.append(stratestr).append(",");
                                        }
                                        String stratgey = stbu.toString();
                                        if (stratgey.endsWith(",")) {
                                            stratgey = StringUtils.substringBeforeLast(stratgey, ",");
                                        }
                                        foundsModel.setStratgey(stratgey);
                                        int foundation = linejo.getIntValue("foundationYear");
                                        if (foundation != 0) {
                                            foundsModel.setFoundation(foundation);
                                        }
                                        Float avgIcoEthRoi = linejo.getFloat("averageRoiEth");
                                        if (avgIcoEthRoi != 0) {
                                            foundsModel.setAvgIcoEthRoi(avgIcoEthRoi);
                                        }
                                        int analytics_reservedint = linejo.getIntValue("analytics_reserved");
                                        String icorating_analytics = "";
                                        if (analytics_reservedint == 0) {
                                            icorating_analytics = "Not Provided";
                                        } else if (analytics_reservedint == 1) {
                                            icorating_analytics = "Provided";
                                        }
                                        foundsModel.setIcorating_analyticsv(icorating_analytics);
                                        String link = linejo.getString("link");
                                        foundsModel.setLink(link);
                                        foundsModel.setPage(page);
                                        foundsModel.setCrawledStatus("ini");
                                        foundsModel.setInsertTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                        foundsModel.setUpdateTime(new Timestamp(Calendar.getInstance().getTime().getTime()));

                                        ICO_icorating_funds_list oldfunds = foundsListDao.getICO_icorating_funds_listByLink(link);
                                        if (oldfunds == null) {
                                            foundsListDao.save(foundsModel);
                                        } else {
                                            log.info("----- already existed ,icorating founds list,link:" + link);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    log.error("!!!request erro ,code:" + code + " ,url:" + url);
                }
                page++;
            } catch (Exception e) {
                e.printStackTrace();
                log.error("!!!icorating founds list Exception");
            }
        }
    }

    @Override
    public void getIcoratingFoundDetail(ICO_icorating_funds_list foundsitem) {
        log.info("***** strart extra icorating founds details");
        try {
            String url = foundsitem.getLink();
            Request request = new Request(url, RequestMethod.GET);
            Response response = HttpClientUtil.doRequest(request);
            int code = response.getCode();
            // 验证请求
            // 验证请求
            if (code == 200) {
                String content = response.getResponseText();
                // 验证页面
                if (StringUtils.isNotBlank(content)) {
                    // 验证是否是正常页面
                    if (content.contains("c-heading")) {
                        ICO_icorating_funds_detail foundsDetailModel = new ICO_icorating_funds_detail();
                        foundsDetailModel.setIco_icorating_funds_list(foundsitem);
                        foundsDetailModel.setLink(foundsitem.getLink());
                        foundsDetailModel.setFund(foundsitem.getFund());
                        foundsDetailModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        foundsDetailModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));

                        Document doc = Jsoup.parse(content);
                        Elements abouteles = doc.select("div.o-grid.o-grid--wrap > div.o-grid__cell.o-grid__cell--width-100 >p.mb20");
                        if (abouteles != null && abouteles.size() > 0) {
                            String about = abouteles.text().trim();
//                            log.info("- about:" + about);
                            foundsDetailModel.setAbout(about);
                        }
                        Elements table1eles = doc.select("div.o-grid.o-grid--wrap > div.o-grid__cell.o-grid__cell--width-100 >div.o-grid.o-grid--wrap.o-grid--negative-indent tbody >tr");
                        if (table1eles != null && table1eles.size() > 0) {
                            for (Element ele : table1eles) {
                                String key = ele.select("th").text().trim();
                                String val = ele.select("td").text().trim();
//                                log.info("- " + key + " = " + val);
                                if (key.contains("Funds return")) {
                                    foundsDetailModel.setFunds_return(val);
                                } else if (key.contains("Date of foundation")) {
                                    foundsDetailModel.setDate_of_foundation(val);
                                } else if (key.contains("Strategy")) {
                                    foundsDetailModel.setStrategy(val);
                                } else if (key.contains("Target")) {
                                    foundsDetailModel.setTarget(val);
                                }
                            }
                        }

                        foundsDetailModel.setAum(foundsitem.getAum());
                        foundsDetailModel.setAvgIcoEthRoi(foundsitem.getAvgIcoEthRoi());

                        Elements table2eles = doc.select("div.o-grid__cell.o-grid__cell--width-100> div.c-card-info.c-card-info--white.mb20 > table.c-card-info__table > tbody > tr");
                        if (table2eles != null && table2eles.size() > 0) {
                            for (Element ele : table2eles) {
                                Elements theles = ele.select("th");
                                if (theles != null && theles.size() > 0) {
                                    String key = theles.text().trim();
                                    String val = "";
                                    Elements tdeles = ele.select("td");
                                    if (tdeles != null && tdeles.size() > 0) {
                                        val = tdeles.text().trim();
                                    }
//                                    log.info("- " + key + " = " + val);
                                    if (key.contains("Email")) {
                                        foundsDetailModel.setEmail(val);
                                    } else if (key.contains("Site")) {
                                        foundsDetailModel.setSite(val);
                                    } else if (key.contains("Based")) {
                                        foundsDetailModel.setBased(val);
                                    }
                                }
                            }
                            Elements socialeles = table2eles.select("div.c-social-block.c-social-block--center > div.c-social-block__item > a");
                            if (socialeles != null && socialeles.size() > 0) {
                                for (Element soele : socialeles) {
                                    String title = soele.attr("title");
                                    String socialUrl = soele.attr("href");
//                                    log.info("- " + title + " = " + socialUrl);
                                    if (title.contains("Facebook")) {
                                        foundsDetailModel.setFacebook(socialUrl);
                                    } else if (title.contains("Twitter")) {
                                        foundsDetailModel.setTwitter(socialUrl);
                                    } else if (title.contains("Medium")) {
                                        foundsDetailModel.setMedium(socialUrl);
                                    } else if (title.contains("Linkedin")) {
                                        foundsDetailModel.setLinkedin(socialUrl);
                                    }
                                }
                            }
                        }

                        ICO_icorating_funds_detail oldfoundsDetailModel = foundsDetailDao.findICO_icorating_funds_detailByLink(foundsDetailModel.getLink());
                        if (oldfoundsDetailModel == null) {
                            //保存到数据库
                            foundsDetailDao.save(foundsDetailModel);
                            //解析founds 人员
                            extraIcoratingFoundsDetailMember(doc, foundsDetailModel);
                        } else {
                            log.info("----- already existed ,icorating founds detail,the link:" + foundsDetailModel.getLink());
                        }

                        //解析成功更改foundslist的状态
                        foundsitem.setCrawledStatus("200");
                        foundsListDao.save(foundsitem);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("!!! extra icorating founds Exception");
        }
    }

    /**
     * 解析icorating founds 人员
     */
    public void extraIcoratingFoundsDetailMember(Document doc, ICO_icorating_funds_detail foundsDetail) {
        log.info("***** extraIcoratingFoundsDetailMember");
        try {
            Elements membereles = doc.select("div.o-grid.o-grid--wrap > div.o-grid__cell.o-grid__cell--width-100 >div.c-table-container.mb20 >table.c-table-custom > tbody > tr.c-table-custom__row");
            if (membereles != null && membereles.size() > 0) {
                List<ICO_icorating_funds_detail_member> membersModelList = new ArrayList<>(10);
                for (Element lineles : membereles) {
                    ICO_icorating_funds_detail_member memberModel = new ICO_icorating_funds_detail_member();
                    memberModel.setIco_icorating_funds_detail(foundsDetail);
                    memberModel.setLink(foundsDetail.getLink());
                    memberModel.setInsert_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    memberModel.setUpdate_Time(new Timestamp(Calendar.getInstance().getTime().getTime()));

                    Elements tdeles = lineles.select("td");
                    if (tdeles != null && tdeles.size() > 0) {
                        for (Element tele : tdeles) {
                            String label = tele.attr("data-label");
                            if (label.contains("FOUND")) {
                                Elements tempeles = tele.select("div.o-media__body.o-media__body--center > a");
                                String member_name = tempeles.text().trim();
                                String member_url = tempeles.attr("href");
//                                log.info("member_name:" + member_name);
//                                log.info("member_url:" + member_url);
                                memberModel.setMember_name(member_name);
                                memberModel.setMember_url(member_url);
                            } else if (label.contains("STATUS")) {
                                String experience = tele.text().trim();
//                                log.info("experience:" + experience);
                                memberModel.setExperience(experience);
                            } else if (label.contains("AUM")) {
                                Elements socialeles = tele.select("div.c-social-block__item > a");
                                if (socialeles != null && socialeles.size() > 0) {
                                    for (Element soele : socialeles) {
                                        String title = soele.attr("title");
                                        String socialUrl = soele.attr("href");
//                                        log.info("- " + title + " = " + socialUrl);
                                        if (title.contains("Facebook")) {
                                            memberModel.setFacebook(socialUrl);
                                        } else if (title.contains("Twitter")) {
                                            memberModel.setTwitter(socialUrl);
                                        } else if (title.contains("Medium")) {
                                            memberModel.setMedium(socialUrl);
                                        } else if (title.contains("Linkedin")) {
                                            memberModel.setLinkedin(socialUrl);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    membersModelList.add(memberModel);
                }
                foundsMemberDao.saveAll(membersModelList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("!!! extraIcoratingFoundsDetailMember Exception");
        }


    }

    @Override
    public JSONObject getIco_icorating_all_index(String dataSourceNameLevel2) {

        JSONObject json = new JSONObject();
        if(StringUtils.equalsIgnoreCase("all",dataSourceNameLevel2)){
            String indexes_sql = "select ifnull(block_name,'') as block_name," +
                    "ifnull(trading_token,'') as trading_token," +
                    "ifnull(contacts_website,'') as contacts_website," +
                    "ifnull(trading_whitepaper,'') as trading_whitepaper," +
                    "ifnull(contacts_facebook,'') as contacts_facebook," +
                    "ifnull(contacts_twitter,'') as contacts_twitter," +
                    "ifnull(contacts_reddit_alien,'') as contacts_reddit_alien," +
                    "ifnull(contacts_medium,'') as contacts_medium," +
                    "ifnull(contacts_github,'') as contacts_github," +
                    "ifnull(contacts_instagram,'') as contacts_instagram," +
                    "ifnull(contacts_telegram_plane,'') as contacts_telegram_plane," +
                    "ifnull(contacts_youtube,'') as contacts_youtube," +
                    "ifnull(contacts_website,'') as contacts_website," +
                    "ifnull(link,'') as link from ico_icorating_detail";
            List<Map<String, Object>> details = jdbcTemplate.queryForList(indexes_sql);
            json.put("number",details.size());
            JSONObject solution_data = new JSONObject();
            for(Map<String, Object> detail:details){

                JSONObject solution_data_url = new JSONObject();
                solution_data_url.put("name",detail.get("block_name").toString());
                solution_data_url.put("token_name",detail.get("trading_token").toString());
                solution_data_url.put("website",detail.get("contacts_website").toString());
                solution_data_url.put("white_paper",detail.get("trading_whitepaper").toString());

                JSONObject social = new JSONObject();
                social.put("facebook", detail.get("contacts_facebook").toString());
                social.put("twitter",detail.get("contacts_twitter").toString());
                social.put("reddit", detail.get("contacts_reddit_alien").toString());
                social.put("medium",detail.get("contacts_medium").toString());
                social.put("github", detail.get("contacts_github").toString());
                social.put("instagram",detail.get("contacts_instagram").toString());
                social.put("telegram",detail.get("contacts_telegram_plane").toString());
                social.put("youtube", detail.get("contacts_youtube").toString());

                solution_data_url.put("social",social);
                solution_data.put(detail.get("link").toString(),solution_data_url);
            }
            json.put("solution_data",solution_data);
            json.put("source","icorating.com."+dataSourceNameLevel2);
        }else if(StringUtils.equalsIgnoreCase("funds",dataSourceNameLevel2)){
            String indexes_sql = "select ifnull(fund,'') as fund," +
                    "ifnull(link,'') as link," +
                    "ifnull(site,'') as site," +
                    "ifnull(facebook,'') as facebook," +
                    "ifnull(twitter,'') as twitter," +
                    "ifnull(medium,'') as medium," +
                    "ifnull(linkedin,'') as linkedin from ico_icorating_funds_detail";
            List<Map<String, Object>> details = jdbcTemplate.queryForList(indexes_sql);
            json.put("number",details.size());
            JSONObject solution_data = new JSONObject();
            for(Map<String, Object> detail:details){

                JSONObject solution_data_url = new JSONObject();
                solution_data_url.put("name",detail.get("fund").toString());
                solution_data_url.put("token_name","");
                solution_data_url.put("website",detail.get("site").toString());
                solution_data_url.put("white_paper","");

                JSONObject social = new JSONObject();
                social.put("facebook", detail.get("facebook").toString());
                social.put("twitter",detail.get("twitter").toString());
                social.put("medium",detail.get("medium").toString());
                social.put("linkedin", detail.get("linkedin").toString());

                solution_data_url.put("social",social);
                solution_data.put(detail.get("link").toString(),solution_data_url);
            }
            json.put("solution_data",solution_data);
            json.put("source","icorating.com."+dataSourceNameLevel2);
        }
        return json;
    }

    @Override
    public JSONObject getICO_icorating_detailByItemUrl(String url) {
        JSONObject json = new JSONObject();
        ICO_icorating_detail detail = detailDao.getICO_icorating_detailByLink(url);
        if(null!=detail){
            List<ICO_icorating_detail_block_development> developments = developmentDao.getICO_icorating_detail_block_developmentsByFkid(detail.getPk_id());
            List<ICO_icorating_detail_block_funds> funds = fundDao.getICO_icorating_detail_block_fundsByFkid(detail.getPk_id());
            List<ICO_icorating_detail_block_team> teams = teamDao.getICO_icorating_detail_block_teamsByFkid(detail.getPk_id());

            /*开始组装数据*/
            ICO_icorating_detailDto detailDto = new ICO_icorating_detailDto();
            try {
                BeanUtils.copyProperties(detailDto,detail);
                json.putAll(BeanUtils.describe(detailDto));

                if(CollectionUtils.isNotEmpty(developments)){
                    List<ICO_icorating_detail_block_developmentDto> developmentDtos = new ArrayList<>();
                    for(ICO_icorating_detail_block_development development:developments){
                        ICO_icorating_detail_block_developmentDto developmentDto = new ICO_icorating_detail_block_developmentDto();
                        BeanUtils.copyProperties(developmentDto,development);
                        developmentDtos.add(developmentDto);
                    }
                    json.put("development", JSON.toJSON(developmentDtos));
                }
                if(CollectionUtils.isNotEmpty(funds)){
                    List<ICO_icorating_detail_block_fundsDto> fundsDtos = new ArrayList<>();
                    for(ICO_icorating_detail_block_funds fund:funds) {
                        ICO_icorating_detail_block_fundsDto fundsDto = new ICO_icorating_detail_block_fundsDto();
                        BeanUtils.copyProperties(fundsDto,fund);
                        fundsDtos.add(fundsDto);
                    }
                    json.put("funds",JSON.toJSON(fundsDtos));
                }
                if(CollectionUtils.isNotEmpty(teams)){
                    List<ICO_icorating_detail_block_teamDto> teamDtos = new ArrayList<>();
                    for(ICO_icorating_detail_block_team team:teams){
                        ICO_icorating_detail_block_teamDto teamDto = new ICO_icorating_detail_block_teamDto();
                        BeanUtils.copyProperties(teamDto,team);
                        teamDtos.add(teamDto);
                    }
                    json.put("team",teamDtos);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            json.remove("class");
            /*组装social信息*/
            JSONObject social = new JSONObject();
            social.put("facebook", json.getString("contacts_facebook"));
            json.remove("contacts_facebook");
            social.put("twitter",json.getString("contacts_twitter"));
            json.remove("contacts_twitter");
            social.put("reddit", json.getString("contacts_reddit_alien"));
            json.remove("contacts_reddit_alien");
            social.put("medium",json.getString("contacts_medium"));
            json.remove("contacts_medium");
            social.put("github", json.getString("contacts_github"));
            json.remove("contacts_github");
            social.put("instagram",json.getString("contacts_instagram"));
            json.remove("contacts_instagram");
            social.put("telegram",json.getString("contacts_telegram_plane"));
            json.remove("contacts_telegram_plane");
            social.put("youtube", json.getString("contacts_youtube"));
            json.remove("contacts_youtube");

            json.put("social",social);
            /*下面处理Block的logo*/
            json.put("solution_photo_url",detail.getIco_icorating_list().getLogo());
        }
        return json;
    }
    @Override
    public JSONObject getICO_icorating_funds_detailByItemUrl(String url) {
        JSONObject json = new JSONObject();
        ICO_icorating_funds_detail detail = foundsDetailDao.findICO_icorating_funds_detailByLink(url);
        if(null!=detail){

            List<ICO_icorating_funds_detail_member> teams = foundsMemberDao.getICO_icorating_funds_detail_membersByFkid(detail.getPk_id());

            /*开始组装数据*/
            ICO_icorating_funds_detailDto detailDto = new ICO_icorating_funds_detailDto();
            try {
                BeanUtils.copyProperties(detailDto,detail);
                json.putAll(BeanUtils.describe(detailDto));


                if(CollectionUtils.isNotEmpty(teams)){
                    List<ICO_icorating_funds_detail_memberDto> teamDtos = new ArrayList<>();
                    for(ICO_icorating_funds_detail_member team:teams){
                        ICO_icorating_funds_detail_memberDto teamDto = new ICO_icorating_funds_detail_memberDto();
                        BeanUtils.copyProperties(teamDto,team);
                        teamDtos.add(teamDto);
                    }
                    json.put("member",teamDtos);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            json.remove("class");
            /*组装social信息*/
            JSONObject social = new JSONObject();
            social.put("facebook", json.getString("facebook"));
            json.remove("facebook");
            social.put("twitter",json.getString("twitter"));
            json.remove("twitter");
            social.put("medium", json.getString("medium"));
            json.remove("medium");
            social.put("linkedin",json.getString("linkedin"));
            json.remove("linkedin");

            json.put("social",social);
            /*下面处理Block的logo*/
            json.put("solution_photo_url",detail.getIco_icorating_funds_list().getUpdateTime());
        }
        return json;
    }

    public static void main(String[] args) {
        IcoratingServiceImp t = new IcoratingServiceImp();
        // String url = "https://icorating.com/ico/bitclave/";
//        String url = "https://icorating.com/ico/ubex-ubex/";
        String url = "https://icorating.com/funds/blocktrade-investments/";

        Request request = null;
        try {
            request = new Request(url, RequestMethod.GET);
        } catch (MethodNotSupportException e) {
            e.printStackTrace();
        }
        Response response = HttpClientUtil.doRequest(request);
        String content = response.getResponseText();
//        extraDetails(content, null);
    }
}
