package com.edmi.entity.etherscan;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class ICO_Etherscan_IO_Blocks {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long pk_id;
    @Column(nullable = false)
    private Long height;
    @Column(nullable = false)
    private Timestamp age;
    @Column(nullable = false)
    private Integer txn;
    @Column(nullable = false)
    private Integer uncles;
    @Column(nullable = false)
    private String miner;
    @Column(nullable = false)
    private Double gasUsed;
    @Column(nullable = false)
    private float gasUsedPercent;
    @Column(nullable = false)
    private Double gasLimit;
    @Column(nullable = false)
    private String avgGasPrice;
    @Column(nullable = false)
    private String reward;
    @Column(nullable = false)
    private Timestamp insert_time;
    @Column(nullable = false)
    private Timestamp modify_time;
    @Column(nullable = false)
    private Long serial_number;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private Timestamp block_info_modify_time;
    @Column(nullable = false)
    private String pagestatus;
    @Column(nullable = false)
    private Timestamp page_modify_time;
    @Column(nullable = false)
    private int server;

    @OneToOne(mappedBy = "blocks",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private ICO_Etherscan_IO_Blocks_Info blocks_info;

    @OneToMany(mappedBy="blocks")
    private List<ICO_Etherscan_IO_Blocks_Txs_Page_List> pages;

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Timestamp getAge() {
        return age;
    }

    public void setAge(Timestamp age) {
        this.age = age;
    }

    public Integer getTxn() {
        return txn;
    }

    public void setTxn(Integer txn) {
        this.txn = txn;
    }

    public Integer getUncles() {
        return uncles;
    }

    public void setUncles(Integer uncles) {
        this.uncles = uncles;
    }

    public String getMiner() {
        return miner;
    }

    public void setMiner(String miner) {
        this.miner = miner;
    }

    public Double getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(Double gasUsed) {
        this.gasUsed = gasUsed;
    }

    public float getGasUsedPercent() {
        return gasUsedPercent;
    }

    public void setGasUsedPercent(float gasUsedPercent) {
        this.gasUsedPercent = gasUsedPercent;
    }

    public Double getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(Double gasLimit) {
        this.gasLimit = gasLimit;
    }

    public String getAvgGasPrice() {
        return avgGasPrice;
    }

    public void setAvgGasPrice(String avgGasPrice) {
        this.avgGasPrice = avgGasPrice;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
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

    public Long getSerial_number() {
        return serial_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getBlock_info_modify_time() {
        return block_info_modify_time;
    }

    public void setBlock_info_modify_time(Timestamp block_info_modify_time) {
        this.block_info_modify_time = block_info_modify_time;
    }

    public void setSerial_number(Long serial_number) {
        this.serial_number = serial_number;
    }

    public ICO_Etherscan_IO_Blocks_Info getBlocks_info() {
        return blocks_info;
    }

    public void setBlocks_info(ICO_Etherscan_IO_Blocks_Info blocks_info) {
        this.blocks_info = blocks_info;
    }

    public List<ICO_Etherscan_IO_Blocks_Txs_Page_List> getPages() {
        return pages;
    }

    public void setPages(List<ICO_Etherscan_IO_Blocks_Txs_Page_List> pages) {
        this.pages = pages;
    }

    public String getPagestatus() {
        return pagestatus;
    }

    public void setPagestatus(String pagestatus) {
        this.pagestatus = pagestatus;
    }

    public Timestamp getPage_modify_time() {
        return page_modify_time;
    }

    public void setPage_modify_time(Timestamp page_modify_time) {
        this.page_modify_time = page_modify_time;
    }

    public int getServer() {
        return server;
    }

    public void setServer(int server) {
        this.server = server;
    }
}
