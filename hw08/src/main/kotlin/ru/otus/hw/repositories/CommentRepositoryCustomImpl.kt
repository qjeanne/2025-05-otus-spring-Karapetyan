package ru.otus.hw.repositories

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import ru.otus.hw.models.Book
import ru.otus.hw.models.Comment

class CommentRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
): CommentRepositoryCustom  {
    override fun updateBook(bookId: String, newBook: Book) {
        val query = Query().addCriteria(Criteria.where("book.id").`is`(bookId))
        val update = Update().set("book", newBook)
        mongoTemplate.updateMulti(query, update, Comment::class.java)
    }
}
