package com.edmi.dao.coinschedule;

import com.edmi.entity.coinschedule.ICO_coinschedule_icos_list;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ICO_coinschedule_icos_listDao 对象数据库操作
 */

public interface ICO_coinschedule_icos_listDao extends JpaRepository<ICO_coinschedule_icos_list, Long> {

    ICO_coinschedule_icos_list findICO_coinschedule_icos_listByName(String name);
}
