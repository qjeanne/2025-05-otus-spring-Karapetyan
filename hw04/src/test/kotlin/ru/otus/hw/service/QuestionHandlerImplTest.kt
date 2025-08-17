package ru.otus.hw.service

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import ru.otus.hw.domain.Answer
import ru.otus.hw.domain.Question

@SpringBootTest(classes = [QuestionHandlerImpl::class])
@ExtendWith(MockitoExtension::class)
class QuestionHandlerImplTest {

    @MockBean
    private lateinit var ioService: LocalizedIOService

    @Autowired
    private lateinit var questionHandler: QuestionHandlerImpl

    private val question = Question(
        text = "question1",
        answers = listOf(
            Answer(text = "answer01", isCorrect = true),
            Answer(text = "answer02", isCorrect = false),
        )
    )

    @Test
    fun `handle should pass the question to the io service and return true if answer correct`() {
        `when`(ioService.readIntForRangeLocalized(any(), any(), any())).thenReturn(1)

        val formattedQuestion = questionHandler.handle(question, 1)

        formattedQuestion shouldBe true

        val inOrder = Mockito.inOrder(ioService)
        inOrder.verify(ioService).printFormattedLineLocalized("QuestionHandler.question", 1, question.text)
        inOrder.verify(ioService).printFormattedLineLocalized("QuestionHandler.answer",1, question.answers[0].text)
        inOrder.verify(ioService).printFormattedLineLocalized("QuestionHandler.answer",2, question.answers[1].text)
    }

    @Test
    fun `handle should pass the question to the io service and return false if answer incorrect`() {
        `when`(ioService.readIntForRangeLocalized(any(), any(), any())).thenReturn(2)

        val formattedQuestion = questionHandler.handle(question, 1)

        formattedQuestion shouldBe false

        val inOrder = Mockito.inOrder(ioService)
        inOrder.verify(ioService).printFormattedLineLocalized("QuestionHandler.question", 1, question.text)
        inOrder.verify(ioService).printFormattedLineLocalized("QuestionHandler.answer",1, question.answers[0].text)
        inOrder.verify(ioService).printFormattedLineLocalized("QuestionHandler.answer",2, question.answers[1].text)
    }
}
