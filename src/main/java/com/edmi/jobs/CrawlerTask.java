package com.edmi.jobs;

import com.edmi.dao.etherscan.*;
import com.edmi.dao.feixiaohao.ICO_Feixiaohao_ExchangeRepository;
import com.edmi.dao.feixiaohao.ICO_Feixiaohao_Exchange_CurrenciesRepository;
import com.edmi.dao.feixiaohao.ICO_Feixiaohao_Exchange_DetailsRepository;
import com.edmi.dao.trackico.ICO_trackico_itemRepository;
import com.edmi.entity.etherscan.*;
import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange;
import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange_Currencies;
import com.edmi.entity.trackico.ICO_trackico_item;
import com.edmi.service.service.EtherscanService;
import com.edmi.service.service.FeixiaohaoService;
import com.edmi.service.service.TrackicoService;
import com.edmi.utils.http.exception.MethodNotSupportException;
import org.apache.commons.lang3.RandomUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component
public class CrawlerTask {

    Logger log = Logger.getLogger(CrawlerTask.class);

    @Autowired
    private EtherscanService etherscanService;

    @Autowired
    private FeixiaohaoService feixiaohaoService;

    @Autowired
    private TrackicoService trackicoService;

    @Autowired
    private ICO_Etherscan_IO_BlocksRepository blocksDao;

    @Autowired
    private ICO_Etherscan_IO_Blocks_ForkedRepository blocks_forkedDao;

    @Autowired
    private ICO_Etherscan_IO_Blocks_Txs_Page_ListRepository txs_page_listDao;

    @Autowired
    private ICO_Etherscan_IO_Blocks_Forked_Txs_Page_ListRepository forked_txs_page_listDao;

    @Autowired
    private ICO_Etherscan_IO_Blocks_TxsRepository txsDao;

    @Autowired
    private ICO_Etherscan_IO_Blocks_Forked_TxsRepository forked_txsDao;

    @Autowired
    private ICO_Feixiaohao_ExchangeRepository exchangeDao;

    @Autowired
    private ICO_Feixiaohao_Exchange_CurrenciesRepository currenciesDao;

    @Autowired
    private ICO_Feixiaohao_Exchange_DetailsRepository exchange_detailsDao;

    @Autowired
    private ICO_trackico_itemRepository ico_trackico_itemDao;


    /*@Scheduled(cron = "0 38 08 25 * ?")*/
    public void getICO_Etherscan_IO_Blocks() throws Exception {
        List<String> links = etherscanService.getICO_Etherscan_IO_Blocks_TotalPageLinks();
        Long serial = Calendar.getInstance().getTime().getTime();
        if (links.size() > 0) {
            for (String link : links) {
                etherscanService.getICO_Etherscan_IO_Blocks(link, serial, links.size());
                Thread.sleep(3 * 1000);
            }
        }
    }

    //    @Scheduled(cron = "0 */1 * * * ?")
    public void getICO_Etherscan_IO_Blocks_Info() throws Exception {
        int server = 1;
        server = RandomUtils.nextInt(1, 3);
        /*int second = Calendar.getInstance().get(Calendar.SECOND);
        if(second>=0&&second<30){
            server = 1;
        }else{
            server = 2;
        }*/
        List<ICO_Etherscan_IO_Blocks> blocks = blocksDao.findTop50ByStatusAndServer("ini", server);
        for (ICO_Etherscan_IO_Blocks block : blocks) {
            etherscanService.getICO_Etherscan_IO_Blocks_Info(block);
            Thread.sleep(5 * 100);
        }
    }

    //    @Scheduled(cron = "30 */1 * * * ?")
    public void getICO_Etherscan_IO_Blocks_Txs_Page_List() throws Exception {
        int server = 1;
        server = RandomUtils.nextInt(1, 3);
        /*int second = Calendar.getInstance().get(Calendar.SECOND);
        if(second>=0&&second<30){
            server = 1;
        }else{
            server = 2;
        }*/
        List<ICO_Etherscan_IO_Blocks> blocks = blocksDao.findTop50ByPagestatusAndServer("ini", server);
        for (ICO_Etherscan_IO_Blocks block : blocks) {
            etherscanService.getICO_Etherscan_IO_Blocks_TxsPages(block);
            Thread.sleep(5 * 100);
        }

    }

