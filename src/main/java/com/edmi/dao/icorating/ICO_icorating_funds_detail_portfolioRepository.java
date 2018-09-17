package com.edmi.dao.icorating;

import com.edmi.entity.icorating.ICO_icorating_funds_detail_portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * ICO_icorating_funds_detail_portfolio 对象数据库操作
 */
public interface ICO_icorating_funds_detail_portfolioRepository extends JpaRepository<ICO_icorating_funds_detail_portfolio, Long> {

    @Query("select info from ICO_icorating_funds_detail_portfolio info where info.ico_icorating_funds_detail.pk_id = :fk_id ")
    List<ICO_icorating_funds_detail_portfolio> getICO_icorating_funds_detail_portfoliosByFkid(@Param("fk_id") long fk_id);

}
