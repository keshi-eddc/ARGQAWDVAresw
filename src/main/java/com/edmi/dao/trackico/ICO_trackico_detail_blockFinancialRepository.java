package com.edmi.dao.trackico;

import org.springframework.data.jpa.repository.JpaRepository;
import com.edmi.entity.trackico.ICO_trackico_detail_blockFinancial;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author keshi
 * @ClassName: ICO_trackico_detail_blockFinancialRepository
 * @Description: 负责公司金融 对象的数据库操作
 * @date 2018年8月6日 下午3:50:55
 */
public interface ICO_trackico_detail_blockFinancialRepository extends JpaRepository<ICO_trackico_detail_blockFinancial, Long> {
    //删除detail
    @Transactional
    @Modifying
    @Query("delete from ICO_trackico_detail_blockFinancial  where fk_id = :fk_id")
    int deleteICO_trackico_detail_blockFinancialByPk_id(@Param("fk_id") long fk_id);
}
