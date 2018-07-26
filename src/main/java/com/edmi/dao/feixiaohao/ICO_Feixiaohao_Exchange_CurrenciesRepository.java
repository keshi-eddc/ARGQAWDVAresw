package com.edmi.dao.feixiaohao;


import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange_Currencies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ICO_Feixiaohao_Exchange_CurrenciesRepository extends JpaRepository<ICO_Feixiaohao_Exchange_Currencies,Long> {

    @Query("from ICO_Feixiaohao_Exchange_Currencies e where e.details_status=:details_status order by e.pk_id asc")
    List<ICO_Feixiaohao_Exchange_Currencies> getICO_Feixiaohao_Exchange_CurrenciesByDetails_status(@Param("details_status")String details_status);

    @Query("select c from ICO_Feixiaohao_Exchange_Currencies c  order by c.pk_id asc")
    Page<ICO_Feixiaohao_Exchange_Currencies> getICO_Feixiaohao_Exchange_Currencies(Pageable pageable);

}
