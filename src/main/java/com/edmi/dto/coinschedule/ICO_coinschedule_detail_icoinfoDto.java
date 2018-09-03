package com.edmi.dto.coinschedule;

import com.edmi.entity.coinschedule.ICO_coinschedule_detail;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * coinschedule_detail_icoinfo 模型
 */
public class ICO_coinschedule_detail_icoinfoDto {


    private String ico_key;
    private String ico_value;

    public String getIco_key() {
        return ico_key;
    }

    public void setIco_key(String ico_key) {
        this.ico_key = ico_key;
    }

    public String getIco_value() {
        return ico_value;
    }

    public void setIco_value(String ico_value) {
        this.ico_value = ico_value;
    }
}
