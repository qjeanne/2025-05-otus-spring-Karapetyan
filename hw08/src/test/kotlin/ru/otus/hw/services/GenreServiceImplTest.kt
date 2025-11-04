package ru.otus.hw.services

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import ru.otus.hw.MongoRepositoryTest
import ru.otus.hw.models.Genre

@Import(GenreServiceImpl::class)
open class GenreServiceImplTest: MongoRepositoryTest() {

    @Autowired
    private lateinit var genreService: GenreService

    @Test
    fun `findAll should return all genres`() {
        val genres = genreService.findAll()

        genres shouldBe mongoTemplate.findAll(Genre::class.java)
    }
}
