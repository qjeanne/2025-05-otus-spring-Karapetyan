package ru.otus.hw.services

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import ru.otus.hw.exceptions.EntityNotFoundException
import ru.otus.hw.repositories.UserRepository

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails =
        username?.let {
            userRepository.findByUsernameValue(it)
        } ?: throw EntityNotFoundException.UserNotFound(username ?: "")
}
