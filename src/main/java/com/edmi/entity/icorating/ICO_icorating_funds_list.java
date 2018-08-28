package com.edmi.entity.icorating;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * icorating founds 列表页模型
 */
@Entity
@Table(name = "ico_icorating_funds_list")
public class ICO_icorating_funds_list {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;

    @Column(nullable = false, name = "link")
    private String link;

    @Column(nullable = false, name = "logo")
    private String logo;

    @Column(nullable = false, name = "fund")
    private String fund;

    @Column(nullable = false, name = "status")
    private String status;

    @Column(nullable = false, name = "aum")
    private long aum;

    @Column(nullable = false, name = "stratgey")
    private String stratgey;

    @Column(nullable = false, name = "foundation")
    private int foundation;

    @Column(nullable = false, name = "avgIcoEthRoi")
    private float avgIcoEthRoi;

    @Column(nullable = false, name = "icorating_analytics")
    private String icorating_analyticsv;

    @Column(nullable = false, name = "page")
    private int page;

    @Column(nullable = false, name = "crawled_status")
    private String crawledStatus;

    @Column(nullable = false, name = "insert_Time")
    private Timestamp insertTime;

    @Column(nullable = false, name = "update_Time")
    private Timestamp updateTime;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getAum() {
        return aum;
    }

    public void setAum(long aum) {
        this.aum = aum;
    }

    public String getStratgey() {
        return stratgey;
    }

    public void setStratgey(String stratgey) {
        this.stratgey = stratgey;
    }

    public int getFoundation() {
        return foundation;
    }

    public void setFoundation(int foundation) {
        this.foundation = foundation;
    }

    public float getAvgIcoEthRoi() {
        return avgIcoEthRoi;
    }

    public void setAvgIcoEthRoi(float avgIcoEthRoi) {
        this.avgIcoEthRoi = avgIcoEthRoi;
    }

    public String getIcorating_analyticsv() {
        return icorating_analyticsv;
    }

    public void setIcorating_analyticsv(String icorating_analyticsv) {
        this.icorating_analyticsv = icorating_analyticsv;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getCrawledStatus() {
        return crawledStatus;
    }

    public void setCrawledStatus(String crawledStatus) {
        this.crawledStatus = crawledStatus;
    }

    public Timestamp getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Timestamp insertTime) {
        this.insertTime = insertTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
