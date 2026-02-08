package com.self.chat_engine.repository;

import com.self.chat_engine.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    List<Member> findByServerId(UUID serverId);

    Optional<Member> findByUserIdAndServerId(UUID userId, UUID serverId);

    boolean existsByUserIdAndServerId(UUID userId, UUID serverId);

    void deleteByUserIdAndServerId(UUID userId, UUID serverId);
}