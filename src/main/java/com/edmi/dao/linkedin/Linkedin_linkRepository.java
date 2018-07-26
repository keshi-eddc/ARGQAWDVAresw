package com.edmi.dao.linkedin;

import com.edmi.entity.linkedin.ICO_Linkedin_Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface Linkedin_linkRepository extends JpaRepository<ICO_Linkedin_Link,Long> {

    @Query("select l from ICO_Linkedin_Link l where l.standard_link is null")
    List<ICO_Linkedin_Link> getICO_Linkedin_Link();

    @Modifying
    @Transactional
    @Query("update ICO_Linkedin_Link l set l.standard_link = :standard_link where l.id = :id")
    int updateICO_Linkedin_LinkById(@Param("standard_link")String standard_link,@Param("id")long id);

    @Query("select l from ICO_Linkedin_Link l where l.standard_link = :standard_link")
    ICO_Linkedin_Link getICO_Linkedin_LinkBystandard_link(@Param("standard_link")String standard_link);
}
