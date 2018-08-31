package com.edmi.dao.coinschedule;

import com.edmi.entity.coinschedule.ICO_coinschedule_detail_member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 人员对象的数据库操作
 */
public interface ICO_coinschedule_detail_memberDao extends JpaRepository<ICO_coinschedule_detail_member, Long> {
    /**
     * 在 detail_member 表里 不在 member_sociallink 表
     *
     * @return
     */
    @Query("select l from ICO_coinschedule_detail_member l where l.member_url not in (select d.member_url from ICO_coinschedule_detail_member_sociallink d)")
    List<ICO_coinschedule_detail_member> findICO_coinschedule_detail_memberWithNotIn();
}
