package ru.andryss.antalk.server.service

import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import ru.andryss.antalk.common.message.ChatDto
import ru.andryss.antalk.common.message.MessageDto
import ru.andryss.antalk.common.message.UpdateDto
import ru.andryss.antalk.common.message.UpdateType.CHAT_CREATED
import ru.andryss.antalk.common.message.UpdateType.MESSAGE_SENT
import ru.andryss.antalk.common.message.UserDto
import ru.andryss.antalk.server.entity.MessageEntity
import ru.andryss.antalk.server.entity.SessionEntity
import ru.andryss.antalk.server.entity.UpdateEntity
import ru.andryss.antalk.server.exception.AlreadySubscribedException
import ru.andryss.antalk.server.exception.ChatNotFoundException
import ru.andryss.antalk.server.exception.UserNotFoundException
import ru.andryss.antalk.server.repository.*

@Service
class UpdateServiceImpl(
    val updateRepository: UpdateRepository,
    val chatRepository: ChatRepository,
    val userRepository: UserRepository,
    val messageRepository: MessageRepository,
    val sessionRepository: SessionRepository,
    val transactionTemplate: TransactionTemplate,
    val messageTemplate: SimpMessagingTemplate,
) : UpdateService {
    override fun subscribe(userId: String, session: String, lastUpdate: Long?) {
        sessionRepository.findById(userId).ifPresent {
            if (it.session != session) throw AlreadySubscribedException(userId)
        }
        var last = lastUpdate
        while (true) {
            val nextUpdate = transactionTemplate.execute {
                val nextUpdate = updateRepository.findByPrev(last)
                if (nextUpdate == null) sessionRepository.save(SessionEntity(userId, session))
                nextUpdate
            } ?: return
            sendToUser(userId, nextUpdate)
            last = nextUpdate.id
        }
    }

    override fun unsubscribe(session: String) {
        sessionRepository.deleteBySession(session)
    }

    override fun sendMessage(senderId: String, chatId: String, text: String) {
        userRepository.findById(senderId).orElseThrow { UserNotFoundException(senderId) }
        val chat = chatRepository.findById(chatId).orElseThrow { ChatNotFoundException(chatId) }
        if (senderId != chat.creatorId && senderId != chat.recipientId) throw ChatNotFoundException(chatId)
        val message = messageRepository.save(MessageEntity(
            chatId = chatId, senderId = senderId, text = text
        ))
        val update = transactionTemplate.execute {
            val lastUpdateId = updateRepository.findLast()?.id
            updateRepository.save(UpdateEntity(
                prev = lastUpdateId, type = MESSAGE_SENT, chatId = chatId, messageId = message.id!!
            ))
        }!!
        if (sessionRepository.existsById(chat.creatorId)) sendToUser(chat.creatorId, update)
        if (sessionRepository.existsById(chat.recipientId)) sendToUser(chat.recipientId, update)
    }

    private fun sendToUser(userId: String, update: UpdateEntity) {
        val chat = chatRepository.findById(update.chatId).get()
        if (userId != chat.creatorId && userId != chat.recipientId) return
        val updateDto = when (update.type) {
            CHAT_CREATED -> {
                val creatorDto = userDtoById(chat.creatorId)
                val recipientDto = userDtoById(chat.recipientId)
                val chatDto = ChatDto(chat.id!!, creatorDto, chat.createdAt, recipientDto)
                val message = messageRepository.findById(update.messageId).get()
                val messageDto = MessageDto(message.id!!, message.senderId, message.text, message.timestamp)
                UpdateDto(id = update.id!!, type = update.type, chat = chatDto, message = messageDto)
            }
            MESSAGE_SENT -> {
                val message = messageRepository.findById(update.messageId).get()
                val messageDto = MessageDto(message.id!!, message.senderId, message.text, message.timestamp)
                UpdateDto(id = update.id!!, type = update.type, chatId = update.chatId, message = messageDto)
            }
        }
        messageTemplate.convertAndSendToUser(userId, "/updates", updateDto)
    }

    private fun userDtoById(userId: String): UserDto {
        return userRepository.findById(userId).get().let { UserDto(it.id!!, it.name) }
    }
}