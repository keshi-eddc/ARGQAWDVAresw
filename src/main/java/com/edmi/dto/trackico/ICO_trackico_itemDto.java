package com.edmi.dto.trackico;

import javax.persistence.*;
import java.sql.Timestamp;


public class ICO_trackico_itemDto {

	private String itemName;
	private String itemUrl;


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
}
