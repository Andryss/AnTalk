package ru.andryss.antalk.server.interactor

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.andryss.antalk.server.service.UpdateService

@Component
class UpdateInteractorImpl(
    val updateService: UpdateService
) : UpdateInteractor {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun subscribeUser(userId: String, session: String, lastUpdate: Long?) {
        log.info("Got subscription for user $userId (session $session last update id $lastUpdate)")
        updateService.subscribe(userId, session, lastUpdate)
    }

    override fun unsubscribeSession(session: String) {
        log.info("Got unsubscribe for session $session")
        updateService.unsubscribe(session)
    }
}