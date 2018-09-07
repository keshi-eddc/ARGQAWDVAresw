package com.edmi.entity.trackico;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author keshi
 * @ClassName: ICO_trackico_detail
 * @Description: 详情页的模型
 * @date 2018年7月31日 上午10:28:30
 */
@Entity
@Table(name = "ico_trackico_detail")
public class ICO_trackico_detail {
    // 主键 自增
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;
    // nullable = false 该字段可以为空
    @Column(nullable = false, name = "block_name")
    private String block_name;

    @Column(nullable = false, name = "block_token")
    private String block_token;

    @Column(nullable = false, name = "block_tag")
    private String block_tag;

    @Column(nullable = false, name = "block_status")
    private String block_status;

    @Column(nullable = false, name = "block_description")
    private String block_description;
    @Column(nullable = false, name = "logo_url")
    private String logo_url;
    @Column(nullable = false, name = "insert_time")
    private Timestamp insert_time;
    @Column(nullable = false, name = "update_time")
    private Timestamp update_time;

    /**
     * @OneToOne list表（ico_trackico_item）和本表是一对一的关系
     * 本表的fk_id是外键，指向ICO_trackico_item的pk_id
     * 会自动关联
     */
    @OneToOne
    @JoinColumn(name = "fk_id")
    private ICO_trackico_item ico_trackico_item;


    public String getBlock_token() {
        return block_token;
    }

    public void setBlock_token(String block_token) {
        this.block_token = block_token;
    }

    public String getBlock_status() {
        return block_status;
    }

    public void setBlock_status(String block_status) {
        this.block_status = block_status;
    }

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public String getBlock_tag() {
        return block_tag;
    }

    public void setBlock_tag(String block_tag) {
        this.block_tag = block_tag;
    }

    public String getBlock_description() {
        return block_description;
    }

    public void setBlock_description(String block_description) {
        this.block_description = block_description;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public Timestamp getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(Timestamp insert_time) {
        this.insert_time = insert_time;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }

    public ICO_trackico_item getIco_trackico_item() {
        return ico_trackico_item;
    }

    public void setIco_trackico_item(ICO_trackico_item ico_trackico_item) {
        this.ico_trackico_item = ico_trackico_item;
    }

    @Override
    public String toString() {
        String str = "ico_trackico_detail[" + "pk_id=" + pk_id + ",fk_id=" + ico_trackico_item.getPk_id() + ",block_name=" + block_name + ",block_tag=" + block_tag + ",block_description="
                + block_description + ",logo_url=" + logo_url + ",insert_time=" + insert_time + ",update_time=" + update_time + "]";
        return str;
    }
}
