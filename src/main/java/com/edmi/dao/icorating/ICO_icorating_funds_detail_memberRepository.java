package com.edmi.dao.icorating;

import com.edmi.entity.icorating.ICO_icorating_funds_detail_member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * funds details member 对象数据库操作
 */
public interface ICO_icorating_funds_detail_memberRepository extends JpaRepository<ICO_icorating_funds_detail_member, Long> {

    @Query("select info from ICO_icorating_funds_detail_member info where info.ico_icorating_funds_detail.pk_id = :fk_id ")
    List<ICO_icorating_funds_detail_member> getICO_icorating_funds_detail_membersByFkid(@Param("fk_id") long fk_id);

}
