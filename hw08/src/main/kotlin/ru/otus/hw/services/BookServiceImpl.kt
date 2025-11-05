package ru.otus.hw.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.otus.hw.exceptions.EntityNotFoundException
import ru.otus.hw.models.Book
import ru.otus.hw.repositories.AuthorRepository
import ru.otus.hw.repositories.BookRepository
import ru.otus.hw.repositories.CommentRepository
import ru.otus.hw.repositories.GenreRepository
import kotlin.jvm.optionals.getOrNull

@Service
open class BookServiceImpl(
    private val authorRepository: AuthorRepository,
    private val genreRepository: GenreRepository,
    private val commentRepository: CommentRepository,
    private val bookRepository: BookRepository,
) : BookService {

    @Transactional(readOnly = true)
    override fun findById(id: String): Book? = bookRepository.findById(id).getOrNull()

    @Transactional(readOnly = true)
    override fun findAll(): List<Book> = bookRepository.findAll()

    @Transactional
    override fun insert(title: String, authorId: String, genresIds: Set<String>): Book =
        save(null, title, authorId, genresIds)

    @Transactional
    override fun update(id: String, title: String, authorId: String, genresIds: Set<String>): Book =
        save(id, title, authorId, genresIds)

    @Transactional
    override fun deleteById(id: String) {
        commentRepository.deleteByBookId(id)
        bookRepository.deleteById(id)
    }

    private fun save(id: String?, title: String, authorId: String, genresIds: Set<String>): Book {
        require(genresIds.isNotEmpty()) { "Genres ids must not be null" }

        val author = authorRepository.findById(authorId).orElseThrow {
            EntityNotFoundException("Author with id $authorId not found")
        }

        val genres = genreRepository.findAllById(genresIds)
        if (genres.isEmpty() || genresIds.size != genres.size) {
            throw EntityNotFoundException("One or all genres with ids $genresIds not found")
        }

        val book = Book(id, title, author, genres)
        if (id != null) {
            commentRepository.saveAll(
                commentRepository.findByBookId(id)
                    .onEach { it.book = book }
            )
        }
        return bookRepository.save(book)
    }
}
