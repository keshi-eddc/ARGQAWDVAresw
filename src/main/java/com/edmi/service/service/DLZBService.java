package com.edmi.service.service;

import com.edmi.entity.dlzb.DLZB_Project_List;
import com.edmi.utils.http.exception.MethodNotSupportException;


public interface DLZBService {

    public void getDLZB_Project_List(String keyword) throws MethodNotSupportException;

    public void getDLZB_Project_List_Basic_Info(DLZB_Project_List project) throws MethodNotSupportException;


}
