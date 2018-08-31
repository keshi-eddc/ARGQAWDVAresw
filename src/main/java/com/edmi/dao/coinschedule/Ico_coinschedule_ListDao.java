package com.edmi.dao.coinschedule;

import com.edmi.entity.coinschedule.Ico_coinschedule_List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Ico_coinschedule_ListDao extends JpaRepository<Ico_coinschedule_List, Long> {

    Ico_coinschedule_List findIco_icocrunch_listByIcoCoinscheduleUrlAndBlockType(String icoCoinscheduleUrl, String blockType);

    /**
     * 在list 表 不在 detail表
     *
     * @return
     */
    @Query("select l from Ico_coinschedule_List l where l.icoCoinscheduleUrl not in (select d.link from ICO_coinschedule_detail d)")
    List<Ico_coinschedule_List> findIco_coinschedule_ListWithNotIn();
}
