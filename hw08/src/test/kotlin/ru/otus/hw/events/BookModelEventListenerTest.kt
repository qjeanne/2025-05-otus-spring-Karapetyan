package ru.otus.hw.events

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSingleElement
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import ru.otus.hw.MongoRepositoryTest
import ru.otus.hw.models.Author
import ru.otus.hw.models.Book
import ru.otus.hw.repositories.BookRepository
import ru.otus.hw.services.SequenceGeneratorService

@Import(
    BookModelEventsListener::class,
    SequenceGeneratorService::class
)
class BookModelEventListenerTest: MongoRepositoryTest() {

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Test
    fun `after insert new book its id should be autoincremented`() {
        val newBook = Book(0, "title", Author(1L, "author"), mutableListOf())

        bookRepository.save(newBook)

        val books = mongoTemplate.findAll(Book::class.java)
        books.filter { it.id == 0L }.shouldBeEmpty()
        books.filter { it.title == newBook.title && it.author == newBook.author && it.genres == newBook.genres }
            .shouldHaveSingleElement(newBook.copy(id = 4L))
    }
}
