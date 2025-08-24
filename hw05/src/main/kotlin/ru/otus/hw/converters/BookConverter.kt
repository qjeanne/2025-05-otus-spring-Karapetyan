package ru.otus.hw.converters

import org.springframework.stereotype.Component
import ru.otus.hw.models.Book

@Component
class BookConverter(
    private val authorConverter: AuthorConverter,
    private val genreConverter: GenreConverter
) {
    fun bookToString(book: Book): String {
        val genresString = book.genres
            .joinToString(separator = ", ") {
                "{${genreConverter.genreToString(it)}}"
            }
        val authorString = authorConverter.authorToString(book.author)

        return "Id: ${book.id}, title: ${book.title}, author: $authorString, genres: $genresString"
    }
}
