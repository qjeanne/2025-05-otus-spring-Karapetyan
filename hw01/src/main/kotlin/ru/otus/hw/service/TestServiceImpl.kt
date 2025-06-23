package ru.otus.hw.service

import ru.otus.hw.dao.QuestionDao

class TestServiceImpl(
    private val ioService: IOService,
    private val questionDao: QuestionDao
) : TestService {
    override fun executeTest() {
        ioService.printLine("")
        questionDao.findAll().forEachIndexed { index, question ->
            ioService.printFormattedLine("Question %d: %s", index + 1, question.text)
            question.answers.forEachIndexed { i, answer ->
                ioService.printFormattedLine("  %d) %s", i + 1, answer.text)
            }
            ioService.printFormattedLine("")
        }
    }
}
