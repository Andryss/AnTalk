package ru.andryss.antalk.server.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.UUID
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "chats")
class ChatEntity (
    @Id
    @GeneratedValue(strategy = UUID)
    var id: String? = null,
    val creatorId: String,
    val createdAt: Instant,
    val recipientId: String,
)