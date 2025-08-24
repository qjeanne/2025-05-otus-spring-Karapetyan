package ru.otus.hw.repositories

import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import ru.otus.hw.models.Genre

@DataJpaTest
@Import(JpaGenreRepository::class)
open class JpaGenreRepositoryTest {

    @Autowired
    private lateinit var repositoryJpa: JpaGenreRepository

    private val genres = (1L..6L).map { id -> Genre(id, "Genre_$id") }

    @Test
    fun `should return correct genre list`() {
        val expectedGenres = genres

        val actualGenres = repositoryJpa.findAll()

        actualGenres shouldContainExactly expectedGenres
    }

    @Test
    fun `should return correct genre list by ids`() {
        val ids = setOf(1L, 3L, 6L)
        val expectedGenres = genres.filter { it.id in ids }

        val actualGenres = repositoryJpa.findAllByIds(ids)

        actualGenres shouldContainExactly expectedGenres
    }
}
