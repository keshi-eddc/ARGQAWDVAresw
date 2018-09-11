package com.edmi.entity.trackico;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.*;

/** 
* @ClassName: ICO_trackico_detail_blockTeam 
* @Description: 详情页的 公司人员 模型 
* @author keshi
* @date 2018年8月6日 上午10:50:21 
*  
*/
@Entity
@Table(name = "ico_trackico_detail_block_team")
public class ICO_trackico_detail_blockTeam {
	// 主键 自增
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pk_id;
	@Column(nullable = false, name = "member_name")
	private String member_name;
	@Column(nullable = false, name = "member_url")
	private String member_url;
	@Column(nullable = false, name = "member_position")
	private String member_position;
	@Column(nullable = false, name = "member_type")
	private String member_type;
	@Column(nullable = false, name = "member_photo_url")
	private String member_photo_url;
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

	@OneToMany(mappedBy = "ico_trackico_detail_blockTeam")
	private List<ICO_trackico_detail_block_team_sociallink> teamSociallinkList;
	
	

	public Long getPk_id() {
		return pk_id;
	}



	public void setPk_id(Long pk_id) {
		this.pk_id = pk_id;
	}



	public String getMember_name() {
		return member_name;
	}



	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}



	public String getMember_url() {
		return member_url;
	}



	public void setMember_url(String member_url) {
		this.member_url = member_url;
	}



	public String getMember_position() {
		return member_position;
	}



	public void setMember_position(String member_position) {
		this.member_position = member_position;
	}



	public String getMember_type() {
		return member_type;
	}



	public void setMember_type(String member_type) {
		this.member_type = member_type;
	}



	public String getMember_photo_url() {
		return member_photo_url;
	}



	public void setMember_photo_url(String member_photo_url) {
		this.member_photo_url = member_photo_url;
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

	public List<ICO_trackico_detail_block_team_sociallink> getTeamSociallinkList() {
		return teamSociallinkList;
	}

	public void setTeamSociallinkList(List<ICO_trackico_detail_block_team_sociallink> teamSociallinkList) {
		this.teamSociallinkList = teamSociallinkList;
	}

	@Override
	public String toString() {
		String str = "ICO_trackico_detail_blockTeam[" + "pk_id=" + pk_id + ",fk_id=" + ico_trackico_detail.getPk_id() + ",member_name=" + member_name + ",member_url=" + member_url + ",member_type="
				+ member_type + ",member_position=" + member_position + ",member_photo_url=" + member_photo_url + ",insert_time=" + insert_time + ",update_time=" + update_time + "]";
		return str;
	}

}
