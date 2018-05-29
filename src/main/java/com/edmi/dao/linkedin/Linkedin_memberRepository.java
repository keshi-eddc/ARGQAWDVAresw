package com.edmi.dao.linkedin;

import com.edmi.entity.linkedin.ICO_Linkedin_Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Linkedin_memberRepository extends JpaRepository<ICO_Linkedin_Member,Long> {

    @Query("from ICO_Linkedin_Member m where m.link_id=:link_id")
    ICO_Linkedin_Member getICO_Linkedin_MemberByLinkId(@Param("link_id")Long link_id);

}
