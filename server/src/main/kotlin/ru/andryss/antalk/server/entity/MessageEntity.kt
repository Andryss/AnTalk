package ru.andryss.antalk.server.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.UUID
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "messages")
class MessageEntity(
    @Id
    @GeneratedValue(strategy = UUID)
    var id: String? = null,
    var chatId: String,
    var senderId: String,
    var text: String,
    var timestamp: Instant = Instant.now(),
)