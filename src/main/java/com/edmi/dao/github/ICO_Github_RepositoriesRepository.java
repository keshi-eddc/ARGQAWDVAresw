package com.edmi.dao.github;

import com.edmi.entity.github.ICO_Github_Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICO_Github_RepositoriesRepository extends JpaRepository<ICO_Github_Repositories,Long> {

    @Query("from ICO_Github_Repositories ogr where ogr.detail_status=:detail_status")
    List<ICO_Github_Repositories> getICO_Github_RepositoriesByStatus(@Param("detail_status")String detail_status);
}
