package com.edmi.entity.etherscan;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class ICO_Etherscan_IO_Blocks_Txs_Info {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long pk_id;
    @Column(nullable = false)
    private String tx_receipt_status;
    @Column(nullable = false)
    private Double gas_limit;
    @Column(nullable = false)
    private Double gas_used_by_txn;
    @Column(nullable = false)
    private String gas_price;
    @Column(nullable = false)
    private String actual_tx_cost_fee;
    @Column(nullable = false)
    private String nonce_position;
    @Column(nullable = false)
    private String input_data;
    @Column(nullable = false)
    private String private_note;
    @Column(nullable = false)
    private Timestamp insert_time;
    @Column(nullable = false)
    private Timestamp modify_time;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name="txs_pk_id")
    private ICO_Etherscan_IO_Blocks_Txs txs;

    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public String getTx_receipt_status() {
        return tx_receipt_status;
    }

    public void setTx_receipt_status(String tx_receipt_status) {
        this.tx_receipt_status = tx_receipt_status;
    }

    public Double getGas_limit() {
        return gas_limit;
    }

    public void setGas_limit(Double gas_limit) {
        this.gas_limit = gas_limit;
    }

    public Double getGas_used_by_txn() {
        return gas_used_by_txn;
    }

    public void setGas_used_by_txn(Double gas_used_by_txn) {
        this.gas_used_by_txn = gas_used_by_txn;
    }

    public String getGas_price() {
        return gas_price;
    }

    public void setGas_price(String gas_price) {
        this.gas_price = gas_price;
    }

    public String getActual_tx_cost_fee() {
        return actual_tx_cost_fee;
    }

    public void setActual_tx_cost_fee(String actual_tx_cost_fee) {
        this.actual_tx_cost_fee = actual_tx_cost_fee;
    }

    public String getNonce_position() {
        return nonce_position;
    }

    public void setNonce_position(String nonce_position) {
        this.nonce_position = nonce_position;
    }

    public String getInput_data() {
        return input_data;
    }

    public void setInput_data(String input_data) {
        this.input_data = input_data;
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

    public ICO_Etherscan_IO_Blocks_Txs getTxs() {
        return txs;
    }

    public void setTxs(ICO_Etherscan_IO_Blocks_Txs txs) {
        this.txs = txs;
    }

    public String getPrivate_note() {
        return private_note;
    }

    public void setPrivate_note(String private_note) {
        this.private_note = private_note;
    }
}
