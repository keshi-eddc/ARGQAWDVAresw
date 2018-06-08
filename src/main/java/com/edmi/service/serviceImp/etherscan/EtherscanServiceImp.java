package com.edmi.service.serviceImp.etherscan;

import com.edmi.dao.etherscan.*;
import com.edmi.entity.etherscan.*;
import com.edmi.service.service.EtherscanService;
import com.edmi.utils.http.HttpClientUtil;
import com.edmi.utils.http.exception.MethodNotSupportException;
import com.edmi.utils.http.request.Request;
import com.edmi.utils.http.request.RequestMethod;
import com.edmi.utils.http.response.Response;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.RandomUtils;
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

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("etherscanServiceImp")
public class EtherscanServiceImp implements EtherscanService {

    Logger log = Logger.getLogger(EtherscanServiceImp.class);

    @Autowired
    private ICO_Etherscan_IO_BlocksRepository blocksDao;

    @Autowired
    private ICO_Etherscan_IO_Blocks_ForkedRepository blocks_forkedDao;

    @Autowired
    private ICO_Etherscan_IO_Blocks_Txs_Page_ListRepository txs_page_listDao;

    @Autowired
    private ICO_Etherscan_IO_Blocks_Forked_Txs_Page_ListRepository forked_txs_page_listDao;

    @Autowired
    private ICO_Etherscan_IO_Blocks_TxsRepository txsDao;

    @Autowired
    private ICO_Etherscan_IO_Blocks_Forked_TxsRepository forked_txsDao;


    @Override
    public List<String> getICO_Etherscan_IO_Blocks_TotalPageLinks() throws MethodNotSupportException {
        /*按照每页100条获取总的页数*/
        String url = "https://etherscan.io/blocks?ps=100&p=1";
        Request request = new Request(url, RequestMethod.GET);
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode(); //response code
        List<String> totalPageLinks = new ArrayList<String>();
        if(200 ==code){
            String content = response.getResponseText(); //response text
            Document doc = Jsoup.parse(content);
            Elements lasts = doc.getElementsContainingOwnText("Last");
            if(null!=lasts&&lasts.size()>0){
                String last_link = lasts.first().attr("href");
                String pages = StringUtils.substringAfterLast(last_link,"p=");
                if(NumberUtils.isDigits(pages)){
                    int totalPages = Integer.valueOf(pages);
                    log.info("blocks共计获取"+totalPages+"页");
                    /*https://etherscan.io/blocks?ps=100&p=2*/
                    for(int i=1;i<=totalPages;i++){
                        String link = "https://etherscan.io/blocks?ps=100&p="+i;
                        totalPageLinks.add(link);
                    }
                }
            }
        }else{
            log.info("获取总页数失败，code = "+code);
        }
        return totalPageLinks;
    }

