package com.edmi.dto.linkedin;


import java.sql.Timestamp;

public class ICO_Linkedin_MemberworkexperienceDto {

    private Long id;
    private String position;
    private String company;
    private String work_address;
    private String duration_start;
    private String duration_end;
    private String position_desc;
    private Timestamp insert_time;
    private Timestamp modify_time;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getWork_address() {
        return work_address;
    }

    public void setWork_address(String work_address) {
        this.work_address = work_address;
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

    public String getPosition_desc() {
        return position_desc;
    }

    public void setPosition_desc(String position_desc) {
        this.position_desc = position_desc;
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
