package com.edmi.dto.icorating;

import com.edmi.entity.icorating.ICO_icorating_detail;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 公司发展 模型
 */
public class ICO_icorating_detail_block_developmentDto {

    private String pre_launch;
    private String launch;
    private String custom;
    private String testnet;
    private String miannet;


    public String getPre_launch() {
        return pre_launch;
    }

    public void setPre_launch(String pre_launch) {
        this.pre_launch = pre_launch;
    }

    public String getLaunch() {
        return launch;
    }

    public void setLaunch(String launch) {
        this.launch = launch;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getTestnet() {
        return testnet;
    }

    public void setTestnet(String testnet) {
        this.testnet = testnet;
    }

    public String getMiannet() {
        return miannet;
    }

    public void setMiannet(String miannet) {
        this.miannet = miannet;
    }
}
