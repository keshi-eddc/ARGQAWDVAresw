package com.edmi.entity.github;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class ICO_Github_Links {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String source_url;
    @Column(nullable = false)
    private String community_link;
    @Column(nullable = false)
    private String github_community;
    @Column(nullable = false)
    private String status;
    @Column(nullable = true)
    private Timestamp insert_time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getCommunity_link() {
        return community_link;
    }

    public void setCommunity_link(String community_link) {
        this.community_link = community_link;
    }

    public String getGithub_community() {
        return github_community;
    }

    public void setGithub_community(String github_community) {
        this.github_community = github_community;
    }

    public Timestamp getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(Timestamp insert_time) {
        this.insert_time = insert_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
