package ru.otus.hw.commands

import org.springframework.shell.command.annotation.Command
import ru.otus.hw.converters.CommentConverter
import ru.otus.hw.services.CommentService

@Command(group = "Comment Commands")
class CommentCommands(
    private val commentService: CommentService,
    private val commentConverter: CommentConverter
) {
    @Command(command = ["cbbid"], description = "Find comments by book id")
    fun findCommentByBookId(id: String): String = commentService.findByBookId(id)
        .joinToString(separator = ",${System.lineSeparator()}") {
            commentConverter.commentToString(it)
        }

    @Command(command = ["cbid"], description = "Find comment by id")
    fun findCommentById(id: String): String = commentService.findById(id)
        ?.let { commentConverter.commentToString(it) }
        ?: "Comment with id $id not found"

    @Command(command = ["cins"], description = "Insert comment")
    fun insertComment(text: String, bookId: String): String {
        val savedBook = commentService.insert(text, bookId)
        return commentConverter.commentToString(savedBook)
    }

    @Command(command = ["cupd"], description = "Update comment")
    fun updateComment(id: String, text: String, bookId: String): String {
        val savedBook = commentService.update(id, text, bookId)
        return commentConverter.commentToString(savedBook)
    }

    @Command(command = ["cdel"], description = "Delete comment by id")
    fun deleteComment(id: String) {
        commentService.deleteById(id)
    }
}
