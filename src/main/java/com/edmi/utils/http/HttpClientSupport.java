package com.edmi.utils.http;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.config.SocketConfig.Builder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import fun.jerry.common.LogSupport;
import fun.jerry.common.UserAgentSupport;
import fun.jerry.common.enumeration.Project;
import fun.jerry.common.enumeration.ProxyType;
import fun.jerry.common.enumeration.Site;
import fun.jerry.httpclient.bean.HttpRequestHeader;
import fun.jerry.httpclient.bean.HttpResponse;
import fun.jerry.proxy.StaticProxySupport;
import fun.jerry.proxy.entity.Proxy;

public class HttpClientSupport {

	
	private static final Logger log = LogSupport.getHttplog();
	
	private static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

	static {
		Builder buider = SocketConfig.custom().setSoTimeout(10000);
		LayeredConnectionSocketFactory sslsf = null;
		try {
			sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("https", sslsf)
				.register("http", new PlainConnectionSocketFactory()).build();
		cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		cm.setMaxTotal(200);
		cm.setDefaultMaxPerRoute(50);
		cm.setDefaultSocketConfig(buider.build());
	}
	
	public static HttpResponse post(HttpRequestHeader header) {
		HttpResponse rtnResponse = null;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			
			HttpPost httpPost = (HttpPost) buildHeader(header, "post");

			if (CollectionUtils.isNotEmpty(header.getValues())) {
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				for (HttpRequestHeader.NameValue nv : header.getValues()) {
					nvps.add(new BasicNameValuePair(nv.getKey(), nv.getValue()));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			}
			
			CloseableHttpResponse response = httpclient.execute(httpPost);
			
			String html = getResponseAsString(header, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rtnResponse;
	}
	
	public static HttpResponse get(HttpRequestHeader header) {
		return execute(header, 0);
	}
	
	private static HttpResponse execute(HttpRequestHeader header, int tryCount) {
		HttpResponse httpResponse = new HttpResponse();
		
		try {
			TimeUnit.MILLISECONDS.sleep((long) (header.getRequestSleepTime() + Math.random() * 100 * tryCount));
		} catch (InterruptedException e) {
			log.error(header.getUrl() + " InterruptedException ", e);
		}
		
		String html = "";
		try {
			HttpGet httpGet = (HttpGet) buildHeader(header, "get");
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			if (header.getProxyType() == ProxyType.PROXY_CLOUD_ABUYUN) {
				//credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("H26U3Y18CA6L02YD", "0567219ED7DF3592"));
				credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("HN54N0TZA3IO945D", "3524EC2B27DDDDF4"));
			}
			if (header.getProxyType() == ProxyType.PROXY_STATIC_DLY) {
				credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("379862802", "infopower"));
			}
			CookieStore cookieStore = new BasicCookieStore();
			CloseableHttpClient httpclient = HttpClients.custom()
					.setConnectionManager(cm)
					.setDefaultCredentialsProvider(credsProvider)
					.setDefaultCookieStore(cookieStore)
					.build();
			
			boolean existError = false;
			
			CloseableHttpResponse response = null;
			try {
				tryCount ++;
				response = httpclient.execute(httpGet);
//				List<Cookie> cookies = cookieStore.getCookies();
//				for (int i = 0; i < cookies.size(); i++) {
//					log.info(cookies.get(i).getName() + "    " + cookies.get(i).getValue());
//	            }
				// 如果响应不为null
				if (null != response) {
					
					log.info(header.getUrl() + " response " + response.getStatusLine().getStatusCode());
					httpResponse.setCode(response.getStatusLine().getStatusCode());
					// 如果请求成功
					if (null != response.getStatusLine() && (response.getStatusLine().getStatusCode() == 200
//							|| response.getStatusLine().getStatusCode() == 404
//							|| response.getStatusLine().getStatusCode() == 403
							)) {
						html = getResponseAsString(header, response);
						httpResponse.setContent(html);
						existError = false;
					} else {
						// 如果出现429，该请求暂停5分钟
						if (response.getStatusLine().getStatusCode() == 429) {
						}
						// 如果请求不成功，需要重试请求,且记录请求状态值
						existError = true;
						
					}
				} else {
					// 否则如果响应为null，需要重试请求
					existError = true;
				}
				
				//达到最大次数的时候直接返回
				if (tryCount > header.getMaxTryTimes()) {
					log.error(header.getUrl() + " has tried " + header.getMaxTryTimes() + " times.");
					return httpResponse;
				}
				
			} catch (Exception e) {
				existError = true;
				log.error(header.getUrl() + " httpclient execute IOException, it will try again.", e);
				httpResponse.setFailReason(e.getMessage());
				httpResponse.setCode(0);
			} finally {
				if (null != response) {
					response.close();
				}
			}
			
			if (existError && tryCount <= header.getMaxTryTimes()) {
				execute(header, tryCount);
			}
			
		} catch (Exception e) {
			log.error("通过代理IP请求页面报错:", e);
			httpResponse.setFailReason(e.getMessage());
		}
		return httpResponse;
	}
	
	private static String getResponseAsString(HttpRequestHeader header, CloseableHttpResponse response) throws Exception {
		StringBuilder entityStringBuilder = new StringBuilder();
		HttpEntity entity = null;
//		if (response.getStatusLine().getStatusCode() == 200) {
			entity = response.getEntity();
			if (null != entity) {
				try {
//					BufferedReader bufferedReader = new BufferedReader(
//							new InputStreamReader(entity.getContent(), 
//									(StringUtils.isNotEmpty(header.getEncode()) ? header.getEncode() : "UTF-8")), 12 * 1024);
//					String line = null;
//					while ((line = bufferedReader.readLine()) != null) {
//						entityStringBuilder.append(line + "\n");
//					}
					entityStringBuilder = new StringBuilder(EntityUtils.toString(entity));
				} catch (Exception e) {
//					log.error("getResponseAsString error ", e);
					throw new Exception(e);
				} finally {
					if (null != response) {
						try {
							response.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
//		} else {
//			
//		}
		return entityStringBuilder.toString();
	}
	
	private static HttpRequestBase buildHeader(HttpRequestHeader header, String method) throws Exception {
		if (!EnumUtils.isValidEnum(ProxyType.class, null != header.getProxyType() ? header.getProxyType().toString() : "")) {
			throw new Exception("HttpRequestHeader proxyType must not be null");
		} else {
			// 暂时只有使用代理云的代理IP时，才必须设置Project和Site，便于统计，其他的代理IP统计暂时不做
			if (ArrayUtils.contains(new ProxyType[] {ProxyType.PROXY_STATIC_DLY}, header.getProxyType())) {
				if (!EnumUtils.isValidEnum(Project.class, null != header.getProject() ? header.getProject().toString() : "")) {
					throw new Exception("HttpRequestHeader project must not be null");
				}
				if (!EnumUtils.isValidEnum(Site.class, null != header.getSite() ? header.getSite().toString() : "")) {
					throw new Exception("HttpRequestHeader site must not be null");
				}
			}
		}
		
		HttpRequestBase httpRequest = null;
		try {
			if (method.equalsIgnoreCase("get")) {
//				httpRequest = new HttpGet(header.getUrl().replace("|", "&brvbar;"));
				httpRequest = new HttpGet(header.getUrl());
			} else if (method.equalsIgnoreCase("post")) {
				httpRequest = new HttpPost(header.getUrl());
			}
			
			// 设置RequestConfig begin
			RequestConfig.Builder builder = RequestConfig.custom()
					.setSocketTimeout(10000)
					.setConnectTimeout(10000)
					.setConnectionRequestTimeout(10000)
					.setRedirectsEnabled(true)
//					.setCookieSpec(CookieSpecs.IGNORE_COOKIES)
					;
			
//			RequestConfig requestConfig = RequestConfig.custom()
////		            .setRedirectsEnabled(false).build();//disable redirect

//			get.setConfig(requestConfig);//poass the request config to request
			if (null != header.getProxy()) {
				HttpHost proxy = new HttpHost(header.getProxy().getIp(), header.getProxy().getPort());
		        builder.setProxy(proxy);
			} else if (ArrayUtils.contains(new ProxyType[] { ProxyType.PROXY_STATIC_AUTO, ProxyType.PROXY_STATIC_DLY,
					ProxyType.PROXY_STATIC_DUNG }, header.getProxyType())) {
				//普通代理IP设置
				Proxy _proxy = StaticProxySupport.getStaticProxy(header.getProxyType(), header.getProject(), header.getSite());
//				_proxy.setIp("1.196.158.145");
//				_proxy.setPort(57112);
				HttpHost proxy = new HttpHost(_proxy.getIp(), _proxy.getPort());
		        builder.setProxy(proxy);
			} else if (header.getProxyType() ==  ProxyType.PROXY_CLOUD_ABUYUN) {
				HttpHost proxy = new HttpHost("http-dyn.abuyun.com", 9020);
		        builder.setProxy(proxy);
			}
			
			httpRequest.setConfig(builder.build());
			
			// 设置RequestConfig end
			
			if (StringUtils.isNotEmpty(header.getHost())) {
				httpRequest.addHeader("Host", header.getHost());
			}
			// 使用AAS Node方式的代理IP时不需要设置该属性
//			if (StringUtils.isNotEmpty(header.getUserAgent()) && http_request_type_static != 2) {
			// 如果配置了使用PC端的UA
			if (header.isAutoPcUa()) {
				httpRequest.addHeader("User-Agent", UserAgentSupport.getPCUserAgent());
			} else if (header.isAutoMobileUa()) {
				// 如果配置了使用Mobile端的UA
				httpRequest.addHeader("User-Agent", UserAgentSupport.getMobileUserAgent());
			} else if (header.isAutoUa()) {
				// 如果配置了自动切换UA
				httpRequest.addHeader("User-Agent", UserAgentSupport.getUserAgent());
			} else if (StringUtils.isNotEmpty(header.getUserAgent())) {
				// 如果配置了不使用自动切换UA，使用自己配置的UA
				httpRequest.addHeader("User-Agent", header.getUserAgent());
			} else {
				// 如果UA不使用自动切换，但没有配置UA，默认UA自动切换
				httpRequest.addHeader("User-Agent", UserAgentSupport.getUserAgent());
			}
			
			if (StringUtils.isNotEmpty(header.getAccept())) {
				httpRequest.addHeader("Accept", header.getAccept());			
			}
			if (StringUtils.isNotEmpty(header.getAcceptLanguage())) {
				httpRequest.addHeader("Accept-Language", header.getAcceptLanguage());
			}
			if (StringUtils.isNotEmpty(header.getAcceptEncoding())) {
				httpRequest.addHeader("Accept-Encoding", header.getAcceptEncoding());
			}
			if (StringUtils.isNotEmpty(header.getReferer())) {
				httpRequest.addHeader("Referer", header.getReferer());
			}
			if (StringUtils.isNotEmpty(header.getCookie())) {
				httpRequest.addHeader("Cookie", header.getCookie());
			}
			if (StringUtils.isNotEmpty(header.getConnection())) {
				httpRequest.addHeader("Connection", header.getConnection());
			}
			if (StringUtils.isNotEmpty(header.getPragma())) {
				httpRequest.addHeader("Pragma", header.getPragma());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpRequest;
	}
	
	public static void main(String[] args) {
//		getViaProxy();
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("url", "https://www.baidu.com/");
//		System.out.println(get(map, null));;
		
//		System.out.println(HttpClientSupport.class.getSimpleName());
		
//		HttpRequestHeader header = new HttpRequestHeader();
////		header.setEncode("GBK");
//		header.setAccept("*/*");
//		header.setUrl("http://www.4008109886.com/Ctcc/start.html?sinopec=sinopec");
////		header.setUrl("http://www.4008109886.com/Ctcc/cars.html?sinopec=sinopec");
//		header.setAcceptEncoding("gzip, deflate");
//		header.setAcceptLanguage("zh-CN,zh;q=0.8,en;q=0.6");
//		header.setConnection("keep-alive");
//		header.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
//		header.setHost("www.4008109886.com");
//		header.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
//		header.setOrigin("http://www.4008109886.com");
//		header.setReferer("http://www.4008109886.com/Ctcc/index.html?sinopec=sinopec");
//		header.setCookie("PHPSESSID=69gfga3gkc2984srnfkbr5t9i5;");
//		header.setXrequestedWith("XMLHttpRequest");
//		
//		List<NameValue> nvs = new ArrayList<NameValue>();
//		nvs.add(header.new NameValue("t", "b"));
//		nvs.add(header.new NameValue("b", "\u5965\u8fea"));
////		nvs.add(header.new NameValue("CIA_front_levels", "deleted"));
////		nvs.add(header.new NameValue("t", "b"));
////		nvs.add(header.new NameValue("b", "奥迪"));
//		
////		header.setValues(nvs);
//		
//		System.out.println(get(header));
		
//		HttpRequestHeader header = new HttpRequestHeader();
////		header.setEncode("GBK");
//		header.setAccept("*/*");
//		header.setUrl("https://www.dianping.com/shop/83617212");
//		header.setAcceptEncoding("gzip, deflate");
//		header.setAcceptLanguage("zh-CN,zh;q=0.8,en;q=0.6");
//		header.setConnection("keep-alive");
//		header.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
//		header.setHost("www.dianping.com");
//		header.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
////		header.setCookie("PHPSESSID=69gfga3gkc2984srnfkbr5t9i5;");
////		header.setXrequestedWith("XMLHttpRequest");
//		
//		log.info(get(header));
		
		HttpRequestHeader header = new HttpRequestHeader();
//		header.setEncode("GBK");
		header.setAccept("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		header.setUrl("http://www.dianping.com/search/category/1/10/g101r7");
		header.setAcceptEncoding("gzip, deflate, sdch");
		header.setAcceptLanguage("zh-CN,zh;q=0.8,en;q=0.6");
		header.setCacheControl("max-age=0");
		header.setConnection("keep-alive");
		header.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
		header.setHost("www.dianping.com");
		header.setUpgradeInsecureRequests("1");
		header.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
//		header.setCookie("PHPSESSID=69gfga3gkc2984srnfkbr5t9i5;");
//		header.setXrequestedWith("XMLHttpRequest");
		
		log.info(get(header));
		
	}

}
