package ru.otus.hw.controllers

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post
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
    fun `should render error page when book not found`() {
        val message = "test"
        every { commentService.insert(any(), any()) } throws EntityNotFoundException(message)

        mockMvc.post("/books/10/comments") {
            param("text", "text")
        }
            .andExpect {
                view {
                    name("error")
                }
                model {
                    attribute("message", message)
                }
            }
    }

    @Test
    fun `should insert new comment and redirect to the book path`() {
        val bookId = 1L
        val text = "test"
        every { commentService.insert(text, bookId) } returns comments[0]

        mockMvc.post("/books/$bookId/comments") {
            param("text", text)
        }
            .andExpect {
                view {
                    name("redirect:/books/$bookId")
                }
            }
        verify { commentService.insert(text, bookId) }
    }

    @Test
    fun `should update the comment and redirect to the book path`() {
        val bookId = 1L
        val id = 2L
        val text = "updated"
        every { commentService.update(id, text, bookId) } returns comments[0]

        mockMvc.post("/books/$bookId/comments/$id") {
            param("text", text)
        }
            .andExpect {
                view {
                    name("redirect:/books/$bookId")
                }
            }
        verify { commentService.update(id, text, bookId) }
    }

    @Test
    fun `should delete the comment and redirect to the books path`() {
        val bookId = 1L
        val id = 2L
        every { commentService.deleteById(id) } just runs

        mockMvc.delete("/books/$bookId/comments/$id")
            .andExpect {
                view {
                    name("redirect:/books/$bookId")
                }
            }
        verify { commentService.deleteById(id) }
    }
}
