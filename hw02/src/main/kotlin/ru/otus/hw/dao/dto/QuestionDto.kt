package ru.otus.hw.dao.dto

import com.opencsv.bean.CsvBindAndSplitByPosition
import com.opencsv.bean.CsvBindByPosition
import ru.otus.hw.domain.Answer
import ru.otus.hw.domain.Question
import kotlin.collections.List

class QuestionDto {
    @CsvBindByPosition(position = 0)
    var text: String = ""

    @CsvBindAndSplitByPosition(
        position = 1,
        collectionType = List::class,
        elementType = Answer::class,
        converter = AnswerCsvConverter::class,
        splitOn = "\\|"
    )
    var answers: List<Answer> = emptyList()

    fun toDomainObject(): Question = Question(text, answers)
}