    @Override
    @Async("myTaskAsyncPool")
    public void getICO_Etherscan_IO_Blocks(String link, Long serial,int page_total) throws MethodNotSupportException {
        int times = 1;
        boolean isLoop = true;
        while(isLoop&&times<=5) {
            log.info("正在第"+times+"次获取block信息，link：" + link + ",共" + page_total + "页");
            Request request = new Request(link, RequestMethod.GET);
            Response response = HttpClientUtil.doRequest(request);
            int code = response.getCode(); //response code
            if (200 == code) {
                String content = response.getResponseText(); //response text
                Document doc = Jsoup.parse(content);
                Elements tables_div = doc.getElementsByClass("table-responsive");
                if (null != tables_div && tables_div.size() > 0) {
                    Element table_div = tables_div.first();
                    Elements tables = table_div.getElementsByTag("table");
                    if (null != tables && tables.size() > 0) {
                        Element table = tables.first();
                        Elements ths = table.getElementsByTag("thead").first().getElementsByTag("th");//获取表头数据
                        Map<Integer, String> ths_map = new HashMap<Integer, String>();
                        for (int i = 0; i < ths.size(); i++) {
                            Element th = ths.get(i);
                            if (StringUtils.equalsIgnoreCase("Height", th.text().trim())) {
                                ths_map.put(i, "Height");
                            } else if (StringUtils.equalsIgnoreCase("Age", th.text().trim())) {
                                ths_map.put(i, "Age");
                            } else if (StringUtils.equalsIgnoreCase("txn", th.text().trim())) {
                                ths_map.put(i, "txn");
                            } else if (StringUtils.equalsIgnoreCase("Uncles", th.text().trim())) {
                                ths_map.put(i, "Uncles");
                            } else if (StringUtils.equalsIgnoreCase("Miner", th.text().trim())) {
                                ths_map.put(i, "Miner");
                            } else if (StringUtils.equalsIgnoreCase("GasUsed", th.text().trim())) {
                                ths_map.put(i, "GasUsed");
                            } else if (StringUtils.equalsIgnoreCase("GasLimit", th.text().trim())) {
                                ths_map.put(i, "GasLimit");
                            } else if (StringUtils.equalsIgnoreCase("Avg.GasPrice", th.text().trim())) {
                                ths_map.put(i, "Avg.GasPrice");
                            } else if (StringUtils.equalsIgnoreCase("Reward", th.text().trim())) {
                                ths_map.put(i, "Reward");
                            }
                        }
                        Elements trs = table.getElementsByTag("tbody").first().getElementsByTag("tr");//获取数据列表
                        if (null != trs && trs.size() > 0) {//解析数据
                            List<ICO_Etherscan_IO_Blocks> blocks = new ArrayList<ICO_Etherscan_IO_Blocks>();
                            for (Element tr : trs) {
                                ICO_Etherscan_IO_Blocks block = new ICO_Etherscan_IO_Blocks();
                                Elements tds = tr.getElementsByTag("td");
                                for (Map.Entry<Integer, String> entry : ths_map.entrySet()) {
                                    int key = entry.getKey();
                                    String value = entry.getValue();

                                    Element column = tds.get(key);
                                    if (value.equals("Height")) {
                                        Elements height_links = column.getElementsByTag("a");
                                        if (null != height_links && height_links.size() > 0) {
                                            Element height_link = height_links.first();
                                            String height = StringUtils.defaultIfEmpty(height_link.text(), "0");
                                            if (NumberUtils.isDigits(height)) {
                                                block.setHeight(NumberUtils.toLong(height));
                                            }
                                        }
                                    } else if (value.equals("Age")) {
                                        Format sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Format sdf = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss a", Locale.ENGLISH);
                                        Elements ages = column.getElementsByTag("span");
                                        if (null != ages && ages.size() > 0) {
                                            String age = ages.first().attr("title");
                                            try {
                                                age = sdf1.format(sdf.parseObject(age));
                                                Date age_date = DateUtils.parseDate(age, new String[]{"yyyy-MM-dd HH:mm:ss"});
                                                block.setAge(new Timestamp(age_date.getTime()));
                                            } catch (ParseException e) {
                                                log.info("日期转换错误：" + age);
                                            }
                                        }
                                    } else if (value.equals("txn")) {
                                        Elements txns = column.getElementsByTag("a");
                                        if (null != txns && txns.size() > 0) {
                                            Element txn = txns.first();
                                            String txn_text = StringUtils.defaultIfEmpty(txn.text(), "0");
                                            if (NumberUtils.isDigits(txn_text)) {
                                                block.setTxn(NumberUtils.toInt(txn_text));
                                            }
                                        }
                                    } else if (value.equals("Uncles")) {
                                        String uncles = column.text();
                                        if (NumberUtils.isDigits(uncles)) {
                                            block.setUncles(NumberUtils.toInt(uncles));
                                        }
                                    } else if (value.equals("Miner")) {
                                        Elements miners = column.getElementsByTag("a");
                                        if (null != miners && miners.size() > 0) {
                                            String miner = miners.first().attr("href");
                                            miner = StringUtils.substringAfterLast(miner, "address/");
                                            block.setMiner(StringUtils.defaultIfEmpty(miner, ""));
                                        }
                                    } else if (value.equals("GasUsed")) {
                                        String gasUsed = column.text();
                                        String gasUsed_value = StringUtils.substringBeforeLast(gasUsed, "(").trim();
                                        String gasUsed_percent = StringUtils.substringBetween(gasUsed, "(", ")").trim();
                                        if (NumberUtils.isDigits(gasUsed_value)) {
                                            block.setGasUsed(NumberUtils.toDouble(gasUsed_value));
                                        }
                                        NumberFormat nbf = NumberFormat.getPercentInstance();
                                        try {
                                            Number number = nbf.parse(gasUsed_percent);
                                            block.setGasUsedPercent(number.floatValue());
                                        } catch (ParseException e) {
                                            log.info("百分比转小数失败：" + gasUsed_percent);
                                        }
                                    } else if (value.equals("GasLimit")) {
                                        String gasLimit = column.text();
                                        if (NumberUtils.isDigits(gasLimit)) {
                                            block.setGasLimit(NumberUtils.toDouble(gasLimit));
                                        }
                                    } else if (value.equals("Avg.GasPrice")) {
                                        String gasPrice = column.text();
                                        block.setAvgGasPrice(StringUtils.defaultIfEmpty(gasPrice, ""));
                                    } else if (value.equals("Reward")) {
                                        String reward = column.text();
                                        block.setReward(StringUtils.defaultIfEmpty(reward, ""));
                                    }
                                }
                                block.setServer(RandomUtils.nextInt(1, 3));
                                block.setStatus("ini");
                                block.setPagestatus("ini");
                                block.setSerial_number(serial);
                                block.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                block.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                blocks.add(block);
                            }
                            blocksDao.saveAll(blocks);
                            log.info("block保存完毕，link:" + link);
                            isLoop = false;
                        }
                    }
                }
            } else {
                times++;
                log.info("block信息获取失败：" + link + ",error code:" + code);
            }
        }
    }
    @Override
    @Async("myTaskAsyncPool")
    public  void getICO_Etherscan_IO_Blocks_Info(ICO_Etherscan_IO_Blocks blocks)  throws MethodNotSupportException {
        log.info("正在抓取ICO_Etherscan_IO_Blocks详情，pk_id:"+blocks.getPk_id()+"server:"+blocks.getServer());
        String link = "https://etherscan.io/block/"+blocks.getHeight();
        log.info("请求link："+link);
        Request request = new Request(link, RequestMethod.GET);
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode(); //response code
        if (200 == code) {
            String content = response.getResponseText(); //response text
            Document doc = Jsoup.parse(content);
            Element table = doc.getElementById("ContentPlaceHolder1_maintable");
            Elements trs = table.getElementsByTag("tr");
            ICO_Etherscan_IO_Blocks_Info blocks_info = blocks.getBlocks_info();
            if(null==blocks_info){//如果没有详情则创建一个
                blocks_info = new ICO_Etherscan_IO_Blocks_Info();
            }
            for(Element tr:trs){
                Elements tds = tr.getElementsByTag("td");
                String name = StringUtils.replaceEachRepeatedly(tds.first().html(),new String[]{"&nbsp;"," "},new String[]{"",""});
                if(StringUtils.containsIgnoreCase(name,"Transactions:")){
                   String transactions = tds.get(1).text();
                   if(StringUtils.isNotEmpty(transactions)){
                       transactions = StringUtils.trim(StringUtils.substringBefore(transactions,"transactions"));
                       if(NumberUtils.isDigits(transactions)){
                           blocks_info.setTransactions(NumberUtils.createInteger(transactions));
                       }
                   }
                }else if(StringUtils.equalsIgnoreCase(name,"Hash:")){
                    String hash = tds.get(1).text();
                    blocks_info.setHash(StringUtils.defaultIfEmpty(hash,""));
                }else if(StringUtils.equalsIgnoreCase(name,"ParentHash:")){
                    String parent_hash = tds.get(1).text();
                    blocks_info.setParent_hash(StringUtils.defaultIfEmpty(parent_hash,""));
                }else if(StringUtils.equalsIgnoreCase(name,"Sha3Uncles:")){
                    String sha3Uncles = tds.get(1).text();
                    blocks_info.setSha3_uncles(StringUtils.defaultIfEmpty(sha3Uncles,""));
                }else if(StringUtils.equalsIgnoreCase(name,"MinedBy:")){
                    String mined_by = tds.get(1).text();
                    blocks_info.setMined_by(StringUtils.defaultIfEmpty(mined_by,""));
                }else if(StringUtils.equalsIgnoreCase(name,"Difficulty:")){
                    String difficulty = tds.get(1).text();
                    blocks_info.setDifficulty(StringUtils.defaultIfEmpty(difficulty,""));
                }else if(StringUtils.equalsIgnoreCase(name,"TotalDifficulty:")){
                    String total_difficulty = tds.get(1).text();
                    blocks_info.setTotal_difficulty(StringUtils.defaultIfEmpty(total_difficulty,""));
                }else if(StringUtils.equalsIgnoreCase(name,"Size:")){
                    String size = tds.get(1).text();
                    blocks_info.setSize(StringUtils.defaultIfEmpty(size,""));
                }else if(StringUtils.equalsIgnoreCase(name,"Nonce:")){
                    String nonce = tds.get(1).text();
                    blocks_info.setNonce(StringUtils.defaultIfEmpty(nonce,""));
                }else if(StringUtils.equalsIgnoreCase(name,"BlockReward:")){
                    String block_reward = tds.get(1).text();
                    blocks_info.setBlock_reward(StringUtils.defaultIfEmpty(block_reward,""));
                }else if(StringUtils.equalsIgnoreCase(name,"UnclesReward:")){
                    String uncles_reward = tds.get(1).text();
                    blocks_info.setUncles_reward(StringUtils.defaultIfEmpty(uncles_reward,""));
                }else if(StringUtils.equalsIgnoreCase(name,"ExtraData:")){
                    String extra_data = tds.get(1).text();
                    blocks_info.setExtra_data(StringUtils.defaultIfEmpty(extra_data,""));
                }
            }
            if(null==blocks.getBlocks_info()){
                blocks_info.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            }

            blocks_info.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            blocks_info.setBlocks(blocks);

            blocks.setBlocks_info(blocks_info);
            blocks.setStatus(String.valueOf(code));
            blocks.setBlock_info_modify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            blocksDao.save(blocks);
            log.info("抓取ICO_Etherscan_IO_Blocks详情保存完毕，pk_id："+blocks.getPk_id());
        }else{
            blocks.setStatus(String.valueOf(code));
            blocks.setBlock_info_modify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            blocksDao.save(blocks);
            log.info("抓取ICO_Etherscan_IO_Blocks详情异常,error_code:"+code+"，pk_id："+blocks.getPk_id());
        }
    }
    @Override
    @Async("myTaskAsyncPool")
    public void getICO_Etherscan_IO_Blocks_TxsPages(ICO_Etherscan_IO_Blocks blocks) throws MethodNotSupportException {
        /*按照每页100条获取总的页数*/
        log.info("正在获取ICO_Etherscan_IO_Blocks的txs页数，block_pk_id="+blocks.getPk_id()+",server:"+blocks.getServer());
        String url = "https://etherscan.io/txs?block="+blocks.getHeight()+"&ps=100&p=1";
        Request request = new Request(url, RequestMethod.GET);
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode(); //response code
        int totalPages = 0;
        if(200 ==code){
            String content = response.getResponseText(); //response text
            Document doc = Jsoup.parse(content);
            Elements lasts = doc.getElementsMatchingText("^Page [1-9]\\d* of [1-9]\\d*$");
            if(null!=lasts&&lasts.size()>0){
                String page_text= lasts.first().text();
                String pages = StringUtils.substringAfterLast(page_text,"of").trim();
                if(NumberUtils.isDigits(pages)){
                    totalPages = Integer.valueOf(pages);
                    List<ICO_Etherscan_IO_Blocks_Txs_Page_List> page_list = new ArrayList<ICO_Etherscan_IO_Blocks_Txs_Page_List>();
                    for(int i=1;i<=totalPages;i++){
                        ICO_Etherscan_IO_Blocks_Txs_Page_List page = new ICO_Etherscan_IO_Blocks_Txs_Page_List();
                        page.setPage(i);
                        page.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        page.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        page.setStatus("ini");
                        page.setBlocks(blocks);
                        page_list.add(page);
                    }
                    txs_page_listDao.saveAll(page_list);
                    blocks.setPagestatus(String.valueOf(code));
                    blocks.setPage_modify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    blocksDao.save(blocks);
                    log.info("blocks共计获取"+totalPages+"页,block_pk_id="+blocks.getPk_id()+",height="+blocks.getHeight());
                }
            }else{
                blocks.setPagestatus(String.valueOf(code));
                blocks.setPage_modify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                blocksDao.save(blocks);
                log.info("blocks共计获取"+0+"页,block_pk_id="+blocks.getPk_id()+",height="+blocks.getHeight());
            }
        }else{
            log.info("获取总页数失败，code = "+code);
            blocks.setPagestatus(String.valueOf(code));
            blocks.setPage_modify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            blocksDao.save(blocks);
            log.info("获取总页数失败,block_pk_id="+blocks.getPk_id()+",height="+blocks.getHeight()+",code = "+code);
        }
    }
    @Override
    @Async("myTaskAsyncPool")
    public void getICO_Etherscan_IO_Blocks_Txs(ICO_Etherscan_IO_Blocks_Txs_Page_List page) throws MethodNotSupportException {
        String link = "https://etherscan.io/txs?block="+page.getBlocks().getHeight()+"&ps=100&p="+page.getPage();
        log.info("正在获取block txs信息，link："+link);
        Request request = new Request(link, RequestMethod.GET);
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode(); //response code
        if (200 == code) {
            String content = response.getResponseText(); //response text
            Document doc = Jsoup.parse(content);
            Elements tables_div = doc.getElementsByClass("table-responsive");
            if (null != tables_div && tables_div.size() > 0) {
                Element table_div = tables_div.first();
                Elements tables = table_div.getElementsByTag("table");
                if (null != tables && tables.size() > 0) {
                    Element table = tables.first();
                    Elements ths = table.getElementsByTag("thead").first().getElementsByTag("th");//获取表头数据
                    Map<Integer, String> ths_map = new HashMap<Integer, String>();
                    for (int i = 0; i < ths.size(); i++) {
                        Element th = ths.get(i);
                        if (StringUtils.equalsIgnoreCase("TxHash", th.text().trim())) {
                            ths_map.put(i, "TxHash");
                        }else if (StringUtils.equalsIgnoreCase("Block", th.text().trim())) {
                            ths_map.put(i, "Block");
                        } else if (StringUtils.equalsIgnoreCase("Age", th.text().trim())) {
                            ths_map.put(i, "Age");
                        } else if (StringUtils.equalsIgnoreCase("From", th.text().trim())) {
                            ths_map.put(i, "From");
                        } else if (StringUtils.equalsIgnoreCase("", th.text().trim())) {
                            ths_map.put(i, "");
                        }else if (StringUtils.equalsIgnoreCase("To", th.text().trim())) {
                            ths_map.put(i, "To");
                        } else if (StringUtils.equalsIgnoreCase("Value", th.text().trim())) {
                            ths_map.put(i, "Value");
                        } else if (StringUtils.equalsIgnoreCase("[TxFee]", th.text().trim())) {
                            ths_map.put(i, "[TxFee]");
                        }
                    }
                    Elements trs = table.getElementsByTag("tbody").first().getElementsByTag("tr");//获取数据列表
                    if (null != trs && trs.size() > 0) {//解析数据
                        List<ICO_Etherscan_IO_Blocks_Txs> txs = new ArrayList<ICO_Etherscan_IO_Blocks_Txs>();
                        for (Element tr : trs) {
                            ICO_Etherscan_IO_Blocks_Txs tx = new ICO_Etherscan_IO_Blocks_Txs();
                            Elements tds = tr.getElementsByTag("td");
                            if(null==tds||tds.size()<ths_map.size()){
                                log.info("数据不合法");
                                continue;
                            }
                            for (Map.Entry<Integer, String> entry : ths_map.entrySet()) {
                                int key = entry.getKey();
                                String value = entry.getValue();

                                Element column = tds.get(key);
                                if (value.equals("TxHash")) {
                                    Elements txHashes = column.getElementsByTag("a");
                                    if (null != txHashes && txHashes.size() > 0) {
                                        Element txHash = txHashes.first();
                                        String txHash_txt = StringUtils.defaultIfEmpty(txHash.text(), "");
                                        tx.setTxhash(txHash_txt);
                                        /*tx = txsDao.findICO_Etherscan_IO_Blocks_TxsByTxhash(txHash_txt);
                                        if(null==tx){
                                            if(StringUtils.isEmpty(txHash_txt)){
                                                break;
                                            }
                                            tx = new ICO_Etherscan_IO_Blocks_Txs();
                                            tx.setTxhash(txHash_txt);
                                        }else{
                                            break;
                                        }*/

                                    }
                                } else if (value.equals("Age")) {
                                    Format sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Format sdf = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss a", Locale.ENGLISH);
                                    Elements ages = column.getElementsByTag("span");
                                    if(null!=ages&&ages.size()>0){
                                        String age = ages.first().attr("title");
                                        try {
                                            age = sdf1.format(sdf.parseObject(age));
                                            Date age_date = DateUtils.parseDate(age, new String[]{"yyyy-MM-dd HH:mm:ss"});
                                            tx.setAge(new Timestamp(age_date.getTime()));
                                        } catch (ParseException e) {
                                            log.info("日期转换错误："+age);
                                        }
                                    }
                                } else if (value.equals("From")) {
                                    Elements froms = column.getElementsByTag("a");
                                    if (null != froms && froms.size() > 0) {
                                        Element from = froms.first();
                                        String from_text = StringUtils.defaultIfEmpty(from.text(), "");
                                        tx.setF(from_text);
                                    }
                                } else if (value.equals("To")) {
                                    Elements tos = column.getElementsByTag("a");
                                    if (null != tos && tos.size() > 0) {
                                        Element to = tos.first();
                                        String to_text = StringUtils.defaultIfEmpty(to.text(), "");
                                        tx.setT(to_text);
                                    }
                                } else if (value.equals("Value")) {
                                    String  value_tx = column.text();
                                    tx.setVal(StringUtils.defaultIfEmpty(value_tx,""));
                                } else if (value.equals("[TxFee]")) {
                                    String  txFee = column.text();
                                    tx.setTxfee(StringUtils.defaultIfEmpty(txFee,""));
                                }
                            }
                            tx.setBlock(page.getBlocks().getHeight());
                            tx.setStatus("ini");
                            tx.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            tx.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            tx.setStatus("ini");
                            tx.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            tx.setPage_list(page);
                            if(null!=tx&&null!=tx.getTxhash()&&StringUtils.isNotEmpty(tx.getTxhash())){
                                txs.add(tx);
                            }else{
                                log.info(link);
                            }

                        }
                        if(null!=txs&&txs.size()>0){
                            txsDao.saveAll(txs);
                        }
                        page.setStatus(String.valueOf(code));
                        page.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        txs_page_listDao.save(page);
                        log.info("block txs保存完毕，link:"+link);
                    }
                }else{
                    page.setStatus("blank");
                    page.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    txs_page_listDao.save(page);
                    log.info("block txs请求数据为空，link:"+link);
                }
            }
        }else{
            page.setStatus(String.valueOf(code));
            page.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            txs_page_listDao.save(page);
            log.info("block txs请求失败，link:"+link+",errorCode="+code);
        }
    }

