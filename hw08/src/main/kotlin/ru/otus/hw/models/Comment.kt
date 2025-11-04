package ru.otus.hw.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "comments")
data class Comment(
    @Id
    var id: Long,

    @Field(name = "text")
    var text: String,

    var book: Book
)
