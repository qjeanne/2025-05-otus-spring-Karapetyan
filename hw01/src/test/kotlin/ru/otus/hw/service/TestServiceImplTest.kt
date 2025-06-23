package ru.otus.hw.service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import ru.otus.hw.dao.QuestionDao
import ru.otus.hw.domain.Answer
import ru.otus.hw.domain.Question

@ExtendWith(MockitoExtension::class)
class TestServiceImplTest {
    @Mock
    private lateinit var ioService: IOService

    @Mock
    private lateinit var questionDao: QuestionDao

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

        testServiceImpl.executeTest()

        val inOrder = inOrder(ioService)
        inOrder.verify(ioService).printLine("")
        inOrder.verify(ioService).printFormattedLine("Question %d: %s", 1, "question1")
        inOrder.verify(ioService).printFormattedLine("  %d) %s", 1, "answer01")
        inOrder.verify(ioService).printFormattedLine("  %d) %s", 2, "answer02")
        inOrder.verify(ioService).printFormattedLine("")
        inOrder.verify(ioService).printFormattedLine("Question %d: %s", 2, "question2")
        inOrder.verify(ioService).printFormattedLine("  %d) %s", 1, "answer11")
        inOrder.verify(ioService).printFormattedLine("")
    }
}