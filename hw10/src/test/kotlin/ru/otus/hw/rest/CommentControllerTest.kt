package ru.otus.hw.rest

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import ru.otus.hw.dto.AuthorResponseDto
import ru.otus.hw.dto.BookResponseDto
import ru.otus.hw.dto.CommentResponseDto
import ru.otus.hw.dto.GenreResponseDto
import ru.otus.hw.exceptions.EntityNotFoundException
import ru.otus.hw.services.CommentService

@WebMvcTest(CommentController::class)
class CommentControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var commentService: CommentService

    private val genres = mutableListOf(
        GenreResponseDto(1, "firstGenre"),
        GenreResponseDto(2, "secondGenre")
    )

    private val author = AuthorResponseDto(1, "firstAuthor")

    private val book = BookResponseDto(1, "first", author, genres)

    private val comments = listOf(
        CommentResponseDto(1, "text1", book),
        CommentResponseDto(2, "text2", book)
    )

    @Test
    fun `should return comments by book id`() {
        every { commentService.findByBookId(any()) } returns comments

        mockMvc.get("/api/books/1/comments")
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    jsonPath("$[0].id") { value(comments[0].id) }
                    jsonPath("$[0].text") { value(comments[0].text) }
                    jsonPath("$[1].id") { value(comments[1].id) }
                    jsonPath("$[1].text") { value(comments[1].text) }
                }
            }
    }

    @Test
    fun `should return error when book not found`() {
        every { commentService.insert(any(), any()) } throws EntityNotFoundException.BookNotFound(1)

        mockMvc.post("/api/books/10/comments") {
            param("text", "text")
        }
            .andExpect {
                status { isNotFound() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    jsonPath("$.detail") { value("Book not found") }
                }
            }
    }

    @Test
    fun `should insert new comment`() {
        val bookId = 1L
        val text = "test"
        every { commentService.insert(text, bookId) } returns comments[0]

        mockMvc.post("/api/books/$bookId/comments") {
            param("text", text)
        }
            .andExpect {
                status { isNoContent() }
            }
        verify { commentService.insert(text, bookId) }
    }

    @Test
    fun `should update the comment and redirect to the book path`() {
        val bookId = 1L
        val id = 2L
        val text = "updated"
        every { commentService.update(id, text, bookId) } returns comments[0]

        mockMvc.put("/api/books/$bookId/comments/$id") {
            param("text", text)
        }
            .andExpect {
                status { isNoContent() }
            }
        verify { commentService.update(id, text, bookId) }
    }

    @Test
    fun `should delete the comment and redirect to the books path`() {
        val bookId = 1L
        val id = 2L
        every { commentService.deleteById(id) } just runs

        mockMvc.delete("/api/books/$bookId/comments/$id")
            .andExpect {
                status { isNoContent() }
            }
        verify { commentService.deleteById(id) }
    }
}
