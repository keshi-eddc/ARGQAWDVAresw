package com.edmi.entity.linkedin;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class ICO_Linkedin_Member {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long link_id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private String position;
    @Column(nullable = true)
    private String company;
    @Column(nullable = true)
    private String nationality;
    @Column(nullable = true)
    private String province;
    @Column(nullable = true)
    private String city;
    @Column(nullable = true)
    private String brief;
    @Column(nullable = true)
    private int friends_num;
    @Column(nullable = true)
    private String school;
    @Column(nullable = false)
    private Timestamp insert_time;
    @Column(nullable = false)
    private Timestamp modify_time;

    @OneToMany(cascade=CascadeType.ALL,mappedBy="member")
    private List<ICO_Linkedin_Membereducationexperience> educationexperiences;

    @OneToMany(cascade=CascadeType.ALL,mappedBy="member")
    private List<ICO_Linkedin_Memberselectionskills> selectionskills;

    @OneToMany(cascade=CascadeType.ALL,mappedBy="member")
    private List<ICO_Linkedin_Memberworkexperience> workexperiences;


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

    public List<ICO_Linkedin_Membereducationexperience> getEducationexperiences() {
        return educationexperiences;
    }

    public void setEducationexperiences(List<ICO_Linkedin_Membereducationexperience> educationexperiences) {
        this.educationexperiences = educationexperiences;
    }

    public List<ICO_Linkedin_Memberselectionskills> getSelectionskills() {
        return selectionskills;
    }

    public void setSelectionskills(List<ICO_Linkedin_Memberselectionskills> selectionskills) {
        this.selectionskills = selectionskills;
    }

    public List<ICO_Linkedin_Memberworkexperience> getWorkexperiences() {
        return workexperiences;
    }

    public void setWorkexperiences(List<ICO_Linkedin_Memberworkexperience> workexperiences) {
        this.workexperiences = workexperiences;
    }
}
