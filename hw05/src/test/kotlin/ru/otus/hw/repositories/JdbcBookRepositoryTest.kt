package ru.otus.hw.repositories

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import ru.otus.hw.models.Author
import ru.otus.hw.models.Book
import ru.otus.hw.models.Genre

@JdbcTest
@Import(JdbcBookRepository::class, JdbcGenreRepository::class)
open class JdbcBookRepositoryTest {

    @Autowired
    private lateinit var repositoryJdbc: JdbcBookRepository

    private lateinit var dbAuthors: List<Author>
    private lateinit var dbGenres: List<Genre>
    private lateinit var dbBooks: List<Book>

    @BeforeEach
    fun setUp() {
        dbAuthors = getDbAuthors()
        dbGenres = getDbGenres()
        dbBooks = getDbBooks(dbAuthors, dbGenres)
    }

    @ParameterizedTest
    @MethodSource("getDbBooks")
    fun `should return correct book by id`(expectedBook: Book) {
        val actualBook = repositoryJdbc.findById(expectedBook.id)

        actualBook shouldBe expectedBook
    }

    @Test
    fun `should return correct books list`() {
        val expectedBooks = dbBooks

        val actualBooks = repositoryJdbc.findAll()

        actualBooks shouldContainExactly expectedBooks
        actualBooks.forEach { println(it) }
    }

    @Test
    fun `should save new book`() {
        val expectedBook = Book(
            id = 0,
            title = "BookTitle_10500",
            author = dbAuthors[0],
            genres = listOf(dbGenres[0], dbGenres[2])
        )

        val returnedBook = repositoryJdbc.save(expectedBook)

        returnedBook should {
            it.id > 0
            it.title shouldBe expectedBook.title
            it.author shouldBe expectedBook.author
            it.genres shouldContainExactly expectedBook.genres
        }
        repositoryJdbc.findById(returnedBook.id) shouldBe returnedBook
    }

    @Test
    fun `should save updated book`() {
        val expectedBook = Book(
            id = 1L,
            title = "BookTitle_10500",
            author = dbAuthors[2],
            genres = listOf(dbGenres[4], dbGenres[5])
        )
        repositoryJdbc.findById(expectedBook.id) shouldNotBe expectedBook

        val returnedBook = repositoryJdbc.save(expectedBook)

        returnedBook should {
            it.id > 0
            it.title shouldBe expectedBook.title
            it.author shouldBe expectedBook.author
            it.genres shouldContainExactly expectedBook.genres
        }
        repositoryJdbc.findById(returnedBook.id) shouldBe returnedBook
    }

    @Test
    fun `should delete book`() {
        repositoryJdbc.findById(1L) shouldNotBe null

        repositoryJdbc.deleteById(1L)

        repositoryJdbc.findById(1L) shouldBe null
    }

    companion object {
        private fun getDbAuthors(): List<Author> {
            return (1L..3L).map { id -> Author(id, "Author_$id") }
        }

        private fun getDbGenres(): List<Genre> {
            return (1L..6L).map { id -> Genre(id, "Genre_$id") }
        }

        @JvmStatic
        private fun getDbBooks(): List<Book> {
            val dbAuthors = getDbAuthors()
            val dbGenres = getDbGenres()
            return getDbBooks(dbAuthors, dbGenres)
        }

        @JvmStatic
        private fun getDbBooks(dbAuthors: List<Author>, dbGenres: List<Genre>): List<Book> {
            return (1..3).map { id ->
                Book(
                    id.toLong(),
                    "BookTitle_$id",
                    dbAuthors[id - 1],
                    dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                )
            }
        }
    }
}
