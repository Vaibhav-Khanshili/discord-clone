package com.self.chat_engine.repository;

import com.self.chat_engine.model.Channel;
import com.self.chat_engine.model.enums.ChannelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

    List<Channel> findByServerIdOrderByCreatedAtAsc(UUID serverId);
    long countByServerId(UUID serverId);

    List<Channel> findAllByServerIdAndType(UUID serverId, ChannelType type);

    @Query("SELECT c FROM Channel c LEFT JOIN FETCH c.messages WHERE c.id = :channelId")
    Optional<Channel> findIdWithMessages(@Param("channelid") UUID channelId);

    boolean existsByIdAndServerId(UUID channelId, UUID serverId);

    long countByServerid(UUID serverId);


}
