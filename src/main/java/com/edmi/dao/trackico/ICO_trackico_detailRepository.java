package com.edmi.dao.trackico;

import org.springframework.data.jpa.repository.JpaRepository;
import com.edmi.entity.trackico.ICO_trackico_detail;

/** 
* @ClassName: ICO_trackico_detailRepository 
* @Description: 负责 ICO_trackico_detail模型的数据库操作
* @author keshi
* @date 2018年7月31日 下午4:36:38 
*  
*/
public interface ICO_trackico_detailRepository extends JpaRepository<ICO_trackico_detail, Long> {

}
