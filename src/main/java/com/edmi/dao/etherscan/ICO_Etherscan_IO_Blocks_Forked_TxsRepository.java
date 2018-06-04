package com.edmi.dao.etherscan;


import com.edmi.entity.etherscan.ICO_Etherscan_IO_Blocks_Forked_Txs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICO_Etherscan_IO_Blocks_Forked_TxsRepository extends JpaRepository<ICO_Etherscan_IO_Blocks_Forked_Txs,Long> {

     ICO_Etherscan_IO_Blocks_Forked_Txs findICO_Etherscan_IO_Blocks_TxsByTxhash(String txhash);
     List<ICO_Etherscan_IO_Blocks_Forked_Txs> findTop50ByStatus(String status);


}
