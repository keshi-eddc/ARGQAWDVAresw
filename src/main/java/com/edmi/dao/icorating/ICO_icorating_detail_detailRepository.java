package com.edmi.dao.icorating;

import com.edmi.entity.icorating.ICO_icorating_detail_detail;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
