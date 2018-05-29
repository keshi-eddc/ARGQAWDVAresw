package com.edmi.utils.johnson;

import java.util.Map;

public class GenerateUrlFactory {

    public static  String getJohnson_sichuan_OrderDistributeGoodsDetails_URL(Map<String,String> params){
        StringBuffer url = new StringBuffer("https://jco.scbid.gov.cn:7075/Enterprise/RelationQuery/AreaDistributionByCompanySCLooks.aspx");
        return url.toString();
    }
}
