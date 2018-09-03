package com.edmi.service.service;

import com.alibaba.fastjson.JSONObject;
import com.edmi.entity.icodrops.ICO_icodrops_list;

/**
 * IcodropsService 接口声明
 */
public interface IcodropsService {
    /**
     * 根据出入的url,解析不同类别的列表
     *
     * @param inputUrl
     */
    public void getIcodropsListWithInput(String inputUrl);

    /**
     * 详情
     *
     * @param item
     */
    public void getIcodropsDetail(ICO_icodrops_list item);

    public JSONObject getIco_icodrops_index(String dataSourceNameLevel2);

    public JSONObject getICO_icodrops_detailByItemUrl(String  url);

}
