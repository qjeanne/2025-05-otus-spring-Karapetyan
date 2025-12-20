package ru.otus.hw.rest

import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList
import reactor.core.publisher.Flux
import ru.otus.hw.dto.AuthorResponseDto
import ru.otus.hw.services.AuthorService

@WebFluxTest(AuthorController::class, excludeAutoConfiguration = [ReactiveSecurityAutoConfiguration::class])
class AuthorControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var authorService: AuthorService

    private val authors = listOf(
        AuthorResponseDto("1", "firstAuthor"),
        AuthorResponseDto("2", "secondAuthor")
    )

    @Test
    fun `should return authors list`() {
        every { authorService.findAll() } returns Flux.just(*authors.toTypedArray())

        webTestClient.get().uri("/api/authors")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(AuthorResponseDto::class.java)
            .hasSize(2)
            .contains(*authors.toTypedArray())
    }
}
