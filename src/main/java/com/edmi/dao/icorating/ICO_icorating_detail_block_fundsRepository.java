package com.edmi.dao.icorating;

import com.edmi.entity.icorating.ICO_icorating_detail_block_funds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 公司金融 对象 数据库操作
 */
public interface ICO_icorating_detail_block_fundsRepository extends JpaRepository<ICO_icorating_detail_block_funds, Long> {

    @Query("select info from ICO_icorating_detail_block_funds info where info.ico_icorating_detail.pk_id = :fk_id ")
    List<ICO_icorating_detail_block_funds> getICO_icorating_detail_block_fundsByFkid(@Param("fk_id") long fk_id);


    List<ICO_icorating_detail_block_funds> findICO_icorating_detail_block_fundsByLink(String link);
}
