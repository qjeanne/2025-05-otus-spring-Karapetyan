package ru.otus.hw.rest

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
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
    fun `should return authors list`() {
        every { authorService.findAll() } returns authors

        mockMvc.get("/api/authors")
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    jsonPath("$[0].id") { value(authors[0].id) }
                    jsonPath("$[0].fullName") { value(authors[0].fullName) }
                    jsonPath("$[1].id") { value(authors[1].id) }
                    jsonPath("$[1].fullName") { value(authors[1].fullName) }
                }
            }
    }
}
