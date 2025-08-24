package ru.otus.hw.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.otus.hw.models.Genre
import ru.otus.hw.repositories.GenreRepository

@Service
open class GenreServiceImpl(
    private val genreRepository: GenreRepository
) : GenreService {
    @Transactional(readOnly = true)
    override fun findAll(): List<Genre> = genreRepository.findAll()
}
