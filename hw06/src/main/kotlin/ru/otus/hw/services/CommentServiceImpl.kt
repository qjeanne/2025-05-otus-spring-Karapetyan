package ru.otus.hw.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
    override fun findById(id: Long): Comment? = commentRepository.findById(id)

    @Transactional(readOnly = true)
    override fun findByBookId(id: Long): List<Comment> = commentRepository.findByBookId(id)

    @Transactional
    override fun insert(text: String, bookId: Long): Comment =
        save(0, text, bookId)

    @Transactional
    override fun update(id: Long, text: String, bookId: Long): Comment =
        save(id, text, bookId)

    @Transactional
    override fun deleteById(id: Long) = commentRepository.deleteById(id)

    private fun save(id: Long, text: String, bookId: Long): Comment {
        val book = bookRepository.findById(bookId)
            ?: throw EntityNotFoundException("Book with id $bookId not found")

        val comment = Comment(id, text, book.id)
        return commentRepository.save(comment)
    }
}
