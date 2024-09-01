package ru.andryss.antalk.server.entity

import jakarta.persistence.*
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.GenerationType.IDENTITY
import ru.andryss.antalk.common.message.UpdateType

@Entity
@Table(name = "updates")
class UpdateEntity(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null,
    val prev: Long?,
    @Enumerated(STRING)
    val type: UpdateType,
    val chatId: String,
    val messageId: String,
)