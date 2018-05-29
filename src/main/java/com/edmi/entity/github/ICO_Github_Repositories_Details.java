package com.edmi.entity.github;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class ICO_Github_Repositories_Details {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long pk_id;
    @Column(nullable = false)
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String full_name;
    @Column(nullable = false)
    private String html_url;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private Timestamp created_at;
    @Column(nullable = false)
    private Timestamp updated_at;
    @Column(nullable = false)
    private Timestamp pushed_at;
    @Column(nullable = false)
    private int size;
    @Column(nullable = false)
    private int stargazers_count;
    @Column(nullable = false)
    private int watchers_count;
    @Column(nullable = false)
    private String language;
    @Column(nullable = false)
    private int forks_count;
    @Column(nullable = false)
    private int open_issues_count;
    @Column(nullable = false)
    private String license_key;
    @Column(nullable = false)
    private String license_name;
    @Column(nullable = false)
    private String license_url;
    @Column(nullable = false)
    private int forks;
    @Column(nullable = false)
    private int open_issues;
    @Column(nullable = false)
    private int watchers;
    @Column(nullable = false)
    private String default_branch;
    @Column(nullable = false)
    private int network_count;
    @Column(nullable = false)
    private int subscribers_count;
    @Column(nullable = false)
    private String commits_url;
    @Column(nullable = false)
    private String git_commits_url;
    @Column(nullable = false)
    private String branches_url;
    @Column(nullable = false)
    private String releases_url;
    @Column(nullable = false)
    private String contributors_url;
    @Column(nullable = false)
    private String issues_url;
    @Column(nullable = false)
    private int issues;
    @Column(nullable = false)
    private String pulls_url;
    @Column(nullable = false)
    private int pulls;
    @Column(nullable = false)
    private Timestamp insert_time;
    @OneToOne
    @JoinColumn(name="repositories_pk_id")
    private ICO_Github_Repositories ico_github_repositories;

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Timestamp getPushed_at() {
        return pushed_at;
    }

    public void setPushed_at(Timestamp pushed_at) {
        this.pushed_at = pushed_at;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStargazers_count() {
        return stargazers_count;
    }

    public void setStargazers_count(int stargazers_count) {
        this.stargazers_count = stargazers_count;
    }

    public int getWatchers_count() {
        return watchers_count;
    }

    public void setWatchers_count(int watchers_count) {
        this.watchers_count = watchers_count;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getForks_count() {
        return forks_count;
    }

    public void setForks_count(int forks_count) {
        this.forks_count = forks_count;
    }

    public int getOpen_issues_count() {
        return open_issues_count;
    }

    public void setOpen_issues_count(int open_issues_count) {
        this.open_issues_count = open_issues_count;
    }

    public String getLicense_key() {
        return license_key;
    }

    public void setLicense_key(String license_key) {
        this.license_key = license_key;
    }

    public String getLicense_name() {
        return license_name;
    }

    public void setLicense_name(String license_name) {
        this.license_name = license_name;
    }

    public String getLicense_url() {
        return license_url;
    }

    public void setLicense_url(String license_url) {
        this.license_url = license_url;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getOpen_issues() {
        return open_issues;
    }

    public void setOpen_issues(int open_issues) {
        this.open_issues = open_issues;
    }

    public int getWatchers() {
        return watchers;
    }

    public void setWatchers(int watchers) {
        this.watchers = watchers;
    }

    public String getDefault_branch() {
        return default_branch;
    }

    public void setDefault_branch(String default_branch) {
        this.default_branch = default_branch;
    }

    public int getNetwork_count() {
        return network_count;
    }

    public void setNetwork_count(int network_count) {
        this.network_count = network_count;
    }

    public int getSubscribers_count() {
        return subscribers_count;
    }

    public void setSubscribers_count(int subscribers_count) {
        this.subscribers_count = subscribers_count;
    }

    public String getCommits_url() {
        return commits_url;
    }

    public void setCommits_url(String commits_url) {
        this.commits_url = commits_url;
    }

    public String getGit_commits_url() {
        return git_commits_url;
    }

    public void setGit_commits_url(String git_commits_url) {
        this.git_commits_url = git_commits_url;
    }

    public String getBranches_url() {
        return branches_url;
    }

    public void setBranches_url(String branches_url) {
        this.branches_url = branches_url;
    }

    public String getReleases_url() {
        return releases_url;
    }

    public void setReleases_url(String releases_url) {
        this.releases_url = releases_url;
    }

    public String getContributors_url() {
        return contributors_url;
    }

    public void setContributors_url(String contributors_url) {
        this.contributors_url = contributors_url;
    }

    public String getIssues_url() {
        return issues_url;
    }

    public void setIssues_url(String issues_url) {
        this.issues_url = issues_url;
    }

    public int getIssues() {
        return issues;
    }

    public void setIssues(int issues) {
        this.issues = issues;
    }

    public String getPulls_url() {
        return pulls_url;
    }

    public void setPulls_url(String pulls_url) {
        this.pulls_url = pulls_url;
    }

    public int getPulls() {
        return pulls;
    }

    public void setPulls(int pulls) {
        this.pulls = pulls;
    }

    public Timestamp getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(Timestamp insert_time) {
        this.insert_time = insert_time;
    }

    public ICO_Github_Repositories getIco_github_repositories() {
        return ico_github_repositories;
    }

    public void setIco_github_repositories(ICO_Github_Repositories ico_github_repositories) {
        this.ico_github_repositories = ico_github_repositories;
    }
}
