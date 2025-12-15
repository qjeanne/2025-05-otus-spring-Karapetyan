package ru.otus.hw.rest

import com.ninjasquad.springmockk.MockkBean
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
import ru.otus.hw.dto.BookResponseDto
import ru.otus.hw.dto.CommentResponseDto
import ru.otus.hw.dto.GenreResponseDto
import ru.otus.hw.exceptions.EntityNotFoundException
import ru.otus.hw.services.BookService
import ru.otus.hw.services.CommentService

@WebFluxTest(CommentController::class, excludeAutoConfiguration = [ReactiveSecurityAutoConfiguration::class])
class CommentControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var bookService: BookService

    @MockkBean
    private lateinit var commentService: CommentService

    private val genres = mutableListOf(
        GenreResponseDto("1", "firstGenre"),
        GenreResponseDto("2", "secondGenre")
    )

    private val author = AuthorResponseDto("1", "firstAuthor")

    private val book = BookResponseDto("1", "first", author, genres)

    private val comments = listOf(
        CommentResponseDto("1", "text1", book),
        CommentResponseDto("2", "text2", book)
    )

    @Test
    fun `should return comments by book id`() {
        every { commentService.findByBookId(any()) } returns Flux.just(*comments.toTypedArray())

        webTestClient.get().uri("/api/books/1/comments")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(CommentResponseDto::class.java)
            .hasSize(2)
            .contains(*comments.toTypedArray())
    }

    @Test
    fun `should return error when book not found`() {
        every { commentService.insert(any(), any()) } returns Mono.error(EntityNotFoundException.BookNotFound("1"))

        webTestClient.post().uri { uriBuilder ->
            uriBuilder
                .path("/api/books/10/comments")
                .queryParam("text", "text")
                .build()
        }.exchange()
            .expectStatus().isNotFound
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.detail").isEqualTo("Book not found")
    }

    @Test
    fun `should insert new comment`() {
        val bookId = "1"
        val text = "test"
        every { commentService.insert(text, bookId) } returns Mono.just(comments[0])

        webTestClient.post().uri { uriBuilder ->
            uriBuilder
                .path("/api/books/$bookId/comments")
                .queryParam("text", text)
                .build()
        }.exchange()
            .expectStatus().isNoContent
        verify { commentService.insert(text, bookId) }
    }

    @Test
    fun `should update the comment`() {
        val bookId = "1"
        val id = "2"
        val text = "updated"
        every { commentService.update(id, text, bookId) } returns Mono.just(comments[0])

        webTestClient.put().uri { uriBuilder ->
            uriBuilder
                .path("/api/books/$bookId/comments/$id")
                .queryParam("text", text)
                .build()
        }.exchange()
            .expectStatus().isNoContent
        verify { commentService.update(id, text, bookId) }
    }

    @Test
    fun `should delete the comment`() {
        val bookId = "1"
        val id = "2"
        every { commentService.deleteById(id) } returns Mono.empty()


        webTestClient.delete().uri("/api/books/$bookId/comments/$id")
            .exchange()
            .expectStatus().isNoContent
        verify { commentService.deleteById(id) }
    }
}
