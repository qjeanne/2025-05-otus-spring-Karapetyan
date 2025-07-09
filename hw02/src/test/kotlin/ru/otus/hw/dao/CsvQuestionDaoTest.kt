package ru.otus.hw.dao

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import ru.otus.hw.config.TestFileNameProvider
import ru.otus.hw.domain.Answer
import ru.otus.hw.domain.Question
import ru.otus.hw.exceptions.QuestionReadException

@ExtendWith(MockitoExtension::class)
class CsvQuestionDaoTest {

    @Mock
    private lateinit var fileNameProvider: TestFileNameProvider

    @InjectMocks
    private lateinit var csvQuestionDao: CsvQuestionDao

    @Test
    fun `findAll should throw QuestionReadException when get resource throw exception`() {
        `when`(fileNameProvider.testFileName).thenThrow(RuntimeException("message"))

        val exception = shouldThrow<QuestionReadException> {
            csvQuestionDao.findAll()
        }

        exception shouldBe QuestionReadException("Error reading questions")
    }

    @Test
    fun `findAll should return list of Question from resource`() {
        `when`(fileNameProvider.testFileName).thenReturn("test.csv")

        val result = csvQuestionDao.findAll()

        result.shouldContainExactly(
            Question(
                text = "question1",
                answers = listOf(
                    Answer(text = "answer01", isCorrect = true),
                    Answer(text = "answer02", isCorrect = false),
                    Answer(text = "answer03", isCorrect = false)
                )
            ),
            Question(
                text = "question2",
                answers = listOf(
                    Answer(text = "answer11", isCorrect = false),
                    Answer(text = "answer12", isCorrect = true),
                    Answer(text = "answer13", isCorrect = false)
                )
            )
        )
    }
}
