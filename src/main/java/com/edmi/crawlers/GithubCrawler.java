package com.edmi.crawlers;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import com.alibaba.fastjson.JSONObject;
import com.edmi.entity.github.ICO_Github_Organization;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;



import java.sql.Timestamp;
import java.util.Calendar;

public class GithubCrawler extends BreadthCrawler {

    static Logger log = Logger.getLogger(GithubCrawler.class);

    /**
     * 构造一个基于伯克利DB的爬虫
     * 伯克利DB文件夹为crawlPath，crawlPath中维护了历史URL等信息
     * 不同任务不要使用相同的crawlPath
     * 两个使用相同crawlPath的爬虫并行爬取会产生错误
     *
     * @param crawlPath 伯克利DB使用的文件夹
     * @param autoParse 是否根据设置的正则自动探测新URL
     */
    public GithubCrawler(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
    }


    @Override
    public void visit(Page page, CrawlDatums crawlDatums) {
        if(200==page.code()){
            JsonObject data = page.jsonObject();
            ICO_Github_Organization organization = JSONObject.parseObject(data.toString(), ICO_Github_Organization.class);
            organization.setStatus("crawled");
            organization.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));


        }else{
            log.info("无该组织信息："+page.url());
        }


    }

    public static void main(String[] args) {
        GithubCrawler exchangeCrawler = new GithubCrawler("crawlPath",true);
        exchangeCrawler.setThreads(2);
        exchangeCrawler.getConf().setExecuteInterval(5*1000);

        exchangeCrawler.addSeed("https://api.github.com/orgs/alibaba");
        try {
            exchangeCrawler.start(1);
        } catch (Exception e) {
           log.info(e.getMessage());
        }
    }


    @Override
    public boolean isAutoParse() {
        return autoParse;
    }

    @Override
    public void setAutoParse(boolean autoParse) {
        this.autoParse = autoParse;
    }
}
