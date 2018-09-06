package com.edmi.dao.trackico;

import com.edmi.entity.trackico.ICO_trackico_detail_block_team_sociallink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * trackico 人员社交链接对象数据库操作
 */
public interface ICO_trackico_detail_block_team_sociallinkRepository extends JpaRepository<ICO_trackico_detail_block_team_sociallink, Long> {

    /**
     * 检验是否解析过，一个人有可能有多个sociallink
     */
    List<ICO_trackico_detail_block_team_sociallink> findICO_trackico_detail_block_team_sociallinksByMemberUrl(String MemberUrl);
}
