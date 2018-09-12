package com.edmi.dao.trackico;

import com.edmi.entity.trackico.ICO_trackico_detail_block_bounty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 2018年9月10日11:52:40
 * ICO_trackico_detail_block_bounty 对象数据库操作
 */
public interface ICO_trackico_detail_block_bountyRepository extends JpaRepository<ICO_trackico_detail_block_bounty, Long> {

    @Query("select bt from ICO_trackico_detail_block_bounty bt where bt.ico_trackico_detail.pk_id = :fk_id ")
    List<ICO_trackico_detail_block_bounty> getICO_trackico_detail_block_bountysByFkid(@Param("fk_id") long fk_id);

}
