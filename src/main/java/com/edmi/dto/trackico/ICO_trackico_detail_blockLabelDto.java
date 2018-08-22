package com.edmi.dto.trackico;

import javax.persistence.*;
import java.sql.Timestamp;

public class ICO_trackico_detail_blockLabelDto {

	private String block_lable_name;
	private String block_lable_url;

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
}
