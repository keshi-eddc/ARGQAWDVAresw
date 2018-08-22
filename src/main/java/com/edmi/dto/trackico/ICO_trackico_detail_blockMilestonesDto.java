package com.edmi.dto.trackico;

import javax.persistence.*;
import java.sql.Timestamp;

public class ICO_trackico_detail_blockMilestonesDto {

	private int milestones_index;
	private String milestones_date;
	private String content;

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
}
