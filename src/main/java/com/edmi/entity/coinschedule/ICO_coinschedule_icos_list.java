package com.edmi.entity.coinschedule;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * ICO_coinschedule_icos_list 模型
 */
@Entity
@Table(name = "ico_coinschedule_icos_list")
public class ICO_coinschedule_icos_list {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "category")
    private String category;

    @Column(nullable = false, name = "ended_on")
    private String ended_on;

    @Column(nullable = false, name = "total_raised")
    private String total_raised;

    @Column(nullable = false, name = "insert_Time")
    private Timestamp insert_Time;

    @Column(nullable = false, name = "update_Time")
    private Timestamp update_Time;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEnded_on() {
        return ended_on;
    }

    public void setEnded_on(String ended_on) {
        this.ended_on = ended_on;
    }

    public String getTotal_raised() {
        return total_raised;
    }

    public void setTotal_raised(String total_raised) {
        this.total_raised = total_raised;
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
}
