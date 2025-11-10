package ru.otus.hw

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.core.io.ClassPathResource
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component
import ru.otus.hw.models.Author
import ru.otus.hw.models.Book
import ru.otus.hw.models.Comment
import ru.otus.hw.models.Genre

data class DataFile(
    val authors: List<Author>,
    val genres: List<Genre>,
    val books: List<Book>,
    val comments: List<Comment>
)

@Component
class InitializeData(
    private val mongoTemplate: MongoTemplate
) {
    @PostConstruct
    fun init() {
        val objectMapper = jacksonObjectMapper()
        val resource = ClassPathResource("data.json")

        val data = resource.inputStream.use {
            objectMapper.readValue(it, object : TypeReference<DataFile>() {})
        }

        mongoTemplate.insert(data.authors, "authors")
        mongoTemplate.insert(data.genres, "genres")
        mongoTemplate.insert(data.books, "books")
        mongoTemplate.insert(data.comments, "comments")
    }
}
