package com.edmi.entity.etherscan;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class ICO_Etherscan_IO_Blocks_Txs_Page_List {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long pk_id;
    @Column(nullable = false)
    private int page;
    @Column(nullable = false)
    private Timestamp insert_time;
    @Column(nullable = false)
    private Timestamp modify_time;
    @Column(nullable = false)
    private String status;


    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name="blocks_pk_id")
    private ICO_Etherscan_IO_Blocks blocks;

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
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

    public ICO_Etherscan_IO_Blocks getBlocks() {
        return blocks;
    }

    public void setBlocks(ICO_Etherscan_IO_Blocks blocks) {
        this.blocks = blocks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
