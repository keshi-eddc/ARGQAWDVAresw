package com.edmi.dao.trackico;

import com.edmi.entity.trackico.ICO_trackico_detail;
import org.springframework.data.jpa.repository.JpaRepository;

import com.edmi.entity.trackico.ICO_trackico_detail_block_info;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Query("select info from ICO_trackico_detail_block_info info where info.ico_trackico_detail.pk_id = :fk_id ")
    ICO_trackico_detail_block_info getICO_trackico_detail_block_infosByFkid(@Param("fk_id") long fk_id);
}
