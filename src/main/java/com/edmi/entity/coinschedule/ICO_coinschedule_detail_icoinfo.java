package com.edmi.entity.coinschedule;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * coinschedule_detail_icoinfo 模型
 */
@Entity
@Table(name = "ico_coinschedule_detail_icoinfo")
public class ICO_coinschedule_detail_icoinfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;

    @Column(nullable = false, name = "ico_key")
    private String ico_key;

    @Column(nullable = false, name = "ico_value")
    private String ico_value;

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

    public String getIco_key() {
        return ico_key;
    }

    public void setIco_key(String ico_key) {
        this.ico_key = ico_key;
    }

    public String getIco_value() {
        return ico_value;
    }

    public void setIco_value(String ico_value) {
        this.ico_value = ico_value;
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
