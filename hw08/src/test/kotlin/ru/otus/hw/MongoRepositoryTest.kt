package ru.otus.hw

import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoTemplate
import ru.otus.hw.models.Author
import ru.otus.hw.models.Book
import ru.otus.hw.models.Comment
import ru.otus.hw.models.Genre

@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
open class MongoRepositoryTest {

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    private val authors = listOf(
        Author("1", "Author_1"),
        Author("2", "Author_2"),
        Author("3", "Author_3")
    )

    private val genres = listOf(
        Genre("1", "Genre_1"),
        Genre("2", "Genre_2"),
        Genre("3", "Genre_3"),
        Genre("4", "Genre_4"),
        Genre("5", "Genre_5"),
        Genre("6", "Genre_6")
    )

    private val books = listOf(
        Book("1", "BookTitle_1", author = authors[0], genres = mutableListOf(genres[0], genres[1])),
        Book("2", "BookTitle_2", author = authors[1], genres = mutableListOf(genres[2], genres[3])),
        Book("3", "BookTitle_3", author = authors[2], genres = mutableListOf(genres[4], genres[5]))
    )

    private val comments = listOf(
        Comment("1", "Comment_1", book = books[0]),
        Comment("2", "Comment_2", book = books[1]),
        Comment("3", "Comment_3", book = books[1])
    )

    @BeforeEach
    fun init() {
        if (mongoTemplate.collectionNames.isEmpty()) {
            mongoTemplate.insertAll(authors)
            mongoTemplate.insertAll(genres)
            mongoTemplate.insertAll(books)
            mongoTemplate.insertAll(comments)
        }
    }

    @Order(1)
    @Test
    fun testDb() {
        mongoTemplate.db.shouldNotBeNull()
    }
}
