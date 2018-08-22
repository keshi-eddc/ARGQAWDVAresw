package com.edmi.dao.trackico;

import com.edmi.entity.trackico.ICO_trackico_detail;
import org.springframework.data.jpa.repository.JpaRepository;
import com.edmi.entity.trackico.ICO_trackico_detail_blockMilestones;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author keshi
 * @ClassName: ICO_trackico_detail_blockMilestonesRepository
 * @Description: 负责 公司里程表 对象的 数据库 操作
 * @date 2018年8月9日 上午11:37:32
 */
public interface ICO_trackico_detail_blockMilestonesRepository extends JpaRepository<ICO_trackico_detail_blockMilestones, Long> {
    //删除detail
    @Transactional
    @Modifying
    @Query("delete from ICO_trackico_detail_blockMilestones  where fk_id = :fk_id")
    int deleteICO_trackico_detai_blockMilestonesByPk_id(@Param("fk_id") long fk_id);

    @Query("select ms from ICO_trackico_detail_blockMilestones ms where ms.ico_trackico_detail.pk_id = :fk_id ")
    List<ICO_trackico_detail_blockMilestones> getICO_trackico_detail_blockMilestonesByFkid(@Param("fk_id") long fk_id);

}