    @Override
    @Async("myTaskAsyncPool")
    public void getICO_Etherscan_IO_Blocks_Txs_Info(ICO_Etherscan_IO_Blocks_Txs txs) throws MethodNotSupportException {
        String link = "https://etherscan.io/tx/"+txs.getTxhash();
        log.info("正在获取tx 详情信息，link："+link);
        Request request = new Request(link, RequestMethod.GET);
        request.addHeader("Cookie","__cfduid=d781ca5b0a8a6427876fcf981ea5b29761525230016; _ga=GA1.2.1820036799.1525230024; etherscan_cookieconsent=True; showfavourite0x829bd824b016326a401d083b33d092293333a830=0; etherscan_userid=yinzhida; etherscan_pwd=yinzhida; etherscan_autologin=True; ASP.NET_SessionId=gc4w3owz41jyewzic0vuugre; __cflb=3866738502; _gid=GA1.2.1464166203.1528165707; _gat_gtag_UA_46998878_6=1");
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode(); //response code
        if (200 == code) {
            String content = response.getResponseText(); //response text
            Document doc = Jsoup.parse(content);
            Element maintable = doc.getElementById("ContentPlaceHolder1_maintable");
            ICO_Etherscan_IO_Blocks_Txs_Info txs_info = txs.getTxs_info();
            if(null==txs_info){//如果没有详情则创建一个
                txs_info = new ICO_Etherscan_IO_Blocks_Txs_Info();
            }
            Elements status = maintable.getElementsContainingOwnText("TxReceipt Status:");
            if(null!=status&&status.size()>0){
                Element receipt_status = status.first();
                Element receipt_status_next = receipt_status.nextElementSibling();
                if(null!=receipt_status_next){
                    txs_info.setTx_receipt_status(StringUtils.defaultIfEmpty(receipt_status_next.text(),""));
                }
            }
            Elements gas_limits = maintable.getElementsContainingOwnText("Gas Limit:");
            if(null!=gas_limits&&gas_limits.size()>0){
                Element gas_limit = gas_limits.first();
                Element gas_limit_next = gas_limit.nextElementSibling();
                if(null!=gas_limit_next){
                    String gas_limit_next_text = gas_limit_next.text().trim();
                    if(NumberUtils.isDigits(gas_limit_next_text)){
                        txs_info.setGas_limit(NumberUtils.toDouble(gas_limit_next_text));
                    }
                }
            }
            Elements gas_used_by_txns = maintable.getElementsContainingOwnText("Gas Used By Txn:");
            if(null!=gas_used_by_txns&&gas_used_by_txns.size()>0){
                Element gas_used_by_txn = gas_used_by_txns.first();
                Element gas_used_by_txn_next = gas_used_by_txn.nextElementSibling();
                if(null!=gas_used_by_txn_next){
                    String gas_used_by_txn_next_text = gas_used_by_txn_next.text().trim();
                    if(NumberUtils.isDigits(gas_used_by_txn_next_text)){
                        txs_info.setGas_used_by_txn(NumberUtils.toDouble(gas_used_by_txn_next_text));
                    }
                }
            }
            Elements gas_prices = maintable.getElementsContainingOwnText("Gas Price:");
            if(null!=gas_prices&&gas_prices.size()>0){
                Element gas_price = gas_prices.first();
                Element gas_price_next = gas_price.nextElementSibling();
                if(null!=gas_price_next){
                    txs_info.setGas_price(StringUtils.defaultIfEmpty(gas_price_next.text(),""));

                }
            }
            Elements actual_tx_cost_fees = maintable.getElementsContainingOwnText("Actual Tx Cost/Fee:");
            if(null!=actual_tx_cost_fees&&actual_tx_cost_fees.size()>0){
                Element actual_tx_cost_fee = actual_tx_cost_fees.first();
                Element actual_tx_cost_fee_next = actual_tx_cost_fee.nextElementSibling();
                if(null!=actual_tx_cost_fee_next){
                    txs_info.setActual_tx_cost_fee(StringUtils.defaultIfEmpty(actual_tx_cost_fee_next.text(),""));
                }
            }
            Elements nonce_positions = maintable.getElementsContainingOwnText("Nonce ");
            if(null!=nonce_positions&&nonce_positions.size()>0){
                Element nonce_position = nonce_positions.first();
                Element nonce_positions_next = nonce_position.nextElementSibling();
                if(null!=nonce_positions_next){
                    txs_info.setNonce_position(StringUtils.defaultIfEmpty(nonce_positions_next.text(),""));
                }
            }
            Element inputdata = maintable.getElementById("inputdata");
            if(null!=inputdata){
                txs_info.setInput_data(StringUtils.defaultIfEmpty(inputdata.text(),""));
            }
            Element txtPrivateNoteArea = maintable.getElementById("txtPrivateNoteArea");
            if(null!=txtPrivateNoteArea){
                txs_info.setPrivate_note(StringUtils.defaultIfEmpty(txtPrivateNoteArea.text(),""));
            }else{
                log.info("账号尚未登录，无法查看private_note，请先登录！");
                txs_info.setPrivate_note(StringUtils.defaultIfEmpty("","NULL"));
            }

            if(null==txs_info.getTxs()){
                txs_info.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            }

            txs_info.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            txs_info.setTxs(txs);

            txs.setTxs_info(txs_info);
            txs.setStatus(String.valueOf(code));
            txs.setDetail_modify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            txsDao.save(txs);
            log.info("正在获取tx 信息详情保存完毕，pk_id："+txs.getPk_id());
        }else{
            txs.setStatus(String.valueOf(code));
            txs.setDetail_modify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            txsDao.save(txs);
            log.info("正在获取tx 信息详情异常,error_code:"+code+"，pk_id："+txs.getPk_id());
        }
    }

