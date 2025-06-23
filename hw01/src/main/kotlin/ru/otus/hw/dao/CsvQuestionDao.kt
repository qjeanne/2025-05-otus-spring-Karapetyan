package ru.otus.hw.dao

import com.opencsv.bean.CsvToBeanBuilder
import ru.otus.hw.config.TestFileNameProvider
import ru.otus.hw.dao.dto.QuestionDto
import ru.otus.hw.domain.Question
import ru.otus.hw.exceptions.QuestionReadException
import java.io.InputStreamReader

class CsvQuestionDao(private val fileNameProvider: TestFileNameProvider) : QuestionDao {
    override fun findAll(): List<Question> {
        val inputStream = try {
            javaClass.classLoader.getResourceAsStream(fileNameProvider.testFileName)
        } catch (e: RuntimeException) {
            throw QuestionReadException(e.message.toString(), e.cause)
        }

        return CsvToBeanBuilder<QuestionDto>(InputStreamReader(inputStream))
            .withType(QuestionDto::class.java)
            .withSkipLines(1)
            .withSeparator(';')
            .build()
            .parse()
            .map { it.toDomainObject() }
    }
}
