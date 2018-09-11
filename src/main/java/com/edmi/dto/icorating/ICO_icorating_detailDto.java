package com.edmi.dto.icorating;

import com.edmi.entity.icorating.ICO_icorating_list;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 详情 模型
 */

public class ICO_icorating_detailDto {

	private String link;

	private String block_name;

	private String block_tag;

	private String block_overview;

	private String contacts_facebook;

	private String contacts_twitter;

	private String contacts_reddit_alien;

	private String contacts_medium;

	private String contacts_github;

	private String contacts_instagram;

	private String contacts_telegram_plane;

	private String contacts_youtube;

	private String contacts_website;

	private String trading_token;

	private String trading_price;

	private String trading_MVP;

	private String trading_registration;



	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

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

	public String getBlock_overview() {
		return block_overview;
	}

	public void setBlock_overview(String block_overview) {
		this.block_overview = block_overview;
	}

	public String getContacts_facebook() {
		return contacts_facebook;
	}

	public void setContacts_facebook(String contacts_facebook) {
		this.contacts_facebook = contacts_facebook;
	}

	public String getContacts_twitter() {
		return contacts_twitter;
	}

	public void setContacts_twitter(String contacts_twitter) {
		this.contacts_twitter = contacts_twitter;
	}

	public String getContacts_reddit_alien() {
		return contacts_reddit_alien;
	}

	public void setContacts_reddit_alien(String contacts_reddit_alien) {
		this.contacts_reddit_alien = contacts_reddit_alien;
	}

	public String getContacts_medium() {
		return contacts_medium;
	}

	public void setContacts_medium(String contacts_medium) {
		this.contacts_medium = contacts_medium;
	}

	public String getContacts_github() {
		return contacts_github;
	}

	public void setContacts_github(String contacts_github) {
		this.contacts_github = contacts_github;
	}

	public String getContacts_instagram() {
		return contacts_instagram;
	}

	public void setContacts_instagram(String contacts_instagram) {
		this.contacts_instagram = contacts_instagram;
	}

	public String getContacts_telegram_plane() {
		return contacts_telegram_plane;
	}

	public void setContacts_telegram_plane(String contacts_telegram_plane) {
		this.contacts_telegram_plane = contacts_telegram_plane;
	}

	public String getContacts_youtube() {
		return contacts_youtube;
	}

	public void setContacts_youtube(String contacts_youtube) {
		this.contacts_youtube = contacts_youtube;
	}

	public String getContacts_website() {
		return contacts_website;
	}

	public void setContacts_website(String contacts_website) {
		this.contacts_website = contacts_website;
	}

	public String getTrading_token() {
		return trading_token;
	}

	public void setTrading_token(String trading_token) {
		this.trading_token = trading_token;
	}

	public String getTrading_price() {
		return trading_price;
	}

	public void setTrading_price(String trading_price) {
		this.trading_price = trading_price;
	}

	public String getTrading_MVP() {
		return trading_MVP;
	}

	public void setTrading_MVP(String trading_MVP) {
		this.trading_MVP = trading_MVP;
	}

	public String getTrading_registration() {
		return trading_registration;
	}

	public void setTrading_registration(String trading_registration) {
		this.trading_registration = trading_registration;
	}
}
