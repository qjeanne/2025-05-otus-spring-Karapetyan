package ru.otus.hw.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import ru.otus.hw.dto.BookRequestDto
import ru.otus.hw.services.AuthorService
import ru.otus.hw.services.BookService
import ru.otus.hw.services.CommentService
import ru.otus.hw.services.GenreService

@Controller
class BookController(
    private val bookService: BookService,
    private val commentService: CommentService,
    private val authorService: AuthorService,
    private val genreService: GenreService
) {
    @GetMapping("/books")
    fun getAllBooks(
        model: Model
    ): String {
        val books = bookService.findAll()
        model.addAttribute("books", books)
        return "books-list"
    }

    @GetMapping("/books/{id}")
    fun getBook(
        @PathVariable id: Long,
        model: Model
    ): String {
        val book = bookService.findById(id)

        val comments = commentService.findByBookId(id)
        val authors = authorService.findAll()
        val genres = genreService.findAll()

        model.addAttribute("book", book)
        model.addAttribute("comments", comments)
        model.addAttribute("authors", authors)
        model.addAttribute("genres", genres)
        model.addAttribute("selectedGenreIds", book.genres.map { it.id })

        return "book-edit"
    }

    @GetMapping("/books/new")
    fun getBook(
        model: Model
    ): String {
        val authors = authorService.findAll()
        val genres = genreService.findAll()

        model.addAttribute("authors", authors)
        model.addAttribute("genres", genres)

        return "book-new"
    }

    @PostMapping("/books")
    fun insertBook(
        book: BookRequestDto
    ): String {
        bookService.insert(book.title, book.authorId, book.genresIds)
        return "redirect:/books"
    }

    @PostMapping("/books/{id}")
    fun updateBook(
        @PathVariable id: Long,
        book: BookRequestDto
    ): String {
        bookService.update(id, book.title, book.authorId, book.genresIds)
        return "redirect:/books/$id"
    }

    @DeleteMapping("/books/{id}")
    fun deleteBook(
        @PathVariable id: Long
    ): String {
        bookService.deleteById(id)
        return "redirect:/books"
    }
}
