package ru.otus.hw.dto

import ru.otus.hw.models.Book

data class BookResponseDto(
    val id: Long,
    val title: String,
    val author: AuthorResponseDto,
    val genres: List<GenreResponseDto>
) {

    companion object {
        fun fromDomainObject(book: Book): BookResponseDto {
            return BookResponseDto(
                id = book.id,
                title = book.title,
                author = AuthorResponseDto.fromDomainObject(book.author),
                genres = book.genres.map { GenreResponseDto.fromDomainObject(it) }
            )
        }
    }
}
