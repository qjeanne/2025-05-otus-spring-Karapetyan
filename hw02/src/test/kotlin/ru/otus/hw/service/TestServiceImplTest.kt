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
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import ru.otus.hw.dao.QuestionDao
import ru.otus.hw.domain.Answer
import ru.otus.hw.domain.Question
import ru.otus.hw.domain.Student
import ru.otus.hw.domain.TestResult
import ru.otus.hw.exceptions.QuestionReadException

@ExtendWith(MockitoExtension::class)
class TestServiceImplTest {
    @Mock
    private lateinit var ioService: IOService

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
        inOrder.verify(ioService).printFormattedLine("Please answer the questions below%n")
    }

    @Test
    fun `executeTestFor should print error if throw exception`() {
        `when`(questionDao.findAll()).thenThrow(QuestionReadException("test"))

        testServiceImpl.executeTestFor(student)

        verify(ioService).printLine("Unable to load questions")
        verify(questionHandler, never()).handle(any(), any())
    }
}
