package ru.andryss.antalk.server.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.CrudRepository
import ru.andryss.antalk.server.entity.SessionEntity

interface SessionRepository: CrudRepository<SessionEntity, String> {
    @Modifying
    @Transactional
    fun deleteBySession(session: String)
}