    @Override
    public List<String> getICO_Etherscan_IO_Blocks_Forked_TotalPageLinks() throws MethodNotSupportException {
        /*按照每页100条获取总的页数*/
        String url = "https://etherscan.io/blocks_forked?ps=100&p=1";
        Request request = new Request(url, RequestMethod.GET);
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode(); //response code
        List<String> totalPageLinks = new ArrayList<String>();
        if(200 ==code){
            String content = response.getResponseText(); //response text
            Document doc = Jsoup.parse(content);
            Elements lasts = doc.getElementsContainingOwnText("Last");
            if(null!=lasts&&lasts.size()>0){
                String last_link = lasts.first().attr("href");
                String pages = StringUtils.substringAfterLast(last_link,"p=");
                if(NumberUtils.isDigits(pages)){
                    int totalPages = Integer.valueOf(pages);
                    log.info("blocks共计获取"+totalPages+"页");
                    /*https://etherscan.io/blocks?ps=100&p=2*/
                    for(int i=1;i<=totalPages;i++){
                        String link = "https://etherscan.io/blocks_forked?ps=100&p="+i;
                        totalPageLinks.add(link);
                    }
                }
            }
        }else{
            log.info("获取总页数失败，code = "+code);
        }
        return totalPageLinks;
    }

