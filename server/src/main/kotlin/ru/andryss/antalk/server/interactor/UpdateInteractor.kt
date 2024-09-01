package ru.andryss.antalk.server.interactor

interface UpdateInteractor {
    fun subscribeUser(userId: String, session: String, lastUpdate: Long?)
    fun unsubscribeSession(session: String)
}