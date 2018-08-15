package com.edmi.entity.trackico;

import java.sql.Timestamp;
import javax.persistence.*;

/** 
* @ClassName: ICO_trackico_detail_block_info 
* @Description: 公司信息 模型 
* @author keshi
* @date 2018年8月9日 下午2:29:47 
*  
*/
@Entity
@Table(name = "ico_trackico_detail_block_info")
public class ICO_trackico_detail_block_info {
	// 主键 自增
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pk_id;
	@Column(nullable = false, name = "pre_sale")
	private String pre_sale;
	@Column(nullable = false, name = "token_sale")
	private String token_sale;
	@Column(nullable = false, name = "country")
	private String country;
	@Column(nullable = false, name = "insert_time")
	private Timestamp insert_time;
	@Column(nullable = false, name = "update_time")
	private Timestamp update_time;

	/** 
	* @OneToOne detail表（ico_trackico_detail）和本表是一对一的关系
	* 本表的fk_id是外键，指向ico_trackico_detail的pk_id
	* 会自动关联
	*/
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_id")
	private ICO_trackico_detail ico_trackico_detail;

	public Long getPk_id() {
		return pk_id;
	}

	public void setPk_id(Long pk_id) {
		this.pk_id = pk_id;
	}

	public String getPre_sale() {
		return pre_sale;
	}

	public void setPre_sale(String pre_sale) {
		this.pre_sale = pre_sale;
	}

	public String getToken_sale() {
		return token_sale;
	}

	public void setToken_sale(String token_sale) {
		this.token_sale = token_sale;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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
		String str = "ICO_trackico_detail_block_info[" + "pk_id=" + pk_id + ",fk_id=" + ico_trackico_detail.getPk_id() + ",pre_sale=" + pre_sale + ",token_sale=" + token_sale + ",country=" + country + ",insert_time=" + insert_time
				+ ",update_time=" + update_time + "]";
		return str;
	}
}
