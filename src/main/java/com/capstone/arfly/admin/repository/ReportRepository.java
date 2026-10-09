package com.capstone.arfly.admin.repository;

import com.capstone.arfly.admin.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByReporterIdAndPostId(Long reporterId, Long postId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Report r WHERE r.post.id = :postId")
    void deleteByPostId(@Param("postId") Long postId);
}
