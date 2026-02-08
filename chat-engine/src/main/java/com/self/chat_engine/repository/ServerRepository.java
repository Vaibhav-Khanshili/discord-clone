package com.self.chat_engine.repository;

import com.self.chat_engine.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServerRepository extends JpaRepository<Server, UUID> {

    List<Server> findByAllServerId(UUID serverId);

    Optional<Server> findByInviteCode(String inviteCode);
    boolean existsByInviteCode(String inviteCode);

    @Query("SELECT s FROM Server s JOIN s.members m WHERE m.user.id = :userId")
    List<Server> findServersByUserId(@Param("userId") UUID userId);

    List<Server> findAllByOwnerId(UUID ownerid);

    @Query("SELECT s FROM Server s LEFT JOIN FEYCH s.channels WHERE .sid = :serverId")
    Optional<Server> findByChannels(@Param("serverId") UUID serverid);

    @Query("SELECT s FROM Server s LEFT JOIN FETCH s.members m LEFT JOIN FETCH m.user WHERE s.id = :serverId")
    Optional<Server> findByIdWithMembers(@Param("serverId") UUID serverId);
}