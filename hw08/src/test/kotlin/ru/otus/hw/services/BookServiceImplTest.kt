package ru.otus.hw.services

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import ru.otus.hw.MongoRepositoryTest
import ru.otus.hw.events.BookModelEventsListener
import ru.otus.hw.models.Author
import ru.otus.hw.models.Book
import ru.otus.hw.models.Genre

@Import(
    BookServiceImpl::class,
    BookModelEventsListener::class,
    SequenceGeneratorService::class
)
open class BookServiceImplTest: MongoRepositoryTest() {

    @Autowired
    private lateinit var bookService: BookService

    @Test
    fun `findById should return the book`() {
        val id = 1L

        val book = bookService.findById(id)

        book shouldBe mongoTemplate.findById(id, Book::class.java)
    }

    @Test
    fun `findById should return null if book with this id not exist`() {
        val book = bookService.findById(10L)

        book.shouldBeNull()
    }

    @Test
    fun `findAll should return all book`() {
        val books = bookService.findAll()

        books shouldBe mongoTemplate.findAll(Book::class.java)
    }

    @Test
    fun `insert should insert the book`() {
        val title = "newTitle"
        val authorId = 1L
        val genreIds = setOf(1L, 5L)

        bookService.insert(title, authorId, genreIds)

        mongoTemplate.findAll(Book::class.java).last() should { book ->
            book.id shouldBe 4L
            book.title shouldBe title
            book.author shouldBe mongoTemplate.findById(authorId, Author::class.java)
            book.genres.zip(genreIds).map {
                it.first shouldBe mongoTemplate.findById(it.second, Genre::class.java)
            }
        }
    }

    @Test
    fun `update should update the book`() {
        val bookId = 1L
        val title = "updatedTitle"
        val authorId = 2L
        val genreIds = setOf(3L, 4L)

        bookService.update(bookId, title, authorId, genreIds)

        mongoTemplate.findById(bookId, Book::class.java).shouldNotBeNull() should { book ->
            book.title shouldBe title
            book.author shouldBe mongoTemplate.findById(authorId, Author::class.java)
            book.genres.zip(genreIds).map {
                it.first shouldBe mongoTemplate.findById(it.second, Genre::class.java)
            }
        }
    }

    @Test
    fun `delete should delete the book`() {
        val bookId = 1L

        bookService.deleteById(bookId)

        mongoTemplate.findById(bookId, Book::class.java).shouldBeNull()
    }
}
