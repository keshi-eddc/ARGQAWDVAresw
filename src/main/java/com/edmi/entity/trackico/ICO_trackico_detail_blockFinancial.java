package com.edmi.entity.trackico;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/** 
* @ClassName: ICO_trackico_detail_block_financial 
* @Description: 公司金融 模型
* @author keshi
* @date 2018年8月6日 下午3:35:14 
*  
*/
@Entity
@Table(name = "ico_trackico_detail_block_financial")
public class ICO_trackico_detail_blockFinancial {
	// 主键 自增
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pk_id;
	@Column(nullable = false, name = "name")
	private String name;
	@Column(nullable = false, name = "value")
	private String value;
	@Column(nullable = false, name = "type")
	private String type;
	@Column(nullable = false, name = "insert_time")
	private Timestamp insert_time;
	@Column(nullable = false, name = "update_time")
	private Timestamp update_time;

	/** 
	* @OneToOne detail表（ico_trackico_detail）和本表是一对一的关系
	* 本表的fk_id是外键，指向ico_trackico_detail的pk_id
	* 会自动关联
	*/
	@OneToOne
	@JoinColumn(name = "fk_id")
	private ICO_trackico_detail ico_trackico_detail;

	
	public Long getPk_id() {
		return pk_id;
	}


	public void setPk_id(Long pk_id) {
		this.pk_id = pk_id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Timestamp getInsert_time() {
		return insert_time;
	}


	public void setInsert_time(Timestamp insert_time) {
		this.insert_time = insert_time;
	}


	public Timestamp getUpdate_time() {
		return update_time;
	}


	public void setUpdate_time(Timestamp update_time) {
		this.update_time = update_time;
	}


	public ICO_trackico_detail getIco_trackico_detail() {
		return ico_trackico_detail;
	}


	public void setIco_trackico_detail(ICO_trackico_detail ico_trackico_detail) {
		this.ico_trackico_detail = ico_trackico_detail;
	}


	@Override
	public String toString() {
		String str = "[" + "pk_id:" + pk_id + ",fk_id:" + ico_trackico_detail.getPk_id() + ",name:" + name + ",value:" + value + ",type:" + type + ",insert_time:" + insert_time + ",update_time:"
				+ update_time + "]";
		return str;
	}

}
