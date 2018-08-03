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
* @ClassName: ICO_trackico_detail_blockLabel 
* @Description 详情页的公司标签链接模型
* @author keshi
* @date 2018年8月3日 下午3:36:24 
*/
@Entity
@Table(name = "ico_trackico_detail_block_label")
public class ICO_trackico_detail_blockLabel {
	// 主键 自增
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pk_id;
	// nullable = false 该字段可以为空
	@Column(nullable = false, name = "block_lable_name")
	private String block_lable_name;
	@Column(nullable = false, name = "block_lable_url")
	private String block_lable_url;
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

	public String getBlock_lable_name() {
		return block_lable_name;
	}

	public void setBlock_lable_name(String block_lable_name) {
		this.block_lable_name = block_lable_name;
	}

	public String getBlock_lable_url() {
		return block_lable_url;
	}

	public void setBlock_lable_url(String block_lable_url) {
		this.block_lable_url = block_lable_url;
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
		String str = "ICO_trackico_detail_blockLabel[" + "pk_id:" + pk_id + ",fk_id:" + ico_trackico_detail.getPk_id() + ",block_lable_name:" + block_lable_name + ",block_lable_url:" + block_lable_url
				+ ",insert_time:" + insert_time + ",update_time:" + update_time + "]";
		return str;
	}

}
