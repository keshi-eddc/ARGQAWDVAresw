package com.edmi.dao.etherscan;


import com.edmi.entity.etherscan.ICO_Etherscan_IO_Blocks_Forked_Txs_Page_List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICO_Etherscan_IO_Blocks_Forked_Txs_Page_ListRepository extends JpaRepository<ICO_Etherscan_IO_Blocks_Forked_Txs_Page_List,Long> {

    List<ICO_Etherscan_IO_Blocks_Forked_Txs_Page_List> findTop50ByStatus(String status);
}
