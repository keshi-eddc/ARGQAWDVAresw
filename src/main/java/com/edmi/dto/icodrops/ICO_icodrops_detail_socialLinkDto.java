package com.edmi.dto.icodrops;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 社交链接模型
 */
public class ICO_icodrops_detail_socialLinkDto {

    private String social_link_key;
    private String social_link_value;

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
