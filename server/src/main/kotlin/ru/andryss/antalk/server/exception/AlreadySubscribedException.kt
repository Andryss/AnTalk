package ru.andryss.antalk.server.exception

class AlreadySubscribedException(userId: String): AnTalkInternalException(userId)