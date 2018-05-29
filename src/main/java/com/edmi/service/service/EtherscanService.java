package com.edmi.service.service;

import com.edmi.entity.etherscan.ICO_Etherscan_IO_Blocks;
import com.edmi.entity.etherscan.ICO_Etherscan_IO_Blocks_Txs;
import com.edmi.entity.etherscan.ICO_Etherscan_IO_Blocks_Txs_Page_List;
import com.edmi.utils.http.exception.MethodNotSupportException;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface EtherscanService {

    public List<String> getICO_Etherscan_IO_Blocks_TotalPageLinks() throws MethodNotSupportException;
    public void getICO_Etherscan_IO_Blocks(String link,Long serial,int page_total) throws MethodNotSupportException;
    public void getICO_Etherscan_IO_Blocks_Info(ICO_Etherscan_IO_Blocks blocks)  throws MethodNotSupportException;
    public void ICO_Etherscan_IO_Blocks_TxsPages(ICO_Etherscan_IO_Blocks blocks) throws MethodNotSupportException;
    public void getICO_Etherscan_IO_Blocks_Txs(ICO_Etherscan_IO_Blocks_Txs_Page_List page) throws MethodNotSupportException;
    public void getICO_Etherscan_IO_Blocks_Txs_Info(ICO_Etherscan_IO_Blocks_Txs txs) throws MethodNotSupportException;
}
