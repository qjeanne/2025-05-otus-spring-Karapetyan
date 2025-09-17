package ru.otus.hw.converters

import org.springframework.stereotype.Component
import ru.otus.hw.dto.AuthorDto
import ru.otus.hw.dto.BookDto
import ru.otus.hw.dto.GenreDto
import ru.otus.hw.models.Author
import ru.otus.hw.models.Book
import ru.otus.hw.models.Genre

@Component
class BookConverter(
    private val authorConverter: AuthorConverter,
    private val genreConverter: GenreConverter
) {
    fun bookToString(book: BookDto): String {
        val genresString = book.genres
            .joinToString(separator = ", ") {
                "{${genreConverter.genreToString(Genre(id = it.id, name = it.name))}}"
            }
        val authorString = authorConverter.authorToString(Author(id = book.author.id, fullName = book.author.fullName))

        return "Id: ${book.id}, title: ${book.title}, author: $authorString, genres: $genresString"
    }

    fun toDto(book: Book): BookDto {
        return BookDto(
            id = book.id,
            title = book.title,
            author = AuthorDto(id = book.author.id, fullName = book.author.fullName),
            genres = book.genres.map { GenreDto(id = it.id, name = it.name) }
        )
    }

    fun toDtoList(books: List<Book>): List<BookDto> {
        return books.map { toDto(it) }
    }
}
