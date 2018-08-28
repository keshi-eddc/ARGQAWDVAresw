package com.edmi.dao.icodrops;

import com.edmi.entity.icodrops.ICO_icodrops_detail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * icodrops_detail 对象数据库操作
 */
public interface ICO_icodrops_detailRepository extends JpaRepository<ICO_icodrops_detail, Long> {

    /**
     * 根据 link 查询一条
     *
     * @param link
     * @return
     */
    ICO_icodrops_detail getICO_icodrops_detailByLink(String link);
}
