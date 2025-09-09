package ru.otus.hw.services

import org.springframework.stereotype.Service
import ru.otus.hw.models.Author
import ru.otus.hw.repositories.AuthorRepository

@Service
open class AuthorServiceImpl(
    private val authorRepository: AuthorRepository
) : AuthorService {
    override fun findAll(): List<Author> = authorRepository.findAll()
}
