package com.edmi.service.service;

import com.alibaba.fastjson.JSONObject;
import com.edmi.entity.icocrunch.Ico_icocrunch_detail;
import com.edmi.entity.icocrunch.Ico_icocrunch_list;
import com.edmi.utils.http.exception.MethodNotSupportException;

import java.util.List;

public interface IcocrunchSevice {

    public int getIco_icocrunch_list_total_pages(String show) throws MethodNotSupportException;

    public void getIco_icocrunch_list(String show,int currentPage) throws MethodNotSupportException;

    public Long getIco_icocrunch_listMaxSerialNumber(String show);

    public Ico_icocrunch_list getNextPageIco_icocrunch_list(String show,long serialNumber);

    public List<String> getIco_icocrunch_listByDetailStatus(String detaiStatus);

    public void getIco_icocrunch_detail(String blockUrl) throws MethodNotSupportException;

    public JSONObject getIco_icocrunch_detailPageable(int page_number, int pageSize);

    public Ico_icocrunch_detail getIco_icocrunch_detailByICOCrunchUrl(String icoCrunchUrl);

}
