package com.edmi.entity.linkedin;

import javax.persistence.*;
import java.sql.Timestamp;
@Entity
public class ICO_Linkedin_Membereducationexperience {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String school;
    @Column(nullable = true)
    private String degree;
    @Column(nullable = true)
    private String major;
    @Column(nullable = true)
    private String academic_performance;
    @Column(nullable = true)
    private String duration_start;
    @Column(nullable = true)
    private String duration_end;
    @Column(nullable = true)
    private String association_activity;
    @Column(nullable = true)
    private String description;
    @Column(nullable = false)
    private Timestamp insert_time;
    @Column(nullable = false)
    private Timestamp modify_time;

    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="member_id")
    private ICO_Linkedin_Member member;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getAcademic_performance() {
        return academic_performance;
    }

    public void setAcademic_performance(String academic_performance) {
        this.academic_performance = academic_performance;
    }

    public String getDuration_start() {
        return duration_start;
    }

    public void setDuration_start(String duration_start) {
        this.duration_start = duration_start;
    }

    public String getDuration_end() {
        return duration_end;
    }

    public void setDuration_end(String duration_end) {
        this.duration_end = duration_end;
    }

    public String getAssociation_activity() {
        return association_activity;
    }

    public void setAssociation_activity(String association_activity) {
        this.association_activity = association_activity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public ICO_Linkedin_Member getMember() {
        return member;
    }

    public void setMember(ICO_Linkedin_Member member) {
        this.member = member;
    }
}
