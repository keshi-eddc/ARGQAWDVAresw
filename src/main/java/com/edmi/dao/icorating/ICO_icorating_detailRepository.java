package com.edmi.dao.icorating;

import com.edmi.entity.icocrunch.Ico_icocrunch_detail;
import com.edmi.entity.icorating.ICO_icorating_detail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 详情对象数据库操作
 */
public interface ICO_icorating_detailRepository extends JpaRepository<ICO_icorating_detail, Long> {
    /**
     * 根据item Url 查在本表是否存在
     */
    Boolean findByLink(String link);

    /**
     * 根据item Url 查在本表是集合
     */
    List<ICO_icorating_detail> getICO_icorating_detailByLink(String link);
}
