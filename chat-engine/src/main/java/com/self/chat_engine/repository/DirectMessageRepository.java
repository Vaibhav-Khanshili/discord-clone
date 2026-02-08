package com.self.chat_engine.repository;

import com.self.chat_engine.model.DirectMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessage, UUID> {

    @Query("SELECT dm FROM DirectMessage dm WHERE " +
            "(dm.sender.id = :userId1 AND dm.recipient.id = :userId2) OR " +
            "(dm.sender.id = :userId2 AND dm.recipient.id = :userId1) " +
            "AND dm.deleted = false ORDER BY dm.createdAt ASC")
    List<DirectMessage> findConversation(@Param("userId1") UUID userId1, @Param("userId2") UUID userId2);

    @Query("SELECT DISTINCT CASE " +
            "WHEN dm.sender.id = :userId THEN dm.recipient " +
            "ELSE dm.sender END FROM DirectMessage dm " +
            "WHERE dm.sender.id = :userId OR dm.recipient.id = :userId")
    List<Object> findUserConversations(@Param("userId") UUID userId);
}

