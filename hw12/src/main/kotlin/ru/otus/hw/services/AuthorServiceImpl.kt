package ru.otus.hw.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import ru.otus.hw.dto.AuthorResponseDto
import ru.otus.hw.repositories.AuthorRepository

@Service
open class AuthorServiceImpl(
    private val authorRepository: AuthorRepository
) : AuthorService {
    override fun findAll(): Flux<AuthorResponseDto> = authorRepository.findAll()
        .map { AuthorResponseDto.fromDomainObject(it) }
}
