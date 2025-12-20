package ru.otus.hw

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.core.io.ClassPathResource
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Component
import ru.otus.hw.models.Author
import ru.otus.hw.models.Book
import ru.otus.hw.models.Comment
import ru.otus.hw.models.Genre
import ru.otus.hw.models.User

data class DataFile(
    val authors: List<Author>,
    val genres: List<Genre>,
    val books: List<Book>,
    val comments: List<Comment>,
    val users: List<User>
)

@Component
class InitializeData(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val objectMapper: ObjectMapper
) {
    @PostConstruct
    fun init() {
        val resource = ClassPathResource("data.json")

        val data = resource.inputStream.use {
            objectMapper.readValue(it, object : TypeReference<DataFile>() {})
        }

        mongoTemplate.insertAll(data.authors)
            .thenMany(mongoTemplate.insertAll(data.genres))
            .thenMany(mongoTemplate.insertAll(data.books))
            .thenMany(mongoTemplate.insertAll(data.comments))
            .thenMany(mongoTemplate.insertAll(data.users))
            .blockLast()
    }
}
