package com.edmi.entity.linkedin;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class ICO_Linkedin_Link {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String link;
    @Column(nullable = false)
    private String link_status;
    @Column(nullable = false)
    private String member_info_status;
    @Column(nullable = true)
    private String member_info_server;
    @Column(nullable = true)
    private Timestamp insert_time;
    @Column(nullable = true)
    private String standard_link;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink_status() {
        return link_status;
    }

    public void setLink_status(String link_status) {
        this.link_status = link_status;
    }

    public String getMember_info_status() {
        return member_info_status;
    }

    public void setMember_info_status(String member_info_status) {
        this.member_info_status = member_info_status;
    }

    public String getMember_info_server() {
        return member_info_server;
    }

    public void setMember_info_server(String member_info_server) {
        this.member_info_server = member_info_server;
    }

    public Timestamp getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(Timestamp insert_time) {
        this.insert_time = insert_time;
    }

    public String getStandard_link() {
        return standard_link;
    }

    public void setStandard_link(String standard_link) {
        this.standard_link = standard_link;
    }
}
