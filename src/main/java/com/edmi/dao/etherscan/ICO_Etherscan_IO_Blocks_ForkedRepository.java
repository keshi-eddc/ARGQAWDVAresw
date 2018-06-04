package com.edmi.dao.etherscan;


import com.edmi.entity.etherscan.ICO_Etherscan_IO_Blocks_Forked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICO_Etherscan_IO_Blocks_ForkedRepository extends JpaRepository<ICO_Etherscan_IO_Blocks_Forked,Long> {

    List<ICO_Etherscan_IO_Blocks_Forked> findTop50ByStatusAndServer(String status, int server);
    List<ICO_Etherscan_IO_Blocks_Forked> findTop50ByPagestatusAndServer(String pageStatus, int server);

}
