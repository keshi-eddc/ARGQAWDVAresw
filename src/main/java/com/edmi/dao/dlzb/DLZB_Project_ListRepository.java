package com.edmi.dao.dlzb;


import com.edmi.entity.dlzb.DLZB_Project_List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DLZB_Project_ListRepository extends JpaRepository<DLZB_Project_List,Long> {

       List<DLZB_Project_List> findTop5000ByStatus(String status);

}
