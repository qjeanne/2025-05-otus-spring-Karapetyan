package ru.otus.hw.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.otus.hw.services.AuthorService

@Controller
@RequestMapping("/authors")
class AuthorController(
    private val authorService: AuthorService
) {
    @GetMapping
    fun getAllAuthors(
        model: Model
    ): String {
        val authors = authorService.findAll()
        model.addAttribute("authors", authors)
        return "authors-list"
    }
}
