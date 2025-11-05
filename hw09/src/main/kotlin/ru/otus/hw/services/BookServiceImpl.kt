package ru.otus.hw.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.otus.hw.dto.BookResponseDto
import ru.otus.hw.exceptions.EntityNotFoundException
import ru.otus.hw.models.Book
import ru.otus.hw.repositories.AuthorRepository
import ru.otus.hw.repositories.BookRepository
import ru.otus.hw.repositories.GenreRepository

@Service
open class BookServiceImpl(
    private val authorRepository: AuthorRepository,
    private val genreRepository: GenreRepository,
    private val bookRepository: BookRepository
) : BookService {

    @Transactional(readOnly = true)
    override fun findById(id: Long): BookResponseDto = bookRepository.findById(id)
        ?.let { BookResponseDto.fromDomainObject(it) }
        ?: throw EntityNotFoundException("Book with id $id not found")

    @Transactional(readOnly = true)
    override fun findAll(): List<BookResponseDto> = bookRepository.findAll()
        .map { BookResponseDto.fromDomainObject(it) }

    @Transactional
    override fun insert(title: String, authorId: Long, genresIds: Set<Long>): BookResponseDto =
        save(0, title, authorId, genresIds)

    @Transactional
    override fun update(id: Long, title: String, authorId: Long, genresIds: Set<Long>): BookResponseDto =
        save(id, title, authorId, genresIds)

    @Transactional
    override fun deleteById(id: Long) = bookRepository.deleteById(id)

    private fun save(id: Long, title: String, authorId: Long, genresIds: Set<Long>): BookResponseDto {
        require(genresIds.isNotEmpty()) { "Genres ids must not be null" }

        if (id > 0 && !bookRepository.existsById(id)) throw EntityNotFoundException("Book with id $id not found")

        val author = authorRepository.findById(authorId)
            ?: throw EntityNotFoundException("Author with id $authorId not found")

        val genres = genreRepository.findAllByIdIn(genresIds)
        if (genres.isEmpty() || genresIds.size != genres.size) {
            throw EntityNotFoundException("One or all genres with ids $genresIds not found")
        }

        val book = Book(id, title, author, genres)
        bookRepository.save(book)

        return BookResponseDto.fromDomainObject(book)
    }
}
