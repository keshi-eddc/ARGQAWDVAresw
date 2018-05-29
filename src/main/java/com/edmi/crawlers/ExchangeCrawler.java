package com.edmi.crawlers;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.plugin.ram.RamCrawler;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class ExchangeCrawler extends RamCrawler {

    static Logger log = Logger.getLogger(ExchangeCrawler.class);

    @Override
    public void visit(Page page, CrawlDatums crawlDatums) {
       if(page.matchType("baidu")){
           log.info(page.url());
       }else if(page.matchType("sina")){
           log.info(page.url());
       }
    }

}
