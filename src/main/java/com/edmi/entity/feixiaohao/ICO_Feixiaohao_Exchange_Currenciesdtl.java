package com.edmi.entity.feixiaohao;

import com.edmi.entity.github.ICO_Github_Repositories;

import javax.persistence.*;
import java.sql.Timestamp;
@Entity
public class ICO_Feixiaohao_Exchange_Currenciesdtl {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long pk_id;
    @Column(nullable = false)
    private String en;
    @Column(nullable = false)
    private String cn;
    @Column(nullable = false)
    private String exchange_number;
    @Column(nullable = false)
    private Timestamp release_time;
    @Column(nullable = false)
    private String raise_price;
    @Column(nullable = false)
    private String is_token;
    @Column(nullable = false)
    private String tokens_platform;
    @Column(nullable = false)
    private Timestamp insert_time;
    @Column(nullable = false)
    private Timestamp modify_time;
    @Column(nullable = false)
    private Long serial_number;

    @OneToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name="currencies_pk_id")
    private ICO_Feixiaohao_Exchange_Currencies currencies;

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public Long getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(Long serial_number) {
        this.serial_number = serial_number;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getExchange_number() {
        return exchange_number;
    }

    public void setExchange_number(String exchange_number) {
        this.exchange_number = exchange_number;
    }

    public Timestamp getRelease_time() {
        return release_time;
    }

    public void setRelease_time(Timestamp release_time) {
        this.release_time = release_time;
    }

    public String getRaise_price() {
        return raise_price;
    }

    public void setRaise_price(String raise_price) {
        this.raise_price = raise_price;
    }

    public String getIs_token() {
        return is_token;
    }

    public void setIs_token(String is_token) {
        this.is_token = is_token;
    }

    public String getTokens_platform() {
        return tokens_platform;
    }

    public void setTokens_platform(String tokens_platform) {
        this.tokens_platform = tokens_platform;
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

    public ICO_Feixiaohao_Exchange_Currencies getCurrencies() {
        return currencies;
    }

    public void setCurrencies(ICO_Feixiaohao_Exchange_Currencies currencies) {
        this.currencies = currencies;
    }
}
