package ru.otus.hw.repositories

import reactor.core.publisher.Mono
import ru.otus.hw.models.Book

fun interface CommentRepositoryCustom {
    fun updateBook(bookId: String, newBook: Book): Mono<Void>
}
