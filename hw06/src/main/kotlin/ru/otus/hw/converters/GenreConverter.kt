package ru.otus.hw.converters

import org.springframework.stereotype.Component
import ru.otus.hw.models.Genre

@Component
class GenreConverter {
    fun genreToString(genre: Genre) = "Id: ${genre.id}, Name: ${genre.name}"
}
