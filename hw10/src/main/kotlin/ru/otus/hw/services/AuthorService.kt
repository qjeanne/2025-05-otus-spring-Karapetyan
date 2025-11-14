package ru.otus.hw.services

import ru.otus.hw.dto.AuthorResponseDto

fun interface AuthorService {
    fun findAll(): List<AuthorResponseDto>
}
