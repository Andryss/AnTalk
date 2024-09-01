package ru.andryss.antalk.server.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "sessions")
class SessionEntity(
    @Id
    val userId: String,
    val session: String,
)