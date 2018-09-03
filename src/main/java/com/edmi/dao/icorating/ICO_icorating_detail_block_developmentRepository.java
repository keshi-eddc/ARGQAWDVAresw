package com.edmi.dao.icorating;

import com.edmi.entity.icorating.ICO_icorating_detail_block_development;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 公司发展 对象的数据库操作
 */
public interface ICO_icorating_detail_block_developmentRepository extends JpaRepository<ICO_icorating_detail_block_development, Long> {

    @Query("select info from ICO_icorating_detail_block_development info where info.ico_icorating_detail.pk_id = :fk_id ")
    List<ICO_icorating_detail_block_development> getICO_icorating_detail_block_developmentsByFkid(@Param("fk_id") long fk_id);

    List<ICO_icorating_detail_block_development> findICO_icorating_detail_block_developmentsByLink(String link);

}
