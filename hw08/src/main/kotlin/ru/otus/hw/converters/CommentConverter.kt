package ru.otus.hw.converters

import org.springframework.stereotype.Component
import ru.otus.hw.models.Comment

@Component
class CommentConverter {
    fun commentToString(comment: Comment) = "id: ${comment.id}, text: ${comment.text}"
}
