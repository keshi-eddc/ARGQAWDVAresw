package com.edmi.dao.icorating;

import com.edmi.entity.icocrunch.Ico_icocrunch_detail;
import com.edmi.entity.icorating.ICO_icorating_detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    @Query("select it from ICO_icorating_detail it where it.link = :link ")
   ICO_icorating_detail getICO_icorating_detailByLink(@Param("link")String link);

}
