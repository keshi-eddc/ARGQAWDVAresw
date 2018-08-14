package com.edmi.entity.trackico;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
* @ClassName: ICO_trackico_item 
* @Description: 列表页模型 
* @author keshi
* @date 2018年7月30日 下午3:53:20 
*  
*/
// @Table(name = "数据库的表名")
@Entity
@Table(name = "ico_trackico_list")
public class ICO_trackico_item {
	// 主键 自增
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pk_id;
	// nullable = false 该字段可以为空
	@Column(nullable = false, name = "itemName")
	private String itemName;
	@Column(nullable = false, name = "itemUrl")
	private String itemUrl;
	@Column(nullable = false, name = "insertTime")
	private Timestamp insertTime;
	@Column(nullable = false, name = "updateTime")
	private Timestamp updateTime;
	@Column(nullable = false, name = "pagenum")
	private int pagenum;
	@Column(nullable = false, name = "status")
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getPagenum() {
		return pagenum;
	}

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	public Long getPk_id() {
		return pk_id;
	}

	public void setPk_id(Long pk_id) {
		this.pk_id = pk_id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemUrl() {
		return itemUrl;
	}

	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}

	public Timestamp getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Timestamp insertTime) {
		this.insertTime = insertTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		String str = "ICO_trackico_item[" + "pk_id=" + pk_id + ",pagenum=" + pagenum + ",itemName=" + itemName
				+ ",status=" + status + ",itemUrl=" + itemUrl + ",insertTime=" + insertTime + ",updateTime="
				+ updateTime + "]";
		return str;
	}
}