    @Override
    public void getICO_Etherscan_IO_Blocks_Forked(String link, Long serial, int page_total) throws MethodNotSupportException {
        int times = 1;
        boolean isLoop = true;
        while(isLoop&&times<=5) {
            log.info("正在第"+times+"次获取block信息，link：" + link + ",共" + page_total + "页");
            Request request = new Request(link, RequestMethod.GET);
            Response response = HttpClientUtil.doRequest(request);
            int code = response.getCode(); //response code
            if (200 == code) {
                String content = response.getResponseText(); //response text
                Document doc = Jsoup.parse(content);
                Elements tables_div = doc.getElementsByClass("table-responsive");
                if (null != tables_div && tables_div.size() > 0) {
                    Element table_div = tables_div.first();
                    Elements tables = table_div.getElementsByTag("table");
                    if (null != tables && tables.size() > 0) {
                        Element table = tables.first();
                        Elements ths = table.getElementsByTag("thead").first().getElementsByTag("th");//获取表头数据
                        Map<Integer, String> ths_map = new HashMap<Integer, String>();
                        for (int i = 0; i < ths.size(); i++) {
                            Element th = ths.get(i);
                            if (StringUtils.equalsIgnoreCase("Height", th.text().trim())) {
                                ths_map.put(i, "Height");
                            } else if (StringUtils.equalsIgnoreCase("Age", th.text().trim())) {
                                ths_map.put(i, "Age");
                            } else if (StringUtils.equalsIgnoreCase("txn", th.text().trim())) {
                                ths_map.put(i, "txn");
                            } else if (StringUtils.equalsIgnoreCase("Uncles", th.text().trim())) {
                                ths_map.put(i, "Uncles");
                            } else if (StringUtils.equalsIgnoreCase("Miner", th.text().trim())) {
                                ths_map.put(i, "Miner");
                            } else if (StringUtils.equalsIgnoreCase("GasUsed", th.text().trim())) {
                                ths_map.put(i, "GasUsed");
                            } else if (StringUtils.equalsIgnoreCase("GasLimit", th.text().trim())) {
                                ths_map.put(i, "GasLimit");
                            } else if (StringUtils.equalsIgnoreCase("Avg.GasPrice", th.text().trim())) {
                                ths_map.put(i, "Avg.GasPrice");
                            } else if (StringUtils.equalsIgnoreCase("Reward", th.text().trim())) {
                                ths_map.put(i, "Reward");
                            }
                        }
                        Elements trs = table.getElementsByTag("tbody").first().getElementsByTag("tr");//获取数据列表
                        if (null != trs && trs.size() > 0) {//解析数据
                            List<ICO_Etherscan_IO_Blocks_Forked> blocks = new ArrayList<ICO_Etherscan_IO_Blocks_Forked>();
                            for (Element tr : trs) {
                                ICO_Etherscan_IO_Blocks_Forked block = new ICO_Etherscan_IO_Blocks_Forked();
                                Elements tds = tr.getElementsByTag("td");
                                for (Map.Entry<Integer, String> entry : ths_map.entrySet()) {
                                    int key = entry.getKey();
                                    String value = entry.getValue();

                                    Element column = tds.get(key);
                                    if (value.equals("Height")) {
                                        Elements height_links = column.getElementsByTag("a");
                                        if (null != height_links && height_links.size() > 0) {
                                            Element height_link = height_links.first();
                                            String height = StringUtils.defaultIfEmpty(height_link.text(), "0");
                                            if (NumberUtils.isDigits(height)) {
                                                block.setHeight(NumberUtils.toLong(height));
                                            }
                                        }
                                    } else if (value.equals("Age")) {
                                        Format sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Format sdf = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss a", Locale.ENGLISH);
                                        Elements ages = column.getElementsByTag("span");
                                        if (null != ages && ages.size() > 0) {
                                            String age = ages.first().attr("title");
                                            try {
                                                age = sdf1.format(sdf.parseObject(age));
                                                Date age_date = DateUtils.parseDate(age, new String[]{"yyyy-MM-dd HH:mm:ss"});
                                                block.setAge(new Timestamp(age_date.getTime()));
                                            } catch (ParseException e) {
                                                log.info("日期转换错误：" + age);
                                            }
                                        }
                                    } else if (value.equals("txn")) {
                                        Elements txns = column.getElementsByTag("a");
                                        if (null != txns && txns.size() > 0) {
                                            Element txn = txns.first();
                                            String txn_text = StringUtils.defaultIfEmpty(txn.text(), "0");
                                            if (NumberUtils.isDigits(txn_text)) {
                                                block.setTxn(NumberUtils.toInt(txn_text));
                                            }
                                        }
                                    } else if (value.equals("Uncles")) {
                                        String uncles = column.text();
                                        if (NumberUtils.isDigits(uncles)) {
                                            block.setUncles(NumberUtils.toInt(uncles));
                                        }
                                    } else if (value.equals("Miner")) {
                                        Elements miners = column.getElementsByTag("a");
                                        if (null != miners && miners.size() > 0) {
                                            String miner = miners.first().attr("href");
                                            miner = StringUtils.substringAfterLast(miner, "address/");
                                            block.setMiner(StringUtils.defaultIfEmpty(miner, ""));
                                        }
                                    } else if (value.equals("GasUsed")) {
                                        String gasUsed = column.text();
                                        String gasUsed_value = StringUtils.substringBeforeLast(gasUsed, "(").trim();
                                        String gasUsed_percent = StringUtils.substringBetween(gasUsed, "(", ")").trim();
                                        if (NumberUtils.isDigits(gasUsed_value)) {
                                            block.setGasUsed(NumberUtils.toDouble(gasUsed_value));
                                        }
                                        NumberFormat nbf = NumberFormat.getPercentInstance();
                                        try {
                                            Number number = nbf.parse(gasUsed_percent);
                                            block.setGasUsedPercent(number.floatValue());
                                        } catch (ParseException e) {
                                            log.info("百分比转小数失败：" + gasUsed_percent);
                                        }
                                    } else if (value.equals("GasLimit")) {
                                        String gasLimit = column.text();
                                        if (NumberUtils.isDigits(gasLimit)) {
                                            block.setGasLimit(NumberUtils.toDouble(gasLimit));
                                        }
                                    } else if (value.equals("Avg.GasPrice")) {
                                        String gasPrice = column.text();
                                        block.setAvgGasPrice(StringUtils.defaultIfEmpty(gasPrice, ""));
                                    } else if (value.equals("Reward")) {
                                        String reward = column.text();
                                        block.setReward(StringUtils.defaultIfEmpty(reward, ""));
                                    }
                                }
                                block.setServer(RandomUtils.nextInt(1, 3));
                                block.setStatus("ini");
                                block.setPagestatus("ini");
                                block.setSerial_number(serial);
                                block.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                block.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                blocks.add(block);
                            }
                            blocks_forkedDao.saveAll(blocks);
                            log.info("block保存完毕，link:" + link);
                            isLoop = false;
                        }
                    }
                }
            } else {
                times++;
                log.info("block信息获取失败：" + link + ",error code:" + code);
            }
        }
    }

