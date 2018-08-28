package com.edmi.dao.icodrops;

import com.edmi.entity.icodrops.ICO_icodrops_list;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * icodrops list对象数据库操作
 */
public interface ICO_icodrops_listRepository extends JpaRepository<ICO_icodrops_list, Long> {

    /**
     * 根据ico_url查出一条
     *
     * @param ico_url
     * @return
     */
    @Query("select it from ICO_icodrops_list it where it.ico_url = :ico_url ")
    List<ICO_icodrops_list> getICO_icodrops_listByIco_url(@Param("ico_url") String ico_url);

    /**
     * 根据crawlStstus 获得所有的
     *
     * @param crawlStstus
     * @return
     */
    List<ICO_icodrops_list> getAllByCrawledStatu(String crawlStstus);

}
