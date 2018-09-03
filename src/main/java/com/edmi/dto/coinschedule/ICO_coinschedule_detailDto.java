package com.edmi.dto.coinschedule;

import com.edmi.entity.coinschedule.Ico_coinschedule_List;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * coinschedule 详情模型
 */
public class ICO_coinschedule_detailDto {


    private String link;
    private String ico_name;
    private String ico_tag;
    private String logo_url;
    private String ico_description;
    private String start_date;
    private String end_date;
    private String restricted_countries;
    private String tokens_for_sale;
    private String pre_start_date;
    private String pre_end_date;
    private String tags;
    private String website;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIco_name() {
        return ico_name;
    }

    public void setIco_name(String ico_name) {
        this.ico_name = ico_name;
    }

    public String getIco_tag() {
        return ico_tag;
    }

    public void setIco_tag(String ico_tag) {
        this.ico_tag = ico_tag;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getIco_description() {
        return ico_description;
    }

    public void setIco_description(String ico_description) {
        this.ico_description = ico_description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getRestricted_countries() {
        return restricted_countries;
    }

    public void setRestricted_countries(String restricted_countries) {
        this.restricted_countries = restricted_countries;
    }

    public String getTokens_for_sale() {
        return tokens_for_sale;
    }

    public void setTokens_for_sale(String tokens_for_sale) {
        this.tokens_for_sale = tokens_for_sale;
    }

    public String getPre_start_date() {
        return pre_start_date;
    }

    public void setPre_start_date(String pre_start_date) {
        this.pre_start_date = pre_start_date;
    }

    public String getPre_end_date() {
        return pre_end_date;
    }

    public void setPre_end_date(String pre_end_date) {
        this.pre_end_date = pre_end_date;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
