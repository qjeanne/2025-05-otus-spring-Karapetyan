package ru.otus.hw.repositories

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import ru.otus.hw.models.Author

@DataJpaTest
@Import(JpaAuthorRepository::class)
open class JpaAuthorRepositoryTest {

    @Autowired
    private lateinit var repositoryJpa: JpaAuthorRepository

    private val authors = (1L..3L).map { id -> Author(id, "Author_$id") }

    @Test
    fun `should return correct author list`() {
        val expectedAuthors = authors

        val actualAuthors = repositoryJpa.findAll()

        actualAuthors shouldContainExactly expectedAuthors
    }

    @Test
    fun `should return correct author by id`() {
        val id = 1L
        val expectedAuthors = authors.first { it.id == id }

        val actualAuthors = repositoryJpa.findById(id)

        actualAuthors shouldBe expectedAuthors
    }
}
