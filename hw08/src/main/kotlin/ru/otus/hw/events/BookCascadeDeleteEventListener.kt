package ru.otus.hw.events

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent
import org.springframework.stereotype.Component
import ru.otus.hw.models.Book
import ru.otus.hw.repositories.CommentRepository

@Component
class BookCascadeDeleteEventListener(
    private val commentRepository: CommentRepository
) : AbstractMongoEventListener<Book>() {
    override fun onAfterDelete(event: AfterDeleteEvent<Book>) {
        super.onAfterDelete(event)

        val source = event.source
        val id = source["_id"].toString().toLong()

        commentRepository.deleteByBookId(id)
    }
}
