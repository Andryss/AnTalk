package ru.andryss.antalk.server.repository

import org.springframework.data.repository.CrudRepository
import ru.andryss.antalk.server.entity.MessageEntity

interface MessageRepository : CrudRepository<MessageEntity,String>