package com.edmi.entity.feixiaohao;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class ICO_Feixiaohao_Exchange_Details {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long pk_id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String name_link;
    @Column(nullable = false)
    private String counter_party;
    @Column(nullable = false)
    private Double price_usd;
    @Column(nullable = false)
    private Double price_cny;
    @Column(nullable = false)
    private Double price_btc;
    @Column(nullable = false)
    private Double price_native;
    @Column(nullable = false)
    private String transaction_num;
    @Column(nullable = false)
    private Double volume_usd;
    @Column(nullable = false)
    private Double volume_cny;
    @Column(nullable = false)
    private Double volume_btc;
    @Column(nullable = false)
    private Double volume_native;
    @Column(nullable = false)
    private String per;
    @Column(nullable = false)
    private String last_update;
    @Column(nullable = false)
    private Timestamp insert_time;
    @Column(nullable = false)
    private Timestamp modify_time;
    @OneToOne
    @JoinColumn(name="exchange_id")
    private ICO_Feixiaohao_Exchange ico_feixiaohao_exchange ;

    public long getPk_id() {
        return pk_id;
    }

    public void setPk_id(long pk_id) {
        this.pk_id = pk_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_link() {
        return name_link;
    }

    public void setName_link(String name_link) {
        this.name_link = name_link;
    }

    public String getCounter_party() {
        return counter_party;
    }

    public void setCounter_party(String counter_party) {
        this.counter_party = counter_party;
    }



    public String getTransaction_num() {
        return transaction_num;
    }

    public void setTransaction_num(String transaction_num) {
        this.transaction_num = transaction_num;
    }

    public Double getPrice_usd() {
        return price_usd;
    }

    public void setPrice_usd(Double price_usd) {
        this.price_usd = price_usd;
    }

    public Double getPrice_cny() {
        return price_cny;
    }

    public void setPrice_cny(Double price_cny) {
        this.price_cny = price_cny;
    }

    public Double getPrice_btc() {
        return price_btc;
    }

    public void setPrice_btc(Double price_btc) {
        this.price_btc = price_btc;
    }

    public Double getPrice_native() {
        return price_native;
    }

    public void setPrice_native(Double price_native) {
        this.price_native = price_native;
    }

    public Double getVolume_usd() {
        return volume_usd;
    }

    public void setVolume_usd(Double volume_usd) {
        this.volume_usd = volume_usd;
    }

    public Double getVolume_cny() {
        return volume_cny;
    }

    public void setVolume_cny(Double volume_cny) {
        this.volume_cny = volume_cny;
    }

    public Double getVolume_btc() {
        return volume_btc;
    }

    public void setVolume_btc(Double volume_btc) {
        this.volume_btc = volume_btc;
    }

    public Double getVolume_native() {
        return volume_native;
    }

    public void setVolume_native(Double volume_native) {
        this.volume_native = volume_native;
    }

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
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

    public ICO_Feixiaohao_Exchange getIco_feixiaohao_exchange() {
        return ico_feixiaohao_exchange;
    }

    public void setIco_feixiaohao_exchange(ICO_Feixiaohao_Exchange ico_feixiaohao_exchange) {
        this.ico_feixiaohao_exchange = ico_feixiaohao_exchange;
    }
}
