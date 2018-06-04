package com.edmi.service.service;

import com.edmi.entity.etherscan.*;
import com.edmi.utils.http.exception.MethodNotSupportException;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface EtherscanService {

    public List<String> getICO_Etherscan_IO_Blocks_TotalPageLinks() throws MethodNotSupportException;
    public void getICO_Etherscan_IO_Blocks(String link,Long serial,int page_total) throws MethodNotSupportException;
    public void getICO_Etherscan_IO_Blocks_Info(ICO_Etherscan_IO_Blocks blocks)  throws MethodNotSupportException;
    public void getICO_Etherscan_IO_Blocks_TxsPages(ICO_Etherscan_IO_Blocks blocks) throws MethodNotSupportException;
    public void getICO_Etherscan_IO_Blocks_Txs(ICO_Etherscan_IO_Blocks_Txs_Page_List page) throws MethodNotSupportException;
    public void getICO_Etherscan_IO_Blocks_Txs_Info(ICO_Etherscan_IO_Blocks_Txs txs) throws MethodNotSupportException;

    public List<String> getICO_Etherscan_IO_Blocks_Forked_TotalPageLinks() throws MethodNotSupportException;
    public void getICO_Etherscan_IO_Blocks_Forked(String link,Long serial,int page_total) throws MethodNotSupportException;
    public void getICO_Etherscan_IO_Blocks_Forked_Info(ICO_Etherscan_IO_Blocks_Forked blocks)  throws MethodNotSupportException;
    public void getICO_Etherscan_IO_Blocks_Forked_TxsPages(ICO_Etherscan_IO_Blocks_Forked blocks) throws MethodNotSupportException;
    public void getICO_Etherscan_IO_Blocks_Forked_Txs(ICO_Etherscan_IO_Blocks_Forked_Txs_Page_List page) throws MethodNotSupportException;
    public void getICO_Etherscan_IO_Blocks_Forked_Txs_Info(ICO_Etherscan_IO_Blocks_Forked_Txs txs) throws MethodNotSupportException;

}
