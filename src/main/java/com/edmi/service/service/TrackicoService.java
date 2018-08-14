package com.edmi.service.service;

import com.edmi.utils.http.exception.MethodNotSupportException;

/**
 * @ClassName: TrackicoService
 * @Description: 定义接口
 * @author keshi
 * @date 2018年7月30日 下午3:40:48
 * 
 */
public interface TrackicoService {
	/** 
	* @Title: getICO_trackico_list 
	* @Description: 抓取列表页的接口方法
	*/
	public void getICO_trackico_list() throws MethodNotSupportException;

	/** 
	* @Title: getICO_trackico_detail 
	* @Description: 解析详情页的接口方法
	*/
	public void getICO_trackico_detail() throws MethodNotSupportException;

}
