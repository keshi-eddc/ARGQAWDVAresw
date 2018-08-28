package com.edmi.dto.icorating;

import com.edmi.entity.icorating.ICO_icorating_detail;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 金融 模型
 */
public class ICO_icorating_detail_block_fundsDto {


    private String fund;
    private String fund_url;
    private String status;
    private String aum;
    private String stratgey;


    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getFund_url() {
        return fund_url;
    }

    public void setFund_url(String fund_url) {
        this.fund_url = fund_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAum() {
        return aum;
    }

    public void setAum(String aum) {
        this.aum = aum;
    }

    public String getStratgey() {
        return stratgey;
    }

    public void setStratgey(String stratgey) {
        this.stratgey = stratgey;
    }
}
