package ru.otus.hw.services

import ru.otus.hw.dto.GenreResponseDto

fun interface GenreService {
    fun findAll(): List<GenreResponseDto>
}
