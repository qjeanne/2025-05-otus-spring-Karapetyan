package ru.otus.hw.services

import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.otus.hw.repositories.UserRepository

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : ReactiveUserDetailsService {
    override fun findByUsername(username: String): Mono<UserDetails> =
        userRepository.findByUsernameValue(username)
            .map { it as UserDetails }
}
