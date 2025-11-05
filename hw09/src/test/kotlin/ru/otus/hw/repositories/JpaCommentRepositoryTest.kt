package ru.otus.hw.repositories

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import ru.otus.hw.models.Author
import ru.otus.hw.models.Book
import ru.otus.hw.models.Comment
import ru.otus.hw.models.Genre

@DataJpaTest
open class JpaCommentRepositoryTest {

    @Autowired
    private lateinit var repository: CommentRepository

    @Autowired
    private lateinit var em: TestEntityManager

    private lateinit var dbAuthors: List<Author>
    private lateinit var dbGenres: List<Genre>
    private lateinit var dbBooks: List<Book>
    private lateinit var dbComments: List<Comment>

    @BeforeEach
    fun setUp() {
        dbAuthors = getDbAuthors()
        dbGenres = getDbGenres()
        dbBooks = getDbBooks(dbAuthors, dbGenres)
        dbComments = listOf(
            Comment(1L, "Comment_1", dbBooks[0]),
            Comment(2L, "Comment_2", dbBooks[1]),
            Comment(3L, "Comment_3", dbBooks[1])
        )
    }

    @Test
    fun `should return correct comment by id`() {
        val expectedComment = dbComments[1]

        val actualComment = repository.findById(expectedComment.id)

        actualComment shouldBe expectedComment
    }

    @Test
    fun `should return correct comment list by book id`() {
        val id = 2L
        val expectedComments = dbComments.filter { it.book.id == id }

        val actualComments = repository.findByBookId(id)

        actualComments shouldContainExactly expectedComments
        actualComments.forEach { println(it) }
    }

    @Test
    fun `should save new comment`() {
        val expectedComment = Comment(
            id = 0,
            text = "Test",
            book = dbBooks[0]
        )

        val returnedComment = repository.save(expectedComment)

        returnedComment should {
            it.id > 0
            it.text shouldBe expectedComment.text
            it.book shouldBe expectedComment.book
        }
        em.find(Comment::class.java, returnedComment.id) shouldBe returnedComment
    }

    @Test
    fun `should save updated comment`() {
        val expectedComment = Comment(
            id = 1L,
            text = "Updated",
            book = dbBooks[0]
        )
        em.find(Comment::class.java, expectedComment.id) shouldNotBe expectedComment

        val returnedComment = repository.save(expectedComment)

        returnedComment should {
            it.id > 0
            it.text shouldBe expectedComment.text
            it.book shouldBe expectedComment.book
        }
        em.find(Comment::class.java, returnedComment.id) shouldBe returnedComment
    }

    @Test
    fun `should delete comment`() {
        val comment = em.find(Comment::class.java, 1L) shouldNotBe null
        em.detach(comment)

        repository.deleteById(1L)

        em.find(Comment::class.java, 1L) shouldBe null
    }

    companion object {
        private fun getDbAuthors(): List<Author> {
            return (1L..3L).map { id -> Author(id, "Author_$id") }
        }

        private fun getDbGenres(): List<Genre> {
            return (1L..6L).map { id -> Genre(id, "Genre_$id") }
        }

        @JvmStatic
        private fun getDbBooks(dbAuthors: List<Author>, dbGenres: List<Genre>): List<Book> {
            return (1..3).map { id ->
                Book(
                    id.toLong(),
                    "BookTitle_$id",
                    dbAuthors[id - 1],
                    dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2).toMutableList()
                )
            }
        }
    }
}
