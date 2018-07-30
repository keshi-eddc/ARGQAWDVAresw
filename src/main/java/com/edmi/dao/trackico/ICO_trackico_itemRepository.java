package com.edmi.dao.trackico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.edmi.entity.trackico.ICO_trackico_item;

/** 
* @ClassName: ICO_trackico_itemRepository 
* @Description: 数据库操作 
* @author keshi
* @date 2018年7月30日 下午3:55:03 
*  
*/
public interface ICO_trackico_itemRepository extends JpaRepository<ICO_trackico_item, Long> {

	/** 
	* @Title: getICO_trackico_itemByItemUrl 
	* @Description: 通过itemUrl从数据库查询并返回对象
	*/
	@Query("select it from ICO_trackico_item it where it.itemUrl = :itemUrl ")
	ICO_trackico_item getICO_trackico_itemByItemUrl(@Param("itemUrl") String itemUrl);

}
