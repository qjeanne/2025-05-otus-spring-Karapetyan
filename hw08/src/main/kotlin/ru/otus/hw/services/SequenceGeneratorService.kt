package ru.otus.hw.services

import org.springframework.data.mongodb.core.FindAndModifyOptions.options
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import ru.otus.hw.models.DatabaseSequence

@Service
class SequenceGeneratorService(
    private val mongoTemplate: MongoTemplate
) {
    fun generateSequence(seqName: String): Long {
        val counter = mongoTemplate.findAndModify(
            query(where("_id").`is`(seqName)),
            Update().inc("seq", 1),
            options().returnNew(true).upsert(true),
            DatabaseSequence::class.java
        )
        return counter?.seq ?: 1L
    }
}
