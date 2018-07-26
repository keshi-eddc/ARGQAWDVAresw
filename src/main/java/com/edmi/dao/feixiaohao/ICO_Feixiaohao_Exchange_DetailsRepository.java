package com.edmi.dao.feixiaohao;


import com.edmi.ManageApplication;
import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange;
import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange_Details;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ICO_Feixiaohao_Exchange_DetailsRepository extends JpaRepository<ICO_Feixiaohao_Exchange_Details,Long> {

    @Query("select distinct d.name_link from ICO_Feixiaohao_Exchange_Details d")
    List<String> getICO_Feixiaohao_Exchange();

    @Query("select distinct ex.name_link as currency_link from ICO_Feixiaohao_Exchange_Details ex \n" +
            "    where not exists(select cur from ICO_Feixiaohao_Exchange_Currencies cur where ex.name_link = cur.currency_link)")
    List<String> getICO_Feixiaohao_ExchangeDistinctByName_link();

    @Query("select exd from ICO_Feixiaohao_Exchange_Details exd where exd.ico_feixiaohao_exchange.pk_id = :exchange_id")
    List<ICO_Feixiaohao_Exchange_Details> getICO_Feixiaohao_Exchange_Details(@Param("exchange_id")long exchange_id);
}
