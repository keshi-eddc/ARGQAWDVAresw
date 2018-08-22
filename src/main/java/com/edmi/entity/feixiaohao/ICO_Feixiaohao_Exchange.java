package com.edmi.entity.feixiaohao;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class ICO_Feixiaohao_Exchange {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long pk_id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String name_link;
    @Column(nullable = false)
    private int star;
    @Column(nullable = false)
    private String des;
    @Column(nullable = false)
    private int counter_party;
    @Column(nullable = false)
    private String state;
    @Column(nullable = false)
    private String transaction_amount_cny;
    @Column(nullable = false)
    private String transaction_amount_btc;
    @Column(nullable = false)
    private String transaction_amount_usd;
    @Column(nullable = false)
    private Timestamp insert_time;
    @Column(nullable = false)
    private Timestamp modify_time;
    @Column(nullable = false)
    private Long serial_number;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private String website;
    @Column(nullable = false)
    private String state_code;
    @Column(nullable = false)
    private String founding_time;
    @Column(nullable = false)
    private String wechat;
    @Column(nullable = false)
    private String telegram;
    @Column(nullable = false)
    private String twitter;
    @Column(nullable = false)
    private String reddit;
    @Column(nullable = false)
    private String medium;
    @Column(nullable = false)
    private String facebook;
    @Column(nullable = false)
    private String blog;


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

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getCounter_party() {
        return counter_party;
    }

    public void setCounter_party(int counter_party) {
        this.counter_party = counter_party;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getName_link() {
        return name_link;
    }

    public void setName_link(String name_link) {
        this.name_link = name_link;
    }

    public Long getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(Long serial_number) {
        this.serial_number = serial_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getFounding_time() {
        return founding_time;
    }

    public void setFounding_time(String founding_time) {
        this.founding_time = founding_time;
    }

    public String getTransaction_amount_cny() {
        return transaction_amount_cny;
    }

    public void setTransaction_amount_cny(String transaction_amount_cny) {
        this.transaction_amount_cny = transaction_amount_cny;
    }

    public String getTransaction_amount_btc() {
        return transaction_amount_btc;
    }

    public void setTransaction_amount_btc(String transaction_amount_btc) {
        this.transaction_amount_btc = transaction_amount_btc;
    }

    public String getTransaction_amount_usd() {
        return transaction_amount_usd;
    }

    public void setTransaction_amount_usd(String transaction_amount_usd) {
        this.transaction_amount_usd = transaction_amount_usd;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
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

    public String getReddit() {
        return reddit;
    }

    public void setReddit(String reddit) {
        this.reddit = reddit;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }
}
