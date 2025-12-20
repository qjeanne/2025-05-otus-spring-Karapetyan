package ru.otus.hw.rest

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import ru.otus.hw.dto.GenreResponseDto
import ru.otus.hw.services.GenreService

@WebFluxTest(GenreController::class, excludeAutoConfiguration = [ReactiveSecurityAutoConfiguration::class])
class GenreControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var genreService: GenreService

    private val genres = listOf(
        GenreResponseDto("1", "first"),
        GenreResponseDto("2", "second")
    )

    @Test
    fun `should return genres list`() {
        every { genreService.findAll() } returns Flux.just(*genres.toTypedArray())

        webTestClient.get().uri("/api/genres")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(GenreResponseDto::class.java)
            .hasSize(2)
            .contains(*genres.toTypedArray())
    }
}
