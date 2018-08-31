package com.edmi.dao.icodrops;

import com.edmi.entity.icodrops.ICO_icodrops_detail_tokenInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * token 对象的 数据库操作
 */
public interface ICO_icodrops_detail_tokenInfoRepository extends JpaRepository<ICO_icodrops_detail_tokenInfo, Long> {

    @Query("select info from ICO_icodrops_detail_tokenInfo info where info.ico_icodrops_detail.pk_id = :fk_id ")
    List<ICO_icodrops_detail_tokenInfo> getICO_icodrops_detail_tokenInfosByFkid(@Param("fk_id") long fk_id);

}
