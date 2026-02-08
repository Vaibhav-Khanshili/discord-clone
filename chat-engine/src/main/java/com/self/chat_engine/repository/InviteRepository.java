package com.self.chat_engine.repository;

import com.self.chat_engine.model.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface InviteRepository extends JpaRepository<Invite, UUID> {
    @Query("SELECT i FROM Invite JOIN FETCH i.server WHERE i.code = :code")
    Optional<Invite> findByCode(@Param("code") String code);

    @Query("SELECT i FROM Invite i WHERE i.server.id = :serverId")
    List<Invite> findAllByServerId(@Param("serverId") UUID serverId);

    @Query("DELETE FROM Invite i WHERE i.code = :code")
    void deleteByCode(@Param("code") String code);
}
