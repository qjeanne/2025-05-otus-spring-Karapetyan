package ru.otus.hw.dto

import ru.otus.hw.models.Genre

data class GenreResponseDto(
    val id: String,
    val name: String
) {
    companion object {
        fun fromDomainObject(genre: Genre): GenreResponseDto {
            return GenreResponseDto(
                id = genre.id!!,
                name = genre.name
            )
        }
    }
}
