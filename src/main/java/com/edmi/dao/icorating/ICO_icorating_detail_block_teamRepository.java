package com.edmi.dao.icorating;

import com.edmi.entity.icorating.ICO_icorating_detail_block_team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * team 对象 数据库操作
 */
public interface ICO_icorating_detail_block_teamRepository extends JpaRepository<ICO_icorating_detail_block_team, Long> {

    @Query("select info from ICO_icorating_detail_block_team info where info.ico_icorating_detail.pk_id = :fk_id ")
    List<ICO_icorating_detail_block_team> getICO_icorating_detail_block_teamsByFkid(@Param("fk_id") long fk_id);

    /**
     * 根据详情页的 link 查询
     *
     * @param link
     * @return
     */
    List<ICO_icorating_detail_block_team> findICO_icorating_detail_block_teamsByLink(String link);

}
