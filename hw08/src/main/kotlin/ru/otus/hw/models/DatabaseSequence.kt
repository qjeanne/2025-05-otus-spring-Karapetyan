package ru.otus.hw.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "sequences")
data class DatabaseSequence (
    @Id
    var id: String,

    var seq: Long
)
