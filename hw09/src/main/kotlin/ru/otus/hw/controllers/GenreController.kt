package ru.otus.hw.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import ru.otus.hw.services.GenreService

@Controller
class GenreController(
    private val genreService: GenreService
) {
    @GetMapping("/genres")
    fun getAllGenres(
        model: Model
    ): String {
        val genres = genreService.findAll()
        model.addAttribute("genres", genres)
        return "genres-list"
    }
}
