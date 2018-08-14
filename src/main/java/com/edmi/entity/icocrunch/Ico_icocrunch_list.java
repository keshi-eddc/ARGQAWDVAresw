package com.edmi.entity.icocrunch;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Ico_icocrunch_list {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pkId;
    @Column(nullable = false)
    private String blockUrl;
    @Column(nullable = false)
    private Timestamp insertTime;
    @Column(nullable = false)
    private Timestamp modifyTime;
    @Column(nullable = false)
    private String blockType;
    @Column(nullable = false)
    private String detailsStatus;
    @Column(nullable = false)
    private long serialNumber;
    @Column(nullable = false)
    private int currentPage;
    @Column(nullable = false)
    private int totalPage;


    public long getPkId() {
    return pkId;
    }

    public void setPkId(long pkId) {
    this.pkId = pkId;
    }


    public String getBlockUrl() {
    return blockUrl;
    }

    public void setBlockUrl(String blockUrl) {
    this.blockUrl = blockUrl;
    }


    public java.sql.Timestamp getInsertTime() {
    return insertTime;
    }

    public void setInsertTime(java.sql.Timestamp insertTime) {
    this.insertTime = insertTime;
    }


    public java.sql.Timestamp getModifyTime() {
    return modifyTime;
    }

    public void setModifyTime(java.sql.Timestamp modifyTime) {
    this.modifyTime = modifyTime;
    }


    public String getBlockType() {
    return blockType;
    }

    public void setBlockType(String blockType) {
    this.blockType = blockType;
    }


    public String getDetailsStatus() {
    return detailsStatus;
    }

    public void setDetailsStatus(String detailsStatus) {
    this.detailsStatus = detailsStatus;
    }


    public long getSerialNumber() {
    return serialNumber;
    }

    public void setSerialNumber(long serialNumber) {
    this.serialNumber = serialNumber;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
