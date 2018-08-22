package com.edmi.entity.icorating;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 公司发展 模型
 */
@Entity
@Table(name = "ico_icorating_detail_block_development")
public class ICO_icorating_detail_block_development {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;

    @Column(nullable = false, name = "link")
    private String link;

    @Column(nullable = false, name = "pre_launch")
    private String pre_launch;

    @Column(nullable = false, name = "launch")
    private String launch;

    @Column(nullable = false, name = "custom")
    private String custom;

    @Column(nullable = false, name = "testnet")
    private String testnet;

    @Column(nullable = false, name = "miannet")
    private String miannet;

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

    public String getPre_launch() {
        return pre_launch;
    }

    public void setPre_launch(String pre_launch) {
        this.pre_launch = pre_launch;
    }

    public String getLaunch() {
        return launch;
    }

    public void setLaunch(String launch) {
        this.launch = launch;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getTestnet() {
        return testnet;
    }

    public void setTestnet(String testnet) {
        this.testnet = testnet;
    }

    public String getMiannet() {
        return miannet;
    }

    public void setMiannet(String miannet) {
        this.miannet = miannet;
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
