package com.edmi.entity.coinschedule;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Ico_coinschedule_List {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long pkId;
  @Column(nullable = false)
  private String icoName;
  @Column(nullable = false)
  private String icoCoinscheduleUrl;
  @Column(nullable = false)
  private String category;
  @Column(nullable = false)
  private String endDate;
  @Column(nullable = false)
  private String endsIn;
  @Column(nullable = false)
  private String startDate;
  @Column(nullable = false)
  private String startsIn;
  @Column(nullable = false)
  private String trust;
  @Column(nullable = false)
  private String blockType;
  @Column(nullable = false)
  private Timestamp insertTime;
  @Column(nullable = false)
  private Timestamp modifyTime;
  @Column(nullable = false)
  private String blockLogo;


  public long getPkId() {
    return pkId;
  }

  public void setPkId(long pkId) {
    this.pkId = pkId;
  }


  public String getIcoName() {
    return icoName;
  }

  public void setIcoName(String icoName) {
    this.icoName = icoName;
  }


  public String getIcoCoinscheduleUrl() {
    return icoCoinscheduleUrl;
  }

  public void setIcoCoinscheduleUrl(String icoCoinscheduleUrl) {
    this.icoCoinscheduleUrl = icoCoinscheduleUrl;
  }


  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }


  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }


  public String getEndsIn() {
    return endsIn;
  }

  public void setEndsIn(String endsIn) {
    this.endsIn = endsIn;
  }


  public String getTrust() {
    return trust;
  }

  public void setTrust(String trust) {
    this.trust = trust;
  }


  public String getBlockType() {
    return blockType;
  }

  public void setBlockType(String blockType) {
    this.blockType = blockType;
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

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getStartsIn() {
    return startsIn;
  }

  public void setStartsIn(String startsIn) {
    this.startsIn = startsIn;
  }

  public String getBlockLogo() {
    return blockLogo;
  }

  public void setBlockLogo(String blockLogo) {
    this.blockLogo = blockLogo;
  }
}
