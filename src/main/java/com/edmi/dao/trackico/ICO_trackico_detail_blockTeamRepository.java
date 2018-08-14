package com.edmi.dao.trackico;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edmi.entity.trackico.ICO_trackico_detail_blockTeam;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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
}
