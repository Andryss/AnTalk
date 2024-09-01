package ru.andryss.antalk.common.message

class UpdateDto(
    val id: Long,
    val type: UpdateType,
    val chat: ChatDto? = null,
    val chatId: String? = null,
    val message: MessageDto,
)