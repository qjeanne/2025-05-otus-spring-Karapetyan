package ru.otus.hw.dto

import ru.otus.hw.models.Genre

data class GenreResponseDto(
    val id: Long,
    val name: String
) {
    companion object {
        fun fromDomainObject(genre: Genre): GenreResponseDto {
            return GenreResponseDto(
                id = genre.id,
                name = genre.name
            )
        }
    }
}
