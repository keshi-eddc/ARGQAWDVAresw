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
    private String transaction_amount;
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

    public String getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(String transaction_amount) {
        this.transaction_amount = transaction_amount;
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
}
