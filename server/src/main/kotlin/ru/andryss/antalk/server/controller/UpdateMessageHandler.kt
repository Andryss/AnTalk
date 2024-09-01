package ru.andryss.antalk.server.controller

import org.springframework.context.event.EventListener
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import ru.andryss.antalk.server.interactor.UpdateInteractor

@Controller
class UpdateMessageHandler(
    val interactor: UpdateInteractor
) {
    @SubscribeMapping("/user/{userId}/updates")
    fun handleSubscription(
        @DestinationVariable userId: String,
        @Header simpSessionId: String,
        @Header(required = false) lastUpdate: Long?,
    ) {
        interactor.subscribeUser(userId, simpSessionId, lastUpdate)
    }

    @EventListener
    fun handleUnsubscribe(
        event: SessionDisconnectEvent
    ) {
        interactor.unsubscribeSession(event.sessionId)
    }
}