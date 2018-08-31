package com.edmi.dao.icodrops;

import com.edmi.entity.icodrops.ICO_icodrops_detail_socialLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 社交链接 对象 数据库操作
 */
public interface ICO_icodrops_detail_socialLinkRepository extends JpaRepository<ICO_icodrops_detail_socialLink, Long> {

    @Query("select info from ICO_icodrops_detail_socialLink info where info.ico_icodrops_detail.pk_id = :fk_id ")
    List<ICO_icodrops_detail_socialLink> getICO_icodrops_detail_socialLinksByFkid(@Param("fk_id") long fk_id);
}
