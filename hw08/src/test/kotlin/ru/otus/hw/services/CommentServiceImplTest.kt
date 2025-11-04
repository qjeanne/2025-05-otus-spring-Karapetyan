package ru.otus.hw.services

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import ru.otus.hw.MongoRepositoryTest
import ru.otus.hw.events.CommentModelEventsListener
import ru.otus.hw.models.Book
import ru.otus.hw.models.Comment

@Import(
    CommentServiceImpl::class,
    CommentModelEventsListener::class,
    SequenceGeneratorService::class
)
open class CommentServiceImplTest: MongoRepositoryTest() {

    @Autowired
    private lateinit var commentService: CommentService

    @Test
    fun `findById should return the comment`() {
        val id = 1L

        val comment = commentService.findById(id)

        comment shouldBe mongoTemplate.findById(id, Comment::class.java)
    }

    @Test
    fun `findById should return null if comment with this id not exist`() {
        val comment = commentService.findById(10L)

        comment.shouldBeNull()
    }

    @Test
    fun `findByBookId should return all comments for this book`() {
        val bookId = 2L

        val books = commentService.findByBookId(bookId)

        books shouldBe mongoTemplate.findAll(Comment::class.java).filter { it.book.id == bookId }
    }

    @Test
    fun `insert should insert the comment`() {
        val text = "newText"
        val bookId = 3L

        commentService.insert(text, bookId)

        mongoTemplate.findAll(Comment::class.java).last() should { comment ->
            comment.id shouldBe 4L
            comment.text shouldBe text
            comment.book shouldBe mongoTemplate.findById(bookId, Book::class.java)
        }
    }

    @Test
    fun `update should update the comment`() {
        val commentId = 1L
        val text = "updatedText"
        val bookId = 3L

        commentService.update(commentId, text, bookId)

        mongoTemplate.findById(commentId, Comment::class.java).shouldNotBeNull() should { comment ->
            comment.text shouldBe text
            comment.book shouldBe mongoTemplate.findById(bookId, Book::class.java)
        }
    }

    @Test
    fun `delete should delete the comment`() {
        val commentId = 1L

        commentService.deleteById(commentId)

        mongoTemplate.findById(commentId, Comment::class.java).shouldBeNull()
    }
}
