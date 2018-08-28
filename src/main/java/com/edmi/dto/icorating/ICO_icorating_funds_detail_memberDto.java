package com.edmi.dto.icorating;

import com.edmi.entity.icorating.ICO_icorating_funds_detail;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * funds_detail_member模型
 */
public class ICO_icorating_funds_detail_memberDto {


    private String member_photo_url;
    private String member_name;
    private String member_url;
    private String experience;
    private String facebook;
    private String twitter;
    private String medium;
    private String linkedin;

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
}
