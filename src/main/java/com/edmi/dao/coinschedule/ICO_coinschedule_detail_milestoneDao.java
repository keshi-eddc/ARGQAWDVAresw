package com.edmi.dao.coinschedule;

import com.edmi.entity.coinschedule.ICO_coinschedule_detail_milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * ICO_coinschedule_detail_milestone 对象数据库操作
 */
public interface ICO_coinschedule_detail_milestoneDao extends JpaRepository<ICO_coinschedule_detail_milestone, Long> {

    @Query("select info from ICO_coinschedule_detail_milestone info where info.ico_coinschedule_detail.pk_id = :pk_id ")
    List<ICO_coinschedule_detail_milestone> getICO_coinschedule_detail_milestonesByFkid(@Param("pk_id") long pk_id);

}
