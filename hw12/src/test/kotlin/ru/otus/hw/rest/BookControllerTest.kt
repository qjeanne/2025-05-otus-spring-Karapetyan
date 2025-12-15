package ru.otus.hw.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.otus.hw.dto.AuthorResponseDto
import ru.otus.hw.dto.BookRequestDto
import ru.otus.hw.dto.BookResponseDto
import ru.otus.hw.dto.CommentResponseDto
import ru.otus.hw.dto.GenreResponseDto
import ru.otus.hw.exceptions.EntityNotFoundException
import ru.otus.hw.services.AuthorService
import ru.otus.hw.services.BookService
import ru.otus.hw.services.CommentService
import ru.otus.hw.services.GenreService

@WebFluxTest(BookController::class, excludeAutoConfiguration = [ReactiveSecurityAutoConfiguration::class])
class BookControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var bookService: BookService

    @MockkBean
    private lateinit var authorService: AuthorService

    @MockkBean
    private lateinit var genreService: GenreService

    @MockkBean
    private lateinit var commentService: CommentService

    private val genres = listOf(
        GenreResponseDto("1", "firstGenre"),
        GenreResponseDto("2", "secondGenre"),
        GenreResponseDto("3", "thirdGenre")
    )

    private val authors = listOf(
        AuthorResponseDto("1", "firstAuthor"),
        AuthorResponseDto("2", "secondAuthor")
    )

    private val books = listOf(
        BookResponseDto("1", "first", authors[0], mutableListOf(genres[0], genres[1])),
        BookResponseDto("2", "second", authors[1], mutableListOf(genres[2]))
    )

    private val comments = listOf(
        CommentResponseDto("1", "text", books[0])
    )

    @Test
    fun `should return books list`() {
        every { bookService.findAll() } returns Flux.just(*books.toTypedArray())

        webTestClient.get().uri("/api/books")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(BookResponseDto::class.java)
            .hasSize(2)
            .contains(*books.toTypedArray())
    }

    @Test
    fun `should return book`() {
        every { bookService.findById("1") } returns Mono.just(books[0])
        every { commentService.findByBookId("1") } returns Flux.just(*comments.toTypedArray())
        every { authorService.findAll() } returns Flux.just(*authors.toTypedArray())
        every { genreService.findAll() } returns Flux.just(*genres.toTypedArray())

        webTestClient.get().uri("/api/books/1")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(BookResponseDto::class.java)
            .value { it shouldBe books[0] }
    }

    @Test
    fun `should return error when book not found`() {
        every { bookService.findById(any()) } returns Mono.error(EntityNotFoundException.BookNotFound("1"))

        webTestClient.get().uri("/api/books/10")
            .exchange()
            .expectStatus().isNotFound
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.detail").isEqualTo("Book not found")
    }

    @Test
    fun `should insert new book`() {
        val bookDto = BookRequestDto(
            title = "title",
            authorId = "1",
            genresIds = setOf("1", "2")
        )
        val bookJson = objectMapper.writeValueAsString(bookDto)
        every { bookService.insert(bookDto.title, bookDto.authorId, bookDto.genresIds) } returns Mono.just(books[0])

        webTestClient.post().uri("/api/books")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(bookJson)
            .exchange()
            .expectStatus().isNoContent
        verify { bookService.insert(bookDto.title, bookDto.authorId, bookDto.genresIds) }
    }

    @Test
    fun `should update the book`() {
        val id = "1"
        val bookDto = BookRequestDto(
            title = "newTitle",
            authorId = "2",
            genresIds = setOf("3")
        )
        val bookJson = objectMapper.writeValueAsString(bookDto)
        every { bookService.update(id, bookDto.title, bookDto.authorId, bookDto.genresIds) } returns Mono.just(books[0])

        webTestClient.put().uri("/api/books/$id")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(bookJson)
            .exchange()
            .expectStatus().isNoContent
        verify { bookService.update(id, bookDto.title, bookDto.authorId, bookDto.genresIds) }
    }

    @Test
    fun `should delete the book`() {
        val id = "1"
        every { bookService.deleteById(id) } returns Mono.empty()

        webTestClient.delete().uri("/api/books/$id")
            .exchange()
            .expectStatus().isNoContent
        verify { bookService.deleteById(id) }
    }
}
