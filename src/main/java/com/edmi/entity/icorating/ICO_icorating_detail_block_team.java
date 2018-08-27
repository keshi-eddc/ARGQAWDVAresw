package com.edmi.entity.icorating;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 公司人员 模型
 */
@Entity
@Table(name = "ico_icorating_detail_block_team")
public class ICO_icorating_detail_block_team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;

    @Column(nullable = false, name = "link")
    private String link;


    @Column(nullable = false, name = "member_name")
    private String member_name;

    @Column(nullable = false, name = "member_url")
    private String member_url;

    @Column(nullable = false, name = "member_position")
    private String member_position;

    @Column(nullable = false, name = "member_type")
    private String member_type;

    @Column(nullable = false, name = "member_score")
    private String member_score;

    @Column(nullable = false, name = "member_social_linkedin")
    private String member_social_linkedin;

    @Column(nullable = false, name = "member_social_facebook")
    private String member_social_facebook;

    @Column(nullable = false, name = "member_social_twitter")
    private String member_social_twitter;

    @Column(nullable = false, name = "insert_Time")
    private Timestamp insert_Time;

    @Column(nullable = false, name = "update_Time")
    private Timestamp update_Time;

    @Column(nullable = false, name = "member_photo_url")
    private String member_photo_url;


    /**
     * 本表和detail表是 多对一 的关系
     * 本表的fk_id是外键，指向detail的pk_id
     * 会自动关联
     */
    @ManyToOne
    @JoinColumn(name = "fk_id")
    private ICO_icorating_detail ico_icorating_detail;

    public String getMember_photo_url() {
        return member_photo_url;
    }

    public void setMember_photo_url(String member_photo_url) {
        this.member_photo_url = member_photo_url;
    }

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

    public String getMember_score() {
        return member_score;
    }

    public void setMember_score(String member_score) {
        this.member_score = member_score;
    }

    public String getMember_social_linkedin() {
        return member_social_linkedin;
    }

    public void setMember_social_linkedin(String member_social_linkedin) {
        this.member_social_linkedin = member_social_linkedin;
    }

    public String getMember_social_facebook() {
        return member_social_facebook;
    }

    public void setMember_social_facebook(String member_social_facebook) {
        this.member_social_facebook = member_social_facebook;
    }

    public String getMember_social_twitter() {
        return member_social_twitter;
    }

    public void setMember_social_twitter(String member_social_twitter) {
        this.member_social_twitter = member_social_twitter;
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

    public ICO_icorating_detail getIco_icorating_detail() {
        return ico_icorating_detail;
    }

    public void setIco_icorating_detail(ICO_icorating_detail ico_icorating_detail) {
        this.ico_icorating_detail = ico_icorating_detail;
    }
}
