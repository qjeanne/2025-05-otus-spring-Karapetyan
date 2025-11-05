package ru.otus.hw.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.otus.hw.services.CommentService

@Controller
@RequestMapping("/books")
class CommentController(
    private val service: CommentService
) {
    @PostMapping("/{id}/comments")
    fun insertComment(
        @PathVariable id: Long,
        text: String
    ): String {
        service.insert(text, id)
        return "redirect:/books/$id"
    }

    @PostMapping("/{bookId}/comments/{id}")
    fun updateComment(
        @PathVariable bookId: Long,
        @PathVariable id: Long,
        text: String
    ): String {
        service.update(id, text, bookId)
        return "redirect:/books/$bookId"
    }

    @DeleteMapping("/{bookId}/comments/{id}")
    fun deleteComment(
        @PathVariable bookId: Long,
        @PathVariable id: Long
    ): String {
        service.deleteById(id)
        return "redirect:/books/$bookId"
    }
}
