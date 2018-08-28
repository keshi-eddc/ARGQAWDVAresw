package com.edmi.dto.icorating;

import com.edmi.entity.icorating.ICO_icorating_detail;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 公司人员 模型
 */
public class ICO_icorating_detail_block_teamDto {



    private String member_name;
    private String member_url;
    private String member_position;
    private String member_type;
    private String member_score;
    private String member_social_linkedin;
    private String member_social_facebook;
    private String member_social_twitter;
    private String member_photo_url;


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

    public String getMember_photo_url() {
        return member_photo_url;
    }

    public void setMember_photo_url(String member_photo_url) {
        this.member_photo_url = member_photo_url;
    }
}
