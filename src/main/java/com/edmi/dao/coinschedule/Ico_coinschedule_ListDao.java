package com.edmi.dao.coinschedule;

import com.edmi.entity.coinschedule.Ico_coinschedule_List;
import com.edmi.entity.icocrunch.Ico_icocrunch_list;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Ico_coinschedule_ListDao extends JpaRepository<Ico_coinschedule_List,Long> {

    Ico_coinschedule_List findIco_icocrunch_listByIcoCoinscheduleUrlAndBlockType(String icoCoinscheduleUrl, String blockType);

}
