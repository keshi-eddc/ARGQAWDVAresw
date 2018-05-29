package com.edmi.entity.feixiaohao;



import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class ICO_Feixiaohao_Exchange_Currencies_Page_Link {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long pk_id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String link;
    @Column(nullable = false)
    private String link_type;
    @Column(nullable = false)
    private Timestamp insert_time;
    @Column(nullable = false)
    private Timestamp modify_time;
    @Column(nullable = false)
    private Long serial_number;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name="currencies_pk_id")
    private ICO_Feixiaohao_Exchange_Currencies currencies;


    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public Long getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(Long serial_number) {
        this.serial_number = serial_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink_type() {
        return link_type;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
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

    public ICO_Feixiaohao_Exchange_Currencies getCurrencies() {
        return currencies;
    }

    public void setCurrencies(ICO_Feixiaohao_Exchange_Currencies currencies) {
        this.currencies = currencies;
    }
}
