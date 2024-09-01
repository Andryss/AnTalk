package ru.andryss.antalk.server.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import ru.andryss.antalk.server.entity.UpdateEntity

interface UpdateRepository: CrudRepository<UpdateEntity, String> {
    fun findByPrev(prev: Long?): UpdateEntity?
    @Query("select * from updates order by id desc limit 1", nativeQuery = true)
    fun findLast(): UpdateEntity?
}