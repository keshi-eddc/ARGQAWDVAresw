package com.edmi.dao.feixiaohao;


import com.edmi.entity.feixiaohao.ICO_Data_Process;
import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICO_Data_ProcessRepository extends JpaRepository<ICO_Data_Process,Long> {

    @Query("select p from ICO_Data_Process p where p.api_category = :api_category")
    List<ICO_Data_Process> getICO_Data_ProcessByApi_category(@Param("api_category") String api_category);
}
