package ru.otus.hw.services

import org.springframework.stereotype.Service
import ru.otus.hw.exceptions.EntityNotFoundException
import ru.otus.hw.models.Book
import ru.otus.hw.repositories.AuthorRepository
import ru.otus.hw.repositories.BookRepository
import ru.otus.hw.repositories.GenreRepository

@Service
class BookServiceImpl(
    private val authorRepository: AuthorRepository,
    private val genreRepository: GenreRepository,
    private val bookRepository: BookRepository
) : BookService {

    override fun findById(id: Long): Book? = bookRepository.findById(id)

    override fun findAll(): List<Book> = bookRepository.findAll()

    override fun insert(title: String, authorId: Long, genresIds: Set<Long>): Book =
        save(0, title, authorId, genresIds)

    override fun update(id: Long, title: String, authorId: Long, genresIds: Set<Long>): Book =
        save(id, title, authorId, genresIds)

    override fun deleteById(id: Long) {
        bookRepository.deleteById(id)
    }

    private fun save(id: Long, title: String, authorId: Long, genresIds: Set<Long>): Book {
        require(genresIds.isNotEmpty()) { "Genres ids must not be null" }

        val author = authorRepository.findById(authorId)
            ?: throw EntityNotFoundException("Author with id $authorId not found")

        val genres = genreRepository.findAllByIds(genresIds)
        if (genres.isEmpty() || genresIds.size != genres.size) {
            throw EntityNotFoundException("One or all genres with ids $genresIds not found")
        }

        val book = Book(id, title, author, genres)
        return bookRepository.save(book)
    }
}
