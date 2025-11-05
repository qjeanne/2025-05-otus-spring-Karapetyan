package ru.otus.hw.controllers

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import ru.otus.hw.dto.AuthorResponseDto
import ru.otus.hw.dto.BookRequestDto
import ru.otus.hw.dto.BookResponseDto
import ru.otus.hw.dto.CommentResponseDto
import ru.otus.hw.dto.GenreResponseDto
import ru.otus.hw.exceptions.EntityNotFoundException
import ru.otus.hw.services.AuthorService
import ru.otus.hw.services.BookService
import ru.otus.hw.services.CommentService
import ru.otus.hw.services.GenreService

@WebMvcTest(BookController::class)
class BookControllerTest {

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

    private val genres = listOf(
        GenreResponseDto(1, "firstGenre"),
        GenreResponseDto(2, "secondGenre"),
        GenreResponseDto(3, "thirdGenre")
    )

    private val authors = listOf(
        AuthorResponseDto(1, "firstAuthor"),
        AuthorResponseDto(2, "secondAuthor")
    )

    private val books = listOf(
        BookResponseDto(1, "first", authors[0], mutableListOf(genres[0], genres[1])),
        BookResponseDto(2, "second", authors[1], mutableListOf(genres[2]))
    )

    private val comments = listOf(
        CommentResponseDto(1, "text", books[0])
    )

    @Test
    fun `should render books list page with correct view and model attributes`() {
        every { bookService.findAll() } returns books

        mockMvc.get("/books")
            .andExpect {
                view {
                    name("books-list")
                }
                model {
                    attribute("books", books)
                }
            }
    }

    @Test
    fun `should render book page with correct view and model attributes`() {
        every { bookService.findById(1) } returns books[0]
        every { commentService.findByBookId(1) } returns comments
        every { authorService.findAll() } returns authors
        every { genreService.findAll() } returns genres

        mockMvc.get("/books/1")
            .andExpect {
                view {
                    name("book-edit")
                }
                model {
                    attribute("book", books[0])
                    attribute("comments", comments)
                    attribute("authors", authors)
                    attribute("genres", genres)
                    attribute("genres", genres.map { it.id })
                }
            }
    }

    @Test
    fun `should render new book page with correct view and model attributes`() {
        every { authorService.findAll() } returns authors
        every { genreService.findAll() } returns genres

        mockMvc.get("/books/new")
            .andExpect {
                view {
                    name("book-new")
                }
                model {
                    attribute("authors", authors)
                    attribute("genres", genres)
                }
            }
    }

    @Test
    fun `should render error page when book not found`() {
        val message = "test"
        every { bookService.findById(any()) } throws EntityNotFoundException(message)

        mockMvc.get("/books/10")
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
    fun `should insert new book and redirect to books list path`() {
        val bookDto = BookRequestDto(
            title = "title",
            authorId = 1L,
            genresIds = setOf(1L, 2L)
        )
        every { bookService.insert(bookDto.title, bookDto.authorId, bookDto.genresIds) } returns books[0]

        mockMvc.post("/books") {
            param("title", bookDto.title)
            param("authorId", bookDto.authorId.toString())
            param("genresIds", *bookDto.genresIds.map { it.toString() }.toTypedArray())
        }
            .andExpect {
                view {
                    name("redirect:/books")
                }
            }
        verify { bookService.insert(bookDto.title, bookDto.authorId, bookDto.genresIds) }
    }

    @Test
    fun `should update the book and redirect to this book path`() {
        val id = 1L
        val bookDto = BookRequestDto(
            title = "newTitle",
            authorId = 2L,
            genresIds = setOf(3L)
        )
        every { bookService.update(id, bookDto.title, bookDto.authorId, bookDto.genresIds) } returns books[0]

        mockMvc.post("/books/$id") {
            param("title", bookDto.title)
            param("authorId", bookDto.authorId.toString())
            param("genresIds", *bookDto.genresIds.map { it.toString() }.toTypedArray())
        }
            .andExpect {
                view {
                    name("redirect:/books/$id")
                }
            }
        verify { bookService.update(id, bookDto.title, bookDto.authorId, bookDto.genresIds) }
    }

    @Test
    fun `should delete the book and redirect to books list`() {
        val id = 1L
        every { bookService.deleteById(id) } just runs

        mockMvc.delete("/books/$id")
            .andExpect {
                view {
                    name("redirect:/books")
                }
            }
        verify { bookService.deleteById(id) }
    }
}
