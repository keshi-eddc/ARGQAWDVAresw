package com.edmi.service.serviceImp.trackico;

import java.sql.Timestamp;
import java.util.Calendar;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edmi.dao.trackico.ICO_trackico_itemRepository;
import com.edmi.entity.trackico.ICO_trackico_item;
import com.edmi.service.service.TrackicoService;
import com.edmi.utils.http.exception.MethodNotSupportException;

import fun.jerry.httpclient.bean.HttpRequestHeader;

/**
 * @ClassName: TrackicoServiceImp
 * @Description: Trackico 列表页
 * @author keshi
 * @date 2018年7月30日 下午3:38:34
 * 
 */
@Service("trackicoService")
public class TrackicoServiceImp implements TrackicoService {
	Logger log = Logger.getLogger(TrackicoServiceImp.class);
	// 是否抓取过程出现中断
	Boolean isInterrupted = false;
	// 如果中断，中断的位置，如58页：https://www.trackico.io/58/
	String interruptUrl = "";

	@Autowired
	private ICO_trackico_itemRepository ico_trackico_itemDao;

	@Override
	public void getICO_trackico_list() throws MethodNotSupportException {

		// 起始页
		String url = "https://www.trackico.io/";
		// 如果有抓取过程中有中断，接着上次最大的位置抓取
		if (isInterrupted) {
			url = interruptUrl;
		}
		// 结束条件
		Boolean isNotLast = true;
		// 当前页码 在解析详情页时获得
		while (isNotLast) {
			try {
				HttpRequestHeader header = new HttpRequestHeader();
				header.setUrl(url);
				String html = SomSiteRequest.getPageContent(header);
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
		log.info("抓取完成");
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

	public static void main(String[] args) throws MethodNotSupportException {
		TrackicoServiceImp t = new TrackicoServiceImp();
		t.getICO_trackico_list();
		// String url = "https://www.trackico.io/";
		// HttpRequestHeader header = new HttpRequestHeader();
		// header.setUrl(url);
		// String html = SomSiteRequest.getPageContent(header);
		// Document doc = Jsoup.parse(html);
		// t.getCurrentItemNum(doc);
		// t.getCurrentItemTotalNum(doc);
		// t.getNextPageLink(doc);
		// t.getCurrentPageNum(doc);
	}
}
