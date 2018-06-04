package com.edmi.entity.etherscan;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class ICO_Etherscan_IO_Blocks_Forked_Info {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long pk_id;
    @Column(nullable = false)
    private int transactions;
    @Column(nullable = false)
    private String hash;
    @Column(nullable = false)
    private String parent_hash;
    @Column(nullable = false)
    private String sha3_uncles;
    @Column(nullable = false)
    private String mined_by;
    @Column(nullable = false)
    private String difficulty;
    @Column(nullable = false)
    private String total_difficulty;
    @Column(nullable = false)
    private String size;
    @Column(nullable = false)
    private String nonce;
    @Column(nullable = false)
    private String block_reward;
    @Column(nullable = false)
    private String uncles_reward;
    @Column(nullable = false)
    private String extra_data;
    @Column(nullable = false)
    private Timestamp insert_time;
    @Column(nullable = false)
    private Timestamp modify_time;

    @OneToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name="blocks_pk_id")
    private ICO_Etherscan_IO_Blocks_Forked blocks;


    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public int getTransactions() {
        return transactions;
    }

    public void setTransactions(int transactions) {
        this.transactions = transactions;
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



    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getParent_hash() {
        return parent_hash;
    }

    public void setParent_hash(String parent_hash) {
        this.parent_hash = parent_hash;
    }

    public String getSha3_uncles() {
        return sha3_uncles;
    }

    public void setSha3_uncles(String sha3_uncles) {
        this.sha3_uncles = sha3_uncles;
    }

    public String getMined_by() {
        return mined_by;
    }

    public void setMined_by(String mined_by) {
        this.mined_by = mined_by;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTotal_difficulty() {
        return total_difficulty;
    }

    public void setTotal_difficulty(String total_difficulty) {
        this.total_difficulty = total_difficulty;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getBlock_reward() {
        return block_reward;
    }

    public void setBlock_reward(String block_reward) {
        this.block_reward = block_reward;
    }

    public String getUncles_reward() {
        return uncles_reward;
    }

    public void setUncles_reward(String uncles_reward) {
        this.uncles_reward = uncles_reward;
    }

    public String getExtra_data() {
        return extra_data;
    }

    public void setExtra_data(String extra_data) {
        this.extra_data = extra_data;
    }

    public ICO_Etherscan_IO_Blocks_Forked getBlocks() {
        return blocks;
    }

    public void setBlocks(ICO_Etherscan_IO_Blocks_Forked blocks) {
        this.blocks = blocks;
    }
}
