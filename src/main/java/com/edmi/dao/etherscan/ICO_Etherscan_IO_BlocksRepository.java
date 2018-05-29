package com.edmi.dao.etherscan;


import com.edmi.entity.etherscan.ICO_Etherscan_IO_Blocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICO_Etherscan_IO_BlocksRepository extends JpaRepository<ICO_Etherscan_IO_Blocks,Long> {

    List<ICO_Etherscan_IO_Blocks> findTop50ByStatusAndServer(String status,int server);
    List<ICO_Etherscan_IO_Blocks> findTop50ByPagestatusAndServer(String pageStatus,int server);

}
