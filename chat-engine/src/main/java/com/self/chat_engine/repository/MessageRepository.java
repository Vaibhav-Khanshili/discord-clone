package com.self.chat_engine.repository;

import com.self.chat_engine.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByChannelIdAndDeletedFalseOrderByCreatedAtAsc(UUID channelId);

    List<Message> findTop50ByChannelIdAndDeletedFalseOrderByCreatedAtDesc(UUID channelId);
}
