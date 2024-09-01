package ru.andryss.antalk.server.controller

import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.*
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import ru.andryss.antalk.common.message.ErrorMessage
import ru.andryss.antalk.server.exception.ChatNotFoundException
import ru.andryss.antalk.server.exception.UserNotFoundException

@RestControllerAdvice
class CustomControllerExceptionHandler {
    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(
        ConstraintViolationException::class,
        HttpMessageNotReadableException::class,
        MissingServletRequestPartException::class,
        MethodArgumentTypeMismatchException::class,
    )
    @ResponseStatus(BAD_REQUEST)
    fun handleBadRequest(e: Exception): ErrorMessage {
        log.info("caught ${e.javaClass.name} returned 400 (${e.message})")
        return ErrorMessage(e.message ?: "error")
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
    fun handleUnsupportedMediaType(e: Exception): ErrorMessage {
        log.info("caught HttpMediaTypeNotSupportedException returned 415 (${e.message})")
        return ErrorMessage(e.message ?: "error")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(BAD_REQUEST)
    fun handleMethodArgumentNotValid(e: MethodArgumentNotValidException): ErrorMessage {
        val message = e.detailMessageArguments[1] as String
        log.info("caught MethodArgumentNotValidException returned 400 ($message)")
        return ErrorMessage(message)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    @ResponseStatus(BAD_REQUEST)
    fun handleMissingServletRequestParameter(e: MissingServletRequestParameterException): ErrorMessage {
        log.info("caught MissingServletRequestParameterException returned 400 (${e.parameterName})")
        return ErrorMessage("request parameter ${e.parameterName} is missing")
    }

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(NOT_FOUND)
    fun handleUserNotFound(e: UserNotFoundException): ErrorMessage {
        log.info("caught UserNotFoundException returned 404 (${e.message})")
        return ErrorMessage("user ${e.message} not found")
    }

    @ExceptionHandler(ChatNotFoundException::class)
    @ResponseStatus(NOT_FOUND)
    fun handleChatNotFound(e: ChatNotFoundException): ErrorMessage {
        log.info("caught ChatNotFoundException returned 404 (${e.message})")
        return ErrorMessage("chat ${e.message} not found")
    }
}