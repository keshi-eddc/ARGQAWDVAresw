package com.edmi.dao.icorating;

import com.edmi.entity.icorating.ICO_icorating_detail_detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * ICO_icorating_detail_detail 对象数据库操作
 */
public interface ICO_icorating_detail_detailRepository extends JpaRepository<ICO_icorating_detail_detail, Long> {
    /**
     * 根据详情页link查
     *
     * @param link
     * @return
     */
    List<ICO_icorating_detail_detail> findICO_icorating_detail_detailsByLink(String link);

    @Query("select info from ICO_icorating_detail_detail info where info.ico_icorating_detail.pk_id = :fk_id ")
    List<ICO_icorating_detail_detail> getICO_icorating_detail_detailsByFkid(@Param("fk_id") long fk_id);
}
