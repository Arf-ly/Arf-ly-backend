package com.capstone.arfly.community.repository;

import com.capstone.arfly.community.domain.Post;
import com.capstone.arfly.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    @EntityGraph(attributePaths = {"member"})
    Optional<Post> findById(Long id);

    Long countByMember(Member member);

    List<Post> findAllByMemberIdOrderByCreatedAtDesc(Long memberId);
}
