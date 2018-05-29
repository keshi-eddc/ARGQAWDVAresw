package com.edmi.entity.dlzb;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class DLZB_Project_List_Basic_Info {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long pk_id;
    @Column(nullable = false)
    private String last_update_time;
    @Column(nullable = false)
    private Integer track_times;
    @Column(nullable = false)
    private String region;
    @Column(nullable = false)
    private String progress;
    @Column(nullable = false)
    private String join_time;
    @Column(nullable = false)
    private String invest_total;
    @Column(nullable = false)
    private String project_nature;
    @Column(nullable = false)
    private String funds_source;
    @Column(nullable = false)
    private String proprietor;
    @Column(nullable = false)
    private String project_cycle;
    @Column(nullable = false)
    private String devices;
    @Column(nullable = false)
    private Timestamp insert_time;
    @Column(nullable = false)
    private Timestamp modify_time;
    @Column(nullable = false)
    private String project_overview;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name="project_list_pk_id")
    private DLZB_Project_List project_list;

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public String getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(String last_update_time) {
        this.last_update_time = last_update_time;
    }

    public Integer getTrack_times() {
        return track_times;
    }

    public void setTrack_times(Integer track_times) {
        this.track_times = track_times;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getJoin_time() {
        return join_time;
    }

    public void setJoin_time(String join_time) {
        this.join_time = join_time;
    }

    public String getInvest_total() {
        return invest_total;
    }

    public void setInvest_total(String invest_total) {
        this.invest_total = invest_total;
    }

    public String getProject_nature() {
        return project_nature;
    }

    public void setProject_nature(String project_nature) {
        this.project_nature = project_nature;
    }

    public String getFunds_source() {
        return funds_source;
    }

    public void setFunds_source(String funds_source) {
        this.funds_source = funds_source;
    }

    public String getProprietor() {
        return proprietor;
    }

    public void setProprietor(String proprietor) {
        this.proprietor = proprietor;
    }

    public String getProject_cycle() {
        return project_cycle;
    }

    public void setProject_cycle(String project_cycle) {
        this.project_cycle = project_cycle;
    }

    public String getDevices() {
        return devices;
    }

    public void setDevices(String devices) {
        this.devices = devices;
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

    public DLZB_Project_List getProject_list() {
        return project_list;
    }

    public void setProject_list(DLZB_Project_List project_list) {
        this.project_list = project_list;
    }

    public String getProject_overview() {
        return project_overview;
    }

    public void setProject_overview(String project_overview) {
        this.project_overview = project_overview;
    }
}
