package ru.otus.hw.services

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import ru.otus.hw.MongoRepositoryTest
import ru.otus.hw.models.Author

@Import(AuthorServiceImpl::class)
open class AuthorServiceImplTest: MongoRepositoryTest() {

    @Autowired
    private lateinit var authorService: AuthorService

    @Test
    fun `findAll should return all authors`() {
        val authors = authorService.findAll()

        authors shouldBe mongoTemplate.findAll(Author::class.java)
    }
}
