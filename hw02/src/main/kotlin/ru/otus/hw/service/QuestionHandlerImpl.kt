package ru.otus.hw.service

import org.springframework.stereotype.Service
import ru.otus.hw.domain.Question

@Service
class QuestionHandlerImpl(
    private val ioService: IOService
) : QuestionHandler {
    override fun handle(question: Question, questionNumber: Int): Boolean {
        ioService.printLine("Question $questionNumber: ${question.text}")
        question.answers.forEachIndexed { answerIndex, answer ->
            ioService.printLine("    ${answerIndex + 1}) ${answer.text}")
        }

        val answer = ioService.readIntForRange(
            1,
            question.answers.size,
            "Enter response number from 1 to ${question.answers.size}"
        )

        return question.answers[answer - 1].isCorrect
    }
}
