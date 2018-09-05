package com.edmi.dao.coinschedule;

import com.edmi.entity.coinschedule.ICO_coinschedule_detail_member_sociallink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * 人员社交链接 对象 数据库操作
 */
public interface ICO_coinschedule_detail_member_sociallinkDao extends JpaRepository<ICO_coinschedule_detail_member_sociallink, Long> {

    /**
     * 一个人有可能有多个sociallink
     *
     * @param memberUrl
     * @return
     */
    List<ICO_coinschedule_detail_member_sociallink> findICO_coinschedule_detail_member_sociallinksByMemberUrl(String memberUrl);

}
