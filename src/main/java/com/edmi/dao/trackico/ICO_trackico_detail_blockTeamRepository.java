package com.edmi.dao.trackico;

import com.edmi.entity.coinschedule.ICO_coinschedule_detail_member;
import com.edmi.entity.trackico.ICO_trackico_detail;
import org.springframework.data.jpa.repository.JpaRepository;

import com.edmi.entity.trackico.ICO_trackico_detail_blockTeam;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** 
* @ClassName: ICO_trackico_detail_blockTeamRepository 
* @Description: 负责公司人员模型（ICO_trackico_detail_blockTeam） 的数据库操作
* @author keshi
* @date 2018年8月6日 上午11:04:13 
*  
*/
public interface ICO_trackico_detail_blockTeamRepository extends JpaRepository<ICO_trackico_detail_blockTeam, Long> {

    //删除detail
    @Transactional
    @Modifying
    @Query("delete from ICO_trackico_detail_blockTeam  where fk_id = :fk_id")
    int deleteICO_trackico_detai_blockTeamlByPk_id(@Param("fk_id")long fk_id);

    @Query("select bt from ICO_trackico_detail_blockTeam bt where bt.ico_trackico_detail.pk_id = :fk_id ")
    List<ICO_trackico_detail_blockTeam> getICO_trackico_detail_blockTeamsByFkid(@Param("fk_id") long fk_id);

    /**
     * notin 增量的方式
     * @return
     */
    @Query("select l from ICO_trackico_detail_blockTeam l where member_url <> '' and l.member_url  not in (select d.memberUrl from ICO_trackico_detail_block_team_sociallink d)")
    List<ICO_trackico_detail_blockTeam> findICO_trackico_detail_blockTeamWithNotIn();

}
