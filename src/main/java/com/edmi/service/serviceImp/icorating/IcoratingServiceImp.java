package com.edmi.service.serviceImp.icorating;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.edmi.dao.icorating.ICO_icorating_detailRepository;
import com.edmi.dao.icorating.ICO_icorating_listRepository;
import com.edmi.entity.icorating.ICO_icorating_detail;
import com.edmi.entity.icorating.ICO_icorating_list;
import com.edmi.service.service.IcoratingService;
import com.edmi.utils.http.HttpClientUtil;
import com.edmi.utils.http.exception.MethodNotSupportException;
import com.edmi.utils.http.request.Request;
import com.edmi.utils.http.request.RequestMethod;
import com.edmi.utils.http.response.Response;
import com.sun.jna.platform.unix.solaris.LibKstat.KstatNamed.UNION.STR;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
	private static ICO_icorating_detailRepository detailDao;

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
					item.setLogo(logo);
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
			List<ICO_icorating_detail> oldDetailList = detailDao.getICO_icorating_detailByLink(itemUrl);
			if (CollectionUtils.isEmpty(oldDetailList)) {
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
							extraDetails(content, item);
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
				log.info("allread exist the detail URL ：" + itemUrl);
			}
		} else {
			log.error("item url is null");
		}
	}

	// 解析详情
	public static void extraDetails(String content, ICO_icorating_list item) {
		log.info("------解析详情");
		Document doc = Jsoup.parse(content);
		ICO_icorating_detail detailModel = new ICO_icorating_detail();
		try {
			// detailModel.setLink(item.getLink());
			// fkid
			// detailModel.setIco_icorating_list(item);
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
						System.out.println("tabletitle:" + tabletitle);
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
									System.out.println(key + " = " + val);
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
			// detailDao.save(detailModel);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("extra exception");
		}
	}

	public static void main(String[] args) {
		// String url = "https://icorating.com/ico/bitclave/";
		String url = "https://icorating.com/ico/ubex-ubex/";
		Request request = null;
		try {
			request = new Request(url, RequestMethod.GET);
		} catch (MethodNotSupportException e) {
			e.printStackTrace();
		}
		Response response = HttpClientUtil.doRequest(request);
		String content = response.getResponseText();
		extraDetails(content, null);
	}
}
