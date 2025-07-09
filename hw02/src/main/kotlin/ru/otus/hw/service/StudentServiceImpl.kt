package ru.otus.hw.service

import org.springframework.stereotype.Service
import ru.otus.hw.domain.Student

@Service
class StudentServiceImpl(
    private val ioService: IOService
) : StudentService {
    override fun determineCurrentStudent(): Student {
        val firstName = ioService.readStringWithPrompt("Please input your first name")
        val lastName = ioService.readStringWithPrompt("Please input your last name")
        return Student(firstName, lastName)
    }
}
