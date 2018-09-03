package com.edmi.dao.coinschedule;

import com.edmi.entity.coinschedule.ICO_coinschedule_detail_sociallink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * ICO_coinschedule_detail_sociallink 对象数据库操作
 */
public interface ICO_coinschedule_detail_sociallinkDao extends JpaRepository<ICO_coinschedule_detail_sociallink, Long> {

}
