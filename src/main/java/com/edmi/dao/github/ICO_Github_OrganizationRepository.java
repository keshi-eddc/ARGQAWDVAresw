package com.edmi.dao.github;

import com.edmi.entity.github.ICO_Github_Links;
import com.edmi.entity.github.ICO_Github_Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICO_Github_OrganizationRepository extends JpaRepository<ICO_Github_Organization,Long> {

    @Query("from ICO_Github_Organization org where org.status=:status")
    List<ICO_Github_Organization> getICO_Github_OrganizationByStatus(@Param("status")String status);
}
