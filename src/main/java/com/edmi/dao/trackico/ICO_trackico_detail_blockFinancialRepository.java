package com.edmi.dao.trackico;

import org.springframework.data.jpa.repository.JpaRepository;
import com.edmi.entity.trackico.ICO_trackico_detail_blockFinancial;

/** 
* @ClassName: ICO_trackico_detail_blockFinancialRepository 
* @Description: 负责公司金融 对象的数据库操作 
* @author keshi
* @date 2018年8月6日 下午3:50:55 
*  
*/
public interface ICO_trackico_detail_blockFinancialRepository extends JpaRepository<ICO_trackico_detail_blockFinancial, Long> {

}
