package com.edmi.entity.trackico;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 2018年9月10日11:47:31
 * ico_trackico_detail_block_bounty 模型
 */
@Entity
@Table(name = "ico_trackico_detail_block_bounty")
public class ICO_trackico_detail_block_bounty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;
    @Column(nullable = false, name = "bounty_key")
    private String bounty_key;
    @Column(nullable = false, name = "bounty_value")
    private String bounty_value;
    @Column(nullable = false, name = "bounty_type")
    private String bounty_type;
    @Column(nullable = false, name = "insert_time")
    private Timestamp insert_time;
    @Column(nullable = false, name = "update_time")
    private Timestamp update_time;

    /**
     * @OneToOne 本表和detail表（ico_trackico_detail）是多对一的关系
     * 本表的fk_id是外键，指向ico_trackico_detail的pk_id
     * 会自动关联
     */
    @ManyToOne
    @JoinColumn(name = "fk_id")
    private ICO_trackico_detail ico_trackico_detail;

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public String getBounty_key() {
        return bounty_key;
    }

    public void setBounty_key(String bounty_key) {
        this.bounty_key = bounty_key;
    }

    public String getBounty_value() {
        return bounty_value;
    }

    public void setBounty_value(String bounty_value) {
        this.bounty_value = bounty_value;
    }

    public String getBounty_type() {
        return bounty_type;
    }

    public void setBounty_type(String bounty_type) {
        this.bounty_type = bounty_type;
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

    public ICO_trackico_detail getIco_trackico_detail() {
        return ico_trackico_detail;
    }

    public void setIco_trackico_detail(ICO_trackico_detail ico_trackico_detail) {
        this.ico_trackico_detail = ico_trackico_detail;
    }
}