    @Override
    @Async("myTaskAsyncPool")
    public void getICO_Etherscan_IO_Blocks_Forked_Info(ICO_Etherscan_IO_Blocks_Forked blocks) throws MethodNotSupportException {
        log.info("正在抓取ICO_Etherscan_IO_Blocks_Forked详情，pk_id:"+blocks.getPk_id()+",server:"+blocks.getServer());
        String link = "https://etherscan.io/block/"+blocks.getHeight();
        log.info("请求link："+link);
        Request request = new Request(link, RequestMethod.GET);
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode(); //response code
        if (200 == code) {
            String content = response.getResponseText(); //response text
            Document doc = Jsoup.parse(content);
            Element table = doc.getElementById("ContentPlaceHolder1_maintable");
            Elements trs = table.getElementsByTag("tr");
            ICO_Etherscan_IO_Blocks_Forked_Info blocks_info = blocks.getBlocks_info();
            if(null==blocks_info){//如果没有详情则创建一个
                blocks_info = new ICO_Etherscan_IO_Blocks_Forked_Info();
            }
            for(Element tr:trs){
                Elements tds = tr.getElementsByTag("td");
                String name = StringUtils.replaceEachRepeatedly(tds.first().html(),new String[]{"&nbsp;"," "},new String[]{"",""});
                if(StringUtils.containsIgnoreCase(name,"Transactions:")){
                    String transactions = tds.get(1).text();
                    if(StringUtils.isNotEmpty(transactions)){
                        transactions = StringUtils.trim(StringUtils.substringBefore(transactions,"transactions"));
                        if(NumberUtils.isDigits(transactions)){
                            blocks_info.setTransactions(NumberUtils.createInteger(transactions));
                        }
                    }
                }else if(StringUtils.equalsIgnoreCase(name,"Hash:")){
                    String hash = tds.get(1).text();
                    blocks_info.setHash(StringUtils.defaultIfEmpty(hash,""));
                }else if(StringUtils.equalsIgnoreCase(name,"ParentHash:")){
                    String parent_hash = tds.get(1).text();
                    blocks_info.setParent_hash(StringUtils.defaultIfEmpty(parent_hash,""));
                }else if(StringUtils.equalsIgnoreCase(name,"Sha3Uncles:")){
                    String sha3Uncles = tds.get(1).text();
                    blocks_info.setSha3_uncles(StringUtils.defaultIfEmpty(sha3Uncles,""));
                }else if(StringUtils.equalsIgnoreCase(name,"MinedBy:")){
                    String mined_by = tds.get(1).text();
                    blocks_info.setMined_by(StringUtils.defaultIfEmpty(mined_by,""));
                }else if(StringUtils.equalsIgnoreCase(name,"Difficulty:")){
                    String difficulty = tds.get(1).text();
                    blocks_info.setDifficulty(StringUtils.defaultIfEmpty(difficulty,""));
                }else if(StringUtils.equalsIgnoreCase(name,"TotalDifficulty:")){
                    String total_difficulty = tds.get(1).text();
                    blocks_info.setTotal_difficulty(StringUtils.defaultIfEmpty(total_difficulty,""));
                }else if(StringUtils.equalsIgnoreCase(name,"Size:")){
                    String size = tds.get(1).text();
                    blocks_info.setSize(StringUtils.defaultIfEmpty(size,""));
                }else if(StringUtils.equalsIgnoreCase(name,"Nonce:")){
                    String nonce = tds.get(1).text();
                    blocks_info.setNonce(StringUtils.defaultIfEmpty(nonce,""));
                }else if(StringUtils.equalsIgnoreCase(name,"BlockReward:")){
                    String block_reward = tds.get(1).text();
                    blocks_info.setBlock_reward(StringUtils.defaultIfEmpty(block_reward,""));
                }else if(StringUtils.equalsIgnoreCase(name,"UnclesReward:")){
                    String uncles_reward = tds.get(1).text();
                    blocks_info.setUncles_reward(StringUtils.defaultIfEmpty(uncles_reward,""));
                }else if(StringUtils.equalsIgnoreCase(name,"ExtraData:")){
                    String extra_data = tds.get(1).text();
                    blocks_info.setExtra_data(StringUtils.defaultIfEmpty(extra_data,""));
                }
            }
            if(null==blocks.getBlocks_info()){
                blocks_info.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            }

            blocks_info.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            blocks_info.setBlocks(blocks);

            blocks.setBlocks_info(blocks_info);
            blocks.setStatus(String.valueOf(code));
            blocks.setBlock_info_modify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            blocks_forkedDao.save(blocks);
            log.info("ICO_Etherscan_IO_Blocks_Forked详情保存完毕，pk_id："+blocks.getPk_id());
        }else{
            blocks.setStatus(String.valueOf(code));
            blocks.setBlock_info_modify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            blocks_forkedDao.save(blocks);
            log.info("ICO_Etherscan_IO_Blocks_Forkeds详情异常,error_code:"+code+"，pk_id："+blocks.getPk_id());
        }
    }

