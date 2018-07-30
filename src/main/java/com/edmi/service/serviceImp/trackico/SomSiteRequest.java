package com.edmi.service.serviceImp.trackico;

import com.edmi.utils.http.HttpClientSupport;

import fun.jerry.common.enumeration.Project;
import fun.jerry.common.enumeration.ProxyType;
import fun.jerry.common.enumeration.Site;
import fun.jerry.httpclient.bean.HttpRequestHeader;

public class SomSiteRequest extends HttpClientSupport {

	public static String getPageContent(HttpRequestHeader header) {
		String html = "";
		// header.setProxyType(ProxyType.PROXY_STATIC_DLY);
		header.setProxyType(ProxyType.NONE);

		header.setProject(Project.OTHER);
		header.setSite(Site.OTHER);
		// 请求休眠时间
		header.setRequestSleepTime(1000);
		// 页面加载时候的超时时间
		header.setTimeOut(5000);
		header.setMaxTryTimes(10);
		html = get(header).getContent();
		return html;
	}

	public static void main(String[] args) {
		String url = "https://www.rakuten.co.jp/";
		HttpRequestHeader header = new HttpRequestHeader();
		header.setUrl(url);
		String content = getPageContent(header);
		System.out.println(content);
	}
}
