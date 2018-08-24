package com.edmi.service.service;

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


}
