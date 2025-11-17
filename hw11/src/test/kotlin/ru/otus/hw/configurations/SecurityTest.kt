package ru.otus.hw.configurations

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import ru.otus.hw.rest.AuthorController
import ru.otus.hw.rest.BookController
import ru.otus.hw.rest.CommentController
import ru.otus.hw.rest.GenreController
import ru.otus.hw.services.AuthorService
import ru.otus.hw.services.BookService
import ru.otus.hw.services.CommentService
import ru.otus.hw.services.GenreService

@WebMvcTest(
    BookController::class,
    AuthorController::class,
    GenreController::class,
    CommentController::class
)
class SecurityTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var bookService: BookService

    @MockkBean
    private lateinit var authorService: AuthorService

    @MockkBean
    private lateinit var genreService: GenreService

    @MockkBean
    private lateinit var commentService: CommentService

    @Test
    fun `should not give access to books to unregistered users`() {
        mockMvc.get("/api/books")
            .andExpect {
                status { isUnauthorized() }
            }
    }

    @WithMockUser
    @Test
    fun `should give access to books to registered users`() {
        every { bookService.findAll() } returns emptyList()

        mockMvc.get("/api/books")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `should not give access to authors to unregistered users`() {
        mockMvc.get("/api/authors")
            .andExpect {
                status { isUnauthorized() }
            }
    }

    @WithMockUser
    @Test
    fun `should give access to authors to registered users`() {
        every { authorService.findAll() } returns emptyList()

        mockMvc.get("/api/authors")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `should not give access to genres to unregistered users`() {
        mockMvc.get("/api/genres")
            .andExpect {
                status { isUnauthorized() }
            }
    }

    @WithMockUser
    @Test
    fun `should give access to genres to registered users`() {
        every { genreService.findAll() } returns emptyList()

        mockMvc.get("/api/genres")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `should not give access to comments to unregistered users`() {
        mockMvc.get("/api/books/1/comments")
            .andExpect {
                status { isUnauthorized() }
            }
    }

    @WithMockUser
    @Test
    fun `should give access to comments to registered users`() {
        every { commentService.findByBookId(any()) } returns emptyList()

        mockMvc.get("/api/books/1/comments")
            .andExpect {
                status { isOk() }
            }
    }
}
