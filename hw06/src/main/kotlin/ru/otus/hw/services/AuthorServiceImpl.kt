package ru.otus.hw.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.otus.hw.models.Author
import ru.otus.hw.repositories.AuthorRepository

@Service
open class AuthorServiceImpl(
    private val authorRepository: AuthorRepository
) : AuthorService {
    @Transactional(readOnly = true)
    override fun findAll(): List<Author> = authorRepository.findAll()
}
