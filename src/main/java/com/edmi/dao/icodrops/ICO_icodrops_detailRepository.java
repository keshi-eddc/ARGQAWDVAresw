package com.edmi.dao.icodrops;

import com.edmi.entity.icodrops.ICO_icodrops_detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

    @Query("select d from ICO_icodrops_detail d where d.link = :link  order by  d.insert_Time desc")
    List<ICO_icodrops_detail> getICO_icodrops_detailByLinkOrOrderByInsert_Time(@Param("link") String link);
}
