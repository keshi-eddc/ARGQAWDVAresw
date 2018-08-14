package com.edmi.entity.icocrunch;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Ico_icocrunch_detail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pkId;
  @Column(nullable = false)
  private String icoName;
  @Column(nullable = false)
  private String logo;
  @Column(nullable = false)
  private String icoWebsite;
  @Column(nullable = false)
  private String categories;
  @Column(nullable = false)
  private String shortDescription;
  @Column(nullable = false)
  private String whitelistDate;
  @Column(nullable = false)
  private String kycDate;
  @Column(nullable = false)
  private String preicoDate;
  @Column(nullable = false)
  private String icoDate;
  @Column(nullable = false)
  private String rised;
  @Column(nullable = false)
  private String hardCapUsd;
  @Column(nullable = false)
  private String priceUsd;
  @Column(nullable = false)
  private String priceEth;
  @Column(nullable = false)
  private String maxBonus;
  @Column(nullable = false)
  private String whitepaper;
  @Column(nullable = false)
  private String telegram;
  @Column(nullable = false)
  private String twitter;
  @Column(nullable = false)
  private String bitcointalk;
  @Column(nullable = false)
  private String facebook;
  @Column(nullable = false)
  private String gitHub;
  @Column(nullable = false)
  private String medium;
  @Column(nullable = false)
  private String tokenNameOrTicker;
  @Column(nullable = false)
  private String icoProjectDescription;
  @Column(nullable = false)
  private Timestamp insertTime;
  @Column(nullable = false)
  private Timestamp modifyTime;
  @Column(nullable = false)
  private String icoCrunchUrl;



  public Long getPkId() {
    return pkId;
  }

  public void setPkId(Long pkId) {
    this.pkId = pkId;
  }


  public String getIcoName() {
    return icoName;
  }

  public void setIcoName(String icoName) {
    this.icoName = icoName;
  }


  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }


  public String getIcoWebsite() {
    return icoWebsite;
  }

  public void setIcoWebsite(String icoWebsite) {
    this.icoWebsite = icoWebsite;
  }


  public String getCategories() {
    return categories;
  }

  public void setCategories(String categories) {
    this.categories = categories;
  }


  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getHardCapUsd() {
    return hardCapUsd;
  }

  public void setHardCapUsd(String hardCapUsd) {
    this.hardCapUsd = hardCapUsd;
  }

  public String getPriceUsd() {
    return priceUsd;
  }

  public void setPriceUsd(String priceUsd) {
    this.priceUsd = priceUsd;
  }

  public String getPriceEth() {
    return priceEth;
  }

  public void setPriceEth(String priceEth) {
    this.priceEth = priceEth;
  }

  public String getMaxBonus() {
    return maxBonus;
  }

  public void setMaxBonus(String maxBonus) {
    this.maxBonus = maxBonus;
  }

  public String getWhitepaper() {
    return whitepaper;
  }

  public void setWhitepaper(String whitepaper) {
    this.whitepaper = whitepaper;
  }


  public String getTelegram() {
    return telegram;
  }

  public void setTelegram(String telegram) {
    this.telegram = telegram;
  }


  public String getTwitter() {
    return twitter;
  }

  public void setTwitter(String twitter) {
    this.twitter = twitter;
  }


  public String getBitcointalk() {
    return bitcointalk;
  }

  public void setBitcointalk(String bitcointalk) {
    this.bitcointalk = bitcointalk;
  }


  public String getFacebook() {
    return facebook;
  }

  public void setFacebook(String facebook) {
    this.facebook = facebook;
  }


  public String getGitHub() {
    return gitHub;
  }

  public void setGitHub(String gitHub) {
    this.gitHub = gitHub;
  }


  public String getMedium() {
    return medium;
  }

  public void setMedium(String medium) {
    this.medium = medium;
  }


  public String getTokenNameOrTicker() {
    return tokenNameOrTicker;
  }

  public void setTokenNameOrTicker(String tokenNameOrTicker) {
    this.tokenNameOrTicker = tokenNameOrTicker;
  }

  public String getWhitelistDate() {
    return whitelistDate;
  }

  public void setWhitelistDate(String whitelistDate) {
    this.whitelistDate = whitelistDate;
  }

  public String getKycDate() {
    return kycDate;
  }

  public void setKycDate(String kycDate) {
    this.kycDate = kycDate;
  }

  public String getPreicoDate() {
    return preicoDate;
  }

  public void setPreicoDate(String preicoDate) {
    this.preicoDate = preicoDate;
  }

  public String getIcoDate() {
    return icoDate;
  }

  public void setIcoDate(String icoDate) {
    this.icoDate = icoDate;
  }

  public String getIcoProjectDescription() {
    return icoProjectDescription;
  }

  public void setIcoProjectDescription(String icoProjectDescription) {
    this.icoProjectDescription = icoProjectDescription;
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

  public String getRised() {
    return rised;
  }

  public void setRised(String rised) {
    this.rised = rised;
  }

  public String getIcoCrunchUrl() {
    return icoCrunchUrl;
  }

  public void setIcoCrunchUrl(String icoCrunchUrl) {
    this.icoCrunchUrl = icoCrunchUrl;
  }
}
