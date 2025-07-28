package ru.otus.hw.dao.dto

import com.opencsv.bean.AbstractCsvConverter
import ru.otus.hw.domain.Answer

class AnswerCsvConverter : AbstractCsvConverter() {
    override fun convertToRead(value: String?): Any? {
        val valueArr = value?.split("%")
        return valueArr?.let { Answer(it.first(), it[1].toBoolean()) }
    }
}
