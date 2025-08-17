package ru.otus.hw.repositories

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import ru.otus.hw.models.Genre
import java.sql.ResultSet
import java.sql.SQLException

@Repository
open class JdbcGenreRepository(
    private val jdbc: NamedParameterJdbcOperations
) : GenreRepository {
    override fun findAll(): List<Genre> = jdbc.query("select id, name from genres", GnreRowMapper())

    override fun findAllByIds(ids: Set<Long>): List<Genre> {
        val params = mapOf("ids" to ids)
        return jdbc.query(
            "select id, name from genres where id in (:ids)",
            params,
            GnreRowMapper()
        )
    }


    private class GnreRowMapper : RowMapper<Genre> {
        @Throws(SQLException::class)
        override fun mapRow(rs: ResultSet, i: Int) =
            Genre(
                id = rs.getLong("id"),
                name = rs.getString("name")
            )
    }
}
