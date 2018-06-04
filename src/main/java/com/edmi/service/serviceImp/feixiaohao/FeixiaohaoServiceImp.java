package com.edmi.service.serviceImp.feixiaohao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.edmi.configs.StateCodeConfig;
import com.edmi.dao.feixiaohao.*;
import com.edmi.entity.feixiaohao.*;
import com.edmi.service.service.FeixiaohaoService;
import com.edmi.utils.http.HttpClientUtil;
import com.edmi.utils.http.exception.MethodNotSupportException;
import com.edmi.utils.http.request.Request;
import com.edmi.utils.http.request.RequestMethod;
import com.edmi.utils.http.response.Response;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@Service("feixiaohaoService")
public class FeixiaohaoServiceImp implements FeixiaohaoService {

    Logger log = Logger.getLogger(FeixiaohaoServiceImp.class);

    @Autowired
    private ICO_Feixiaohao_ExchangeRepository exchangeDao;
    @Autowired
    private ICO_Feixiaohao_Exchange_DetailsRepository exchange_detailsRepository;
    @Autowired
    private ICO_Feixiaohao_Exchange_Counter_Party_DetailsRepository counter_party_detailsRepository;
    @Autowired
    private ICO_Feixiaohao_Exchange_CurrenciesRepository currenciesDao;
    @Autowired
    private ICO_Feixiaohao_Exchange_CurrenciesdtlRepository currenciesdtlDao;
    @Autowired
    private ICO_Feixiaohao_Exchange_Currencies_PageLinkRepository pageLinkDao;


