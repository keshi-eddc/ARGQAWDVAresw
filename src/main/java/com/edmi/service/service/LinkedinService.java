package com.edmi.service.service;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.nodes.Document;

public interface LinkedinService {

    public void importLinkedInLinks();

    public boolean analysisMembersToBase(Document doc, long link_id);

    public void readLinkedinFilesToBase();

    public JSONObject get_ico_linkedin_member_info(String link);
}
