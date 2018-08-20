package com.edmi.entity.icorating;

import com.edmi.entity.trackico.ICO_trackico_item;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 详情 模型
 */
@Entity
public class ICO_icorating_detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;

    @Column(nullable = false, name = "link")
    private String link;

    @Column(nullable = false, name = "block_name")
    private String block_name;

    @Column(nullable = false, name = "block_tag")
    private String block_tag;

    @Column(nullable = false, name = "block_overview")
    private String block_overview;

    @Column(nullable = false, name = "contacts_facebook")
    private String contacts_facebook;

    @Column(nullable = false, name = "contacts_twitter")
    private String contacts_twitter;

    @Column(nullable = false, name = "contacts_reddit_alien")
    private String contacts_reddit_alien;

    @Column(nullable = false, name = "contacts_medium")
    private String contacts_medium;

    @Column(nullable = false, name = "contacts_github")
    private String contacts_github;

    @Column(nullable = false, name = "contacts_instagram")
    private String contacts_instagram;

    @Column(nullable = false, name = "contacts_telegram_plane")
    private String contacts_telegram_plane;

    @Column(nullable = false, name = "contacts_youtube")
    private String contacts_youtube;

    @Column(nullable = false, name = "contacts_website")
    private String contacts_website;

    @Column(nullable = false, name = "trading_start_ico")
    private String trading_start_ico;

    @Column(nullable = false, name = "trading_end_ico")
    private String trading_end_ico;

    @Column(nullable = false, name = "trading_token")
    private String trading_token;

    @Column(nullable = false, name = "trading_price")
    private String trading_price;

    @Column(nullable = false, name = "trading_MVP")
    private String trading_MVP;

    @Column(nullable = false, name = "trading_registration")
    private String trading_registration;

    @Column(nullable = false, name = "trading_whitepaper")
    private String trading_whitepaper;

    @Column(nullable = false, name = "trading_basicReview")
    private String trading_basicReview;

    @Column(nullable = false, name = "detail_tokenSale_icoStartDate")
    private String detail_tokenSale_icoStartDate;

    @Column(nullable = false, name = "detail_tokenSale_icoEndDate")
    private String detail_tokenSale_icoEndDate;

    @Column(nullable = false, name = "detail_tokenSale_raised")
    private String detail_tokenSale_raised;

    @Column(nullable = false, name = "detail_legal_IcoPlatform")
    private String detail_legal_IcoPlatform;

    @Column(nullable = false, name = "detail_legal_registrationCountry")
    private String detail_legal_registrationCountry;

    @Column(nullable = false, name = "detail_tokenDetails_ticker")
    private String detail_tokenDetails_ticker;

    @Column(nullable = false, name = "detail_tokenDetails_AdditionalTokenEmission")
    private String detail_tokenDetails_AdditionalTokenEmissionv;

    @Column(nullable = false, name = "detail_tokenDetails_AcceptedCurrencies")
    private String detail_tokenDetails_AcceptedCurrencies;

    @Column(nullable = false, name = "detail_tokenDetails_TokenDistribution")
    private String detail_tokenDetails_TokenDistribution;

    @Column(nullable = false, name = "insert_Time")
    private Timestamp insert_Time;

    @Column(nullable = false, name = "update_Time")
    private Timestamp update_Time;

    /**
     * @OneToOne list表和本表是一对一的关系
     * 本表的fk_id是外键，指向list的pk_id
     * 会自动关联
     */
    @OneToOne
    @JoinColumn(name = "fk_id")
    private ICO_icorating_list ico_icorating_list;

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

    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public String getBlock_tag() {
        return block_tag;
    }

    public void setBlock_tag(String block_tag) {
        this.block_tag = block_tag;
    }

    public String getBlock_overview() {
        return block_overview;
    }

    public void setBlock_overview(String block_overview) {
        this.block_overview = block_overview;
    }

    public String getContacts_facebook() {
        return contacts_facebook;
    }

    public void setContacts_facebook(String contacts_facebook) {
        this.contacts_facebook = contacts_facebook;
    }

    public String getContacts_twitter() {
        return contacts_twitter;
    }

    public void setContacts_twitter(String contacts_twitter) {
        this.contacts_twitter = contacts_twitter;
    }

    public String getContacts_reddit_alien() {
        return contacts_reddit_alien;
    }

    public void setContacts_reddit_alien(String contacts_reddit_alien) {
        this.contacts_reddit_alien = contacts_reddit_alien;
    }

    public String getContacts_medium() {
        return contacts_medium;
    }

    public void setContacts_medium(String contacts_medium) {
        this.contacts_medium = contacts_medium;
    }

    public String getContacts_github() {
        return contacts_github;
    }

    public void setContacts_github(String contacts_github) {
        this.contacts_github = contacts_github;
    }

    public String getContacts_instagram() {
        return contacts_instagram;
    }

    public void setContacts_instagram(String contacts_instagram) {
        this.contacts_instagram = contacts_instagram;
    }

    public String getContacts_telegram_plane() {
        return contacts_telegram_plane;
    }

    public void setContacts_telegram_plane(String contacts_telegram_plane) {
        this.contacts_telegram_plane = contacts_telegram_plane;
    }

    public String getContacts_youtube() {
        return contacts_youtube;
    }

    public void setContacts_youtube(String contacts_youtube) {
        this.contacts_youtube = contacts_youtube;
    }

    public String getContacts_website() {
        return contacts_website;
    }

    public void setContacts_website(String contacts_website) {
        this.contacts_website = contacts_website;
    }

    public String getTrading_start_ico() {
        return trading_start_ico;
    }

    public void setTrading_start_ico(String trading_start_ico) {
        this.trading_start_ico = trading_start_ico;
    }

    public String getTrading_end_ico() {
        return trading_end_ico;
    }

    public void setTrading_end_ico(String trading_end_ico) {
        this.trading_end_ico = trading_end_ico;
    }

    public String getTrading_token() {
        return trading_token;
    }

    public void setTrading_token(String trading_token) {
        this.trading_token = trading_token;
    }

    public String getTrading_price() {
        return trading_price;
    }

    public void setTrading_price(String trading_price) {
        this.trading_price = trading_price;
    }

    public String getTrading_MVP() {
        return trading_MVP;
    }

    public void setTrading_MVP(String trading_MVP) {
        this.trading_MVP = trading_MVP;
    }

    public String getTrading_registration() {
        return trading_registration;
    }

    public void setTrading_registration(String trading_registration) {
        this.trading_registration = trading_registration;
    }

    public String getTrading_whitepaper() {
        return trading_whitepaper;
    }

    public void setTrading_whitepaper(String trading_whitepaper) {
        this.trading_whitepaper = trading_whitepaper;
    }

    public String getTrading_basicReview() {
        return trading_basicReview;
    }

    public void setTrading_basicReview(String trading_basicReview) {
        this.trading_basicReview = trading_basicReview;
    }

    public String getDetail_tokenSale_icoStartDate() {
        return detail_tokenSale_icoStartDate;
    }

    public void setDetail_tokenSale_icoStartDate(String detail_tokenSale_icoStartDate) {
        this.detail_tokenSale_icoStartDate = detail_tokenSale_icoStartDate;
    }

    public String getDetail_tokenSale_icoEndDate() {
        return detail_tokenSale_icoEndDate;
    }

    public void setDetail_tokenSale_icoEndDate(String detail_tokenSale_icoEndDate) {
        this.detail_tokenSale_icoEndDate = detail_tokenSale_icoEndDate;
    }

    public String getDetail_tokenSale_raised() {
        return detail_tokenSale_raised;
    }

    public void setDetail_tokenSale_raised(String detail_tokenSale_raised) {
        this.detail_tokenSale_raised = detail_tokenSale_raised;
    }

    public String getDetail_legal_IcoPlatform() {
        return detail_legal_IcoPlatform;
    }

    public void setDetail_legal_IcoPlatform(String detail_legal_IcoPlatform) {
        this.detail_legal_IcoPlatform = detail_legal_IcoPlatform;
    }

    public String getDetail_legal_registrationCountry() {
        return detail_legal_registrationCountry;
    }

    public void setDetail_legal_registrationCountry(String detail_legal_registrationCountry) {
        this.detail_legal_registrationCountry = detail_legal_registrationCountry;
    }

    public String getDetail_tokenDetails_ticker() {
        return detail_tokenDetails_ticker;
    }

    public void setDetail_tokenDetails_ticker(String detail_tokenDetails_ticker) {
        this.detail_tokenDetails_ticker = detail_tokenDetails_ticker;
    }

    public String getDetail_tokenDetails_AdditionalTokenEmissionv() {
        return detail_tokenDetails_AdditionalTokenEmissionv;
    }

    public void setDetail_tokenDetails_AdditionalTokenEmissionv(String detail_tokenDetails_AdditionalTokenEmissionv) {
        this.detail_tokenDetails_AdditionalTokenEmissionv = detail_tokenDetails_AdditionalTokenEmissionv;
    }

    public String getDetail_tokenDetails_AcceptedCurrencies() {
        return detail_tokenDetails_AcceptedCurrencies;
    }

    public void setDetail_tokenDetails_AcceptedCurrencies(String detail_tokenDetails_AcceptedCurrencies) {
        this.detail_tokenDetails_AcceptedCurrencies = detail_tokenDetails_AcceptedCurrencies;
    }

    public String getDetail_tokenDetails_TokenDistribution() {
        return detail_tokenDetails_TokenDistribution;
    }

    public void setDetail_tokenDetails_TokenDistribution(String detail_tokenDetails_TokenDistribution) {
        this.detail_tokenDetails_TokenDistribution = detail_tokenDetails_TokenDistribution;
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

    public ICO_icorating_list getIco_icorating_list() {
        return ico_icorating_list;
    }

    public void setIco_icorating_list(ICO_icorating_list ico_icorating_list) {
        this.ico_icorating_list = ico_icorating_list;
    }
}
