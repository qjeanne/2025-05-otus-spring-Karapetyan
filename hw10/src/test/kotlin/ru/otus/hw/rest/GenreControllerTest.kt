package ru.otus.hw.rest

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import ru.otus.hw.dto.GenreResponseDto
import ru.otus.hw.services.GenreService

@WebMvcTest(GenreController::class)
class GenreControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var genreService: GenreService

    private val genres = mutableListOf(
        GenreResponseDto(1, "firstGenre"),
        GenreResponseDto(2, "secondGenre")
    )

    @Test
    fun `should return genres list`() {
        every { genreService.findAll() } returns genres

        mockMvc.get("/api/genres")
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    jsonPath("$[0].id") { value(genres[0].id) }
                    jsonPath("$[0].name") { value(genres[0].name) }
                    jsonPath("$[1].id") { value(genres[1].id) }
                    jsonPath("$[1].name") { value(genres[1].name) }
                }
            }
    }
}
