package ru.otus.hw.controllers

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.ModelAndView
import ru.otus.hw.exceptions.EntityNotFoundException

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException::class)
    fun handle(e: EntityNotFoundException): ModelAndView {
        val mv = ModelAndView("error")

        val message = when (e) {
            is EntityNotFoundException.BookNotFound -> "Book not found"
            is EntityNotFoundException.AuthorNotFound -> "Author not found"
            is EntityNotFoundException.GenresNotFound -> "One or all genres not found"
            is EntityNotFoundException.CommentNotFound -> "Comment not found"
            is EntityNotFoundException.CommentNotFoundForBook -> "This comment belongs to another book"
        }

        mv.addObject("message", message)
        return mv
    }

    @ExceptionHandler(Exception::class)
    fun handle(e: Exception): ModelAndView {
        val mv = ModelAndView("error")
        mv.addObject("message", "Unexpected error")
        return mv
    }
}
