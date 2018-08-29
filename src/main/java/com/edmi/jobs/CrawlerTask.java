package com.edmi.jobs;

import com.edmi.dao.etherscan.*;
import com.edmi.dao.feixiaohao.ICO_Feixiaohao_ExchangeRepository;
import com.edmi.dao.feixiaohao.ICO_Feixiaohao_Exchange_CurrenciesRepository;
import com.edmi.dao.feixiaohao.ICO_Feixiaohao_Exchange_DetailsRepository;
import com.edmi.dao.icodrops.ICO_icodrops_listRepository;
import com.edmi.dao.icorating.ICO_icorating_funds_listRepository;
import com.edmi.dao.icorating.ICO_icorating_listRepository;
import com.edmi.dao.trackico.ICO_trackico_itemRepository;
import com.edmi.entity.etherscan.*;
import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange;
import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange_Currencies;
import com.edmi.entity.icodrops.ICO_icodrops_list;
import com.edmi.entity.icorating.ICO_icorating_funds_list;
import com.edmi.entity.icorating.ICO_icorating_list;
import com.edmi.entity.trackico.ICO_trackico_item;
import com.edmi.service.service.*;
import com.edmi.utils.http.exception.MethodNotSupportException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    private IcoratingService icoratingService;
    @Autowired
    private IcodropsService icodropsService;

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

    @Autowired
    private ICO_icorating_listRepository ico_icorating_listDao;

    @Autowired
    private ICO_icorating_funds_listRepository foundsListDao;
    @Autowired
    private ICO_icodrops_listRepository icodropsItemDao;

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
    //@Scheduled(cron = "0 34 14 * * ?")
    public void getICO_Feixiaohao_Exchange() throws Exception {
        feixiaohaoService.getICO_Feixiaohao_Exchange();
    }

    //@Scheduled(cron = "0 49 17 * * ?")
    public void getICO_Feixiaohao_Exchange_Details() throws Exception {
        List<ICO_Feixiaohao_Exchange> exchanges = exchangeDao.getICO_Feixiaohao_ExchangeByStatus("ini");
        for (ICO_Feixiaohao_Exchange exchange : exchanges) {
            feixiaohaoService.getICO_Feixiaohao_Exchange_Details(exchange);
        }
    }

    //@Scheduled(cron = "0 36 13 * * ?")
    public void getICO_Feixiaohao_Exchange_Counter_Party_Details() throws MethodNotSupportException {
        List<String> links = exchange_detailsDao.getICO_Feixiaohao_Exchange();
        for (int i = 0; i < links.size(); i++) {
            String link = links.get(i);
            log.info("正在抓取" + links.size() + "-" + (i + 1) + "个交易对详情，link：" + link);
            feixiaohaoService.getICO_Feixiaohao_Exchange_Counter_Party_Details(link);
            try {
                Thread.sleep(1 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //@Scheduled(cron = "0 57 17 * * ?")
    public void importICO_Feixiaohao_Exchange_Currencies() {
        feixiaohaoService.importICO_Feixiaohao_Exchange_Currencies();
    }

    // @Scheduled(cron = "0 04 18 * * ?")
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
//    @Scheduled(cron = "0 00 21 * * ?")
    public void getICO_Trackico_list() throws MethodNotSupportException {
        trackicoService.getICO_trackico_list();
    }

    //每10分钟执行
//    @Scheduled(cron = "0 0/5 * * * ?")
    //all
//    @Scheduled(cron = "0 30 03 * * ?")
    public void getICO_Trackico_detail() throws MethodNotSupportException {

        //all
        List<ICO_trackico_item> items = ico_trackico_itemDao.findAllByStatus("ini");

//        List<ICO_trackico_item> items = ico_trackico_itemDao.findTop10ByStatus("ini");

        // List<ICO_trackico_item> items =
        // ico_trackico_itemDao.findOneByItemUrl("https://www.trackico.io/ico/w12/");
        log.info("get items num :" + items.size());
        if (items.size() != 0) {
            // 获取开始时间
            long startTime = System.currentTimeMillis();

            for (int i = 0; i < items.size(); i++) {
                ICO_trackico_item item = items.get(i);
                log.info("--- will extra Trackico_detail: " + i + " ,total:" + items.size());
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
            log.info(" this time crawled," + "items num:" + items.size() + ".cost;" + timestr);
        } else {
            log.info("get item from databash ,item num is 0");
        }
    }

    // <===================== 下面是icorating的相关job ===================================>
    //每3小时 0 0 */4 * * ?
//    @Scheduled(cron = "0 0 */4 * * ?")
    public void getICO_icorating_list() throws MethodNotSupportException {
        icoratingService.getIcotatingList();
    }

    //    @Scheduled(cron = "0 0/5 * * * ?")
//    @Scheduled(cron = "0 00 17 * * ?")
    public void icoratingDetailManager() {
        // 获取开始时间
        long startTime = System.currentTimeMillis();
        log.info("******** start icotatingDetail task ********");
//        List<ICO_icorating_list> listItems = ico_icorating_listDao.findTop10ByCrawledStatu("ini");
        //all
        List<ICO_icorating_list> listItems = ico_icorating_listDao.findAllByCrawledStatu("ini");

        log.info("get items num : " + listItems.size() + "  ,from list table");
        if (CollectionUtils.isNotEmpty(listItems)) {
            for (int i = 0; i < listItems.size(); i++) {
                ICO_icorating_list item = listItems.get(i);
//                String name = item.getName();
//                log.info("name:" + name);
                log.info("--- will extra IcoratingDetail: " + i + " ,total:" + listItems.size());
                icoratingService.getIcoratingDetail(item);
            }
        } else {
            log.info("get null from list table");
        }
        // 获取结束时间
        long endTime = System.currentTimeMillis();
        long mss = endTime - startTime;
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        String timestr = hours + " hours " + minutes + " minutes " + seconds + " seconds ";
        log.info(">>>>>>>>>> this time crawled," + "items num:" + listItems.size() + ".cost:" + timestr);
    }

    //    @Scheduled(cron = "0 00 21 * * ?")
    public void getIcoratingFoundsList() {
        icoratingService.getIcoratingFundsList();
    }

    @Scheduled(cron = "0 30 07 * * ?")
    public void icoratingFoundsDetailManager() {
        //查出所有的item，因为列表页已经判断，此处不会有重复
        List<ICO_icorating_funds_list> foundslist = new ArrayList<>();
        foundslist = foundsListDao.getAllByCrawledStatus("ini");
        //测试
//        ICO_icorating_funds_list one = foundsListDao.findICO_icorating_funds_listByLink("https://icorating.com/funds/blocktrade-investments/");
//        foundslist.add(one);
        log.info("********** Start icorating founds detail task **********");
        log.info("--- get from icorating_funds_list ,items num:" + foundslist.size());
        if (CollectionUtils.isNotEmpty(foundslist)) {
            for (int i = 0; i < foundslist.size(); i++) {
                ICO_icorating_funds_list foundsitem = foundslist.get(i);
//                log.info(foundsitem.getLink());
                if (StringUtils.isNotEmpty(foundsitem.getLink())) {
                    log.info("--- will extra  :" + i);
                    icoratingService.getIcoratingFoundDetail(foundsitem);
                }
            }
            log.info("********** icorating founds detail task over **********");
        } else {
            log.info("get null from list table");
        }
    }

    // <===================== 下面是icodrops的相关job ===================================>
//    @Scheduled(cron = "0 00 21 * * ?")
    public void icodropsListManager() {
        log.info("***** getIcodropsListWithInput task start");
        ArrayList<String> urlList = new ArrayList<>(10);
        urlList.add("https://icodrops.com/category/active-ico/");
        urlList.add("https://icodrops.com/category/upcoming-ico/");
        urlList.add("https://icodrops.com/category/ended-ico/");
        for (String url : urlList) {
            icodropsService.getIcodropsListWithInput(url);
        }
        log.info("***** getIcodropsListWithInput task over");
    }

//    @Scheduled(cron = "0 00 06 * * ?")
    public void icodropsDetailManager() {
        log.info("***** start icodropsDetailManager task *****");
        //1106
        List<ICO_icodrops_list> itemList = new ArrayList<>();
        itemList = icodropsItemDao.getAllByCrawledStatu("ini");
//        itemList = itemDao.getICO_icodrops_listByIco_url("https://icodrops.com/hashgraph/");
        if (CollectionUtils.isNotEmpty(itemList)) {
            log.info("--- total items is : " + itemList.size());
            for (int i = 0; i < itemList.size(); i++) {
                log.info("----- will extra item:" + i);
                ICO_icodrops_list item = itemList.get(i);
                icodropsService.getIcodropsDetail(item);
            }
            log.info("***** icodropsDetailManager task over *****");

        } else {
            log.info("--- get null ,from icodrops list tabel");
        }
    }
}

