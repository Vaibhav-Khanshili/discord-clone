package com.self.chat_engine.model;

import com.self.chat_engine.model.enums.MemberRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "members", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "server_id"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id", nullable = false)
    private Server server;

    @Column(nullable = false)
    private String role; // OWNER, ADMIN, MODERATOR, MEMBER

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @Enumerated(EnumType.STRING)


    public boolean isModerator() {
        if (this.role == null) return false;
        return this.role == MemberRole.OWNER || this.role == MemberRole.MODERATOR;
    }
}
