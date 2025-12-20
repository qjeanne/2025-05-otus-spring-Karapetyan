package ru.otus.hw.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import ru.otus.hw.dto.GenreResponseDto
import ru.otus.hw.repositories.GenreRepository

@Service
open class GenreServiceImpl(
    private val genreRepository: GenreRepository
) : GenreService {
    @Transactional(readOnly = true)
    override fun findAll(): Flux<GenreResponseDto> = genreRepository.findAll()
        .map { GenreResponseDto.fromDomainObject(it) }
}
