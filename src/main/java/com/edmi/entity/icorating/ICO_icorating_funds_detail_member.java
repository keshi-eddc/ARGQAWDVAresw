package com.edmi.entity.icorating;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * funds_detail_member模型
 */
@Entity
@Table(name = "ico_icorating_funds_detail_member")
public class ICO_icorating_funds_detail_member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;

    @Column(nullable = false, name = "link")
    private String link;

    @Column(nullable = false, name = "member_name")
    private String member_name;

    @Column(nullable = false, name = "member_url")
    private String member_url;

    @Column(nullable = false, name = "member_photo_url")
    private String member_photo_url;

    @Column(nullable = false, name = "experience")
    private String experience;

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
     * @OneToOne detail表和本表是多对一的关系
     * 本表的fk_id是外键，指向detail的pk_id
     * 会自动关联
     */
    @ManyToOne
    @JoinColumn(name = "fk_id")
    private ICO_icorating_funds_detail ico_icorating_funds_detail;

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

    public String getMember_photo_url() {
        return member_photo_url;
    }

    public void setMember_photo_url(String member_photo_url) {
        this.member_photo_url = member_photo_url;
    }

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

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
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

    public ICO_icorating_funds_detail getIco_icorating_funds_detail() {
        return ico_icorating_funds_detail;
    }

    public void setIco_icorating_funds_detail(ICO_icorating_funds_detail ico_icorating_funds_detail) {
        this.ico_icorating_funds_detail = ico_icorating_funds_detail;
    }
}
