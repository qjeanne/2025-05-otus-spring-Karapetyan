package ru.otus.hw.dto

import ru.otus.hw.models.Author

data class AuthorResponseDto(
    val id: Long,
    val fullName: String
) {
    companion object {
        fun fromDomainObject(author: Author): AuthorResponseDto {
            return AuthorResponseDto(
                id = author.id,
                fullName = author.fullName
            )
        }
    }
}
