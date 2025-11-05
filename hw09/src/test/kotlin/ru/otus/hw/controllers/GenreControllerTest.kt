package ru.otus.hw.controllers

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
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
    fun `should render genres list page with correct view and model attributes`() {
        every { genreService.findAll() } returns genres

        mockMvc.get("/genres")
            .andExpect {
                view {
                    name("genres-list")
                }
                model {
                    attribute("genres", genres)
                }
            }
    }
}
