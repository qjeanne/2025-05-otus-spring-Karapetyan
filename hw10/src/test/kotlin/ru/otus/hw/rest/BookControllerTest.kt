package ru.otus.hw.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
    fun `should return books list`() {
        every { bookService.findAll() } returns books

        mockMvc.get("/api/books")
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    jsonPath("$[0].id") { value(books[0].id) }
                    jsonPath("$[0].title") { value(books[0].title) }
                    jsonPath("$[0].author.id") { value(books[0].author.id) }
                    jsonPath("$[0].author.fullName") { value(books[0].author.fullName) }
                    jsonPath("$[0].genres[0].id") { value(books[0].genres[0].id) }
                    jsonPath("$[0].genres[0].name") { value(books[0].genres[0].name) }
                    jsonPath("$[0].genres[1].id") { value(books[0].genres[1].id) }
                    jsonPath("$[0].genres[1].name") { value(books[0].genres[1].name) }
                    jsonPath("$[1].id") { value(books[1].id) }
                    jsonPath("$[1].title") { value(books[1].title) }
                    jsonPath("$[1].author.id") { value(books[1].author.id) }
                    jsonPath("$[1].author.fullName") { value(books[1].author.fullName) }
                    jsonPath("$[1].genres[0].id") { value(books[1].genres[0].id) }
                    jsonPath("$[1].genres[0].name") { value(books[1].genres[0].name) }
                }
            }
    }

    @Test
    fun `should return book`() {
        every { bookService.findById(1) } returns books[0]
        every { commentService.findByBookId(1) } returns comments
        every { authorService.findAll() } returns authors
        every { genreService.findAll() } returns genres

        mockMvc.get("/api/books/1")
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    jsonPath("$.id") { value(books[0].id) }
                    jsonPath("$.title") { value(books[0].title) }
                    jsonPath("$.author.id") { value(books[0].author.id) }
                    jsonPath("$.author.fullName") { value(books[0].author.fullName) }
                    jsonPath("$.genres[0].id") { value(books[0].genres[0].id) }
                    jsonPath("$.genres[0].name") { value(books[0].genres[0].name) }
                    jsonPath("$.genres[1].id") { value(books[0].genres[1].id) }
                    jsonPath("$.genres[1].name") { value(books[0].genres[1].name) }
                }
            }
    }

    @Test
    fun `should return error when book not found`() {
        every { bookService.findById(any()) } throws EntityNotFoundException.BookNotFound(1)

        mockMvc.get("/api/books/10")
            .andExpect {
                status { isNotFound() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    jsonPath("$.detail") { value("Book not found") }
                }
            }
    }

    @Test
    fun `should insert new book`() {
        val bookDto = BookRequestDto(
            title = "title",
            authorId = 1L,
            genresIds = setOf(1L, 2L)
        )
        val objectMapper = jacksonObjectMapper()
        val bookJson = objectMapper.writeValueAsString(bookDto)
        every { bookService.insert(bookDto.title, bookDto.authorId, bookDto.genresIds) } returns books[0]

        mockMvc.post("/api/books") {
            contentType = MediaType.APPLICATION_JSON
            content = bookJson
        }
            .andExpect {
                status { isNoContent() }
            }
        verify { bookService.insert(bookDto.title, bookDto.authorId, bookDto.genresIds) }
    }

    @Test
    fun `should update the book`() {
        val id = 1L
        val bookDto = BookRequestDto(
            title = "newTitle",
            authorId = 2L,
            genresIds = setOf(3L)
        )
        val objectMapper = jacksonObjectMapper()
        val bookJson = objectMapper.writeValueAsString(bookDto)
        every { bookService.update(id, bookDto.title, bookDto.authorId, bookDto.genresIds) } returns books[0]

        mockMvc.put("/api/books/$id") {
            contentType = MediaType.APPLICATION_JSON
            content = bookJson
        }
            .andExpect {
                status { isNoContent() }
            }
        verify { bookService.update(id, bookDto.title, bookDto.authorId, bookDto.genresIds) }
    }

    @Test
    fun `should delete the book`() {
        val id = 1L
        every { bookService.deleteById(id) } just runs

        mockMvc.delete("/api/books/$id")
            .andExpect {
                status { isNoContent() }
            }
        verify { bookService.deleteById(id) }
    }
}
