package ru.otus.hw.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.otus.hw.exceptions.EntityNotFoundException
import ru.otus.hw.models.Book
import ru.otus.hw.repositories.AuthorRepository
import ru.otus.hw.repositories.BookRepository
import ru.otus.hw.repositories.GenreRepository
import kotlin.jvm.optionals.getOrNull

@Service
open class BookServiceImpl(
    private val authorRepository: AuthorRepository,
    private val genreRepository: GenreRepository,
    private val bookRepository: BookRepository
) : BookService {

    @Transactional(readOnly = true)
    override fun findById(id: Long): Book? = bookRepository.findById(id).getOrNull()

    @Transactional(readOnly = true)
    override fun findAll(): List<Book> = bookRepository.findAll()

    @Transactional
    override fun insert(title: String, authorId: Long, genresIds: Set<Long>): Book =
        save(0, title, authorId, genresIds)

    @Transactional
    override fun update(id: Long, title: String, authorId: Long, genresIds: Set<Long>): Book =
        save(id, title, authorId, genresIds)

    @Transactional
    override fun deleteById(id: Long) = bookRepository.deleteById(id)

    private fun save(id: Long, title: String, authorId: Long, genresIds: Set<Long>): Book {
        require(genresIds.isNotEmpty()) { "Genres ids must not be null" }

        val author = authorRepository.findById(authorId).orElseThrow {
            EntityNotFoundException("Author with id $authorId not found")
        }

        val genres = genreRepository.findAllById(genresIds)
        if (genres.isEmpty() || genresIds.size != genres.size) {
            throw EntityNotFoundException("One or all genres with ids $genresIds not found")
        }

        val book = Book(id, title, author, genres)
        return bookRepository.save(book)
    }
}
