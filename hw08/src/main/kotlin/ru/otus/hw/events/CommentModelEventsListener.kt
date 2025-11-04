package ru.otus.hw.events

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent
import org.springframework.stereotype.Component
import ru.otus.hw.models.Comment
import ru.otus.hw.services.SequenceGeneratorService

@Component
class CommentModelEventsListener(
    private val sequenceGenerator: SequenceGeneratorService
) : AbstractMongoEventListener<Comment>() {
    override fun onBeforeConvert(event: BeforeConvertEvent<Comment>) {
        if (event.source.id < 1) {
            event.source.id = sequenceGenerator.generateSequence(event.collectionName)
        }
    }
}
