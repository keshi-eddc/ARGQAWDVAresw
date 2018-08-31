package com.edmi.dto.icodrops;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * icodrops_list 模型
 */
public class ICO_icodrops_listDto {

    private String ico_photo_url;
    private String interest;
    private String categ_type;
    private String received;
    private String received_percent;
    private String goal;
    private String end_date;
    private String end_date_time;
    private String start_date;
    private String tag_one;
    private String tag_four;
    private String market;

    public String getIco_photo_url() {
        return ico_photo_url;
    }

    public void setIco_photo_url(String ico_photo_url) {
        this.ico_photo_url = ico_photo_url;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getCateg_type() {
        return categ_type;
    }

    public void setCateg_type(String categ_type) {
        this.categ_type = categ_type;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public String getReceived_percent() {
        return received_percent;
    }

    public void setReceived_percent(String received_percent) {
        this.received_percent = received_percent;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getEnd_date_time() {
        return end_date_time;
    }

    public void setEnd_date_time(String end_date_time) {
        this.end_date_time = end_date_time;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getTag_four() {
        return tag_four;
    }

    public void setTag_four(String tag_four) {
        this.tag_four = tag_four;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getTag_one() {
        return tag_one;
    }

    public void setTag_one(String tag_one) {
        this.tag_one = tag_one;
    }
}
