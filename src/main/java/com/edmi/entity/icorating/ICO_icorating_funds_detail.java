package com.edmi.entity.icorating;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * icorating founds detail 模型
 */
@Entity
@Table(name = "ico_icorating_funds_detail")
public class ICO_icorating_funds_detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;

    @Column(nullable = false, name = "link")
    private String link;

    @Column(nullable = false, name = "fund")
    private String fund;

    @Column(nullable = false, name = "about")
    private String about;

    @Column(nullable = false, name = "funds_return")
    private String funds_return;

    @Column(nullable = false, name = "date_of_foundation")
    private String date_of_foundation;

    @Column(nullable = false, name = "strategy")
    private String strategy;

    @Column(nullable = false, name = "target")
    private String target;

    @Column(nullable = false, name = "aum")
    private String aum;

    @Column(nullable = false, name = "avgIcoEthRoi")
    private String avgIcoEthRoi;

    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "site")
    private String site;

    @Column(nullable = false, name = "based")
    private String based;

    @Column(nullable = false, name = "facebook")
    private String facebook;

    @Column(nullable = false, name = "twitter")
    private String twitter;

    @Column(nullable = false, name = "medium")
    private String medium;

    @Column(nullable = false, name = "linkedin")
    private String linkedin;

    @Column(nullable = false, name = "insert_Time")
    private Timestamp insert_Time;

    @Column(nullable = false, name = "update_Time")
    private Timestamp update_Time;

    /**
     * @OneToOne list表和本表是一对一的关系
     * 本表的fk_id是外键，指向list的pk_id
     * 会自动关联
     */
    @OneToOne
    @JoinColumn(name = "fk_id")
    private ICO_icorating_funds_list ico_icorating_funds_list;

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getFunds_return() {
        return funds_return;
    }

    public void setFunds_return(String funds_return) {
        this.funds_return = funds_return;
    }

    public String getDate_of_foundation() {
        return date_of_foundation;
    }

    public void setDate_of_foundation(String date_of_foundation) {
        this.date_of_foundation = date_of_foundation;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getAum() {
        return aum;
    }

    public void setAum(String aum) {
        this.aum = aum;
    }

    public String getAvgIcoEthRoi() {
        return avgIcoEthRoi;
    }

    public void setAvgIcoEthRoi(String avgIcoEthRoi) {
        this.avgIcoEthRoi = avgIcoEthRoi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getBased() {
        return based;
    }

    public void setBased(String based) {
        this.based = based;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
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

    public ICO_icorating_funds_list getIco_icorating_funds_list() {
        return ico_icorating_funds_list;
    }

    public void setIco_icorating_funds_list(ICO_icorating_funds_list ico_icorating_funds_list) {
        this.ico_icorating_funds_list = ico_icorating_funds_list;
    }
}
