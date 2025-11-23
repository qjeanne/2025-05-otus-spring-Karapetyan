package ru.otus.hw.services

import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
@Import(BookServiceImpl::class)
open class BookServiceImplTest {

    @Autowired
    private lateinit var bookService: BookService

    @Test
    fun `access to the book author does not cause a LazyInitializationException`() {
        val book = bookService.findById(1L)

        val authorName = book.author.fullName

        authorName.shouldNotBeNull()
    }

    @Test
    fun `access to the book genres does not cause a LazyInitializationException`() {
        val book = bookService.findById(1L)

        val genresCount = book.genres.size

        genresCount.shouldNotBeNull()
        genresCount shouldBeGreaterThan 0
    }

    @Test
    fun `access to the book author does not cause a LazyInitializationException in findAll`() {
        val book = bookService.findAll()

        val authorName = book.map { it.author.fullName }

        authorName.map { it.shouldNotBeNull() }
    }

    @Test
    fun `access to the book genres does not cause a LazyInitializationException in findAll`() {
        val books = bookService.findAll()

        val genresCount = books.map { it.genres.size }

        genresCount.map {
            it.shouldNotBeNull()
            it shouldBeGreaterThan 0
        }
    }
}
