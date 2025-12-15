package ru.otus.hw.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.otus.hw.dto.BookRequestDto
import ru.otus.hw.dto.BookResponseDto
import ru.otus.hw.services.BookService

@RestController
class BookController(
    private val bookService: BookService
) {
    @GetMapping("/api/books")
    fun getAllBooks(): Flux<BookResponseDto> = bookService.findAll()

    @GetMapping("/api/books/{id}")
    fun getBook(
        @PathVariable id: String
    ): Mono<BookResponseDto> = bookService.findById(id)

    @PostMapping("/api/books")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun insertBook(
        @RequestBody book: BookRequestDto
    ): Mono<Void> = bookService.insert(book.title, book.authorId, book.genresIds).then()

    @PutMapping("/api/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateBook(
        @PathVariable id: String,
        @RequestBody book: BookRequestDto
    ): Mono<Void> = bookService.update(id, book.title, book.authorId, book.genresIds).then()

    @DeleteMapping("/api/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(
        @PathVariable id: String
    ): Mono<Void> = bookService.deleteById(id)
}
