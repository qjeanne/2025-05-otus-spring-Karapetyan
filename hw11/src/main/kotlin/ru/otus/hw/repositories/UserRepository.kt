package ru.otus.hw.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.otus.hw.models.User

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsernameValue(username: String): User?
}
