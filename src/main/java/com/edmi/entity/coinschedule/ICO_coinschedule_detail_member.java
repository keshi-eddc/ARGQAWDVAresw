package com.edmi.entity.coinschedule;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * 人员模型
 */
@Entity
@Table(name = "ico_coinschedule_detail_member")
public class ICO_coinschedule_detail_member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;

    @Column(nullable = false, name = "member_url")
    private String member_url;

    @Column(nullable = false, name = "member_name")
    private String member_name;

    @Column(nullable = false, name = "member_photo_url")
    private String member_photo_url;

    @Column(nullable = false, name = "member_position")
    private String member_position;

    @Column(nullable = false, name = "member_description")
    private String member_description;

    @Column(nullable = false, name = "member_type")
    private String member_type;

    @Column(nullable = false, name = "insert_Time")
    private Timestamp insert_Time;

    @Column(nullable = false, name = "update_Time")
    private Timestamp update_Time;

    /**
     * 本表和detail表是多对一的关系
     * 本表的fk_id是外键，指向detail的pk_id
     */
    @ManyToOne
    @JoinColumn(name = "fk_id")
    private ICO_coinschedule_detail ico_coinschedule_detail;

    @OneToMany(mappedBy = "ico_coinschedule_detail_member")
    private List<ICO_coinschedule_detail_member_sociallink> memberSociallinkList;

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public String getMember_url() {
        return member_url;
    }

    public void setMember_url(String member_url) {
        this.member_url = member_url;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_photo_url() {
        return member_photo_url;
    }

    public void setMember_photo_url(String member_photo_url) {
        this.member_photo_url = member_photo_url;
    }

    public String getMember_position() {
        return member_position;
    }

    public void setMember_position(String member_position) {
        this.member_position = member_position;
    }

    public String getMember_description() {
        return member_description;
    }

    public void setMember_description(String member_description) {
        this.member_description = member_description;
    }

    public String getMember_type() {
        return member_type;
    }

    public void setMember_type(String member_type) {
        this.member_type = member_type;
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

    public ICO_coinschedule_detail getIco_coinschedule_detail() {
        return ico_coinschedule_detail;
    }

    public void setIco_coinschedule_detail(ICO_coinschedule_detail ico_coinschedule_detail) {
        this.ico_coinschedule_detail = ico_coinschedule_detail;
    }

    public List<ICO_coinschedule_detail_member_sociallink> getMemberSociallinkList() {
        return memberSociallinkList;
    }

    public void setMemberSociallinkList(List<ICO_coinschedule_detail_member_sociallink> memberSociallinkList) {
        this.memberSociallinkList = memberSociallinkList;
    }
}
