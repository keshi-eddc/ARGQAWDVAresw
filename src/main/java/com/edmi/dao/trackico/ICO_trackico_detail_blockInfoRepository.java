package com.edmi.dao.trackico;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edmi.entity.trackico.ICO_trackico_detail_block_info;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/** 
* @ClassName: ICO_trackico_detail_blockInfoRepository 
* @Description: 负责 公司信息(blockInfo) 对象的数据库操作
* @author keshi
* @date 2018年8月9日 下午2:39:04 
*  
*/
public interface ICO_trackico_detail_blockInfoRepository extends JpaRepository<ICO_trackico_detail_block_info, Long> {
    //删除detail
    @Transactional
    @Modifying
    @Query("delete from ICO_trackico_detail_block_info  where fk_id = :fk_id")
    int deleteICO_trackico_detail_block_infoByPk_id(@Param("fk_id")long fk_id);
}
