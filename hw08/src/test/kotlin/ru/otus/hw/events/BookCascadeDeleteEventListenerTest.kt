package ru.otus.hw.events

import io.kotest.matchers.collections.shouldBeEmpty
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import ru.otus.hw.MongoRepositoryTest
import ru.otus.hw.models.Comment
import ru.otus.hw.repositories.BookRepository

@Import(BookCascadeDeleteEventListener::class)
class BookCascadeDeleteEventListenerTest: MongoRepositoryTest() {

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Test
    fun `after deleting a book its comments should be deleted`() {
        val id = 1L

        bookRepository.deleteById(id)

        mongoTemplate.findAll(Comment::class.java)
            .filter { it.book.id == id }
            .shouldBeEmpty()
    }
}
