package ru.otus.hw.commands

import org.springframework.shell.command.annotation.Command
import ru.otus.hw.converters.AuthorConverter
import ru.otus.hw.services.AuthorService

@Command(group = "Author Commands")
class AuthorCommands(
    private val authorService: AuthorService,
    private val authorConverter: AuthorConverter
) {
    @Command(command = ["aa"], description = "Find all authors")
    fun findAllAuthors() = authorService.findAll()
        .joinToString(separator = ",${System.lineSeparator()}") {
            authorConverter.authorToString(it)
        }
}
