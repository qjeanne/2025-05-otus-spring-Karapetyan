package ru.otus.hw.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
    override fun findById(id: Long): CommentResponseDto = commentRepository.findById(id)
        ?.let { CommentResponseDto.fromDomainObject(it) }
        ?: throw EntityNotFoundException.CommentNotFound(id)

    @Transactional(readOnly = true)
    override fun findByBookId(id: Long): List<CommentResponseDto> = commentRepository.findByBookId(id)
        .map { CommentResponseDto.fromDomainObject(it) }

    @Transactional
    override fun insert(text: String, bookId: Long): CommentResponseDto =
        save(0, text, bookId)

    @Transactional
    override fun update(id: Long, text: String, bookId: Long): CommentResponseDto =
        save(id, text, bookId)

    @Transactional
    override fun deleteById(id: Long) = commentRepository.deleteById(id)

    private fun save(id: Long, text: String, bookId: Long): CommentResponseDto {
        val book = bookRepository.findById(bookId)
            ?: throw EntityNotFoundException.BookNotFound(bookId)

        if (id > 0) {
            val comment = commentRepository.findById(id)
                ?: throw EntityNotFoundException.CommentNotFound(id)

            if (comment.book.id != bookId) throw EntityNotFoundException.CommentNotFoundForBook(id, bookId)
        }

        val newComment = Comment(
            id = id,
            text = text,
            book = book
        )
        commentRepository.save(newComment)

        return CommentResponseDto.fromDomainObject(newComment)
    }
}
