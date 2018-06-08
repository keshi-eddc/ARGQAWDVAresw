package com.edmi.dao.etherscan;


import com.edmi.entity.etherscan.ICO_Etherscan_IO_Blocks;
import com.edmi.entity.etherscan.ICO_Etherscan_IO_Blocks_Txs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICO_Etherscan_IO_Blocks_TxsRepository extends JpaRepository<ICO_Etherscan_IO_Blocks_Txs,Long> {

     ICO_Etherscan_IO_Blocks_Txs findICO_Etherscan_IO_Blocks_TxsByTxhash(String txhash);
     List<ICO_Etherscan_IO_Blocks_Txs> findTop30ByStatus(String status);


}
