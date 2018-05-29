package com.edmi.jobs;

import com.edmi.dao.etherscan.ICO_Etherscan_IO_BlocksRepository;
import com.edmi.dao.etherscan.ICO_Etherscan_IO_Blocks_TxsRepository;
import com.edmi.dao.etherscan.ICO_Etherscan_IO_Blocks_Txs_Page_ListRepository;
import com.edmi.entity.etherscan.ICO_Etherscan_IO_Blocks;
import com.edmi.entity.etherscan.ICO_Etherscan_IO_Blocks_Txs;
import com.edmi.entity.etherscan.ICO_Etherscan_IO_Blocks_Txs_Page_List;
import com.edmi.service.service.EtherscanService;
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
    private ICO_Etherscan_IO_BlocksRepository blocksDao;

    @Autowired
    private ICO_Etherscan_IO_Blocks_Txs_Page_ListRepository txs_page_listDao;

    @Autowired
    private ICO_Etherscan_IO_Blocks_TxsRepository txsDao;

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

    @Scheduled(cron = "0/30 * * * * ?")
    public void getICO_Etherscan_IO_Blocks_Info() throws Exception {
        int server = 1;
        int second = Calendar.getInstance().get(Calendar.SECOND);
        if(second>=0&&second<30){
            server = 1;
        }else{
            server = 2;
        }
        List<ICO_Etherscan_IO_Blocks> blocks = blocksDao.findTop50ByStatusAndServer("ini",server);
        for(ICO_Etherscan_IO_Blocks block:blocks){
            etherscanService.getICO_Etherscan_IO_Blocks_Info(block);
            Thread.sleep(5*100);
        }
    }
    @Scheduled(cron = "0/30 * * * * ?")
    public void getICO_Etherscan_IO_Blocks_Txs_Page_List() throws Exception {
        int server = 1;
        int second = Calendar.getInstance().get(Calendar.SECOND);
        if(second>=0&&second<30){
            server = 1;
        }else{
            server = 2;
        }
        List<ICO_Etherscan_IO_Blocks> blocks = blocksDao.findTop50ByPagestatusAndServer("ini",server);
        for(ICO_Etherscan_IO_Blocks block:blocks){
            etherscanService.ICO_Etherscan_IO_Blocks_TxsPages(block);
            Thread.sleep(5*100);
        }

    }
    /*@Scheduled(cron = "0 0/1 * * * ?")*/
    public void getICO_Etherscan_IO_Blocks_Txs() throws Exception {

        List<ICO_Etherscan_IO_Blocks_Txs_Page_List> page_list = txs_page_listDao.findTop20ByStatus("ini");

        for(ICO_Etherscan_IO_Blocks_Txs_Page_List page:page_list){
            etherscanService.getICO_Etherscan_IO_Blocks_Txs(page);
        }
    }


    /*@Scheduled(cron = "0 0/1 * * * ?")*/
    public void getICO_Etherscan_IO_Blocks_Txs_Info() throws Exception {

        List<ICO_Etherscan_IO_Blocks_Txs> txs = txsDao.findTop30ByStatus("ini");
        for(ICO_Etherscan_IO_Blocks_Txs tx:txs){
            etherscanService.getICO_Etherscan_IO_Blocks_Txs_Info(tx);
            Thread.sleep(5*1*100);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        while(true){
            Thread.sleep(5*100);
            System.out.println(Calendar.getInstance().get(Calendar.SECOND));

        }

    }
}
