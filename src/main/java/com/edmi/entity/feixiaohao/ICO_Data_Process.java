package com.edmi.entity.feixiaohao;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class ICO_Data_Process {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long pk_id;
    @Column(nullable = false)
    private String data_name;
    @Column(nullable = false)
    private String data_status;
    @Column(nullable = false)
    private String api_url;
    @Column(nullable = false)
    private String api_category;

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public String getData_name() {
        return data_name;
    }

    public void setData_name(String data_name) {
        this.data_name = data_name;
    }

    public String getData_status() {
        return data_status;
    }

    public void setData_status(String data_status) {
        this.data_status = data_status;
    }

    public String getApi_url() {
        return api_url;
    }

    public void setApi_url(String api_url) {
        this.api_url = api_url;
    }

    public String getApi_category() {
        return api_category;
    }

    public void setApi_category(String api_category) {
        this.api_category = api_category;
    }
}
