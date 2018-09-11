package com.edmi.dto.trackico;


import com.edmi.entity.trackico.ICO_trackico_detail_block_team_sociallink;

import java.util.List;

public class ICO_trackico_detail_blockTeamDto {

	private String member_name;
	private String member_url;
	private String member_position;
	private String member_type;
	private String member_photo_url;

	private List<ICO_trackico_detail_block_team_sociallink> teamSociallinkList;

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

	public List<ICO_trackico_detail_block_team_sociallink> getTeamSociallinkList() {
		return teamSociallinkList;
	}

	public void setTeamSociallinkList(List<ICO_trackico_detail_block_team_sociallink> teamSociallinkList) {
		this.teamSociallinkList = teamSociallinkList;
	}
}
