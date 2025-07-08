package ru.otus.hw.dao

import com.opencsv.bean.CsvToBeanBuilder
import ru.otus.hw.config.TestFileNameProvider
import ru.otus.hw.dao.dto.QuestionDto
import ru.otus.hw.domain.Question
import ru.otus.hw.exceptions.QuestionReadException
import java.io.InputStreamReader

class CsvQuestionDao(private val fileNameProvider: TestFileNameProvider) : QuestionDao {
    override fun findAll(): List<Question> {
        return try {
            javaClass.classLoader
                .getResourceAsStream(fileNameProvider.testFileName)
                .use { inputStream ->
                    CsvToBeanBuilder<QuestionDto>(inputStream?.let { InputStreamReader(it) })
                        .withType(QuestionDto::class.java)
                        .withSkipLines(1)
                        .withSeparator(';')
                        .build()
                        .parse()
                        .map { it.toDomainObject() }
            }
        } catch (e: RuntimeException) {
            throw QuestionReadException("Error reading questions", e)
        }
    }
}