    //@Scheduled(cron = "0 */1 * * * ?")
    public void getICO_Etherscan_IO_Blocks_Txs() throws Exception {

        List<ICO_Etherscan_IO_Blocks_Txs_Page_List> page_list = txs_page_listDao.findTop30ByStatus("ini");

        for (ICO_Etherscan_IO_Blocks_Txs_Page_List page : page_list) {
            etherscanService.getICO_Etherscan_IO_Blocks_Txs(page);
            Thread.sleep(5 * 100);
        }
    }


    //@Scheduled(cron = "30 */1 * * * ?")
    public void getICO_Etherscan_IO_Blocks_Txs_Info() throws Exception {

        List<ICO_Etherscan_IO_Blocks_Txs> txs = txsDao.findTop30ByStatus("ini");
        for (ICO_Etherscan_IO_Blocks_Txs tx : txs) {
            etherscanService.getICO_Etherscan_IO_Blocks_Txs_Info(tx);
            Thread.sleep(5 * 100);
        }
    }

// <=====================  下面是Blocks_Forked的job ===================================>

    //   @Scheduled(cron = "0 05 11 31 * ?")
    public void getICO_Etherscan_IO_Blocks_Forked() throws Exception {
        List<String> links = etherscanService.getICO_Etherscan_IO_Blocks_Forked_TotalPageLinks();
        Long serial = Calendar.getInstance().getTime().getTime();
        if (links.size() > 0) {
            for (String link : links) {
                etherscanService.getICO_Etherscan_IO_Blocks_Forked(link, serial, links.size());
                Thread.sleep(3 * 1000);
            }
        }
    }

    //@Scheduled(cron = "0 0/1 * * * ?")
    public void getICO_Etherscan_IO_Blocks_Forked_Info() throws Exception {
        int server = 1;
        server = RandomUtils.nextInt(1, 3);
        /*int second = Calendar.getInstance().get(Calendar.SECOND);
        if(second>=0&&second<30){
            server = 1;
        }else{
            server = 2;
        }*/
        List<ICO_Etherscan_IO_Blocks_Forked> blocks = blocks_forkedDao.findTop50ByStatusAndServer("ini", server);
        for (ICO_Etherscan_IO_Blocks_Forked block : blocks) {
            etherscanService.getICO_Etherscan_IO_Blocks_Forked_Info(block);
            Thread.sleep(5 * 100);
        }
    }

    //@Scheduled(cron = "30 0/1 * * * ?")
    public void getICO_Etherscan_IO_Blocks_Forked_Txs_Page_List() throws Exception {
        int server = 1;
        server = RandomUtils.nextInt(1, 3);
        /*int second = Calendar.getInstance().get(Calendar.SECOND);
        if(second>=0&&second<30){
            server = 1;
        }else{
            server = 2;
        }*/
        List<ICO_Etherscan_IO_Blocks_Forked> blocks = blocks_forkedDao.findTop50ByPagestatusAndServer("ini", server);
        for (ICO_Etherscan_IO_Blocks_Forked block : blocks) {
            etherscanService.getICO_Etherscan_IO_Blocks_Forked_TxsPages(block);
            Thread.sleep(5 * 100);
        }

    }

    //   @Scheduled(cron = "0 */1 * * * ?")
    public void getICO_Etherscan_IO_Blocks_Forked_Txs() throws Exception {

        List<ICO_Etherscan_IO_Blocks_Forked_Txs_Page_List> page_list = forked_txs_page_listDao.findTop50ByStatus("ini");

        for (ICO_Etherscan_IO_Blocks_Forked_Txs_Page_List page : page_list) {
            etherscanService.getICO_Etherscan_IO_Blocks_Forked_Txs(page);
            Thread.sleep(5 * 100);
        }
    }

