package ru.otus.hw.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.hw.dto.AuthorResponseDto
import ru.otus.hw.services.AuthorService

@RestController
class AuthorController(
    private val authorService: AuthorService
) {
    @GetMapping("/api/authors")
    fun getAllAuthors(): ResponseEntity<List<AuthorResponseDto>> =
        ResponseEntity.ok(
            authorService.findAll()
        )
}
