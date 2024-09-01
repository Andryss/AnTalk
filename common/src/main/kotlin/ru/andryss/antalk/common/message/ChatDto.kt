package ru.andryss.antalk.common.message

import java.time.Instant

class ChatDto(
    val id: String,
    val creator: UserDto,
    val createdAt: Instant,
    val recipient: UserDto,
)