    @Override
    public void getICO_Feixiaohao_Exchange() throws MethodNotSupportException {
        String url = "https://www.feixiaohao.com/exchange";
        Request request = new Request(url, RequestMethod.GET);
        Response response = HttpClientUtil.doRequest(request);
        Map<String,String> stateCodes = StateCodeConfig.getStateCode();
        int code = response.getCode(); //response code
        if(200 ==code){
            String content = response.getResponseText(); //response text
            Document doc = Jsoup.parse(content);
            Elements plantList = doc.getElementsByClass("exchange-table");//获取页面数据
            Elements pageList = doc.getElementsByAttributeValue("class","pageList");
            int page_total = 1;
            int current_page = 1;
            if(null!=pageList&&pageList.size()>0){//获取总页数
                Element page = pageList.first();
                Elements page_links = page.getElementsByClass("btn-white");
                if(page_links!=null&&page_links.size()>0){
                    for(Element page_link:page_links){
                        String page_number = page_link.text().trim();
                        if(NumberUtils.isDigits(page_number)){
                            int number = NumberUtils.createInteger(page_number);
                            if(number>page_total){
                                page_total = number;
                            }
                        }
                    }
                }
            }
            if(null!=plantList&&plantList.size()>0){
                Element plant = plantList.first();
                Elements exchanges = plant.getElementsByTag("li");//数据列表总集

                while (current_page<page_total){
                    current_page+=1;
                    log.info("正在获取第"+current_page+"-"+page_total+"页");
                    url = "https://www.feixiaohao.com/exchange/list_"+String.valueOf(current_page)+".html";
                    request = new Request(url, RequestMethod.GET);
                    response = HttpClientUtil.doRequest(request);
                    code = response.getCode(); //response code
                    if(200 ==code) {
                        content = response.getResponseText(); //response text
                        doc = Jsoup.parse(content);
                        Elements plantList_next = doc.getElementsByClass("plantList");//获取页面数据
                        if(null!=plantList_next&&plantList_next.size()>0) {
                            Element plant_next = plantList_next.first();
                            Elements exchanges_next = plant_next.getElementsByTag("li");
                            exchanges.addAll(exchanges_next);
                            try {
                                Thread.sleep(1000*5);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
                        exchanges.clear();
                        log.info("获取本页列表出错code："+code+",page:"+current_page);
                        return;
                    }
                }
                if(null!=exchanges&&exchanges.size()>0){//解析所有页数总集的数据
                    List<ICO_Feixiaohao_Exchange> ico_feixiaohao_exchanges = new ArrayList<ICO_Feixiaohao_Exchange>();
                    long serial_number = Calendar.getInstance().getTime().getTime();
                    for(Element exchange:exchanges){
                        Elements infos = exchange.getElementsByClass("info");
                        ICO_Feixiaohao_Exchange ico_feixiaohao_exchange = new ICO_Feixiaohao_Exchange();
                        if(null!=infos&&infos.size()>0){
                            Element info = infos.first();
                            Elements tits = info.getElementsByClass("tit");//名字
                            Elements dess = info.getElementsByClass("des");//描述
                            Elements detals = info.getElementsByClass("detal");//描述

                            if(null!=tits&&tits.size()>0){
                               Element tit = tits.first();
                               Elements tit_a = tit.getElementsByTag("a");
                               Elements stars = tit.getElementsByClass("star");
                               if(null!=tit_a&&tit_a.size()>0){
                                   String name = tit_a.first().text();
                                   String name_link = "https://www.feixiaohao.com"+tit_a.attr("href");
                                   ico_feixiaohao_exchange.setName(StringUtils.defaultIfEmpty(name,""));
                                   ico_feixiaohao_exchange.setName_link(StringUtils.defaultIfEmpty(name_link,""));
                               }
                               if(null!=stars&&stars.size()>0){
                                   String star = stars.first().attr("class");
                                   star = StringUtils.substringAfterLast(star,"star");
                                   if(NumberUtils.isDigits(star)){
                                       ico_feixiaohao_exchange.setStar(NumberUtils.createInteger(star));
                                   }

                               }
                            }
                            if(null!=dess&&dess.size()>0){
                                String des = dess.first().text();
                                ico_feixiaohao_exchange.setDes(StringUtils.defaultIfEmpty(des,""));
                                if(StringUtils.isNotEmpty(des)){//提取公司成立时间
                                    String year = StringUtils.substring(des, StringUtils.indexOf(des,"年")-4, StringUtils.indexOf(des,"年"));
                                    int year_index = StringUtils.indexOf(des,"年");
                                    if(NumberUtils.isDigits(year)){
                                        String date_str = StringUtils.substring(des,year_index-4,year_index+6);
                                        if(StringUtils.contains(date_str,"月")&& StringUtils.contains(date_str,"日")){
                                            date_str = StringUtils.substringBeforeLast(date_str,"日")+"日";
                                        }else if (StringUtils.contains(date_str,"月")){
                                            date_str = StringUtils.substringBeforeLast(date_str,"月")+"月";
                                        }else{
                                            date_str = year+"年";
                                        }
                                        ico_feixiaohao_exchange.setFounding_time(date_str);
                                    }
                                }
                            }
                            if(null!=detals&&detals.size()>0){
                                String detal = detals.first().text();
                                String counter_party = StringUtils.substringBetween(detal,"交易对:","国家:").trim();
                                String state = StringUtils.substringBetween(detal,"国家:","成交额").trim();
                                String transaction_amount = StringUtils.substringBetween(detal,"成交额(24h):","支持：");
                                if(NumberUtils.isDigits(counter_party)){
                                    ico_feixiaohao_exchange.setCounter_party(NumberUtils.createInteger(counter_party));
                                }
                                if(StringUtils.isNotEmpty(state)){
                                    if(StringUtils.contains(state,"中国")){
                                        state = "中国";
                                    }else if(StringUtils.contains(state,"蒙古")){
                                        state = "蒙古";
                                    }else if(StringUtils.equalsIgnoreCase("俄罗斯",state)){
                                        state = "俄罗斯联邦";
                                    }else if(StringUtils.equalsIgnoreCase("澳洲",state)){
                                        state = "澳大利亚";
                                    }
                                    if(null!=stateCodes.get(state)){
                                        ico_feixiaohao_exchange.setState_code(stateCodes.get(state));
                                    }
                                }
                                ico_feixiaohao_exchange.setStatus("ini");
                                ico_feixiaohao_exchange.setSerial_number(serial_number);
                                ico_feixiaohao_exchange.setState(StringUtils.defaultIfEmpty(state,""));
                                ico_feixiaohao_exchange.setTransaction_amount(StringUtils.defaultIfEmpty(transaction_amount,""));
                                ico_feixiaohao_exchange.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                ico_feixiaohao_exchange.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            }
                        }
                        ico_feixiaohao_exchanges.add(ico_feixiaohao_exchange);
                    }
                    ico_feixiaohao_exchanges = exchangeDao.saveAll(ico_feixiaohao_exchanges);
                    if(null!=ico_feixiaohao_exchanges){
                        log.info("exchange列表抓取成功，共计："+exchanges.size());
                    }else{
                        log.info("exchange列表抓取失败!");
                    }
                }
            }
        }
    }

    @Override
    public void getICO_Feixiaohao_Exchange_Details(ICO_Feixiaohao_Exchange exchange) throws MethodNotSupportException {
        String url = exchange.getName_link();
        Request request = new Request(url, RequestMethod.GET);
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode(); //response code
        if(200 ==code) {
            String content = response.getResponseText(); //response text
            Document doc = Jsoup.parse(content);
            Elements tables = doc.getElementsByAttributeValue("class", "table noBg");
            if(null!=tables&&tables.size()>0){
                Element table = tables.first();
                Elements ths = table.getElementsByTag("thead").first().getElementsByTag("th");//获取表头数据
                Map<Integer,String> ths_map = new HashMap<Integer,String>();
                for(int i=0;i<ths.size();i++){
                    Element th = ths.get(i);
                    if(StringUtils.equalsIgnoreCase("名称",th.text().trim())){
                        ths_map.put(i,"名称");
                    }else if(StringUtils.equalsIgnoreCase("交易对",th.text().trim())){
                        ths_map.put(i,"交易对");
                    }else if(StringUtils.equalsIgnoreCase("价格",th.text().trim())){
                        ths_map.put(i,"价格");
                    }else if(StringUtils.equalsIgnoreCase("成交量",th.text().trim())){
                        ths_map.put(i,"成交量");
                    }else if(StringUtils.equalsIgnoreCase("成交额",th.text().trim())){
                        ths_map.put(i,"成交额");
                    }else if(StringUtils.equalsIgnoreCase("占比",th.text().trim())){
                        ths_map.put(i,"占比");
                    }else if(StringUtils.equalsIgnoreCase("更新时间",th.text().trim())){
                        ths_map.put(i,"更新时间");
                    }
                }
                Elements trs = table.getElementsByTag("tbody").first().getElementsByTag("tr");//获取数据列表
                if(null!=trs&&trs.size()>0){//解析数据
                    List<ICO_Feixiaohao_Exchange_Details> ico_feixiaohao_exchange_details_list = new ArrayList<ICO_Feixiaohao_Exchange_Details>();
                    for(Element tr:trs){
                        ICO_Feixiaohao_Exchange_Details ico_feixiaohao_exchange_details = new ICO_Feixiaohao_Exchange_Details();
                        Elements tds = tr.getElementsByTag("td");
                        for(Map.Entry<Integer, String> entry:ths_map.entrySet()){
                            int key = entry.getKey();
                            String value = entry.getValue();

                            Element column = tds.get(key);
                            if(value.equals("名称")){
                                Elements name_links = column.getElementsByTag("a");
                                ico_feixiaohao_exchange_details.setName(StringUtils.defaultIfEmpty(column.text(),""));
                                if(null!=name_links&&name_links.size()>0){
                                    Element name_link = name_links.first();
                                    String link = "https://www.feixiaohao.com/"+ name_link.attr("href");
                                    ico_feixiaohao_exchange_details.setName_link(link);
                                }
                            }else if(value.equals("交易对")){
                                ico_feixiaohao_exchange_details.setCounter_party(StringUtils.defaultIfEmpty(column.text(),""));
                            }else if(value.equals("价格")){
                                String data_usd = StringUtils.replace(column.attr("data-usd"),"?","");
                                String data_cny = StringUtils.replace(column.attr("data-cny"),"?","");
                                String data_btc = StringUtils.replace(column.attr("data-btc"),"?","");
                                String data_native = StringUtils.replace(column.attr("data-native"),"?","");
                                ico_feixiaohao_exchange_details.setPrice_usd(Double.valueOf(StringUtils.defaultIfEmpty(data_usd,"0.0d").trim()));
                                ico_feixiaohao_exchange_details.setPrice_cny(Double.valueOf(StringUtils.defaultIfEmpty(data_cny,"0.0d").trim()));
                                ico_feixiaohao_exchange_details.setPrice_btc(Double.valueOf(StringUtils.defaultIfEmpty(data_btc,"0.0d").trim()));
                                ico_feixiaohao_exchange_details.setPrice_native(Double.valueOf(StringUtils.defaultIfEmpty(data_native,"0.0d").trim()));
                            }else if(value.equals("成交量")){
                                ico_feixiaohao_exchange_details.setTransaction_num(StringUtils.defaultIfEmpty(column.text(),""));
                            }else if(value.equals("成交额")){
                                String data_usd = StringUtils.replace(column.attr("data-usd"),"?","");
                                String data_cny = StringUtils.replace(column.attr("data-cny"),"?","");
                                String data_btc = StringUtils.replace(column.attr("data-btc"),"?","");
                                String data_native = StringUtils.replace(column.attr("data-native"),"?","");
                                ico_feixiaohao_exchange_details.setVolume_usd(Double.valueOf(StringUtils.defaultIfEmpty(data_usd,"0.0d").trim()));
                                ico_feixiaohao_exchange_details.setVolume_cny(Double.valueOf(StringUtils.defaultIfEmpty(data_cny,"0.0d").trim()));
                                ico_feixiaohao_exchange_details.setVolume_btc(Double.valueOf(StringUtils.defaultIfEmpty(data_btc,"0.0d").trim()));
                                ico_feixiaohao_exchange_details.setVolume_native(Double.valueOf(StringUtils.defaultIfEmpty(data_native,"0.0d").trim()));
                            }else if(value.equals("占比")){
                                ico_feixiaohao_exchange_details.setPer(StringUtils.defaultIfEmpty(column.text(),""));
                            }else if(value.equals("更新时间")){
                                ico_feixiaohao_exchange_details.setLast_update(StringUtils.defaultIfEmpty(column.text(),""));
                            }
                        }
                        ico_feixiaohao_exchange_details.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        ico_feixiaohao_exchange_details.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        ico_feixiaohao_exchange_details.setIco_feixiaohao_exchange(exchange);
                        ico_feixiaohao_exchange_details_list.add(ico_feixiaohao_exchange_details);
                    }
                    ico_feixiaohao_exchange_details_list = exchange_detailsRepository.saveAll(ico_feixiaohao_exchange_details_list);
                    if(null!=ico_feixiaohao_exchange_details_list){
                        log.info("exchange详情列表抓取成功exchange_pk_id:"+exchange.getPk_id());
                        exchange.setStatus("200");
                        exchangeDao.save(exchange);
                    }else{
                        log.info("exchange详情列表抓取失败exchange_pk_id:"+exchange.getPk_id());
                    }
                }else{
                    log.info("exchange详情列表抓取成功exchange_pk_id:"+exchange.getPk_id());
                    exchange.setStatus("200");
                    exchangeDao.save(exchange);
                }
            }
            Elements marketinfos = doc.getElementsByClass("marketinfo");//获取官网地址
            if(null!=marketinfos&&marketinfos.size()>0){
                Element marketinfo = marketinfos.first();
                Elements infos = marketinfo.getElementsByClass("info");
                if(null!=infos&&infos.size()>0){
                    Element info = infos.first();
                    Elements webs = info.getElementsByClass("web");
                    if(null!=webs&&webs.size()>0){
                        Element web = webs.first();
                        Elements websites = web.getElementsContainingOwnText("官网地址");
                        if(null!=websites&&websites.size()>0) {
                            String web_text = websites.first().text();
                            web_text = StringUtils.substringAfterLast(web_text,"官网地址：");
                            exchange.setWebsite(web_text);
                            exchange = exchangeDao.save(exchange);
                            if (null != exchange) {
                                log.info("exchange官网地址更新成功：" + exchange.getPk_id());
                            } else {
                                log.info("exchange官网地址更新失败：" + exchange.getPk_id());
                            }
                        }

                    }
                }
            }
        }
    }

    @Override
    @Async("myTaskAsyncPool")
    public void getICO_Feixiaohao_Exchange_Counter_Party_Details(String link) throws MethodNotSupportException {

        String counter_party_link = "https://api.feixiaohao.com/coinhisdata/"+StringUtils.substringBetween(link,"currencies/","/") +"/";
        String org_counter_party_link = counter_party_link;
        /*获取该交易以对最后更新的时间*/
        Timestamp lastUpdate = counter_party_detailsRepository.getICO_Feixiaohao_Exchange_LastUpdateTime(counter_party_link);
        if(null!=lastUpdate){
            long last_time = lastUpdate.getTime();
            long now_time = Calendar.getInstance().getTime().getTime();
            counter_party_link = counter_party_link + last_time+"/"+now_time;
        }
        Request request = new Request(counter_party_link, RequestMethod.GET);
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode(); //response code
        if(200 ==code) {
            String content = response.getResponseText(); //response text
            JSONObject datas = JSONObject.parseObject(content);
            if(datas.containsKey("price_usd")&&datas.containsKey("market_cap_by_available_supply")&&datas.containsKey("vol_usd")&&datas.containsKey("price_btc")){
                JSONArray price_usds = datas.getJSONArray("price_usd");
                JSONArray supplys = datas.getJSONArray("market_cap_by_available_supply");
                JSONArray vol_usds = datas.getJSONArray("vol_usd");
                JSONArray price_btcs = datas.getJSONArray("price_btc");
                if(price_usds.size()==supplys.size()&&price_usds.size()==vol_usds.size()&&price_usds.size()==price_btcs.size()){
                    List<ICO_Feixiaohao_Exchange_Counter_Party_Details> counter_party_details= new ArrayList<ICO_Feixiaohao_Exchange_Counter_Party_Details>();
                    for(int i=0;i<price_usds.size();i++){
                        ICO_Feixiaohao_Exchange_Counter_Party_Details counter_party_detail = new ICO_Feixiaohao_Exchange_Counter_Party_Details();
                        JSONArray price_usd = price_usds.getJSONArray(i);
                        JSONArray supply = supplys.getJSONArray(i);
                        JSONArray vol_usd = vol_usds.getJSONArray(i);
                        JSONArray price_btc = price_btcs.getJSONArray(i);

                        counter_party_detail.setPrice_usd(price_usd.getDouble(1));
                        counter_party_detail.setPrice_usd_time(price_usd.getTimestamp(0));
                        counter_party_detail.setMarket_cap_by_available_supply(supply.getDouble(1));
                        counter_party_detail.setSupply_time(supply.getTimestamp(0));
                        counter_party_detail.setVol_usd(vol_usd.getDouble(1));
                        counter_party_detail.setVol_usd_time(vol_usd.getTimestamp(0));
                        counter_party_detail.setPrice_btc(price_btc.getDouble(1));
                        counter_party_detail.setPrice_btc_time(price_btc.getTimestamp(0));

                        counter_party_detail.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        counter_party_detail.setExchange_details_link(org_counter_party_link);

                        counter_party_details.add(counter_party_detail);
                    }
                    counter_party_details = counter_party_detailsRepository.saveAll(counter_party_details);
                    if(null!=counter_party_details){
                        log.info("交易对更新成功："+counter_party_link);
                    }else{
                        log.info("交易对更新失败："+counter_party_link);
                    }
                    try {
                        Thread.sleep(3*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    log.info("返回的交易对信息不对称："+counter_party_link);
                }
            }else{
                log.info("该交易对返回信息缺失："+counter_party_link);
            }
        }else{
            log.info("交易对信息获取失败，code="+code+",交易对："+counter_party_link);
        }
    }
    public void importICO_Feixiaohao_Exchange_Currencies(){
        log.info("<---开始从ICO_Feixiaohao_Exchange_Details提取交易币信息--->");
        List<String> links = exchange_detailsRepository.getICO_Feixiaohao_ExchangeDistinctByName_link();
        List<ICO_Feixiaohao_Exchange_Currencies> currencies = new ArrayList<ICO_Feixiaohao_Exchange_Currencies>();
        for(String link :links){
            ICO_Feixiaohao_Exchange_Currencies currency = new ICO_Feixiaohao_Exchange_Currencies();
            currency.setCurrency_link(link);
            currency.setDetails_status("ini");
            currency.setParty_details_status("ini");
            currency.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            currencies.add(currency);
        }
        currencies = currenciesDao.saveAll(currencies);
        if(null!=currencies&&currencies.size()>0){
            log.info("交易币导入成功，共计："+currencies.size());
        }else{
            log.info("交易币导入失败，共计："+currencies.size());
        }
        log.info("<---ICO_Feixiaohao_Exchange_Details提取交易币信息完毕--->");
    }
    @Async("myTaskAsyncPool")
    public void getICO_Feixiaohao_Exchange_Currenciesdtl(ICO_Feixiaohao_Exchange_Currencies currency) throws MethodNotSupportException {
        Long serial_num = Calendar.getInstance().getTime().getTime();
        log.info("正在抓取交易币信息pk_id="+currency.getPk_id());
        Request request = new Request(currency.getCurrency_link(), RequestMethod.GET);
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode();
        if(200 ==code) {
            Document doc = Jsoup.parse(response.getResponseText());
            Element baseInfo = doc.getElementById("baseInfo");
            Elements secondParks = baseInfo.getElementsByClass("secondPark");
            if(null!=secondParks&&secondParks.size()>0){
                Element secondPark = secondParks.first();
                ICO_Feixiaohao_Exchange_Currenciesdtl currenciesdtl = new ICO_Feixiaohao_Exchange_Currenciesdtl();
                Elements ens = secondPark.getElementsContainingOwnText("英文名");
                Elements cns = secondPark.getElementsContainingOwnText("中文名");
                Elements exchange_numbers = secondPark.getElementsContainingOwnText("上架交易所");
                Elements release_times = secondPark.getElementsContainingOwnText("发行时间");

                Elements is_tokens = secondPark.getElementsContainingOwnText("是否代币");
                Elements tokens_platforms = secondPark.getElementsContainingOwnText("代币平台");
                Elements raise_prices = secondPark.getElementsContainingOwnText("众筹价格");


                Elements websites = secondPark.getElementsContainingOwnText("网站");
                Elements blocksites = secondPark.getElementsContainingOwnText("区块站");
                Elements conceptions = secondPark.getElementsContainingOwnText("相关概念");
                Elements white_paper_links = secondPark.getElementsContainingOwnText("白皮书");

                if(null!=ens&&ens.size()>0){//获取英文名
                    Element en = ens.first();
                    Element en_value = en.nextElementSibling();
                    currenciesdtl.setEn(StringUtils.defaultIfEmpty(en_value.text(),""));
                }
                if(null!=cns&&cns.size()>0){//获取中文名
                    Element cn = cns.first();
                    Element cn_value = cn.nextElementSibling();
                    currenciesdtl.setCn(StringUtils.defaultIfEmpty(cn_value.text(),""));
                }
                if(null!=exchange_numbers&&exchange_numbers.size()>0){//上架交易所
                    Element exchange_number = exchange_numbers.first();
                    Element exchange_number_value = exchange_number.nextElementSibling();
                    currenciesdtl.setExchange_number(StringUtils.defaultIfEmpty(exchange_number_value.text(),""));
                }
                if(null!=release_times&&release_times.size()>0){//发行时间
                    Element release_time = release_times.first();
                    Element release_time_value = release_time.nextElementSibling();
                    if(StringUtils.isNotEmpty(release_time_value.text().trim())){
                        try {
                            Date date = DateUtils.parseDate(release_time_value.text().trim(), new String[]{"yyyy-MM-dd"});
                            currenciesdtl.setRelease_time(new Timestamp(date.getTime()));
                        } catch (ParseException e) {
                            log.info("不合法的日期："+e.getMessage());
                        }
                    }
                }
                if(null!=is_tokens&&is_tokens.size()>0){//是否代币
                    Element is_token = is_tokens.first();
                    Element is_token_value = is_token.nextElementSibling();
                    currenciesdtl.setIs_token(StringUtils.defaultIfEmpty(is_token_value.text(),""));
                }
                if(null!=tokens_platforms&&tokens_platforms.size()>0){//是否代币
                    Element tokens_platform = tokens_platforms.first();
                    Element tokens_platform_value = tokens_platform.nextElementSibling();
                    currenciesdtl.setTokens_platform(StringUtils.defaultIfEmpty(tokens_platform_value.text(),""));
                }
                if(null!=raise_prices&&raise_prices.size()>0){//众筹价格
                    Element raise_price = raise_prices.first();
                    Element raise_price_value = raise_price.nextElementSibling();
                    currenciesdtl.setRaise_price(StringUtils.defaultIfEmpty(raise_price_value.text(),""));
                }

                List<ICO_Feixiaohao_Exchange_Currencies_Page_Link> pageLinks = new ArrayList<ICO_Feixiaohao_Exchange_Currencies_Page_Link>();
                if(null!=websites&&websites.size()>0){//网站
                    Element website = websites.first();
                    Element website_value = website.nextElementSibling();
                    Elements links = website_value.getElementsByTag("a");
                    if(null!=links&&links.size()>0){
                        for(Element link:links){//获取所有的网站链接
                            String name = link.text();
                            String href =  link.attr("href");
                            if(StringUtils.startsWith(href,"//")){
                                href = StringUtils.replaceOnce(href,"//","https://");
                            }

                            ICO_Feixiaohao_Exchange_Currencies_Page_Link pageLink = new ICO_Feixiaohao_Exchange_Currencies_Page_Link();
                            pageLink.setName(StringUtils.defaultIfEmpty(name,""));
                            pageLink.setLink(StringUtils.defaultIfEmpty(href,""));
                            pageLink.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            pageLink.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            pageLink.setCurrencies(currency);
                            pageLink.setLink_type("website");
                            pageLink.setSerial_number(serial_num);

                            pageLinks.add(pageLink);
                        }
                    }
                 }
                if(null!=blocksites&&blocksites.size()>0){//区块站
                     Element blocksite = blocksites.first();
                     Element blocksite_value = blocksite.nextElementSibling();
                     Elements links = blocksite_value.getElementsByTag("a");
                     if(null!=links&&links.size()>0){
                         for(Element link:links){//获取所有的白皮书链接
                             String name = link.text();
                             String href = link.attr("href");
                             if(StringUtils.startsWith(href,"//")){
                                 href = StringUtils.replaceOnce(href,"//","https://");
                             }
                             ICO_Feixiaohao_Exchange_Currencies_Page_Link pageLink = new ICO_Feixiaohao_Exchange_Currencies_Page_Link();
                             pageLink.setName(StringUtils.defaultIfEmpty(name,""));
                             pageLink.setLink(StringUtils.defaultIfEmpty(href,""));
                             pageLink.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                             pageLink.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                             pageLink.setCurrencies(currency);
                             pageLink.setLink_type("blocksite");
                             pageLink.setSerial_number(serial_num);

                             pageLinks.add(pageLink);
                         }
                     }
                 }
                if(null!=conceptions&&conceptions.size()>0){//相关概念
                    Element conception = conceptions.first();
                    Element conception_value = conception.nextElementSibling();
                    Elements links = conception_value.getElementsByTag("a");
                    if(null!=links&&links.size()>0){
                        for(Element link:links){//获取所有相关概念
                            String name = link.text();
                            String href = "https://www.feixiaohao.com"+link.attr("href");

                            ICO_Feixiaohao_Exchange_Currencies_Page_Link pageLink = new ICO_Feixiaohao_Exchange_Currencies_Page_Link();
                            pageLink.setName(StringUtils.defaultIfEmpty(name,""));
                            pageLink.setLink(StringUtils.defaultIfEmpty(href,""));
                            pageLink.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            pageLink.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            pageLink.setCurrencies(currency);
                            pageLink.setLink_type("conception");
                            pageLink.setSerial_number(serial_num);

                            pageLinks.add(pageLink);
                        }
                    }
                }
                if(null!=white_paper_links&&white_paper_links.size()>0){//白皮书
                    Element white_paper_link = white_paper_links.first();
                    Element white_paper_link_value = white_paper_link.nextElementSibling();
                    Elements links = white_paper_link_value.getElementsByTag("a");
                    if(null!=links&&links.size()>0){
                        for(Element link:links){//获取所有的白皮书链接
                            String name = link.text();
                            String href = link.attr("href");
                            if(StringUtils.startsWith(href,"//")){
                                href = StringUtils.replaceOnce(href,"//","https://");
                            }
                            ICO_Feixiaohao_Exchange_Currencies_Page_Link pageLink = new ICO_Feixiaohao_Exchange_Currencies_Page_Link();
                            pageLink.setName(StringUtils.defaultIfEmpty(name,""));
                            pageLink.setLink(StringUtils.defaultIfEmpty(href,""));
                            pageLink.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            pageLink.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            pageLink.setCurrencies(currency);
                            pageLink.setLink_type("white_paper");
                            pageLink.setSerial_number(serial_num);

                            pageLinks.add(pageLink);
                        }
                    }
                }
                currenciesdtl.setSerial_number(serial_num);
                currenciesdtl.setCurrencies(currency);
                currenciesdtl.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                currenciesdtl.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));


                currency.setDetails_status(String.valueOf(code));
                currency.setCurrency_name(currenciesdtl.getEn());
                currency.setCurrenciesdtl(currenciesdtl);
                currency.setPageLinks(pageLinks);
                currency.setCurrenciesdtl(currenciesdtl);
                currency.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                currenciesDao.save(currency);
                /*pageLinkDao.saveAll(pageLinks);
                currenciesdtlDao.save(currenciesdtl).getCurrencies();*/

                log.info("交易币信息保存完毕pk_id："+currency.getPk_id());
            }else{
                log.info("页面找不到交易币信息pk_id："+currency.getPk_id());
            }
        }else{
            log.info("请求错误，交易币信息pk_id："+currency.getPk_id());
        }

    }
}
