package com.edmi.entity.icodrops;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * icodrops detail 模型
 */
@Entity
@Table(name = "ico_icodrops_detail")
public class ICO_icodrops_detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;

    @Column(nullable = false, name = "link")
    private String link;

    @Column(nullable = false, name = "ico_name")
    private String ico_name;

    @Column(nullable = false, name = "ico_description")
    private String ico_description;

    @Column(nullable = false, name = "insert_Time")
    private Timestamp insert_Time;

    @Column(nullable = false, name = "update_Time")
    private Timestamp update_Time;

    /**
     * list表和本表是多对一的关系
     * 本表的fk_id是外键，指向list的pk_id
     * 会自动关联
     */
    @OneToOne
    @JoinColumn(name = "fk_id")
    private ICO_icodrops_list ico_icodrops_list;

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

    public String getIco_name() {
        return ico_name;
    }

    public void setIco_name(String ico_name) {
        this.ico_name = ico_name;
    }

    public String getIco_description() {
        return ico_description;
    }

    public void setIco_description(String ico_description) {
        this.ico_description = ico_description;
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

    public ICO_icodrops_list getIco_icodrops_list() {
        return ico_icodrops_list;
    }

    public void setIco_icodrops_list(ICO_icodrops_list ico_icodrops_list) {
        this.ico_icodrops_list = ico_icodrops_list;
    }
}
