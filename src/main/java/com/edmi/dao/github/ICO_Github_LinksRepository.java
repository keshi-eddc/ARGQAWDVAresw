package com.edmi.dao.github;

import com.edmi.entity.github.ICO_Github_Links;
import com.edmi.entity.linkedin.ICO_Linkedin_Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICO_Github_LinksRepository extends JpaRepository<ICO_Github_Links,Long> {

    @Query("from ICO_Github_Links l where l.status=:status")
    List<ICO_Github_Links> getICO_Github_LinksByStatus(@Param("status")String status);
}
