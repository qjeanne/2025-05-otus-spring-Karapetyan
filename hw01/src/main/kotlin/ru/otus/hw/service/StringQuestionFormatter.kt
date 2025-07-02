package ru.otus.hw.service

import ru.otus.hw.domain.Question

class StringQuestionFormatter : QuestionFormatter {
    override fun format(questions: List<Question>) = buildString {
        questions.forEachIndexed { questionIndex, question ->
            appendLine("Question ${questionIndex + 1}: ${question.text}")
            question.answers.forEachIndexed { answerIndex, answer ->
                appendLine("    ${answerIndex + 1}) ${answer.text}")
            }
        }
    }
}
