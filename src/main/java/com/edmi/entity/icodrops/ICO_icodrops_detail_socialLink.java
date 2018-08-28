package com.edmi.entity.icodrops;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 社交链接模型
 */
@Entity
@Table(name = "ico_icodrops_detail_socialLink")
public class ICO_icodrops_detail_socialLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;

    @Column(nullable = false, name = "link")
    private String link;

    @Column(nullable = false, name = "social_link_key")
    private String social_link_key;

    @Column(nullable = false, name = "social_link_value")
    private String social_link_value;

    @Column(nullable = false, name = "insert_Time")
    private Timestamp insert_Time;

    @Column(nullable = false, name = "update_Time")
    private Timestamp update_Time;

    /**
     * 本表和detail表是多对一的关系
     * 本表的fk_id是外键，指向detail的pk_id
     * 会自动关联
     */
    @ManyToOne
    @JoinColumn(name = "fk_id")
    private ICO_icodrops_detail ico_icodrops_detail;

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

    public ICO_icodrops_detail getIco_icodrops_detail() {
        return ico_icodrops_detail;
    }

    public void setIco_icodrops_detail(ICO_icodrops_detail ico_icodrops_detail) {
        this.ico_icodrops_detail = ico_icodrops_detail;
    }
}
