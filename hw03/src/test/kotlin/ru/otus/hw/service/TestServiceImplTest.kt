package ru.otus.hw.service

import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import ru.otus.hw.dao.QuestionDao
import ru.otus.hw.domain.Answer
import ru.otus.hw.domain.Question
import ru.otus.hw.domain.Student
import ru.otus.hw.domain.TestResult

@ExtendWith(MockitoExtension::class)
class TestServiceImplTest {
    @Mock
    private lateinit var ioService: LocalizedIOService

    @Mock
    private lateinit var questionDao: QuestionDao

    @Mock
    private lateinit var questionHandler: QuestionHandler

    @InjectMocks
    private lateinit var testServiceImpl: TestServiceImpl

    private val student = Student("first", "last")

    @Test
    fun `executeTestFor should receive questions and pass them to the handler`() {
        val question1 = Question(
            text = "question1",
            answers = listOf(
                Answer(text = "answer01", isCorrect = true),
                Answer(text = "answer02", isCorrect = false),
            )
        )
        val question2 = Question(
            text = "question2",
            answers = listOf(
                Answer(text = "answer11", isCorrect = true)
            )
        )
        val questions = listOf(question1, question2)
        val expected = TestResult(student)
        expected.applyAnswer(question1, false)
        expected.applyAnswer(question2, true)
        `when`(questionDao.findAll()).thenReturn(questions)
        `when`(questionHandler.handle(eq(question1), any())).thenReturn(false)
        `when`(questionHandler.handle(eq(question2), any())).thenReturn(true)

        val result = testServiceImpl.executeTestFor(student)

        result shouldBeEqualToComparingFields expected

        val inOrder = inOrder(ioService)
        inOrder.verify(ioService).printLine("")
        inOrder.verify(ioService).printLineLocalized("TestService.answer.the.questions")
    }
}
