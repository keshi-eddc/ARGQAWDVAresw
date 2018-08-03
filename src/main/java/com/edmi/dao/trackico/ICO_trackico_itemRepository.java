package com.edmi.dao.trackico;

import java.util.List;
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

	/** 
	* @Title: getICO_trackico_itemList 
	* @Description: 从数据库查询列表页，返回所有的列表页item对象
	* @param  startTime 本次抓取-列表页-开始的时间
	* 如：2018-07-30 15:21:38
	* 只抓取增量列表的item 的详情
	*/
	// e where e.InsertTime >=:startTime
	@Query("select it from ICO_trackico_item it where it.insertTime >= :startTime ")
	List<ICO_trackico_item> getICO_trackico_itemList(@Param("startTime") java.util.Date startTime);

	/** 
	* @Title: getICO_trackico_item_section_List 
	* @Description: 从数据库分批查，获得分批的list页的items 
	* 根据ico_trackico_list的status来判断是否抓过详情
	* status为空没有抓过详情，则抓取详情
	* status有状态，则不抓了
	*/
	@Query("select it from ICO_trackico_item it where it.status is null ")
	List<ICO_trackico_item> getICO_trackico_item_section_List();

	/** 
	* @Title: findTop10ByStatus 
	* @Description: 从数据库分批查，获得Status的Top100
	*/
	List<ICO_trackico_item> findTop10ByStatus(String status);

	/** 
	* @Title: findOneByItemUrl 
	* @Description:  传入itemUrl 获得item对象
	*/
	List<ICO_trackico_item> findOneByItemUrl(String itemUrl);

}
