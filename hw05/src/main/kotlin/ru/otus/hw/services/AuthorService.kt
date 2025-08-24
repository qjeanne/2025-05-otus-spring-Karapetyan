package ru.otus.hw.services

import ru.otus.hw.models.Author

fun interface AuthorService {
    fun findAll(): List<Author>
}
