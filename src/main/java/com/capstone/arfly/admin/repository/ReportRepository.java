package com.capstone.arfly.admin.repository;

import com.capstone.arfly.admin.domain.Report;
import com.capstone.arfly.admin.domain.ReportStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByReporterIdAndPostId(Long reporterId, Long postId);

    @EntityGraph(attributePaths = {"reporter", "post"})
    List<Report> findAllByStatusOrderByCreatedAtAsc(ReportStatus status);

    @EntityGraph(attributePaths = {"reporter", "post", "post.member"})
    Optional<Report> findWithReporterAndPostById(Long id);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Report r WHERE r.post.id = :postId")
    void deleteByPostId(@Param("postId") Long postId);
}
