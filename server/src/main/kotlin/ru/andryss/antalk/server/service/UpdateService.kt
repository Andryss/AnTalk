package ru.andryss.antalk.server.service

interface UpdateService {
    fun subscribe(userId: String, session: String, lastUpdate: Long?)
    fun unsubscribe(session: String)
    fun sendMessage(senderId: String, chatId: String, text: String)
}