package ru.otus.hw.repositories

import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import ru.otus.hw.models.Genre

@DataJpaTest
open class JpaGenreRepositoryTest {

    @Autowired
    private lateinit var repository: GenreRepository

    private val genres = (1L..6L).map { id -> Genre(id, "Genre_$id") }

    @Test
    fun `should return correct genre list`() {
        val expectedGenres = genres

        val actualGenres = repository.findAll()

        actualGenres shouldContainExactly expectedGenres
    }

    @Test
    fun `should return correct genre list by ids`() {
        val ids = setOf(1L, 3L, 6L)
        val expectedGenres = genres.filter { it.id in ids }

        val actualGenres = repository.findAllByIdIn(ids)

        actualGenres shouldContainExactly expectedGenres
    }
}
