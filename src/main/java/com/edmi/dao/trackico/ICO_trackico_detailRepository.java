package com.edmi.dao.trackico;

import com.edmi.entity.icocrunch.Ico_icocrunch_detail;
import com.edmi.entity.trackico.ICO_trackico_item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.edmi.entity.trackico.ICO_trackico_detail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** 
* @ClassName: ICO_trackico_detailRepository 
* @Description: 负责 ICO_trackico_detail模型的数据库操作
* @author keshi
* @date 2018年7月31日 下午4:36:38 
*  
*/
public interface ICO_trackico_detailRepository extends JpaRepository<ICO_trackico_detail, Long> {

    @Query("select it from ICO_trackico_detail it where it.ico_trackico_item.pk_id = :fk_id ")
    List<ICO_trackico_detail> getICO_trackico_detailsByFkid(@Param("fk_id") long fk_id);

    //删除detail
    @Transactional
    @Modifying
    @Query("delete from ICO_trackico_detail  where fk_id = :fk_id")
    int deleteICO_trackico_detailByPk_id(@Param("fk_id")long fk_id);

    @Query("select d from ICO_trackico_detail d  order by d.pk_id asc")
    Page<ICO_trackico_detail> getICO_trackico_detailPageable(Pageable pageable);
}
