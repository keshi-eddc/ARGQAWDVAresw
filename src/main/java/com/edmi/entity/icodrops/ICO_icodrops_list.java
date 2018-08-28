package com.edmi.entity.icodrops;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * icodrops_list 模型
 */
@Entity
@Table(name = "ico_icodrops_list")
public class ICO_icodrops_list {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;

    @Column(nullable = false, name = "input_type")
    private String input_type;

    @Column(nullable = false, name = "table_type")
    private String table_type;

    @Column(nullable = false, name = "ico_name")
    private String ico_name;

    @Column(nullable = false, name = "ico_url")
    private String ico_url;

    @Column(nullable = false, name = "ico_photo_url")
    private String ico_photo_url;

    @Column(nullable = false, name = "interest")
    private String interest;

    @Column(nullable = false, name = "categ_type")
    private String categ_type;

    @Column(nullable = false, name = "received")
    private String received;

    @Column(nullable = false, name = "received_percent")
    private String received_percent;

    @Column(nullable = false, name = "goal")
    private String goal;

    @Column(nullable = false, name = "end_date")
    private String end_date;

    @Column(nullable = false, name = "end_date_time")
    private String end_date_time;

    @Column(nullable = false, name = "start_date")
    private String start_date;

    @Column(nullable = false, name = "tag_one")
    private String tag_one;

    @Column(nullable = false, name = "tag_two")
    private String tag_two;

    @Column(nullable = false, name = "tag_three")
    private String tag_three;

    @Column(nullable = false, name = "tag_four")
    private String tag_four;

    @Column(nullable = false, name = "tag_five")
    private String tag_five;

    @Column(nullable = false, name = "market")
    private String market;

    @Column(nullable = false, name = "crawled_status")
    private String crawledStatu;

    @Column(nullable = false, name = "insert_Time")
    private Timestamp insertTime;

    @Column(nullable = false, name = "update_Time")
    private Timestamp updateTime;

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public String getIco_photo_url() {
        return ico_photo_url;
    }

    public void setIco_photo_url(String ico_photo_url) {
        this.ico_photo_url = ico_photo_url;
    }

    public String getInput_type() {
        return input_type;
    }

    public void setInput_type(String input_type) {
        this.input_type = input_type;
    }

    public String getTable_type() {
        return table_type;
    }

    public void setTable_type(String table_type) {
        this.table_type = table_type;
    }

    public String getIco_name() {
        return ico_name;
    }

    public void setIco_name(String ico_name) {
        this.ico_name = ico_name;
    }

    public String getIco_url() {
        return ico_url;
    }

    public void setIco_url(String ico_url) {
        this.ico_url = ico_url;
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

    public String getTag_one() {
        return tag_one;
    }

    public void setTag_one(String tag_one) {
        this.tag_one = tag_one;
    }

    public String getTag_two() {
        return tag_two;
    }

    public void setTag_two(String tag_two) {
        this.tag_two = tag_two;
    }

    public String getTag_three() {
        return tag_three;
    }

    public void setTag_three(String tag_three) {
        this.tag_three = tag_three;
    }

    public String getTag_four() {
        return tag_four;
    }

    public void setTag_four(String tag_four) {
        this.tag_four = tag_four;
    }

    public String getTag_five() {
        return tag_five;
    }

    public void setTag_five(String tag_five) {
        this.tag_five = tag_five;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getCrawledStatu() {
        return crawledStatu;
    }

    public void setCrawledStatu(String crawledStatu) {
        this.crawledStatu = crawledStatu;
    }

    public Timestamp getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Timestamp insertTime) {
        this.insertTime = insertTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
