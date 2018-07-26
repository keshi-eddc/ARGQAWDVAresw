package com.edmi.entity.feixiaohao;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class ICO_Feixiaohao_Exchange_Currencies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_id;
    @Column(nullable = false)
    private String currency_name;
    @Column(nullable = false)
    private String currency_link;
    @Column(nullable = false)
    private String details_status;
    @Column(nullable = false)
    private String party_details_status;
    @Column(nullable = false)
    private Timestamp insert_time;
    @Column(nullable = false)
    private Timestamp modify_time;

/*    @OneToMany(cascade=CascadeType.PERSIST,mappedBy="currencies")
    private List<ICO_Feixiaohao_Exchange_Currencies_Page_Link> pageLinks;

    @OneToOne(cascade=CascadeType.PERSIST,mappedBy = "currencies")
    private ICO_Feixiaohao_Exchange_Currenciesdtl currenciesdtl;*/



    public Long getPk_id() {
        return pk_id;
    }

    public void setPk_id(Long pk_id) {
        this.pk_id = pk_id;
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    public String getCurrency_link() {
        return currency_link;
    }

    public void setCurrency_link(String currency_link) {
        this.currency_link = currency_link;
    }

    public String getDetails_status() {
        return details_status;
    }

    public void setDetails_status(String details_status) {
        this.details_status = details_status;
    }

    public String getParty_details_status() {
        return party_details_status;
    }

    public void setParty_details_status(String party_details_status) {
        this.party_details_status = party_details_status;
    }

    public Timestamp getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(Timestamp insert_time) {
        this.insert_time = insert_time;
    }

/*    public List<ICO_Feixiaohao_Exchange_Currencies_Page_Link> getPageLinks() {
        return pageLinks;
    }

    public void setPageLinks(List<ICO_Feixiaohao_Exchange_Currencies_Page_Link> pageLinks) {
        this.pageLinks = pageLinks;
    }

    public ICO_Feixiaohao_Exchange_Currenciesdtl getCurrenciesdtl() {
        return currenciesdtl;
    }

    public void setCurrenciesdtl(ICO_Feixiaohao_Exchange_Currenciesdtl currenciesdtl) {
        this.currenciesdtl = currenciesdtl;
    }*/

    public Timestamp getModify_time() {
        return modify_time;
    }

    public void setModify_time(Timestamp modify_time) {
        this.modify_time = modify_time;
    }
}
