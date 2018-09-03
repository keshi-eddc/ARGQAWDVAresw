package com.edmi.dao.coinschedule;

import com.edmi.entity.coinschedule.ICO_coinschedule_detail_icoinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * ICO_coinschedule_detail_icoinfo 对象数据库操作
 */
public interface ICO_coinschedule_detail_icoinfoDao extends JpaRepository<ICO_coinschedule_detail_icoinfo, Long> {

    @Query("select info from ICO_coinschedule_detail_icoinfo info where info.ico_coinschedule_detail.pk_id = :fk_id ")
    List<ICO_coinschedule_detail_icoinfo> getICO_coinschedule_detail_icoinfosByFkid(@Param("fk_id") long fk_id);

}
