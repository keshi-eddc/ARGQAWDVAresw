package com.edmi.entity.coinschedule;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * coinschedule_detail社交链接
 */
@Entity
@Table(name = "ico_coinschedule_detail_sociallink")
public class ICO_coinschedule_detail_sociallink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;

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
     */
    @ManyToOne
    @JoinColumn(name = "fk_id")
    private ICO_coinschedule_detail ico_coinschedule_detail;

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
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

    public ICO_coinschedule_detail getIco_coinschedule_detail() {
        return ico_coinschedule_detail;
    }

    public void setIco_coinschedule_detail(ICO_coinschedule_detail ico_coinschedule_detail) {
        this.ico_coinschedule_detail = ico_coinschedule_detail;
    }
}
