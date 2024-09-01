package ru.andryss.antalk.server.controller

import org.slf4j.LoggerFactory
import org.springframework.messaging.MessagingException
import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ControllerAdvice
import ru.andryss.antalk.server.exception.AnTalkInternalException

@ControllerAdvice
class CustomMessageExceptionHandler {
    private val log = LoggerFactory.getLogger(this::class.java)

    @MessageExceptionHandler
    fun handleMessagingException(e: MessagingException) {
        val message = e.mostSpecificCause.message
        val trimmed = message?.subSequence(0, message.lastIndexOf('\n'))
        log.warn("Caught MessagingException for message ${e.failedMessage} (${trimmed})")
    }

    @MessageExceptionHandler
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException) {
        val sb = StringBuilder()
        e.bindingResult?.allErrors?.forEach {
            if (it is FieldError) sb.append(it.field).append(" - ").append(it.defaultMessage).append("; ")
        }
        if (sb.isNotEmpty()) sb.setLength(sb.length - 1)
        log.warn("Caught MethodArgumentNotValidException for message ${e.failedMessage} ($sb)")
    }

    @MessageExceptionHandler
    fun handleInternalException(e: AnTalkInternalException) {
        log.warn("Caught AnTalkInternalException: ${e.javaClass.simpleName}: ${e.message}")
    }

}