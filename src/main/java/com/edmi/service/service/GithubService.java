package com.edmi.service.service;


import com.edmi.entity.github.ICO_Github_Organization;
import com.edmi.entity.github.ICO_Github_Repositories;
import com.edmi.utils.http.exception.MethodNotSupportException;

public interface GithubService {
     public void importICO_Github_Links(String file);
     public void getICO_Github_Organization() throws MethodNotSupportException;
     public void getICO_Github_Repositories(ICO_Github_Organization org) throws MethodNotSupportException;
     public void getICO_Github_Repositories_Details(ICO_Github_Repositories ogr) throws MethodNotSupportException;
}
