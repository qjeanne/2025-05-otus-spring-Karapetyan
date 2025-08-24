package ru.otus.hw.repositories

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import ru.otus.hw.models.Author
import java.sql.ResultSet
import java.sql.SQLException

@Repository
open class JdbcAuthorRepository(
    private val jdbc: NamedParameterJdbcOperations
) : AuthorRepository {
    override fun findAll(): List<Author> = jdbc.query("select id, full_name from authors", AuthorRowMapper())

    override fun findById(id: Long): Author? {
        val params = mapOf("id" to id)

        val authors = jdbc.query(
            "select id, full_name from authors where id = :id",
            params,
            AuthorRowMapper()
        )

        return if (authors.size == 1) authors.first() else null
    }

    private class AuthorRowMapper : RowMapper<Author> {
        @Throws(SQLException::class)
        override fun mapRow(rs: ResultSet, i: Int) =
            Author(
                id = rs.getLong("id"),
                fullName = rs.getString("full_name")
            )
    }
}
