package ru.otus.hw.service

import ru.otus.hw.dao.QuestionDao
import ru.otus.hw.exceptions.QuestionReadException

class TestServiceImpl(
    private val ioService: IOService,
    private val questionDao: QuestionDao,
    private val questionFormatter: QuestionFormatter
) : TestService {
    override fun executeTest() {
        try {
            val questions = questionDao.findAll()
            ioService.printLine("")
            ioService.printLine(questionFormatter.format(questions))
        } catch (e: QuestionReadException) {
            ioService.printLine("Unable to load questions")
        }
    }
}
