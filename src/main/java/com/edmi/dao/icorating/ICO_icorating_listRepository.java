package com.edmi.dao.icorating;

import com.edmi.entity.icorating.ICO_icorating_list;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 负责 ICO_icorating_list 对象的数据库操作
 */
public interface ICO_icorating_listRepository extends JpaRepository<ICO_icorating_list, Long> {
    /**
     * @return
     */
    @Query("select it from ICO_icorating_list it where it.link = :link ")
    ICO_icorating_list getICO_icorating_listByLink(@Param("link") String link);

    @Transactional
    @Modifying
    @Query("delete from ICO_icorating_list  where link = :link")
    int deleteICO_icorating_listByLink(@Param("link") String link);

    /**
     * 获得上次抓取的批次
     *nativeQuery = true 写数据库的属性
     * @return
     */
    @Query(value = "select max(li.crawled_times) from ICO_icorating_list li", nativeQuery = true)
    Integer getMaxCrawledTimes();

    /**
     * 获得对应批次的 最大页数
     * 写实体类的属性
     * @return
     */
    @Query("select it from ICO_icorating_list it where it.crawledTimes = :crawled_times order by it.currentPage desc")
    List<ICO_icorating_list> getMaxCurrentPageWithMaxCrawledTimes(@Param("crawled_times") Integer crawled_times);

    /**
     * 获得crawlstatus = ini 的top10
     */
    List<ICO_icorating_list> findTop10ByCrawledStatu(String crawledStatu);

    /**
     *  获得crawlstatus = ini 所有的
     * @param crawledStatus
     * @return
     */
    List<ICO_icorating_list> findAllByCrawledStatu(String crawledStatus);
}
