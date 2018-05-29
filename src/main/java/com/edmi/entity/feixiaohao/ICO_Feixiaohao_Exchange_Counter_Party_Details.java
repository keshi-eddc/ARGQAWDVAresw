package com.edmi.entity.feixiaohao;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class ICO_Feixiaohao_Exchange_Counter_Party_Details {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long pk_id;
    @Column(nullable = false)
    private Double market_cap_by_available_supply;
    @Column(nullable = false)
    private Timestamp supply_time;
    @Column(nullable = false)
    private Double price_usd;
    @Column(nullable = false)
    private Timestamp price_usd_time;
    @Column(nullable = false)
    private Double price_btc;
    @Column(nullable = false)
    private Timestamp price_btc_time;
    @Column(nullable = false)
    private Double vol_usd;
    @Column(nullable = false)
    private Timestamp vol_usd_time;
    @Column(nullable = false)
    private Timestamp insert_time;
    @Column(nullable = false)
    private String exchange_details_link;


    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public Double getMarket_cap_by_available_supply() {
        return market_cap_by_available_supply;
    }

    public void setMarket_cap_by_available_supply(Double market_cap_by_available_supply) {
        this.market_cap_by_available_supply = market_cap_by_available_supply;
    }

    public Timestamp getSupply_time() {
        return supply_time;
    }

    public void setSupply_time(Timestamp supply_time) {
        this.supply_time = supply_time;
    }

    public Double getPrice_usd() {
        return price_usd;
    }

    public void setPrice_usd(Double price_usd) {
        this.price_usd = price_usd;
    }

    public Timestamp getPrice_usd_time() {
        return price_usd_time;
    }

    public void setPrice_usd_time(Timestamp price_usd_time) {
        this.price_usd_time = price_usd_time;
    }

    public Double getPrice_btc() {
        return price_btc;
    }

    public void setPrice_btc(Double price_btc) {
        this.price_btc = price_btc;
    }

    public Timestamp getPrice_btc_time() {
        return price_btc_time;
    }

    public void setPrice_btc_time(Timestamp price_btc_time) {
        this.price_btc_time = price_btc_time;
    }

    public Double getVol_usd() {
        return vol_usd;
    }

    public void setVol_usd(Double vol_usd) {
        this.vol_usd = vol_usd;
    }

    public Timestamp getVol_usd_time() {
        return vol_usd_time;
    }

    public void setVol_usd_time(Timestamp vol_usd_time) {
        this.vol_usd_time = vol_usd_time;
    }

    public Timestamp getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(Timestamp insert_time) {
        this.insert_time = insert_time;
    }

    public String getExchange_details_link() {
        return exchange_details_link;
    }

    public void setExchange_details_link(String exchange_details_link) {
        this.exchange_details_link = exchange_details_link;
    }
}
