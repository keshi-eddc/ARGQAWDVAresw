package com.edmi.dao.icorating;

import com.edmi.entity.icorating.ICO_icorating_funds_list;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * icorating founds 列表对象数据库操作
 */
public interface ICO_icorating_funds_listRepository extends JpaRepository<ICO_icorating_funds_list, Long> {
    /**
     * 根据link查询
     */
    @Query("select it from ICO_icorating_funds_list it where it.link = :link ")
    ICO_icorating_funds_list getICO_icorating_funds_listByLink(@Param("link") String link);

    /**
     * 查询所有的item
     *
     * @return
     */
    @Override
    List<ICO_icorating_funds_list> findAll();

    /**
     * 查询所有的item 通过status
     *
     * @param crawled_status
     * @return
     */
    List<ICO_icorating_funds_list> getAllByCrawledStatus(String crawledStatus);

    /**
     * 根据link查一条
     *
     * @param link
     * @return
     */
    ICO_icorating_funds_list findICO_icorating_funds_listByLink(String link);
}
