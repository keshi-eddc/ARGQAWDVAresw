package com.edmi.dao.icorating;

import com.edmi.entity.icorating.ICO_icorating_funds_detail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * icorating founds detail 对象的数据库操作
 */
public interface ICO_icorating_funds_detailRepository extends JpaRepository<ICO_icorating_funds_detail, Long> {
    /**
     * 根据链接查本表
     *
     * @return
     */
    ICO_icorating_funds_detail findICO_icorating_funds_detailByLink(String link);
}
