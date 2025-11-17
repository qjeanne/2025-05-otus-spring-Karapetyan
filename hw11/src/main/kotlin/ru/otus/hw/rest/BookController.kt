package ru.otus.hw.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.otus.hw.dto.BookRequestDto
import ru.otus.hw.dto.BookResponseDto
import ru.otus.hw.services.BookService

@RestController
class BookController(
    private val bookService: BookService
) {
    @GetMapping("/api/books")
    fun getAllBooks(): ResponseEntity<List<BookResponseDto>> =
        ResponseEntity.ok(
            bookService.findAll()
        )

    @GetMapping("/api/books/{id}")
    fun getBook(
        @PathVariable id: Long
    ): ResponseEntity<BookResponseDto> =
        ResponseEntity.ok(
            bookService.findById(id)
        )

    @PostMapping("/api/books")
    fun insertBook(
        @RequestBody book: BookRequestDto
    ): ResponseEntity<Unit> {
        bookService.insert(book.title, book.authorId, book.genresIds)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/api/books/{id}")
    fun updateBook(
        @PathVariable id: Long,
        @RequestBody book: BookRequestDto
    ): ResponseEntity<Unit> {
        bookService.update(id, book.title, book.authorId, book.genresIds)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/api/books/{id}")
    fun deleteBook(
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        bookService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
