package com.edmi.entity.trackico;

import java.sql.Timestamp;
import javax.persistence.*;

/** 
* @ClassName: ICO_trackico_detail_blockMilestones 
* @Description: 公司里程表 模型 
* @author keshi
* @date 2018年8月9日 上午11:27:18 
*  
*/
@Entity
@Table(name = "ico_trackico_detail_block_milestones")
public class ICO_trackico_detail_blockMilestones {
	// 主键 自增
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pk_id;
	@Column(nullable = false, name = "milestones_index")
	private int milestones_index;
	@Column(nullable = false, name = "milestones_date")
	private String milestones_date;
	@Column(nullable = false, name = "content")
	private String content;
	@Column(nullable = false, name = "insert_time")
	private Timestamp insert_time;
	@Column(nullable = false, name = "update_time")
	private Timestamp update_time;

	/** 
	* @OneToOne detail表（ico_trackico_detail）和本表是一对一的关系
	* 本表的fk_id是外键，指向ico_trackico_detail的pk_id
	* 会自动关联
	*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_id")
	private ICO_trackico_detail ico_trackico_detail;

	public Long getPk_id() {
		return pk_id;
	}

	public void setPk_id(Long pk_id) {
		this.pk_id = pk_id;
	}

	public int getMilestones_index() {
		return milestones_index;
	}

	public void setMilestones_index(int milestones_index) {
		this.milestones_index = milestones_index;
	}

	public String getMilestones_date() {
		return milestones_date;
	}

	public void setMilestones_date(String milestones_date) {
		this.milestones_date = milestones_date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
		String str = "ico_trackico_detail_block_milestones[" + "pk_id:" + pk_id + ",fk_id:" + ico_trackico_detail.getPk_id() + ",milestones_index:" + milestones_index + ",milestones_date:"
				+ milestones_date + ",content:" + content + ",insert_time:" + insert_time + ",update_time:" + update_time + "]";
		return str;
	}
}
