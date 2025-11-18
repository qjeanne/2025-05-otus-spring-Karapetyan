package ru.otus.hw.configurations

import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import ru.otus.hw.rest.AuthorController
import ru.otus.hw.rest.BookController
import ru.otus.hw.rest.CommentController
import ru.otus.hw.rest.GenreController
import ru.otus.hw.services.AuthorService
import ru.otus.hw.services.BookService
import ru.otus.hw.services.CommentService
import ru.otus.hw.services.GenreService

data class Endpoint(
    val method: HttpMethod,
    val path: String,
    val body: String? = null
)

@WebMvcTest(
    BookController::class,
    AuthorController::class,
    GenreController::class,
    CommentController::class
)
@Import(SecurityConf::class)
class SecurityTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean(relaxed = true, relaxUnitFun = true)
    private lateinit var bookService: BookService

    @MockkBean(relaxed = true, relaxUnitFun = true)
    private lateinit var authorService: AuthorService

    @MockkBean(relaxed = true, relaxUnitFun = true)
    private lateinit var genreService: GenreService

    @MockkBean(relaxed = true, relaxUnitFun = true)
    private lateinit var commentService: CommentService

    @ParameterizedTest
    @MethodSource("getEndpoints")
    fun `should not give access to get endpoints to unregistered users`(endpoint: Endpoint) {
        mockMvc.performEndpoint(endpoint)
            .andExpect {
                status { isFound() }
                redirectedUrl("http://localhost/login")
            }
    }

    @WithMockUser
    @ParameterizedTest
    @MethodSource("getEndpoints")
    fun `should give access to get endpoints to registered users`(endpoint: Endpoint) {
        mockMvc.performEndpoint(endpoint)
            .andExpect {
                status { is2xxSuccessful() }
            }
    }

    companion object {
        @JvmStatic
        private fun getEndpoints(): List<Endpoint> =
            listOf(
                Endpoint(HttpMethod.GET, "/api/books"),
                Endpoint(HttpMethod.GET, "/api/books/1"),
                Endpoint(HttpMethod.GET, "/api/authors"),
                Endpoint(HttpMethod.GET, "/api/genres"),
                Endpoint(HttpMethod.GET, "/api/books/1/comments"),

                Endpoint(HttpMethod.POST, "/api/books", """{"title":"t","authorId":1,"genresIds":[1]}"""),
                Endpoint(HttpMethod.POST, "/api/books/1/comments", "test"),

                Endpoint(HttpMethod.PUT, "/api/books/1", """{"title":"updated","authorId":1,"genresIds":[1]}"""),
                Endpoint(HttpMethod.PUT, "/api/books/1/comments/1", "upd"),

                Endpoint(HttpMethod.DELETE, "/api/books/1"),
                Endpoint(HttpMethod.DELETE, "/api/books/1/comments/1")
            )
    }
}

private fun MockMvc.performEndpoint(endpoint: Endpoint): ResultActionsDsl {
    if (endpoint.path.contains("comments") && (endpoint.method == HttpMethod.POST || endpoint.method == HttpMethod.PUT)) {
        return when (endpoint.method) {
            HttpMethod.POST -> post(endpoint.path) {
                param("text", endpoint.body.toString())
            }
            HttpMethod.PUT -> put(endpoint.path) {
                param("text", endpoint.body.toString())
            }
            else -> error("Unsupported method")
        }
    }
    return when (endpoint.method) {
        HttpMethod.GET -> get(endpoint.path)
        HttpMethod.POST -> post(endpoint.path) {
            contentType = MediaType.APPLICATION_JSON
            content = endpoint.body ?: ""
        }
        HttpMethod.PUT -> put(endpoint.path) {
            contentType = MediaType.APPLICATION_JSON
            content = endpoint.body ?: ""
        }
        HttpMethod.DELETE -> delete(endpoint.path)
        else -> error("Unsupported method")
    }
}
