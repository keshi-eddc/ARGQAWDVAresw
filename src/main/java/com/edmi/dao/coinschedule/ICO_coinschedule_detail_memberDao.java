package com.edmi.dao.coinschedule;

import com.edmi.entity.coinschedule.ICO_coinschedule_detail_member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 人员对象的数据库操作
 */
public interface ICO_coinschedule_detail_memberDao extends JpaRepository<ICO_coinschedule_detail_member, Long> {
}
