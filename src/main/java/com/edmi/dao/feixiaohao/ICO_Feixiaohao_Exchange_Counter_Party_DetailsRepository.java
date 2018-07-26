package com.edmi.dao.feixiaohao;


import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange;
import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange_Counter_Party_Details;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface ICO_Feixiaohao_Exchange_Counter_Party_DetailsRepository extends JpaRepository<ICO_Feixiaohao_Exchange_Counter_Party_Details,Long> {

    @Query("select max(cpd.supply_time) from ICO_Feixiaohao_Exchange_Counter_Party_Details cpd where cpd.exchange_details_link = :exchange_details_link")
    Timestamp getICO_Feixiaohao_Exchange_LastUpdateTime(@Param("exchange_details_link")String exchange_details_link);

    @Query(value = "select cpd from ICO_Feixiaohao_Exchange_Counter_Party_Details cpd  order by cpd.pk_id asc")
    Page<ICO_Feixiaohao_Exchange_Counter_Party_Details> getICO_Feixiaohao_Exchange_Counter_Party_Details(Pageable pageable);
}
