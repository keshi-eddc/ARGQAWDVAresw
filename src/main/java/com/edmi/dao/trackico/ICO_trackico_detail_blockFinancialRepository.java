package com.edmi.dao.trackico;

import com.edmi.entity.trackico.ICO_trackico_detail;
import org.springframework.data.jpa.repository.JpaRepository;
import com.edmi.entity.trackico.ICO_trackico_detail_blockFinancial;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


    @Query("select fina from ICO_trackico_detail_blockFinancial fina where fina.ico_trackico_detail.pk_id = :fk_id ")
    List<ICO_trackico_detail_blockFinancial> getICO_trackico_detail_blockFinancialsByFkid(@Param("fk_id") long fk_id);
}
