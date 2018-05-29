package com.edmi.entity.linkedin;

import javax.persistence.*;
import java.sql.Timestamp;
@Entity
public class ICO_Linkedin_Memberselectionskills {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true)
    private String skill;
    @Column(nullable = true)
    private int approve_num;
    @Column(nullable = false)
    private Timestamp modify_time;
    @Column(nullable = false)
    private Timestamp insert_time;

    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="member_id")
    private ICO_Linkedin_Member member;

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

    public ICO_Linkedin_Member getMember() {
        return member;
    }

    public void setMember(ICO_Linkedin_Member member) {
        this.member = member;
    }
}
