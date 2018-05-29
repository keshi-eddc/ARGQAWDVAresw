package com.edmi.entity.dlzb;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class DLZB_Project_List {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long pk_id;
    @Column(nullable = false)
    private String keyword;
    @Column(nullable = false)
    private String gc_title;
    @Column(nullable = false)
    private String gc_date;
    @Column(nullable = false)
    private String gc_link;
    @Column(nullable = false)
    private Timestamp insert_time;
    @Column(nullable = false)
    private Timestamp modify_time;
    @Column(nullable = false)
    private String status;

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getGc_title() {
        return gc_title;
    }

    public void setGc_title(String gc_title) {
        this.gc_title = gc_title;
    }

    public String getGc_date() {
        return gc_date;
    }

    public void setGc_date(String gc_date) {
        this.gc_date = gc_date;
    }

    public String getGc_link() {
        return gc_link;
    }

    public void setGc_link(String gc_link) {
        this.gc_link = gc_link;
    }

    public Timestamp getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(Timestamp insert_time) {
        this.insert_time = insert_time;
    }

    public Timestamp getModify_time() {
        return modify_time;
    }

    public void setModify_time(Timestamp modify_time) {
        this.modify_time = modify_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
