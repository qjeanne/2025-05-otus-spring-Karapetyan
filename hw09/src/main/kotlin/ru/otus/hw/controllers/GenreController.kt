package ru.otus.hw.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.otus.hw.services.GenreService

@Controller
@RequestMapping("/genres")
class GenreController(
    private val genreService: GenreService
) {
    @GetMapping
    fun getAllGenres(
        model: Model
    ): String {
        val genres = genreService.findAll()
        model.addAttribute("genres", genres)
        return "genres-list"
    }
}
