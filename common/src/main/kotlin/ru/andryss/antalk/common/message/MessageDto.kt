package ru.andryss.antalk.common.message

import java.time.Instant

data class MessageDto(
    val id: String,
    val senderId: String,
    val text: String,
    val timestamp: Instant,
)