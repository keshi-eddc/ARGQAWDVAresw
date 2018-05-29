package com.edmi.service.service;

import org.jsoup.nodes.Document;

public interface LinkedinService {

    public void importLinkedInLinks();

    public boolean analysisMembersToBase(Document doc, long link_id);

    public void readLinkedinFilesToBase();
}
