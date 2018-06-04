package com.edmi.entity.etherscan;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class ICO_Etherscan_IO_Blocks_Forked_Txs {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long pk_id;
    @Column(nullable = false)
    private String txhash;
    @Column(nullable = false)
    private Long block;
    @Column(nullable = false)
    private Timestamp age;
    @Column(nullable = false)
    private String f;
    @Column(nullable = false)
    private String t;
    @Column(nullable = false)
    private String val;
    @Column(nullable = false)
    private String txfee;
    @Column(nullable = false)
    private Timestamp insert_time;
    @Column(nullable = false)
    private Timestamp modify_time;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private Timestamp detail_modify_time;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name="page_list_pk_id")
    private ICO_Etherscan_IO_Blocks_Forked_Txs_Page_List page_list;

    @OneToOne(mappedBy = "txs",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private ICO_Etherscan_IO_Blocks_Forked_Txs_Info txs_info;

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public String getTxhash() {
        return txhash;
    }

    public void setTxhash(String txhash) {
        this.txhash = txhash;
    }

    public Long getBlock() {
        return block;
    }

    public void setBlock(Long block) {
        this.block = block;
    }

    public Timestamp getAge() {
        return age;
    }

    public void setAge(Timestamp age) {
        this.age = age;
    }

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getTxfee() {
        return txfee;
    }

    public void setTxfee(String txfee) {
        this.txfee = txfee;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getDetail_modify_time() {
        return detail_modify_time;
    }

    public void setDetail_modify_time(Timestamp detail_modify_time) {
        this.detail_modify_time = detail_modify_time;
    }

    public ICO_Etherscan_IO_Blocks_Forked_Txs_Page_List getPage_list() {
        return page_list;
    }

    public void setPage_list(ICO_Etherscan_IO_Blocks_Forked_Txs_Page_List page_list) {
        this.page_list = page_list;
    }

    public ICO_Etherscan_IO_Blocks_Forked_Txs_Info getTxs_info() {
        return txs_info;
    }

    public void setTxs_info(ICO_Etherscan_IO_Blocks_Forked_Txs_Info txs_info) {
        this.txs_info = txs_info;
    }
}
