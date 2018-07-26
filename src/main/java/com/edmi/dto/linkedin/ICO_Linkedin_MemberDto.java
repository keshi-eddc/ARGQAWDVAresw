package com.edmi.dto.linkedin;

import java.sql.Timestamp;

public class ICO_Linkedin_MemberDto {

    private Long id;
    private Long link_id;
    private String name;
    private String position;
    private String company;
    private String nationality;
    private String province;
    private String city;
    private String brief;
    private int friends_num;
    private String school;
    private Timestamp insert_time;
    private Timestamp modify_time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLink_id() {
        return link_id;
    }

    public void setLink_id(Long link_id) {
        this.link_id = link_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public int getFriends_num() {
        return friends_num;
    }

    public void setFriends_num(int friends_num) {
        this.friends_num = friends_num;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
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
