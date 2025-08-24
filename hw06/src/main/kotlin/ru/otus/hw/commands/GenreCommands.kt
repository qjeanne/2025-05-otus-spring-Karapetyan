package ru.otus.hw.commands

import org.springframework.shell.command.annotation.Command
import ru.otus.hw.converters.GenreConverter
import ru.otus.hw.services.GenreService


@Command(group = "Genre Commands")
class GenreCommands(
    private val genreService: GenreService,
    private val genreConverter: GenreConverter
) {
    @Command(command = ["ag"], description = "Find all genres")
    fun findAllGenres(): String = genreService.findAll()
        .joinToString(separator = ",${System.lineSeparator()}") {
            genreConverter.genreToString(it)
        }
}
