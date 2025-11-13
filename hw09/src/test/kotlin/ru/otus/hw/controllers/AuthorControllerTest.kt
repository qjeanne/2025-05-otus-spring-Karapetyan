package ru.otus.hw.controllers

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import ru.otus.hw.dto.AuthorResponseDto
import ru.otus.hw.services.AuthorService

@WebMvcTest(AuthorController::class)
class AuthorControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var authorService: AuthorService

    private val authors = listOf(
        AuthorResponseDto(1, "firstAuthor"),
        AuthorResponseDto(2, "secondAuthor")
    )

    @Test
    fun `should render authors list page with correct view and model attributes`() {
        every { authorService.findAll() } returns authors

        mockMvc.get("/authors")
            .andExpect {
                view {
                    name("authors-list")
                }
                model {
                    attribute("authors", authors)
                }
            }
    }
}
