package com.edmi.entity.icorating;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * ICO_icorating_detail_detail 模型
 */
@Entity
@Table(name = "ico_icorating_detail_detail")
public class ICO_icorating_detail_detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;

    @Column(nullable = false, name = "link")
    private String link;

    @Column(nullable = false, name = "detail_key")
    private String detail_key;

    @Column(nullable = false, name = "detail_value")
    private String detail_value;

    @Column(nullable = false, name = "detail_type")
    private String detail_type;


    @Column(nullable = false, name = "insert_Time")
    private Timestamp insert_Time;

    @Column(nullable = false, name = "update_Time")
    private Timestamp update_Time;

    /**
     * 本表和detail表是 多对一 的关系
     * 本表的fk_id是外键，指向detail的pk_id
     * 会自动关联
     */
    @ManyToOne
    @JoinColumn(name = "fk_id")
    private ICO_icorating_detail ico_icorating_detail;

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

    public String getDetail_key() {
        return detail_key;
    }

    public void setDetail_key(String detail_key) {
        this.detail_key = detail_key;
    }

    public String getDetail_value() {
        return detail_value;
    }

    public void setDetail_value(String detail_value) {
        this.detail_value = detail_value;
    }

    public String getDetail_type() {
        return detail_type;
    }

    public void setDetail_type(String detail_type) {
        this.detail_type = detail_type;
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
