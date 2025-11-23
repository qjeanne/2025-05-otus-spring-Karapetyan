package ru.otus.hw.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.otus.hw.dto.GenreResponseDto
import ru.otus.hw.repositories.GenreRepository

@Service
open class GenreServiceImpl(
    private val genreRepository: GenreRepository
) : GenreService {
    @Transactional(readOnly = true)
    override fun findAll(): List<GenreResponseDto> = genreRepository.findAll()
        .map { GenreResponseDto.fromDomainObject(it) }
}
