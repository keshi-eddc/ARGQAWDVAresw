package com.edmi.entity.github;

import javax.persistence.*;
import java.sql.Timestamp;
@Entity
public class ICO_Github_Organization {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long pk_id;
    @Column(nullable = false)
    private String id;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String blog;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String company;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String repos_url;
    @Column(nullable = false)
    private String events_url;
    @Column(nullable = false)
    private String hooks_url;
    @Column(nullable = false)
    private String issues_url;
    @Column(nullable = false)
    private String members_url;
    @Column(nullable = false)
    private String public_members_url;
    @Column(nullable = false)
    private int public_members;
    @Column(nullable = false)
    private String avatar_url;
    @Column(nullable = false)
    private Boolean has_organization_projects;
    @Column(nullable = false)
    private Boolean has_repository_projects;
    @Column(nullable = false)
    private int public_repos;
    @Column(nullable = false)
    private int public_gists;
    @Column(nullable = false)
    private int followers;
    @Column(nullable = false)
    private int following;
    @Column(nullable = false)
    private String html_url;
    @Column(nullable = false)
    private Timestamp created_at;
    @Column(nullable = false)
    private Timestamp updated_at;

    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private Timestamp insert_time;
    @OneToOne
    @JoinColumn(name="link_id")
    private ICO_Github_Links ico_github_links;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRepos_url() {
        return repos_url;
    }

    public void setRepos_url(String repos_url) {
        this.repos_url = repos_url;
    }

    public String getEvents_url() {
        return events_url;
    }

    public void setEvents_url(String events_url) {
        this.events_url = events_url;
    }

    public String getHooks_url() {
        return hooks_url;
    }

    public void setHooks_url(String hooks_url) {
        this.hooks_url = hooks_url;
    }

    public String getIssues_url() {
        return issues_url;
    }

    public void setIssues_url(String issues_url) {
        this.issues_url = issues_url;
    }

    public String getMembers_url() {
        return members_url;
    }

    public void setMembers_url(String members_url) {
        this.members_url = members_url;
    }

    public String getPublic_members_url() {
        return public_members_url;
    }

    public void setPublic_members_url(String public_members_url) {
        this.public_members_url = public_members_url;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getHas_organization_projects() {
        return has_organization_projects;
    }

    public void setHas_organization_projects(Boolean has_organization_projects) {
        this.has_organization_projects = has_organization_projects;
    }

    public Boolean getHas_repository_projects() {
        return has_repository_projects;
    }

    public void setHas_repository_projects(Boolean has_repository_projects) {
        this.has_repository_projects = has_repository_projects;
    }

    public int getPublic_repos() {
        return public_repos;
    }

    public void setPublic_repos(int public_repos) {
        this.public_repos = public_repos;
    }

    public int getPublic_gists() {
        return public_gists;
    }

    public void setPublic_gists(int public_gists) {
        this.public_gists = public_gists;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(Timestamp insert_time) {
        this.insert_time = insert_time;
    }

    public ICO_Github_Links getIco_github_links() {
        return ico_github_links;
    }

    public void setIco_github_links(ICO_Github_Links ico_github_links) {
        this.ico_github_links = ico_github_links;
    }

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public int getPublic_members() {
        return public_members;
    }

    public void setPublic_members(int public_members) {
        this.public_members = public_members;
    }
}
