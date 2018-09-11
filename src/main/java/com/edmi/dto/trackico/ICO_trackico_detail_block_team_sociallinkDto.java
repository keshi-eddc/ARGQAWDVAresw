package com.edmi.dto.trackico;

/**
 * trackico 人员社交链接
 */
public class ICO_trackico_detail_block_team_sociallinkDto {

    private String memberUrl;
    private String social_link_key;
    private String social_link_value;

    public String getMemberUrl() {
        return memberUrl;
    }

    public void setMemberUrl(String memberUrl) {
        this.memberUrl = memberUrl;
    }

    public String getSocial_link_key() {
        return social_link_key;
    }

    public void setSocial_link_key(String social_link_key) {
        this.social_link_key = social_link_key;
    }

    public String getSocial_link_value() {
        return social_link_value;
    }

    public void setSocial_link_value(String social_link_value) {
        this.social_link_value = social_link_value;
    }
}
