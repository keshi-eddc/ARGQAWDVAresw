package com.edmi.entity.icorating;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 金融 模型
 */
@Entity
@Table(name = "ico_icorating_detail_block_funds")
public class ICO_icorating_detail_block_funds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;

    @Column(nullable = false, name = "link")
    private String link;

    @Column(nullable = false, name = "fund")
    private String fund;

    @Column(nullable = false, name = "fund_url")
    private String fund_url;

    @Column(nullable = false, name = "status")
    private String status;

    @Column(nullable = false, name = "aum")
    private String aum;

    @Column(nullable = false, name = "stratgey")
    private String stratgey;

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

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getFund_url() {
        return fund_url;
    }

    public void setFund_url(String fund_url) {
        this.fund_url = fund_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAum() {
        return aum;
    }

    public void setAum(String aum) {
        this.aum = aum;
    }

    public String getStratgey() {
        return stratgey;
    }

    public void setStratgey(String stratgey) {
        this.stratgey = stratgey;
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
