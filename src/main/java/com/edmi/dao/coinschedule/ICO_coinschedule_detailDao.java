package com.edmi.dao.coinschedule;

import com.edmi.entity.coinschedule.ICO_coinschedule_detail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ICO_coinschedule_detail 对象数据库操作
 */
public interface ICO_coinschedule_detailDao extends JpaRepository<ICO_coinschedule_detail, Long> {

    ICO_coinschedule_detail findICO_coinschedule_detailByLink(String link);
}
