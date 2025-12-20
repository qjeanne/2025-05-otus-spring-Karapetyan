package ru.otus.hw.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.otus.hw.dto.CommentResponseDto
import ru.otus.hw.exceptions.EntityNotFoundException
import ru.otus.hw.models.Comment
import ru.otus.hw.repositories.BookRepository
import ru.otus.hw.repositories.CommentRepository

@Service
open class CommentServiceImpl(
    private val bookRepository: BookRepository,
    private val commentRepository: CommentRepository
) : CommentService {

    @Transactional(readOnly = true)
    override fun findById(id: String): Mono<CommentResponseDto> = commentRepository.findById(id)
        .switchIfEmpty(Mono.error(EntityNotFoundException.CommentNotFound(id)))
        .map { CommentResponseDto.fromDomainObject(it) }

    @Transactional(readOnly = true)
    override fun findByBookId(id: String): Flux<CommentResponseDto> = commentRepository.findByBookId(id)
        .map { CommentResponseDto.fromDomainObject(it) }

    @Transactional
    override fun insert(text: String, bookId: String): Mono<CommentResponseDto> =
        save(null, text, bookId)

    @Transactional
    override fun update(id: String, text: String, bookId: String): Mono<CommentResponseDto> =
        save(id, text, bookId)

    @Transactional
    override fun deleteById(id: String): Mono<Void> = commentRepository.deleteById(id)

    private fun save(id: String?, text: String, bookId: String): Mono<CommentResponseDto> {
        val bookMono = bookRepository.findById(bookId)
            .switchIfEmpty(Mono.error(EntityNotFoundException.BookNotFound(bookId)))

        val commentCheckMono = if (id != null) {
            commentRepository.findById(id)
                .filter { it.book.id == bookId }
                .switchIfEmpty(Mono.error(EntityNotFoundException.CommentNotFoundForBook(id, bookId)))
                .then()
        } else Mono.just(Unit)

        return commentCheckMono
            .then(bookMono)
            .flatMap { book ->
                val newComment = Comment(id, text, book)
                commentRepository.save(newComment)
            }
            .map { CommentResponseDto.fromDomainObject(it) }
    }
}
