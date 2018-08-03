package com.edmi.service.serviceImp.trackico;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.print.Doc;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edmi.configs.StateCodeConfig;
import com.edmi.dao.trackico.ICO_trackico_detailRepository;
import com.edmi.dao.trackico.ICO_trackico_detail_blockLabelRepository;
import com.edmi.dao.trackico.ICO_trackico_itemRepository;
import com.edmi.entity.trackico.ICO_trackico_detail;
import com.edmi.entity.trackico.ICO_trackico_detail_blockLabel;
import com.edmi.entity.trackico.ICO_trackico_item;
import com.edmi.service.service.TrackicoService;
import com.edmi.utils.http.HttpClientUtil;
import com.edmi.utils.http.exception.MethodNotSupportException;
import com.edmi.utils.http.request.Request;
import com.edmi.utils.http.request.RequestMethod;
import com.edmi.utils.http.response.Response;
import com.sun.jna.platform.unix.solaris.LibKstat.KstatNamed.UNION.STR;

/**
 * @ClassName: TrackicoServiceImp
 * @Description: Trackico 列表页 详情页
 * @author keshi
 * @date 2018年7月30日 下午3:38:34
 * 
 */
@Service("trackicoService")
public class TrackicoServiceImp implements TrackicoService {
	Logger log = Logger.getLogger(TrackicoServiceImp.class);
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// 是否抓取过程出现中断
	Boolean isInterrupted = false;
	// 如果中断，中断的位置，如58页：https://www.trackico.io/58/
	String interruptUrl = "";

	@Autowired
	private ICO_trackico_itemRepository ico_trackico_itemDao;
	@Autowired
	private ICO_trackico_detailRepository ico_trackico_detailDao;
	@Autowired
	private ICO_trackico_detail_blockLabelRepository detail_blockLabelDao;

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
		int maxRetry = 5;
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
		Boolean isNotLast = true;
		while (isNotLast) {
			try {
				// 1从数据库里查出新的，没有抓过详情的item，根据status
				// 2如果item没有抓取成功，status没有修改，把它放到末尾
				List<ICO_trackico_item> items = ico_trackico_itemDao.findTop10ByStatus("ini");
				// List<ICO_trackico_item> items =
				// ico_trackico_itemDao.findOneByItemUrl("https://www.trackico.io/ico/w12/");

				System.out.println("size:" + items.size());
				// 最后一次，退出循环
				if (CollectionUtils.isEmpty(items)) {
					isNotLast = false;
					log.info("----trackico，已从数据库查询完所有的item，-----trackico 抓取完成 ");
				} else {
					for (ICO_trackico_item item : items) {
						System.out.println(item.toString());
						extraOneDetailPage(item);
					}
				}
			} catch (Exception e) {
				log.error("从数据库里查出所有的更新时 error");
				e.printStackTrace();
			}
		}
	}

	/** 
	* @Title: extraOneDetailPage 
	* @Description: 解析详情页
	*
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
			log.info(detailModel.toString());
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
	* @Description:解析详情页的-公司标签链接
	* 对应表ico_trackico_detail_block_label
	* 解析错误status = extraBlockLabelError
	*/
	public void extraDetailPageBlockLabel(ICO_trackico_item item, ICO_trackico_detail detail, Document doc) {
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

					System.out.println(blockLabelModel.toString());
					blockLabelModelList.add(blockLabelModel);
					System.out.println("===============");
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
