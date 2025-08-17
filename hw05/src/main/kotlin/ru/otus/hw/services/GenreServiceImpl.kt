package ru.otus.hw.services

import org.springframework.stereotype.Service
import ru.otus.hw.models.Genre
import ru.otus.hw.repositories.GenreRepository

@Service
class GenreServiceImpl(
    private val genreRepository: GenreRepository
) : GenreService {
    override fun findAll(): List<Genre> = genreRepository.findAll()
}