    /*@Scheduled(cron = "0/30 * * * * ?")*/
    public void getICO_Etherscan_IO_Blocks_Forked_Txs_Info() throws Exception {

        List<ICO_Etherscan_IO_Blocks_Forked_Txs> txs = forked_txsDao.findTop50ByStatus("ini");
        for (ICO_Etherscan_IO_Blocks_Forked_Txs tx : txs) {
            etherscanService.getICO_Etherscan_IO_Blocks_Forked_Txs_Info(tx);
            Thread.sleep(5 * 100);
        }
    }

    // <===================== 下面是Feixiaohao的相关job ===================================>
    //@Scheduled(cron = "0 55 14 * * ?")
    public void getICO_Feixiaohao_Exchange() throws Exception {
        feixiaohaoService.getICO_Feixiaohao_Exchange();
    }

    //@Scheduled(cron = "0 28 15 * * ?")
    public void getICO_Feixiaohao_Exchange_Details() throws Exception {
        List<ICO_Feixiaohao_Exchange> exchanges = exchangeDao.getICO_Feixiaohao_ExchangeByStatus("ini");
        for (ICO_Feixiaohao_Exchange exchange : exchanges) {
            feixiaohaoService.getICO_Feixiaohao_Exchange_Details(exchange);
        }
    }

    /*@Scheduled(cron = "0 50 06 * * ?")*/
    public void getICO_Feixiaohao_Exchange_Counter_Party_Details() throws MethodNotSupportException {
        List<String> links = exchange_detailsDao.getICO_Feixiaohao_Exchange();
        for (int i = 0; i < links.size(); i++) {
            String link = links.get(i);
            log.info("正在抓取" + links.size() + "-" + (i + 1) + "个交易对详情，link：" + link);
            feixiaohaoService.getICO_Feixiaohao_Exchange_Counter_Party_Details(link);
        }
    }

    //@Scheduled(cron = "0 0 14 * * ?")
    public void importICO_Feixiaohao_Exchange_Currencies() {
        feixiaohaoService.importICO_Feixiaohao_Exchange_Currencies();
    }

    // @Scheduled(cron = "0 07 14 * * ?")
    public void getICO_Feixiaohao_Exchange_Currenciesdtl() throws MethodNotSupportException {
        List<ICO_Feixiaohao_Exchange_Currencies> currencies = currenciesDao.getICO_Feixiaohao_Exchange_CurrenciesByDetails_status("ini");
        int i = 0;
        for (ICO_Feixiaohao_Exchange_Currencies currency : currencies) {
            feixiaohaoService.getICO_Feixiaohao_Exchange_Currenciesdtl(currency);
            i++;
            try {
                if (i % 10 == 0) {
                    Thread.sleep(5 * 1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // <===================== 下面是Trackico的相关job ===================================>
    //每天早晨5点开始
    @Scheduled(cron = "0 00 05 * * ?")
    public void getICO_Trackico_list() throws MethodNotSupportException {
        trackicoService.getICO_trackico_list();
    }

    //每10分钟执行
    @Scheduled(cron = "0 0/5 * * * ?")
    public void getICO_Trackico_detail() throws MethodNotSupportException {

        //all
//        List<ICO_trackico_item> items = ico_trackico_itemDao.findAllByStatus("ini");

        List<ICO_trackico_item> items = ico_trackico_itemDao.findTop10ByStatus("ini");

        // List<ICO_trackico_item> items =
        // ico_trackico_itemDao.findOneByItemUrl("https://www.trackico.io/ico/w12/");
        log.info("get items num ：" + items.size());
        if (items.size() != 0) {
            // 获取开始时间
            long startTime = System.currentTimeMillis();

            for (ICO_trackico_item item : items) {
                trackicoService.getICO_trackico_detail(item);
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 获取结束时间
            long endTime = System.currentTimeMillis();
            long mss = endTime - startTime;
            long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (mss % (1000 * 60)) / 1000;
            String timestr = hours + " hours " + minutes + " minutes " + seconds + " seconds ";
            log.info("this time crawled，" + "items num：" + items.size() + ".cost：" + timestr);
        } else {
            log.info("get item from databash ,item num is 0");
        }

    }

}

