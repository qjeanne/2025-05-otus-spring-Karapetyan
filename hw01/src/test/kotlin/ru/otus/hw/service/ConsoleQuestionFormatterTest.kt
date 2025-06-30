package ru.otus.hw.service

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import ru.otus.hw.domain.Answer
import ru.otus.hw.domain.Question

class ConsoleQuestionFormatterTest {

    private val questionFormatter = ConsoleQuestionFormatter()

    @Test
    fun `format should format questions with answers`() {
        val questions = listOf(
            Question(
                text = "question1",
                answers = listOf(
                    Answer(text = "answer01", isCorrect = true),
                    Answer(text = "answer02", isCorrect = false),
                )
            ),
            Question(
                text = "question2",
                answers = listOf(
                    Answer(text = "answer11", isCorrect = false),
                )
            )
        )
        val expected = """
            Question 1: question1
                1) answer01
                2) answer02
            Question 2: question2
                1) answer11
            
        """.trimIndent()

        val formattedQuestion = questionFormatter.format(questions)

        formattedQuestion shouldBe expected
    }
}