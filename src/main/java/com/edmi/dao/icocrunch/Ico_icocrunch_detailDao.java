package com.edmi.dao.icocrunch;

import com.edmi.entity.icocrunch.Ico_icocrunch_detail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Ico_icocrunch_detailDao extends JpaRepository<Ico_icocrunch_detail,Long> {

    @Query("select d from Ico_icocrunch_detail d  order by d.pkId asc")
    Page<Ico_icocrunch_detail> getIco_icocrunch_detailPageable(Pageable pageable);

    @Query("select d from Ico_icocrunch_detail d where d.icoCrunchUrl = :icoCrunchUrl")
    Ico_icocrunch_detail getIco_icocrunch_detailByICOCrunchUrl(@Param("icoCrunchUrl") String icoCrunchUrl);

}
