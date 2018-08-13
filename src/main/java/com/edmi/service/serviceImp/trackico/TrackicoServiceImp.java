package com.edmi.service.serviceImp.trackico;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edmi.dao.trackico.ICO_trackico_detailRepository;
import com.edmi.dao.trackico.ICO_trackico_detail_blockFinancialRepository;
import com.edmi.dao.trackico.ICO_trackico_detail_blockInfoRepository;
import com.edmi.dao.trackico.ICO_trackico_detail_blockLabelRepository;
import com.edmi.dao.trackico.ICO_trackico_detail_blockMilestonesRepository;
import com.edmi.dao.trackico.ICO_trackico_detail_blockTeamRepository;
import com.edmi.dao.trackico.ICO_trackico_itemRepository;
import com.edmi.entity.trackico.ICO_trackico_detail;
import com.edmi.entity.trackico.ICO_trackico_detail_blockFinancial;
import com.edmi.entity.trackico.ICO_trackico_detail_blockLabel;
import com.edmi.entity.trackico.ICO_trackico_detail_blockMilestones;
import com.edmi.entity.trackico.ICO_trackico_detail_blockTeam;
import com.edmi.entity.trackico.ICO_trackico_detail_block_info;
import com.edmi.entity.trackico.ICO_trackico_item;
import com.edmi.service.service.TrackicoService;
import com.edmi.utils.http.HttpClientUtil;
import com.edmi.utils.http.exception.MethodNotSupportException;
import com.edmi.utils.http.request.Request;
import com.edmi.utils.http.request.RequestMethod;
import com.edmi.utils.http.response.Response;

/**
 * @author keshi
 * @ClassName: TrackicoServiceImp
 * @Description: Trackico 列表页 详情页
 * @date 2018年7月30日 下午3:38:34
 */
@Service("trackicoService")
public class TrackicoServiceImp implements TrackicoService {
    Logger log = Logger.getLogger(TrackicoServiceImp.class);
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 是否抓取过程出现中断
    Boolean isInterrupted = false;
    // 如果中断，中断的位置，如58页：https://www.trackico.io/58/
    // 如https://www.trackico.io/3/?order=recent
    String interruptUrl = "";

    @Autowired
    private ICO_trackico_itemRepository ico_trackico_itemDao;
    @Autowired
    private ICO_trackico_detailRepository ico_trackico_detailDao;
    @Autowired
    private ICO_trackico_detail_blockLabelRepository detail_blockLabelDao;
    @Autowired
    private ICO_trackico_detail_blockTeamRepository detail_blockTeamDao;
    @Autowired
    private ICO_trackico_detail_blockFinancialRepository detail_financialTeamDao;
    @Autowired
    private ICO_trackico_detail_blockMilestonesRepository detail_milestonesDao;
    @Autowired
    private ICO_trackico_detail_blockInfoRepository detail_InfoDao;

