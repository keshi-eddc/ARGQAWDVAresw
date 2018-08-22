package com.edmi.entity.icorating;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * ICO_icorating_list 列表页模型
 */
@Entity
@Table(name = "ico_icorating_list")
public class ICO_icorating_list {
    // 主键 自增
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;
    @Column(nullable = false, name = "name")
    private String name;
    @Column(nullable = false, name = "ticker")
    private String ticker;
    @Column(nullable = false, name = "name_short")
    private String nameShort;
    @Column(nullable = false, name = "link")
    private String link;
    @Column(nullable = false, name = "logo")
    private String logo;
    @Column(nullable = false, name = "status")
    private String status;
    @Column(nullable = false, name = "hype_score_text")
    private String hypeScoreText;
    @Column(nullable = false, name = "risk_score_text")
    private String riskScoreText;
    @Column(nullable = false, name = "basic_review_link")
    private String basicReviewLink;
    @Column(nullable = false, name = "investment_rating_text")
    private String investmentRatingText;
    @Column(nullable = false, name = "investment_rating_link")
    private String investmentRatingLink;
    @Column(nullable = false, name = "post_ico_rating")
    private String postIcoRating;
    @Column(nullable = false, name = "raised")
    private float raised;
    @Column(nullable = false, name = "raised_percent")
    private float raisedPercent;
    @Column(nullable = false, name = "current_page")
    private int currentPage;
    @Column(nullable = false, name = "last_page")
    private int lastPage;
    @Column(nullable = false, name = "crawled_times")
    private int crawledTimes;
    @Column(nullable = false, name = "crawled_status")
    private String crawledStatu;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHypeScoreText() {
        return hypeScoreText;
    }

    public void setHypeScoreText(String hypeScoreText) {
        this.hypeScoreText = hypeScoreText;
    }

    public String getRiskScoreText() {
        return riskScoreText;
    }

    public void setRiskScoreText(String riskScoreText) {
        this.riskScoreText = riskScoreText;
    }

    public String getBasicReviewLink() {
        return basicReviewLink;
    }

    public void setBasicReviewLink(String basicReviewLink) {
        this.basicReviewLink = basicReviewLink;
    }

    public String getInvestmentRatingText() {
        return investmentRatingText;
    }

    public void setInvestmentRatingText(String investmentRatingText) {
        this.investmentRatingText = investmentRatingText;
    }

    public String getInvestmentRatingLink() {
        return investmentRatingLink;
    }

    public void setInvestmentRatingLink(String investmentRatingLink) {
        this.investmentRatingLink = investmentRatingLink;
    }

    public String getPostIcoRating() {
        return postIcoRating;
    }

    public void setPostIcoRating(String postIcoRating) {
        this.postIcoRating = postIcoRating;
    }

    public float getRaised() {
        return raised;
    }

    public void setRaised(float raised) {
        this.raised = raised;
    }

    public float getRaisedPercent() {
        return raisedPercent;
    }

    public void setRaisedPercent(float raisedPercent) {
        this.raisedPercent = raisedPercent;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getCrawledTimes() {
        return crawledTimes;
    }

    public void setCrawledTimes(int crawledTimes) {
        this.crawledTimes = crawledTimes;
    }

    public String getCrawledStatu() {
        return crawledStatu;
    }

    public void setCrawledStatu(String crawledStatu) {
        this.crawledStatu = crawledStatu;
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
