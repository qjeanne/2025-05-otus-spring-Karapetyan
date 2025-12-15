package ru.otus.hw.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.otus.hw.dto.BookResponseDto
import ru.otus.hw.exceptions.EntityNotFoundException
import ru.otus.hw.models.Book
import ru.otus.hw.repositories.AuthorRepository
import ru.otus.hw.repositories.BookRepository
import ru.otus.hw.repositories.CommentRepository
import ru.otus.hw.repositories.GenreRepository

@Service
open class BookServiceImpl(
    private val authorRepository: AuthorRepository,
    private val genreRepository: GenreRepository,
    private val bookRepository: BookRepository,
    private val commentRepository: CommentRepository
) : BookService {

    @Transactional(readOnly = true)
    override fun findById(id: String): Mono<BookResponseDto> = bookRepository.findById(id)
        .switchIfEmpty(Mono.error(EntityNotFoundException.BookNotFound(id)))
        .map { BookResponseDto.fromDomainObject(it) }

    @Transactional(readOnly = true)
    override fun findAll(): Flux<BookResponseDto> = bookRepository.findAll()
        .map { BookResponseDto.fromDomainObject(it) }

    @Transactional
    override fun insert(title: String, authorId: String, genresIds: Set<String>): Mono<BookResponseDto> =
        save(null, title, authorId, genresIds)

    @Transactional
    override fun update(id: String, title: String, authorId: String, genresIds: Set<String>): Mono<BookResponseDto> =
        save(id, title, authorId, genresIds)

    @Transactional
    override fun deleteById(id: String): Mono<Void> = bookRepository.deleteById(id)

    private fun save(id: String?, title: String, authorId: String, genresIds: Set<String>): Mono<BookResponseDto> {
        require(genresIds.isNotEmpty()) { "Genres ids must not be null" }

        val bookCheckMono = if (id != null) {
            bookRepository.existsById(id)
                .flatMap { exists ->
                    if (!exists) Mono.error(EntityNotFoundException.BookNotFound(id))
                    else Mono.just(Unit)
                }
        } else Mono.just(Unit)

        val authorMono = authorRepository.findById(authorId)
            .switchIfEmpty(Mono.error(EntityNotFoundException.AuthorNotFound(authorId)))

        val genresMono = genreRepository.findAllByIdIn(genresIds)
            .collectList()
            .filter { it.size == genresIds.size }
            .switchIfEmpty(Mono.error(EntityNotFoundException.GenresNotFound(genresIds)))

        return bookCheckMono
            .then(authorMono)
            .zipWith(genresMono) { author, genres ->
                Book(id, title, author, genres)
            }
            .flatMap { book ->
                if (id != null) {
                    commentRepository.updateBook(id, book)
                        .then(bookRepository.save(book))
                } else {
                    bookRepository.save(book)
                }
            }
            .map { BookResponseDto.fromDomainObject(it) }
    }
}