    @Override
    @Async("myTaskAsyncPool")
    public void getICO_Etherscan_IO_Blocks_Forked_TxsPages(ICO_Etherscan_IO_Blocks_Forked blocks) throws MethodNotSupportException {
        /*按照每页100条获取总的页数*/
        log.info("正在获取ICO_Etherscan_IO_Blocks_Forked的txs页数，block_pk_id="+blocks.getPk_id()+",server:"+blocks.getServer());
        String url = "https://etherscan.io/txs?block="+blocks.getHeight()+"&ps=100&p=1";
        Request request = new Request(url, RequestMethod.GET);
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode(); //response code
        int totalPages = 0;
        if(200 ==code){
            String content = response.getResponseText(); //response text
            Document doc = Jsoup.parse(content);
            Elements lasts = doc.getElementsMatchingText("^Page [1-9]\\d* of [1-9]\\d*$");
            if(null!=lasts&&lasts.size()>0){
                String page_text= lasts.first().text();
                String pages = StringUtils.substringAfterLast(page_text,"of").trim();
                if(NumberUtils.isDigits(pages)){
                    totalPages = Integer.valueOf(pages);
                    List<ICO_Etherscan_IO_Blocks_Forked_Txs_Page_List> page_list = new ArrayList<ICO_Etherscan_IO_Blocks_Forked_Txs_Page_List>();
                    for(int i=1;i<=totalPages;i++){
                        ICO_Etherscan_IO_Blocks_Forked_Txs_Page_List page = new ICO_Etherscan_IO_Blocks_Forked_Txs_Page_List();
                        page.setPage(i);
                        page.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        page.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        page.setStatus("ini");
                        page.setBlocks(blocks);
                        page_list.add(page);
                    }
                    forked_txs_page_listDao.saveAll(page_list);
                    blocks.setPagestatus(String.valueOf(code));
                    blocks.setPage_modify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    blocks_forkedDao.save(blocks);
                    log.info("blocks共计获取"+totalPages+"页,block_pk_id="+blocks.getPk_id()+",height="+blocks.getHeight());
                }
            }else{
                blocks.setPagestatus(String.valueOf(code));
                blocks.setPage_modify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                blocks_forkedDao.save(blocks);
                log.info("blocks共计获取"+0+"页,block_pk_id="+blocks.getPk_id()+",height="+blocks.getHeight());
            }
        }else{
            log.info("获取总页数失败，code = "+code);
            blocks.setPagestatus(String.valueOf(code));
            blocks.setPage_modify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            blocks_forkedDao.save(blocks);
            log.info("获取总页数失败,block_pk_id="+blocks.getPk_id()+",height="+blocks.getHeight()+",code = "+code);
        }
    }

    @Override
    @Async("myTaskAsyncPool")
    public void getICO_Etherscan_IO_Blocks_Forked_Txs(ICO_Etherscan_IO_Blocks_Forked_Txs_Page_List page) throws MethodNotSupportException {
        String link = "https://etherscan.io/txs?block="+page.getBlocks().getHeight()+"&ps=100&p="+page.getPage();
        log.info("正在获取block txs信息，link："+link);
        Request request = new Request(link, RequestMethod.GET);
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode(); //response code
        if (200 == code) {
            String content = response.getResponseText(); //response text
            Document doc = Jsoup.parse(content);
            Elements tables_div = doc.getElementsByClass("table-responsive");
            if (null != tables_div && tables_div.size() > 0) {
                Element table_div = tables_div.first();
                Elements tables = table_div.getElementsByTag("table");
                if (null != tables && tables.size() > 0) {
                    Element table = tables.first();
                    Elements ths = table.getElementsByTag("thead").first().getElementsByTag("th");//获取表头数据
                    Map<Integer, String> ths_map = new HashMap<Integer, String>();
                    for (int i = 0; i < ths.size(); i++) {
                        Element th = ths.get(i);
                        if (StringUtils.equalsIgnoreCase("TxHash", th.text().trim())) {
                            ths_map.put(i, "TxHash");
                        }else if (StringUtils.equalsIgnoreCase("Block", th.text().trim())) {
                            ths_map.put(i, "Block");
                        } else if (StringUtils.equalsIgnoreCase("Age", th.text().trim())) {
                            ths_map.put(i, "Age");
                        } else if (StringUtils.equalsIgnoreCase("From", th.text().trim())) {
                            ths_map.put(i, "From");
                        } else if (StringUtils.equalsIgnoreCase("", th.text().trim())) {
                            ths_map.put(i, "");
                        }else if (StringUtils.equalsIgnoreCase("To", th.text().trim())) {
                            ths_map.put(i, "To");
                        } else if (StringUtils.equalsIgnoreCase("Value", th.text().trim())) {
                            ths_map.put(i, "Value");
                        } else if (StringUtils.equalsIgnoreCase("[TxFee]", th.text().trim())) {
                            ths_map.put(i, "[TxFee]");
                        }
                    }
                    Elements trs = table.getElementsByTag("tbody").first().getElementsByTag("tr");//获取数据列表
                    if (null != trs && trs.size() > 0) {//解析数据
                        List<ICO_Etherscan_IO_Blocks_Forked_Txs> txs = new ArrayList<ICO_Etherscan_IO_Blocks_Forked_Txs>();
                        for (Element tr : trs) {
                            ICO_Etherscan_IO_Blocks_Forked_Txs tx = new ICO_Etherscan_IO_Blocks_Forked_Txs();
                            Elements tds = tr.getElementsByTag("td");
                            if(null==tds||tds.size()<ths_map.size()){
                                log.info("数据不合法");
                                continue;
                            }
                            for (Map.Entry<Integer, String> entry : ths_map.entrySet()) {
                                int key = entry.getKey();
                                String value = entry.getValue();

                                Element column = tds.get(key);
                                if (value.equals("TxHash")) {
                                    Elements txHashes = column.getElementsByTag("a");
                                    if (null != txHashes && txHashes.size() > 0) {
                                        Element txHash = txHashes.first();
                                        String txHash_txt = StringUtils.defaultIfEmpty(txHash.text(), "");
                                        tx.setTxhash(txHash_txt);
                                        /*tx = txsDao.findICO_Etherscan_IO_Blocks_TxsByTxhash(txHash_txt);
                                        if(null==tx){
                                            if(StringUtils.isEmpty(txHash_txt)){
                                                break;
                                            }
                                            tx = new ICO_Etherscan_IO_Blocks_Txs();
                                            tx.setTxhash(txHash_txt);
                                        }else{
                                            break;
                                        }*/

                                    }
                                } else if (value.equals("Age")) {
                                    Format sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Format sdf = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss a", Locale.ENGLISH);
                                    Elements ages = column.getElementsByTag("span");
                                    if(null!=ages&&ages.size()>0){
                                        String age = ages.first().attr("title");
                                        try {
                                            age = sdf1.format(sdf.parseObject(age));
                                            Date age_date = DateUtils.parseDate(age, new String[]{"yyyy-MM-dd HH:mm:ss"});
                                            tx.setAge(new Timestamp(age_date.getTime()));
                                        } catch (ParseException e) {
                                            log.info("日期转换错误："+age);
                                        }
                                    }
                                } else if (value.equals("From")) {
                                    Elements froms = column.getElementsByTag("a");
                                    if (null != froms && froms.size() > 0) {
                                        Element from = froms.first();
                                        String from_text = StringUtils.defaultIfEmpty(from.text(), "");
                                        tx.setF(from_text);
                                    }
                                } else if (value.equals("To")) {
                                    Elements tos = column.getElementsByTag("a");
                                    if (null != tos && tos.size() > 0) {
                                        Element to = tos.first();
                                        String to_text = StringUtils.defaultIfEmpty(to.text(), "");
                                        tx.setT(to_text);
                                    }
                                } else if (value.equals("Value")) {
                                    String  value_tx = column.text();
                                    tx.setVal(StringUtils.defaultIfEmpty(value_tx,""));
                                } else if (value.equals("[TxFee]")) {
                                    String  txFee = column.text();
                                    tx.setTxfee(StringUtils.defaultIfEmpty(txFee,""));
                                }
                            }
                            tx.setBlock(page.getBlocks().getHeight());
                            tx.setStatus("ini");
                            tx.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            tx.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            tx.setStatus("ini");
                            tx.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            tx.setPage_list(page);
                            if(null!=tx&&null!=tx.getTxhash()&&StringUtils.isNotEmpty(tx.getTxhash())){
                                txs.add(tx);
                            }else{
                                log.info(link);
                            }

                        }
                        if(null!=txs&&txs.size()>0){
                            forked_txsDao.saveAll(txs);
                        }
                        page.setStatus(String.valueOf(code));
                        page.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        forked_txs_page_listDao.save(page);
                        log.info("block txs保存完毕，link:"+link);
                    }
                }else{
                    page.setStatus("blank");
                    page.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    forked_txs_page_listDao.save(page);
                    log.info("block txs请求数据为空，link:"+link);
                }
            }
        }else{
            page.setStatus(String.valueOf(code));
            page.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            forked_txs_page_listDao.save(page);
            log.info("block txs请求失败，link:"+link+",errorCode="+code);
        }
    }

