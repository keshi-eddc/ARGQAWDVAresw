package com.edmi.dto.linkedin;

import com.edmi.entity.linkedin.ICO_Linkedin_Member;

import java.sql.Timestamp;

public class ICO_Linkedin_MemberselectionskillsDto {

    private Long id;
    private String skill;
    private int approve_num;
    private Timestamp modify_time;
    private Timestamp insert_time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public int getApprove_num() {
        return approve_num;
    }

    public void setApprove_num(int approve_num) {
        this.approve_num = approve_num;
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

}
