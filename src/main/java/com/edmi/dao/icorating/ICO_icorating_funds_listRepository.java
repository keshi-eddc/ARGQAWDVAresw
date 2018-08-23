package com.edmi.dao.icorating;

import com.edmi.entity.icorating.ICO_icorating_funds_list;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * icorating founds 列表对象数据库操作
 */
public interface ICO_icorating_funds_listRepository extends JpaRepository<ICO_icorating_funds_list, Long> {
    /**
     * 根据link查询
     */
    @Query("select it from ICO_icorating_funds_list it where it.link = :link ")
    ICO_icorating_funds_list getICO_icorating_funds_listByLink(@Param("link") String link);

}
