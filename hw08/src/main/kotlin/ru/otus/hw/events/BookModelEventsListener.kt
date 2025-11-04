package ru.otus.hw.events

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent
import org.springframework.stereotype.Component
import ru.otus.hw.models.Book
import ru.otus.hw.services.SequenceGeneratorService

@Component
class BookModelEventsListener(
    private val sequenceGenerator: SequenceGeneratorService
) : AbstractMongoEventListener<Book>() {
    override fun onBeforeConvert(event: BeforeConvertEvent<Book>) {
        if (event.source.id < 1) {
            event.source.id = sequenceGenerator.generateSequence(event.collectionName)
        }
    }
}
