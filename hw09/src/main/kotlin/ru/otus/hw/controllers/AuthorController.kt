package ru.otus.hw.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import ru.otus.hw.services.AuthorService

@Controller
class AuthorController(
    private val authorService: AuthorService
) {
    @GetMapping("/authors")
    fun getAllAuthors(
        model: Model
    ): String {
        val authors = authorService.findAll()
        model.addAttribute("authors", authors)
        return "authors-list"
    }
}
