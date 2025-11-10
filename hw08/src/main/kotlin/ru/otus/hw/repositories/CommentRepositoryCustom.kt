package ru.otus.hw.repositories

import ru.otus.hw.models.Book

fun interface CommentRepositoryCustom {
    fun updateBook(bookId: String, newBook: Book)
}
