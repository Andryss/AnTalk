package ru.andryss.antalk.server.repository

import org.springframework.data.repository.CrudRepository
import ru.andryss.antalk.server.entity.UserEntity

interface UserRepository : CrudRepository<UserEntity,String>