package com.edmi.service.service;

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
}
