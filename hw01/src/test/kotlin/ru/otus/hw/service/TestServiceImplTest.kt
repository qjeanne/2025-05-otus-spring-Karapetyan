package ru.otus.hw.service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import ru.otus.hw.dao.QuestionDao
import ru.otus.hw.domain.Answer
import ru.otus.hw.domain.Question
import ru.otus.hw.exceptions.QuestionReadException

@ExtendWith(MockitoExtension::class)
class TestServiceImplTest {
    @Mock
    private lateinit var ioService: IOService

    @Mock
    private lateinit var questionDao: QuestionDao

    @Mock
    private lateinit var questionFormatter: QuestionFormatter

    @InjectMocks
    private lateinit var testServiceImpl: TestServiceImpl

    @Test
    fun `executeTest should print questions with answers`() {
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
        `when`(questionDao.findAll()).thenReturn(questions)
        `when`(questionFormatter.format(any())).thenReturn("test")

        testServiceImpl.executeTest()

        val inOrder = inOrder(ioService)
        inOrder.verify(ioService).printLine("")
        inOrder.verify(ioService).printLine("test")
    }

    @Test
    fun `executeTest should print error if throw exception`() {
        `when`(questionDao.findAll()).thenThrow(QuestionReadException("test"))

        testServiceImpl.executeTest()

        verify(ioService).printLine("Unable to load questions")
        verify(questionFormatter, never()).format(any())
    }
}