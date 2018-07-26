package com.edmi.dao.feixiaohao;


import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange_Currencies_Page_Link;
import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange_Currenciesdtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ICO_Feixiaohao_Exchange_CurrenciesdtlRepository extends JpaRepository<ICO_Feixiaohao_Exchange_Currenciesdtl,Long> {


    @Query("select dtl from ICO_Feixiaohao_Exchange_Currenciesdtl dtl where dtl.currencies.pk_id = :currencies_pk_id")
    ICO_Feixiaohao_Exchange_Currenciesdtl getICO_Feixiaohao_Exchange_Currenciesdtl(@Param("currencies_pk_id")long currencies_pk_id);
}
