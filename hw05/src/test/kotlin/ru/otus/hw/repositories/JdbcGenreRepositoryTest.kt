package ru.otus.hw.repositories

import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import ru.otus.hw.models.Genre

@JdbcTest
@Import(JdbcGenreRepository::class)
open class JdbcGenreRepositoryTest {

    @Autowired
    private lateinit var repositoryJdbc: JdbcGenreRepository

    private val genres = (1L..6L).map { id -> Genre(id, "Genre_$id") }

    @Test
    fun `should return correct genre list`() {
        val expectedGenres = genres

        val actualGenres = repositoryJdbc.findAll()

        actualGenres shouldContainExactly expectedGenres
    }

    @Test
    fun `should return correct genre list by ids`() {
        val ids = setOf(1L, 3L, 6L)
        val expectedGenres = genres.filter { it.id in ids }

        val actualGenres = repositoryJdbc.findAllByIds(ids)

        actualGenres shouldContainExactly expectedGenres
    }
}
