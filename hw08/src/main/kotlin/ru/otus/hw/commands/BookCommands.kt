package ru.otus.hw.commands

import org.springframework.shell.command.annotation.Command
import ru.otus.hw.converters.BookConverter
import ru.otus.hw.services.BookService

@Command(group = "Book Commands")
class BookCommands(
    private val bookService: BookService,
    private val bookConverter: BookConverter
) {
    @Command(command = ["ab"], description = "Find all books")
    fun findAllBooks(): String = bookService.findAll()
        .joinToString(separator = ",${System.lineSeparator()}") {
            bookConverter.bookToString(it)
        }

    @Command(command = ["bbid"], description = "Find book by id")
    fun findBookById(id: String): String = bookService.findById(id)
        ?.let { bookConverter.bookToString(it) }
        ?: "Book with id $id not found"

    @Command(command = ["bins"], description = "Insert book")
    fun insertBook(title: String, authorId: String, genresIds: Set<String>): String {
        val savedBook = bookService.insert(title, authorId, genresIds)
        return bookConverter.bookToString(savedBook)
    }

    @Command(command = ["bupd"], description = "Update book")
    fun updateBook(id: String, title: String, authorId: String, genresIds: Set<String>): String {
        val savedBook = bookService.update(id, title, authorId, genresIds)
        return bookConverter.bookToString(savedBook)
    }

    @Command(command = ["bdel"], description = "Delete book by id")
    fun deleteBook(id: String) {
        bookService.deleteById(id)
    }
}
