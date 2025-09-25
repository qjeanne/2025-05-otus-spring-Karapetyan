package ru.otus.hw.converters

import org.springframework.stereotype.Component
import ru.otus.hw.models.Author

@Component
class AuthorConverter {
    fun authorToString(author: Author) = "Id: ${author.id}, FullName: ${author.fullName}"
}
