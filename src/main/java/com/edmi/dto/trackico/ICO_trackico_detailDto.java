package com.edmi.dto.trackico;



public class ICO_trackico_detailDto {

	private String block_token;
	private String block_tag;
	private String block_status;

	private String block_name;
	private String block_description;
	private String logo_url;

	public String getBlock_name() {
		return block_name;
	}

	public void setBlock_name(String block_name) {
		this.block_name = block_name;
	}

	public String getBlock_tag() {
		return block_tag;
	}

	public void setBlock_tag(String block_tag) {
		this.block_tag = block_tag;
	}

	public String getBlock_description() {
		return block_description;
	}

	public void setBlock_description(String block_description) {
		this.block_description = block_description;
	}

	public String getLogo_url() {
		return logo_url;
	}

	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}

	public String getBlock_token() {
		return block_token;
	}

	public void setBlock_token(String block_token) {
		this.block_token = block_token;
	}

	public String getBlock_status() {
		return block_status;
	}

	public void setBlock_status(String block_status) {
		this.block_status = block_status;
	}
}
