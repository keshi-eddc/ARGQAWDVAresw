package com.edmi.service.service;

import com.alibaba.fastjson.JSONObject;
import com.edmi.entity.trackico.ICO_trackico_detail_blockTeam;
import com.edmi.entity.trackico.ICO_trackico_item;
import com.edmi.utils.http.exception.MethodNotSupportException;

/**
 * @author keshi
 * @ClassName: TrackicoService
 * @Description: 定义接口
 * @date 2018年7月30日 下午3:40:48
 */
public interface TrackicoService {
    /**
     * @Title: getICO_trackico_list
     * @Description: 抓取列表页的接口方法
     */
    public void getICO_trackico_list() throws MethodNotSupportException;

    /**
     * @Title: getICO_trackico_detail
     * @Description: 解析详情页的接口方法
     */
    public void getICO_trackico_detail(ICO_trackico_item item) throws MethodNotSupportException;

    //删除 详情
    public int deleteICO_trackico_detailByPk_id(long fk_id);

    //删除 详情 公司人员
    public int deleteICO_trackico_detai_blockTeamlByPk_id(long fk_id);

    //删除 详情 公司里程碑
    public int deleteICO_trackico_detai_blockMilestonesByPk_id(long fk_id);

    //删除 详情 公司标签连接
    public int deleteICO_trackico_detail_blockLabelByPk_id(long fk_id);

    //删除 详情 公司信息
    public int deleteICO_trackico_detail_block_infoByPk_id(long fk_id);

    //删除 详情 公司金融
    public int deleteICO_trackico_detail_blockFinancialByPk_id(long fk_id);

    public JSONObject getIco_trackico_detail_index();

    public ICO_trackico_item getICO_trackico_listByItemUrl(String url);

    public JSONObject getICO_trackico_detailByItemUrl(String url);

    //人员的社交链接
    public void extraMemberSocialLinks(ICO_trackico_detail_blockTeam member);

}