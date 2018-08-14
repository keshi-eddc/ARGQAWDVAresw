package com.edmi.dao.feixiaohao;


import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICO_Feixiaohao_ExchangeRepository extends JpaRepository<ICO_Feixiaohao_Exchange,Long> {

    @Query("from ICO_Feixiaohao_Exchange e where e.status=:status")
    List<ICO_Feixiaohao_Exchange> getICO_Feixiaohao_ExchangeByStatus(@Param("status")String status);

    @Query("from ICO_Feixiaohao_Exchange e where e.name_link =:name_link")
    ICO_Feixiaohao_Exchange getICO_Feixiaohao_ExchangeByName_link(@Param("name_link")String name_link);

    @Query(value = "select ex from ICO_Feixiaohao_Exchange ex order by ex.pk_id asc")
    Page<ICO_Feixiaohao_Exchange> getICO_Feixiaohao_Exchange(Pageable pageable);
}
