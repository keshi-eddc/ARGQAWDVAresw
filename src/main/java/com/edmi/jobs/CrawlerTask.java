package com.edmi.jobs;

import com.edmi.dao.etherscan.*;
import com.edmi.dao.feixiaohao.ICO_Feixiaohao_ExchangeRepository;
import com.edmi.dao.feixiaohao.ICO_Feixiaohao_Exchange_DetailsRepository;
import com.edmi.entity.etherscan.*;
import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange;
import com.edmi.service.service.EtherscanService;
import com.edmi.service.service.FeixiaohaoService;
import com.edmi.utils.http.exception.MethodNotSupportException;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component
public class CrawlerTask {

    @Autowired
    private EtherscanService etherscanService;

    @Autowired
    private FeixiaohaoService feixiaohaoService;

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
    private ICO_Feixiaohao_Exchange_DetailsRepository exchange_detailsDao;

    /*@Scheduled(cron = "0 38 08 25 * ?")*/
    public void getICO_Etherscan_IO_Blocks() throws Exception {
        List<String> links = etherscanService.getICO_Etherscan_IO_Blocks_TotalPageLinks();
        Long serial = Calendar.getInstance().getTime().getTime();
        if(links.size()>0){
            for(String link:links){
                etherscanService.getICO_Etherscan_IO_Blocks(link,serial,links.size());
                Thread.sleep(3*1000);
            }
        }
    }

//    @Scheduled(cron = "0 */1 * * * ?")
    public void getICO_Etherscan_IO_Blocks_Info() throws Exception {
        int server = 1;
        server = RandomUtils.nextInt(1,3);
        /*int second = Calendar.getInstance().get(Calendar.SECOND);
        if(second>=0&&second<30){
            server = 1;
        }else{
            server = 2;
        }*/
        List<ICO_Etherscan_IO_Blocks> blocks = blocksDao.findTop50ByStatusAndServer("ini",server);
        for(ICO_Etherscan_IO_Blocks block:blocks){
            etherscanService.getICO_Etherscan_IO_Blocks_Info(block);
            Thread.sleep(5*100);
        }
    }
//    @Scheduled(cron = "30 */1 * * * ?")
    public void getICO_Etherscan_IO_Blocks_Txs_Page_List() throws Exception {
        int server = 1;
        server = RandomUtils.nextInt(1,3);
        /*int second = Calendar.getInstance().get(Calendar.SECOND);
        if(second>=0&&second<30){
            server = 1;
        }else{
            server = 2;
        }*/
        List<ICO_Etherscan_IO_Blocks> blocks = blocksDao.findTop50ByPagestatusAndServer("ini",server);
        for(ICO_Etherscan_IO_Blocks block:blocks){
            etherscanService.getICO_Etherscan_IO_Blocks_TxsPages(block);
            Thread.sleep(5*100);
        }

    }
//    @Scheduled(cron = "0 */1 * * * ?")
    public void getICO_Etherscan_IO_Blocks_Txs() throws Exception {

        List<ICO_Etherscan_IO_Blocks_Txs_Page_List> page_list = txs_page_listDao.findTop50ByStatus("ini");

        for(ICO_Etherscan_IO_Blocks_Txs_Page_List page:page_list){
            etherscanService.getICO_Etherscan_IO_Blocks_Txs(page);
            Thread.sleep(5*100);
        }
    }


//   @Scheduled(cron = "30 */1 * * * ?")
    public void getICO_Etherscan_IO_Blocks_Txs_Info() throws Exception {

        List<ICO_Etherscan_IO_Blocks_Txs> txs = txsDao.findTop50ByStatus("ini");
        for(ICO_Etherscan_IO_Blocks_Txs tx:txs){
            etherscanService.getICO_Etherscan_IO_Blocks_Txs_Info(tx);
            Thread.sleep(5*100);
        }
    }

// <=====================  下面是Blocks_Forked的job ===================================>

//   @Scheduled(cron = "0 05 11 31 * ?")
   public void getICO_Etherscan_IO_Blocks_Forked() throws Exception {
       List<String> links = etherscanService.getICO_Etherscan_IO_Blocks_Forked_TotalPageLinks();
       Long serial = Calendar.getInstance().getTime().getTime();
       if(links.size()>0){
           for(String link:links){
               etherscanService.getICO_Etherscan_IO_Blocks_Forked(link,serial,links.size());
               Thread.sleep(3*1000);
           }
       }
   }
    //@Scheduled(cron = "0 0/1 * * * ?")
    public void getICO_Etherscan_IO_Blocks_Forked_Info() throws Exception {
        int server = 1;
        server = RandomUtils.nextInt(1,3);
        /*int second = Calendar.getInstance().get(Calendar.SECOND);
        if(second>=0&&second<30){
            server = 1;
        }else{
            server = 2;
        }*/
        List<ICO_Etherscan_IO_Blocks_Forked> blocks = blocks_forkedDao.findTop50ByStatusAndServer("ini",server);
        for(ICO_Etherscan_IO_Blocks_Forked block:blocks){
            etherscanService.getICO_Etherscan_IO_Blocks_Forked_Info(block);
            Thread.sleep(5*100);
        }
    }
    //@Scheduled(cron = "30 0/1 * * * ?")
    public void getICO_Etherscan_IO_Blocks_Forked_Txs_Page_List() throws Exception {
        int server = 1;
        server = RandomUtils.nextInt(1,3);
        /*int second = Calendar.getInstance().get(Calendar.SECOND);
        if(second>=0&&second<30){
            server = 1;
        }else{
            server = 2;
        }*/
        List<ICO_Etherscan_IO_Blocks_Forked> blocks = blocks_forkedDao.findTop50ByPagestatusAndServer("ini",server);
        for(ICO_Etherscan_IO_Blocks_Forked block:blocks){
            etherscanService.getICO_Etherscan_IO_Blocks_Forked_TxsPages(block);
            Thread.sleep(5*100);
        }

    }

//   @Scheduled(cron = "0 */1 * * * ?")
    public void getICO_Etherscan_IO_Blocks_Forked_Txs() throws Exception {

        List<ICO_Etherscan_IO_Blocks_Forked_Txs_Page_List> page_list = forked_txs_page_listDao.findTop50ByStatus("ini");

        for(ICO_Etherscan_IO_Blocks_Forked_Txs_Page_List page:page_list){
            etherscanService.getICO_Etherscan_IO_Blocks_Forked_Txs(page);
            Thread.sleep(5*100);
        }
    }
    //@Scheduled(cron = "0/30 * * * * ?")
    public void getICO_Etherscan_IO_Blocks_Forked_Txs_Info() throws Exception {

        List<ICO_Etherscan_IO_Blocks_Forked_Txs> txs = forked_txsDao.findTop50ByStatus("ini");
        for(ICO_Etherscan_IO_Blocks_Forked_Txs tx:txs){
            etherscanService.getICO_Etherscan_IO_Blocks_Forked_Txs_Info(tx);
            Thread.sleep(5*100);
        }
    }
    // <===================== 下面是Feixiaohao的相关job ===================================>
    @Scheduled(cron = "0 16/10 * * * ?")
    public void getICO_Feixiaohao_Exchange() throws Exception {
        System.out.println("1111111111111");
        feixiaohaoService.getICO_Feixiaohao_Exchange();
        System.out.println("2222222222222");
    }
    /*@Scheduled(cron = "0/30 * * * * ?")*/
    public void getICO_Feixiaohao_Exchange_Details() throws Exception {
        List<ICO_Feixiaohao_Exchange> exchanges = exchangeDao.getICO_Feixiaohao_ExchangeByStatus("ini");
        for(ICO_Feixiaohao_Exchange exchange:exchanges){
            feixiaohaoService.getICO_Feixiaohao_Exchange_Details(exchange);
        }
    }
    /*@Scheduled(cron = "0/30 * * * * ?")*/
    public void getICO_Feixiaohao_Exchange_Counter_Party_Details() throws MethodNotSupportException {
        List<String> links = exchange_detailsDao.getICO_Feixiaohao_Exchange();
        for(String link:links){
            feixiaohaoService.getICO_Feixiaohao_Exchange_Counter_Party_Details(link);
        }
    }
}
