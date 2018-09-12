package com.edmi.entity.icorating;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * ico_icorating_funds_detail_portfolio 模型
 */
@Entity
@Table(name = "ico_icorating_funds_detail_portfolio")
public class ICO_icorating_funds_detail_portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;

    @Column(nullable = false, name = "project_link")
    private String project_link;

    @Column(nullable = false, name = "project_logo_url")
    private String project_logo_url;

    @Column(nullable = false, name = "project_name")
    private String project_name;

    @Column(nullable = false, name = "industries")
    private String industries;

    @Column(nullable = false, name = "eth_return")
    private String eth_return;

    @Column(nullable = false, name = "money_raised")
    private String money_raised;
    @Column(nullable = false, name = "insert_Time")
    private Timestamp insert_Time;

    @Column(nullable = false, name = "update_Time")
    private Timestamp update_Time;

    /**
     * 本表 和 funds_detail 表是多对一的关系
     * 本表的fk_id是外键，指向funds_detail的pk_id
     */
    @ManyToOne
    @JoinColumn(name = "fk_id")
    private ICO_icorating_funds_detail ico_icorating_funds_detail;

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public String getProject_link() {
        return project_link;
    }

    public void setProject_link(String project_link) {
        this.project_link = project_link;
    }

    public String getProject_logo_url() {
        return project_logo_url;
    }

    public void setProject_logo_url(String project_logo_url) {
        this.project_logo_url = project_logo_url;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getIndustries() {
        return industries;
    }

    public void setIndustries(String industries) {
        this.industries = industries;
    }

    public String getEth_return() {
        return eth_return;
    }

    public void setEth_return(String eth_return) {
        this.eth_return = eth_return;
    }

    public String getMoney_raised() {
        return money_raised;
    }

    public void setMoney_raised(String money_raised) {
        this.money_raised = money_raised;
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

    public ICO_icorating_funds_detail getIco_icorating_funds_detail() {
        return ico_icorating_funds_detail;
    }

    public void setIco_icorating_funds_detail(ICO_icorating_funds_detail ico_icorating_funds_detail) {
        this.ico_icorating_funds_detail = ico_icorating_funds_detail;
    }
}
