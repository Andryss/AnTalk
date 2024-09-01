package ru.andryss.antalk.server.repository

import org.springframework.data.repository.CrudRepository
import ru.andryss.antalk.server.entity.ChatEntity

interface ChatRepository : CrudRepository<ChatEntity,String>