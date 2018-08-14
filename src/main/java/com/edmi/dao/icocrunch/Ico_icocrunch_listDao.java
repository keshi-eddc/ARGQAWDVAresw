package com.edmi.dao.icocrunch;

import com.edmi.entity.icocrunch.Ico_icocrunch_list;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface Ico_icocrunch_listDao extends JpaRepository<Ico_icocrunch_list,Long> {

    Ico_icocrunch_list findIco_icocrunch_listByBlockUrlAndBlockType(String blockUrl,String blockType);

    @Query("select max(li.serialNumber) from Ico_icocrunch_list li where li.blockType = :blockType")
    Long getIco_icocrunch_listMaxSerialNumber(@Param("blockType")String show);


    Ico_icocrunch_list findTop1ByBlockTypeAndSerialNumberOrderByCurrentPageDesc(String show,long serialNumber);

    @Query("select distinct li.blockUrl from Ico_icocrunch_list li where li.detailsStatus = :detailsStatus")
    List<String> getIco_icocrunch_listByDetailsStatus(@Param("detailsStatus")String detailsStatus);

    @Modifying
    @Query("update Ico_icocrunch_list set detailsStatus = :detailsStatus where blockUrl = :blockUrl")
    int updateIco_icocrunch_listByBlockUrl(@Param("detailsStatus")String detailsStatus,@Param("blockUrl")String blockUrl);

}
