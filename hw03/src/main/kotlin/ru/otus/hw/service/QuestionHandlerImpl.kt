package ru.otus.hw.service

import org.springframework.stereotype.Service
import ru.otus.hw.domain.Question

@Service
class QuestionHandlerImpl(
    private val ioService: LocalizedIOService
) : QuestionHandler {
    override fun handle(question: Question, questionNumber: Int): Boolean {
        ioService.printFormattedLineLocalized("QuestionHandler.question", questionNumber, question.text)
        question.answers.forEachIndexed { answerIndex, answer ->
            ioService.printFormattedLineLocalized("QuestionHandler.answer", answerIndex + 1, answer.text)
        }

        val answer = ioService.readIntForRangeLocalized(
            1,
            question.answers.size,
            "QuestionHandler.error.message"
        )

        return question.answers[answer - 1].isCorrect
    }
}
