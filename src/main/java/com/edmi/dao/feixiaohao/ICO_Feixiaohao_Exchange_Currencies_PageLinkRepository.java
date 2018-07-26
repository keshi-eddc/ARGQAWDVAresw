package com.edmi.dao.feixiaohao;


import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange_Currencies_Page_Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ICO_Feixiaohao_Exchange_Currencies_PageLinkRepository extends JpaRepository<ICO_Feixiaohao_Exchange_Currencies_Page_Link,Long> {

    @Query("select page from ICO_Feixiaohao_Exchange_Currencies_Page_Link page where page.currencies.pk_id = :currencies_pk_id")
    List<ICO_Feixiaohao_Exchange_Currencies_Page_Link> getICO_Feixiaohao_Exchange_Currencies_Page_Link(@Param("currencies_pk_id")long currencies_pk_id);

}