    @Override
    public void getICO_trackico_list() throws MethodNotSupportException {

        // 起始页
        // String url = "https://www.trackico.io/";
        // 按时间排序
        String url = "https://www.trackico.io/?order=recent";
        // 如果有抓取过程中有中断，接着上次最大的位置抓取
        if (isInterrupted) {
            url = interruptUrl;
        }
        // 结束条件
        Boolean isNotLast = true;
        // 当前页码 在解析详情页时获得
        while (isNotLast) {
            try {
                // kangkang的请求方法
                // HttpRequestHeader header = new HttpRequestHeader();
                // header.setUrl(url);
                // String html = SomSiteRequest.getPageContent(header);
                String html = getPageContent(url);
                if (StringUtils.isNotBlank(html)) {
                    // 验证页面是否正常
                    if (html.contains("card-body") && html.contains("page-item")) {
                        Document doc = Jsoup.parse(html);
                        // 解析列表页
                        extraOneListPage(doc);
                        // 获得当前item总数
                        int currentItemTotalNum = getCurrentItemTotalNum(doc);
                        // 获得当前item数
                        int currentItemNum = getCurrentItemNum(doc);
                        // 判断是否是最后一页
                        if (currentItemNum >= currentItemTotalNum) {
                            isNotLast = false;
                        }
                        // 获得下一页链接
                        url = getNextPageLink(doc);
                        // Thread.sleep(1000);
                    } else {
                        log.error("异常页面：" + url);
                    }
                } else {
                    log.error("页面为空：" + url);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("trackico-列表页-抓取完成");
    }

    // 获得当前item总数
    public int getCurrentItemTotalNum(Document doc) {
        int currentItemTotalNum = 0;
        Elements eles = doc.select("footer > span.text-right");
        if (eles != null && eles.size() > 0) {
            String str = eles.text();
            if (str.contains("-") && str.contains("of")) {
                String itemNumstr = StringUtils.substringAfterLast(str, "of").trim();
                currentItemTotalNum = Integer.valueOf(itemNumstr);
            }
        }
        log.info("currentItemTotalNum:" + currentItemTotalNum);
        return currentItemTotalNum;
    }

    // 获得当前item数
    public int getCurrentItemNum(Document doc) {
        int currentItemNum = 0;
        Elements eles = doc.select("footer > span.text-right");
        if (eles != null && eles.size() > 0) {
            String str = eles.text();
            if (str.contains("-") && str.contains("of")) {
                String itemNumstr = StringUtils.substringBetween(str, "-", "of").trim();
                currentItemNum = Integer.valueOf(itemNumstr);
            }
        }
        log.info("currentItemNum:" + currentItemNum);
        return currentItemNum;
    }

    // 获得下一页链接
    public String getNextPageLink(Document doc) {
        String nextPageUrl = "";
        Elements eles = doc.select("nav > ul.pagination  > li.page-item > a");
        if (eles != null && eles.size() > 0) {
            String temp = eles.last().attr("href");
            nextPageUrl = temp;
        }
        if (!nextPageUrl.contains("https://www.trackico.io")) {
            nextPageUrl = "https://www.trackico.io" + nextPageUrl;
        }
        return nextPageUrl;
    }

    // 获取当前页码
    public int getCurrentPageNum(Document doc) {
        int currentPageNum = 0;
        Elements eles = doc.select("nav > ul.pagination  > li.page-item.active > a");
        if (eles != null && eles.size() > 0) {
            String temp = eles.text();
            currentPageNum = Integer.valueOf(temp);
        }
        log.info("currentPageNum:" + currentPageNum);
        return currentPageNum;
    }

    /*
     * @Async("myTaskAsyncPool") 通过注解异步多线程
     */
    // 解析列表页的一页
    public void extraOneListPage(Document doc) {
        Elements itemeles = doc.select("div.main-content > div.row > div.col-md-6");
        if (itemeles != null && itemeles.size() > 0) {
            for (Element itemele : itemeles) {
                try {
                    String itemUrl = "";
                    String itemName = "";
                    ICO_trackico_item itemModel = new ICO_trackico_item();
                    Elements itemUrleles = itemele.select("a.card-body");
                    if (itemUrleles != null && itemUrleles.size() > 0) {
                        itemUrl = itemUrleles.attr("href").trim();
                        if (!itemUrl.contains("https://www.trackico.io")) {
                            itemUrl = "https://www.trackico.io" + itemUrl;
                        }
                    }
                    Elements itemNameles = itemele.select("h5.mt-1");
                    if (itemNameles != null && itemNameles.size() > 0) {
                        itemName = itemNameles.text().trim();
                    }
                    itemModel.setItemName(itemName);
                    itemModel.setItemUrl(itemUrl);
                    itemModel.setStatus("ini");
                    itemModel.setInsertTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    itemModel.setUpdateTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    // 获得当前页数
                    int currentPageNum = getCurrentPageNum(doc);
                    itemModel.setPagenum(currentPageNum);
                    log.info(itemModel.toString());
                    // 插入数据前，执行一次查询
                    ICO_trackico_item item = ico_trackico_itemDao.getICO_trackico_itemByItemUrl(itemModel.getItemUrl());
                    // 如果没有查到，说明是新数据
                    if (null == item) {
                        // 一条一条存
                        ico_trackico_itemDao.save(itemModel);
                        log.info("插入数据");
                    } else {
                        log.info("数据已经存在");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getPageContent(String url) {
        String pageContent = null;
        Request request;
        int maxRetry = 10;
        int maxRetryException = 10;
        int retryNnum = 0;
        Boolean isright = false;
        do {
            try {
                request = new Request(url, RequestMethod.GET);
                Response response = HttpClientUtil.doRequest(request);
                // response code
                int code = response.getCode();
                for (int i = 0; i < maxRetry; i++) {
                    if (code == 200) {
                        log.info("网络请求：" + i + " 次成功");
                        // response text
                        pageContent = response.getResponseText();
                        // 请求成功
                        isright = false;
                        break;
                    } else {
                        log.error("网络请求，返回码 不是 200 ，重试：" + i);
                        request = new Request(url, RequestMethod.GET);
                        response = HttpClientUtil.doRequest(request);
                    }
                }
            } catch (MethodNotSupportException e) {
                e.printStackTrace();
                retryNnum++;
                log.error("请求是发生异常，重试：" + retryNnum);
                if (retryNnum > maxRetryException) {
                    log.error("请求是发生异常，重试：" + maxRetryException + " 没有成功。");
                    isright = false;
                } else {
                    isright = true;
                }
            }
        } while (isright);

        return pageContent;
    }

    // ---detail---
    /*
     * @Title:getICO_trackico_detail
     *
     * @Description:从数据库ico_trackico_list，每次查10个item 到最后一个，查到的item
     * 传入extraOneDetailPage(item) 在方法中把item的status该为200，下次查询就会得到新的
     */
    @Override
    public void getICO_trackico_detail() throws MethodNotSupportException {
        try {
            // 获取开始时间
            long startTime = System.currentTimeMillis();
            // 1从数据库里查出新的，没有抓过详情的item，根据status
            List<ICO_trackico_item> items = ico_trackico_itemDao.findTop10ByStatus("ini");
            // List<ICO_trackico_item> items =
            // ico_trackico_itemDao.findOneByItemUrl("https://www.trackico.io/ico/w12/");
            for (ICO_trackico_item item : items) {
                System.out.println(item.toString());
                extraOneDetailPage(item);
            }
            // 获取结束时间
            long endTime = System.currentTimeMillis();
            long mss = endTime - startTime;
            String timestr = formatDuring(mss);
            log.info("本次详情抓取完成，" + "items个数：" + items.size() + ".用时：" + timestr);
        } catch (Exception e) {
            log.error("从数据库里查出所有的更新时 error");
            e.printStackTrace();
        }
    }

    /**
     * @Title: extraOneDetailPage
     * @Description: 解析详情页
     */
    // @Async("myTaskAsyncPool")
    public void extraOneDetailPage(ICO_trackico_item item) {
        try {
            // 详情页链接
            String url = item.getItemUrl();
            // https://www.trackico.io/ico/ubcoin/
            // 请求页面
            Request request = new Request(url, RequestMethod.GET);
            Response response = HttpClientUtil.doRequest(request);
            int code = response.getCode();
            // 500 Read timed out
            System.out.println("code:" + code);
            // 验证请求
            if (code == 200) {
                String content = response.getResponseText();
                // 验证页面
                if (StringUtils.isNotBlank(content)) {
                    // 验证是否是正常页面
                    if (content.contains("card-body")) {
                        Document doc = Jsoup.parse(content);
                        // 模型
                        ICO_trackico_detail detailModel = new ICO_trackico_detail();
                        // 解析详情页的-详情
                        extraDetailPageDetails(item, detailModel, doc);
                        // 解析详情页的-公司标签链接
                        extraDetailPageBlockLabel(item, detailModel, doc);
                        // 解析详情页的-公司人员
                        extraDetailPageBlockTeam(item, detailModel, doc);
                        // 解析详情页的-公司金融
                        extraDetailPageBlockFinancial(item, detailModel, doc);
                        // 解析详情页的-公司里程表
                        extraDetailPageBlockMilestones(item, detailModel, doc);
                        // 解析详情页的-公司信息
                        extraDetailPageBlockInfo(item, detailModel, doc);
                    } else {
                        log.error("页面异常：" + url);
                    }
                } else {
                    log.error("页面为空：" + url);
                }

            } else {
                // 更新item对象的status -请求不正确，把status = 状态码
                item.setStatus(String.valueOf(code));
                ico_trackico_itemDao.save(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @Title: extraDetailPageDetails
     * @Description:解析详情页的-详情 对应ico_trackico_detail
     * 把ICO_trackico_detail解析对象入库
     * 传入item对象，解析完成，把item对象的status更新为已抓取
     * 访问和解析正常 status =200
     * 访问异常  status = 状态码
     * 解析异常 status = extraDetailsError
     * 未解析 status = ini
     */
    public void extraDetailPageDetails(ICO_trackico_item item, ICO_trackico_detail detailModel, Document doc) {
        log.info("解析详情页的-详情");
        // 如果解析异常，catch 处修改status
        try {
            // 解析页面
            // block_name
            // block_tag
            String block_name = "";
            String block_tag = "";
            Elements block_nameles = doc.select("div.flexbox > div.flex-grow > div.align-items-baseline > h1");
            if (block_nameles != null && block_nameles.size() > 0) {
                String temp = block_nameles.text();
                if (temp.contains("(") && temp.contains(")")) {
                    block_name = StringUtils.substringBefore(temp, "(").trim();
                    block_tag = StringUtils.substringBetween(temp, "(", ")").trim();
                } else {
                    block_name = temp.trim();
                }
                detailModel.setBlock_name(block_name);
                detailModel.setBlock_tag(block_tag);
            }
            // block_description
            Elements block_descriptioneles = doc.select("div.card-body > div.row > div.col-12  p");
            if (block_descriptioneles != null && block_descriptioneles.size() > 0) {
                // System.out.println(block_descriptioneles.toString());
                String block_description = block_descriptioneles.text().trim();
                detailModel.setBlock_description(block_description);
            }
            // logo_url
            Elements logo_urleles = doc.select("div.card-body > div.flexbox > div.img-thumbnail > img");
            if (logo_urleles != null && logo_urleles.size() > 0) {
                // System.out.println(logo_urleles.toString());
                String logo_url = logo_urleles.attr("src").trim();
                if (!logo_url.contains("https://www.trackico.io")) {
                    logo_url = "https://www.trackico.io" + logo_url;
                }
                detailModel.setLogo_url(logo_url);
            }

            detailModel.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            detailModel.setUpdate_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            // 传入item对象，设置fk_id
            detailModel.setIco_trackico_item(item);
            // 模型入库
            ico_trackico_detailDao.save(detailModel);
            // log.info(detailModel.toString());
            // 更新item对象的status -正常解析
            item.setStatus("200");
            ico_trackico_itemDao.save(item);
        } catch (Exception e) {
            // 更新item对象的status -解析异常
            item.setStatus("extraDetailsError");
            ico_trackico_itemDao.save(item);
            log.error("解析详情页的-详情-异常");
            e.printStackTrace();
        }
    }

    /**
     * @Title: extraDetailPageBlockLabel
     * @Description:解析详情页的-公司标签链接 对应表ico_trackico_detail_block_label
     * 解析错误status = extraBlockLabelError
     */
    public void extraDetailPageBlockLabel(ICO_trackico_item item, ICO_trackico_detail detail, Document doc) {
        log.info("解析详情页的-公司标签链接");
        try {
            List<ICO_trackico_detail_blockLabel> blockLabelModelList = new ArrayList<>(100);
            Elements eles = doc.select("div.card-body > div.flexbox > div.flex-grow > div.d-flex.flex-row.align-items-center.flex-wrap.mt-2 > a");
            if (eles != null && eles.size() > 0) {
                for (Element ele : eles) {
                    ICO_trackico_detail_blockLabel blockLabelModel = new ICO_trackico_detail_blockLabel();
                    // block_lable_name
                    String block_lable_name = "";
                    // block_lable_url
                    String block_lable_url = "";
                    block_lable_url = ele.attr("href").trim();
                    if (ele.hasClass("btn-label")) {
                        block_lable_name = ele.text().trim();
                    } else {
                        block_lable_name = ele.attr("data-original-title").trim();
                    }
                    if (!block_lable_url.contains("http")) {
                        block_lable_url = "http://" + block_lable_url;
                    }

                    blockLabelModel.setBlock_lable_name(block_lable_name);
                    blockLabelModel.setBlock_lable_url(block_lable_url);
                    blockLabelModel.setIco_trackico_detail(detail);
                    blockLabelModel.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    blockLabelModel.setUpdate_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    blockLabelModelList.add(blockLabelModel);
                }
                try {
                    // ICO_trackico_detail_blockLabel 模型存入数据库
                    detail_blockLabelDao.saveAll(blockLabelModelList);
                } catch (Exception e) {
                    log.error("ICO_trackico_detail_blockLabel 入库异常");
                    e.printStackTrace();
                }
            } else {
                log.error("页面标签不存在，请检查" + " extraDetailPageBlockLabel");
            }

        } catch (Exception e) {
            // 更新item对象的status的状态-解析详情页的-公司标签链接
            item.setStatus("extraBlockLabelError");
            ico_trackico_itemDao.save(item);
            log.error("解析详情页的-公司标签链接 异常");
            e.printStackTrace();
        }
    }

    /**
     * @Title: extraDetailPageBlockTeam
     * @Description: 解析详情页的-公司人员
     * 如果 解析错误 ICO_trackico_item的status的状态 = extraBlockTeamError
     */
    public void extraDetailPageBlockTeam(ICO_trackico_item item, ICO_trackico_detail detail, Document doc) {
        log.info("解析详情页的-公司人员");
        try {
            // 详情页的所有的人员
            List<ICO_trackico_detail_blockTeam> blockTeamList = new ArrayList<>(200);
            Elements teameles = doc.select("div.tab-content > div#tab-team >div.row.equal-height > div");
            if (teameles != null && teameles.size() > 0) {
                Boolean isNotAdvisors = true;
                for (Element ele : teameles) {
                    ICO_trackico_detail_blockTeam blockTeam = new ICO_trackico_detail_blockTeam();
                    if (isNotAdvisors) {
                        // 区分有没有顾问Advisors
                        String temp = ele.attr("class");
                        if (temp.equals("col-12")) {
                            isNotAdvisors = false;
                            continue;
                        }
                        // 解析team
                        String member_type = "member";
                        extraOneMember(ele, blockTeam, member_type, detail);

                    } else {
                        // 解析顾问
                        String member_type = "advisor";
                        extraOneMember(ele, blockTeam, member_type, detail);
                    }
                    // System.out.println(blockTeam.toString());
                    blockTeamList.add(blockTeam);
                }
                // blockTeamList 对象插入数据库
                try {
                    detail_blockTeamDao.saveAll(blockTeamList);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("ICO_trackico_detail_blockTeam 入库异常");
                }
            }

        } catch (Exception e) {
            // 更新item对象的status的状态-解析详情页的-公司人员
            item.setStatus("extraBlockTeamError");
            ico_trackico_itemDao.save(item);
            log.error("解析详情页的-公司人员  异常");
            e.printStackTrace();
        }
    }

    /**
     * @Title: extraOneMember
     * @Description: 解析一个人的信息
     */
    public static void extraOneMember(Element ele, ICO_trackico_detail_blockTeam blockTeam, String member_type, ICO_trackico_detail detail) {
        // 人员名称member_name
        String member_name = "";
        Elements member_nameles = ele.select("div.card > div.card-body > h5");
        if (member_nameles != null && member_nameles.size() > 0) {
            member_name = member_nameles.text().trim();
        }
        // 人员链接 member_url
        String member_url = "";
        Elements member_urleles = ele.select("div.card > div.card-body > h5 > a");
        if (member_urleles != null && member_urleles.size() > 0) {
            member_url = member_nameles.attr("href").trim();
            if (!member_url.contains("https://www.trackico.io")) {
                member_url = "https://www.trackico.io" + member_url;
            }
        }

        // 人员责任（职位） member_position
        String member_position = "";
        Elements member_positioneles = ele.select("div.card > div.card-body > span");
        if (member_positioneles != null && member_positioneles.size() > 0) {
            member_position = member_positioneles.text().trim();
        }
        // 头像地址 member_photo_url
        String member_photo_url = "";
        Elements member_photo_urleles = ele.select("div.card > div.card-body > a > span.avatar > img");
        if (member_photo_urleles != null && member_photo_urleles.size() > 0) {
            member_photo_url = member_photo_urleles.attr("src");
            if (!member_photo_url.contains("https://www.trackico.io")) {
                member_photo_url = "https://www.trackico.io" + member_photo_url;
            }
        }

        // 2 fk_id
        blockTeam.setIco_trackico_detail(detail);
        // 3人员名称member_name
        blockTeam.setMember_name(member_name);
        // 4人员链接 member_url
        blockTeam.setMember_url(member_url);
        // 5人员责任（职位） member_position
        blockTeam.setMember_position(member_position);
        // 5人员类型 member_type
        blockTeam.setMember_type(member_type);
        // 6 头像地址 member_photo_url
        blockTeam.setMember_photo_url(member_photo_url);
        // 7 插入时间
        blockTeam.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
        // 8 更新时间
        blockTeam.setUpdate_time(new Timestamp(Calendar.getInstance().getTime().getTime()));

    }

    /**
     * @Title: extraDetailPageBlockFinancial
     * @Description: 解析详情页的-公司金融
     * 如果 解析错误 ICO_trackico_item的status的状态 = extraBlockFinancialError
     */
    public void extraDetailPageBlockFinancial(ICO_trackico_item item, ICO_trackico_detail detail, Document doc) {
        log.info("解析详情页的-公司金融");
        // 公司金融 的列 目前发现有两个
        try {
            // 公司金融 - 列
            Elements financialeles = doc.select("div.tab-content > div#tab-financial > div.row > div");
            if (financialeles != null && financialeles.size() > 0) {
                List<ICO_trackico_detail_blockFinancial> blockFinancialList = new ArrayList<>(200);
                for (Element ele : financialeles) {
                    // 验证是否是规则的列
                    Elements card_titleles = ele.select("h4.card-title");
                    if (card_titleles != null && card_titleles.size() > 0) {
                        // 有card_title
                        String card_title = card_titleles.text().trim();
                        // System.out.println("card_title:" + card_title);
                        Elements cardeles = ele.select("div.card > table.table > tbody > tr");
                        if (cardeles != null && cardeles.size() > 0) {
                            // 一个card 里的 一行
                            for (Element linele : cardeles) {
                                // 对象block_financial
                                ICO_trackico_detail_blockFinancial blockFinancial = new ICO_trackico_detail_blockFinancial();

                                Elements keyeles = linele.select("th");
                                String key = "";
                                if (keyeles != null && keyeles.size() > 0) {
                                    key = keyeles.text().trim();
                                }
                                Elements valueles = linele.select("td");
                                String value = "";
                                if (valueles != null && valueles.size() > 0) {
                                    value = valueles.text().trim();
                                }
                                // System.out.println("==========" + key + ":" + value);
                                // 2fk_id
                                blockFinancial.setIco_trackico_detail(detail);
                                // name
                                blockFinancial.setName(key);
                                // value
                                blockFinancial.setValue(value);
                                // type
                                blockFinancial.setType(card_title);
                                // insert_time
                                blockFinancial.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                // update_time
                                blockFinancial.setUpdate_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                blockFinancialList.add(blockFinancial);
                            }
                            // System.out.println("--------card-------");

                        }
                    } else {
                        log.error("解析-公司金融,发现不符合规则的列");
                    }
                }
                // 一次存一页
                detail_financialTeamDao.saveAll(blockFinancialList);
            }
        } catch (Exception e) {
            // 更新item对象的status的状态-解析详情页的-公司金融
            item.setStatus("extraBlockFinancialError");
            ico_trackico_itemDao.save(item);
            log.error("解析详情页的-公司金融  异常");
            e.printStackTrace();
        }
    }

    /**
     * @Title: extraDetailPageBlockMilestones
     * @Description: 解析详情页的-公司里程表
     * 如果 解析错误 ICO_trackico_item的status的状态 = extraBlockMilestonesError
     */
    public void extraDetailPageBlockMilestones(ICO_trackico_item item, ICO_trackico_detail detail, Document doc) {
        log.info("解析详情页的-公司里程表");
        try {
            Elements milestoneles = doc.select("div.tab-content > div#tab-milestones ol.timeline > li");
            // System.out.println("含有：" + milestoneles.size() + " 条里程表");
            if (milestoneles != null && milestoneles.size() > 0) {
                List<ICO_trackico_detail_blockMilestones> stoneslist = new ArrayList<>(100);
                for (Element stonele : milestoneles) {
                    // 验证是否是规则的里程表 有序号
                    Elements indexeles = stonele.select("span.avatar.bg-info > strong");
                    if (indexeles != null && indexeles.size() > 0) {
                        ICO_trackico_detail_blockMilestones milestonesModel = new ICO_trackico_detail_blockMilestones();
                        int milestones_index = Integer.valueOf(indexeles.text());
                        // milestones_date
                        String milestones_date = "";
                        Elements dateles = stonele.select("h4.card-title > strong");
                        if (dateles != null && dateles.size() > 0) {
                            milestones_date = dateles.text().trim();
                        }
                        // content
                        String content = "";
                        Elements contenteles = stonele.select("div.card-body > p");
                        if (contenteles != null && contenteles.size() > 0) {
                            content = contenteles.text().trim();
                        }
                        // System.out.println("milestones_index :" + milestones_index);
                        // System.out.println("milestones_date:" + milestones_date);
                        // System.out.println("content:" + content);
                        // System.out.println("============");
                        // 2pk_id
                        milestonesModel.setIco_trackico_detail(detail);
                        // 3milestones_index
                        milestonesModel.setMilestones_index(milestones_index);
                        // 4milestones_date
                        milestonesModel.setMilestones_date(milestones_date);
                        // 5content
                        milestonesModel.setContent(content);
                        // 6insert_time
                        milestonesModel.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        // 7update_time
                        milestonesModel.setUpdate_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        stoneslist.add(milestonesModel);
                    }
                }
                // 存入数据库，存一页的里程表
                detail_milestonesDao.saveAll(stoneslist);
            }
        } catch (Exception e) {
            // 更新item对象的status的状态-解析详情页的-公司里程表
            item.setStatus("extraBlockMilestonesError");
            ico_trackico_itemDao.save(item);
            log.error("解析详情页的-公司里程表  异常");
            e.printStackTrace();
        }
    }

    /**
     * @Title: extraDetailPageBlockInfo
     * @Description:解析详情页的-公司信息 如果 解析错误 ICO_trackico_item的status的状态 = extraBlockInfoError
     */
    public void extraDetailPageBlockInfo(ICO_trackico_item item, ICO_trackico_detail detail, Document doc) {
        log.info("解析详情页的-公司信息");
        try {
            Elements blockInfoeles = doc.select("div.col-md-4.col-xl-3.d-none.d-md-block > div > div:nth-child(3)");
            if (blockInfoeles != null && blockInfoeles.size() > 0) {
                String pre_sale = "";
                String token_sale = "";
                String country = "";
                ICO_trackico_detail_block_info blockInfoModel = new ICO_trackico_detail_block_info();
                Elements tableles = blockInfoeles.select("table.table.table-sm > tbody > tr");
                if (tableles != null && tableles.size() > 0) {
                    for (Element linele : tableles) {
                        Elements keyeles = linele.select("th");
                        String key = "";
                        if (keyeles != null && keyeles.size() > 0) {
                            key = keyeles.text().trim();
                        }
                        Elements valueles = linele.select("td");
                        String value = "";
                        if (valueles != null && valueles.size() > 0) {
                            value = valueles.text().trim();
                        }
                        if (key.equalsIgnoreCase("Country")) {
                            country = value;
                        } else if (key.contains("Pre")) {
                            pre_sale = value;
                        } else if (key.contains("Token")) {
                            token_sale = value;
                        }
                    }
                    // 校验是否是规则的公司信息, 一定有 Country
                    if (StringUtils.isNotBlank(country)) {
                        // 2pk_id
                        blockInfoModel.setIco_trackico_detail(detail);
                        // 3pre_sale
                        blockInfoModel.setPre_sale(pre_sale);
                        // 4token_sale
                        blockInfoModel.setToken_sale(token_sale);
                        // 5country
                        blockInfoModel.setCountry(country);
                        // 6insert_time
                        blockInfoModel.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        // 7update_time
                        blockInfoModel.setUpdate_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        // 插入数据库
                        // System.out.println(blockInfoModel.toString());
                        detail_InfoDao.save(blockInfoModel);
                    } else {
                        log.info("不符合 公司信息 country 不为空 的规则,不插入数据库 ");
                    }
                }
            }
        } catch (Exception e) {
            // 更新item对象的status的状态-解析详情页的-公司信息
            item.setStatus("extraBlockInfoError");
            ico_trackico_itemDao.save(item);
            log.error("解析详情页的-公司信息  异常");
            e.printStackTrace();
        }
    }

    //毫秒转到时分秒
    public static String formatDuring(long mss) {
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return hours + " 小时 " + minutes + " 分钟 " + seconds + " 秒 ";
    }

    public static void main(String[] args) throws MethodNotSupportException {
        TrackicoServiceImp t = new TrackicoServiceImp();
        // t.getICO_trackico_list();
        // String url = "https://www.trackico.io/";
        // HttpRequestHeader header = new HttpRequestHeader();
        // header.setUrl(url);
        // String html = SomSiteRequest.getPageContent(header);
        // Document doc = Jsoup.parse(html);
        // t.getCurrentItemNum(doc);
        // t.getCurrentItemTotalNum(doc);
        // t.getNextPageLink(doc);
        // t.getCurrentPageNum(doc);

        // String url = "https://www.trackico.io/";
        // String content = t.getPageContent(url);
        // System.out.println(content);

        String startTime = "https://www.trackico.io/ico/ubcoin/";
        System.out.println(t.ico_trackico_itemDao.getICO_trackico_itemByItemUrl(startTime).toString());
    }

}
