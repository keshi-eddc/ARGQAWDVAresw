package com.edmi.entity.coinschedule;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * coinschedule 详情模型
 */
@Entity
@Table(name = "ico_coinschedule_detail")
public class ICO_coinschedule_detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;

    @Column(nullable = false, name = "link")
    private String link;

    @Column(nullable = false, name = "ico_name")
    private String ico_name;

    @Column(nullable = false, name = "ico_tag")
    private String ico_tag;

    @Column(nullable = false, name = "logo_url")
    private String logo_url;

    @Column(nullable = false, name = "ico_description")
    private String ico_description;

    @Column(nullable = false, name = "start_date")
    private String start_date;

    @Column(nullable = false, name = "end_date")
    private String end_date;

    @Column(nullable = false, name = "restricted_countries")
    private String restricted_countries;

    @Column(nullable = false, name = "tokens_for_sale")
    private String tokens_for_sale;

    @Column(nullable = false, name = "pre_start_date")
    private String pre_start_date;

    @Column(nullable = false, name = "pre_end_date")
    private String pre_end_date;

    @Column(nullable = false, name = "tags")
    private String tags;

    @Column(nullable = false, name = "website")
    private String website;

    @Column(nullable = false, name = "insert_Time")
    private Timestamp insert_Time;

    @Column(nullable = false, name = "update_Time")
    private Timestamp update_Time;


    /**
     * 本表和list表是一对一的关系
     * 本表的fk_id是外键，指向list的pk_id
     * 会自动关联
     */
    @OneToOne
    @JoinColumn(name = "fk_id")
    private Ico_coinschedule_List ico_coinschedule_list;

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public Timestamp getInsert_Time() {
        return insert_Time;
    }

    public void setInsert_Time(Timestamp insert_Time) {
        this.insert_Time = insert_Time;
    }

    public Timestamp getUpdate_Time() {
        return update_Time;
    }

    public void setUpdate_Time(Timestamp update_Time) {
        this.update_Time = update_Time;
    }

    public Ico_coinschedule_List getIco_coinschedule_list() {
        return ico_coinschedule_list;
    }

    public void setIco_coinschedule_list(Ico_coinschedule_List ico_coinschedule_list) {
        this.ico_coinschedule_list = ico_coinschedule_list;
    }
}
