package ru.otus.hw.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import ru.otus.hw.dto.GenreResponseDto
import ru.otus.hw.services.GenreService

@RestController
class GenreController(
    private val genreService: GenreService
) {
    @GetMapping("/api/genres")
    fun getAllGenres(): Flux<GenreResponseDto> = genreService.findAll()
}
