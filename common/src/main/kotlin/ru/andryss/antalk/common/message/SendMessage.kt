package ru.andryss.antalk.common.message

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class SendMessage(
    @get:NotNull
    val senderId: Long?,
    @get:NotNull
    val chatId: Long?,
    @get:NotBlank
    val text: String?,
)