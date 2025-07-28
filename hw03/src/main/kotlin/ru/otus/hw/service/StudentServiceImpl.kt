package ru.otus.hw.service

import org.springframework.stereotype.Service
import ru.otus.hw.domain.Student

@Service
class StudentServiceImpl(
    private val ioService: LocalizedIOService
) : StudentService {
    override fun determineCurrentStudent(): Student {
        val firstName = ioService.readStringWithPromptLocalized("StudentService.input.first.name")
        val lastName = ioService.readStringWithPromptLocalized("StudentService.input.last.name")
        return Student(firstName, lastName)
    }
}