    @Override
    @Async("myTaskAsyncPool")
    public void getICO_Etherscan_IO_Blocks_Forked_Txs_Info(ICO_Etherscan_IO_Blocks_Forked_Txs txs) throws MethodNotSupportException {
        String link = "https://etherscan.io/tx/"+txs.getTxhash();
        log.info("正在获取tx 详情信息，link："+link);
        Request request = new Request(link, RequestMethod.GET);
        request.addHeader("Cookie","__cfduid=d781ca5b0a8a6427876fcf981ea5b29761525230016; _ga=GA1.2.1820036799.1525230024; etherscan_cookieconsent=True; showfavourite0x829bd824b016326a401d083b33d092293333a830=0; etherscan_userid=yinzhida; etherscan_pwd=yinzhida; etherscan_autologin=True; ASP.NET_SessionId=gc4w3owz41jyewzic0vuugre; __cflb=2482413123; _gid=GA1.2.1353010658.1527658357");
        Response response = HttpClientUtil.doRequest(request);
        int code = response.getCode(); //response code
        if (200 == code) {
            String content = response.getResponseText(); //response text
            Document doc = Jsoup.parse(content);
            Element maintable = doc.getElementById("ContentPlaceHolder1_maintable");
            ICO_Etherscan_IO_Blocks_Forked_Txs_Info txs_info = txs.getTxs_info();
            if(null==txs_info){//如果没有详情则创建一个
                txs_info = new ICO_Etherscan_IO_Blocks_Forked_Txs_Info();
            }
            Elements status = maintable.getElementsContainingOwnText("TxReceipt Status:");
            if(null!=status&&status.size()>0){
                Element receipt_status = status.first();
                Element receipt_status_next = receipt_status.nextElementSibling();
                if(null!=receipt_status_next){
                    txs_info.setTx_receipt_status(StringUtils.defaultIfEmpty(receipt_status_next.text(),""));
                }
            }
            Elements gas_limits = maintable.getElementsContainingOwnText("Gas Limit:");
            if(null!=gas_limits&&gas_limits.size()>0){
                Element gas_limit = gas_limits.first();
                Element gas_limit_next = gas_limit.nextElementSibling();
                if(null!=gas_limit_next){
                    String gas_limit_next_text = gas_limit_next.text().trim();
                    if(NumberUtils.isDigits(gas_limit_next_text)){
                        txs_info.setGas_limit(NumberUtils.toDouble(gas_limit_next_text));
                    }
                }
            }
            Elements gas_used_by_txns = maintable.getElementsContainingOwnText("Gas Used By Txn:");
            if(null!=gas_used_by_txns&&gas_used_by_txns.size()>0){
                Element gas_used_by_txn = gas_used_by_txns.first();
                Element gas_used_by_txn_next = gas_used_by_txn.nextElementSibling();
                if(null!=gas_used_by_txn_next){
                    String gas_used_by_txn_next_text = gas_used_by_txn_next.text().trim();
                    if(NumberUtils.isDigits(gas_used_by_txn_next_text)){
                        txs_info.setGas_used_by_txn(NumberUtils.toDouble(gas_used_by_txn_next_text));
                    }
                }
            }
            Elements gas_prices = maintable.getElementsContainingOwnText("Gas Price:");
            if(null!=gas_prices&&gas_prices.size()>0){
                Element gas_price = gas_prices.first();
                Element gas_price_next = gas_price.nextElementSibling();
                if(null!=gas_price_next){
                    txs_info.setGas_price(StringUtils.defaultIfEmpty(gas_price_next.text(),""));

                }
            }
            Elements actual_tx_cost_fees = maintable.getElementsContainingOwnText("Actual Tx Cost/Fee:");
            if(null!=actual_tx_cost_fees&&actual_tx_cost_fees.size()>0){
                Element actual_tx_cost_fee = actual_tx_cost_fees.first();
                Element actual_tx_cost_fee_next = actual_tx_cost_fee.nextElementSibling();
                if(null!=actual_tx_cost_fee_next){
                    txs_info.setActual_tx_cost_fee(StringUtils.defaultIfEmpty(actual_tx_cost_fee_next.text(),""));
                }
            }
            Elements nonce_positions = maintable.getElementsContainingOwnText("Nonce ");
            if(null!=nonce_positions&&nonce_positions.size()>0){
                Element nonce_position = nonce_positions.first();
                Element nonce_positions_next = nonce_position.nextElementSibling();
                if(null!=nonce_positions_next){
                    txs_info.setNonce_position(StringUtils.defaultIfEmpty(nonce_positions_next.text(),""));
                }
            }
            Element inputdata = maintable.getElementById("inputdata");
            if(null!=inputdata){
                txs_info.setInput_data(StringUtils.defaultIfEmpty(inputdata.text(),""));
            }
            Element txtPrivateNoteArea = maintable.getElementById("txtPrivateNoteArea");
            if(null!=txtPrivateNoteArea){
                txs_info.setPrivate_note(StringUtils.defaultIfEmpty(txtPrivateNoteArea.text(),""));
            }else{
                log.info("账号尚未登录，无法查看private_note，请先登录！");
                txs_info.setPrivate_note(StringUtils.defaultIfEmpty("","NULL"));
            }

            if(null==txs_info.getTxs()){
                txs_info.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            }

            txs_info.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            txs_info.setTxs(txs);

            txs.setTxs_info(txs_info);
            txs.setStatus(String.valueOf(code));
            txs.setDetail_modify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            forked_txsDao.save(txs);
            log.info("正在获取tx 信息详情保存完毕，pk_id："+txs.getPk_id());
        }else{
            txs.setStatus(String.valueOf(code));
            txs.setDetail_modify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
            forked_txsDao.save(txs);
            log.info("正在获取tx 信息详情异常,error_code:"+code+"，pk_id："+txs.getPk_id());
        }
    }

    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.parse(new File("C:\\Users\\EDDC\\Desktop\\test.htm"), "utf-8");
        Elements lasts = doc.getElementsMatchingText("^Page [1-9]\\d* of [1-9]\\d*$");
        System.out.println(lasts.text());
    }
}
