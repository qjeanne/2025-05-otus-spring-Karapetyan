package ru.otus.hw.repositories

import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import ru.otus.hw.exceptions.EntityNotFoundException
import ru.otus.hw.models.Author
import ru.otus.hw.models.Book
import ru.otus.hw.models.Genre
import java.sql.ResultSet
import java.sql.SQLException

@Repository
open class JdbcBookRepository(
    private val jdbc: NamedParameterJdbcOperations,
    private val genreRepository: GenreRepository
) : BookRepository {

    override fun findById(id: Long): Book? {
        val params = mapOf("id" to id)
        return jdbc.query(
            """
                select b.id as bookId,
                b.title as title,
                a.id as authorId,
                a.full_name as authorFullName,
                g.id as genreId,
                g.name as genreName
                from books b 
                join authors a on b.author_id = a.id
                join books_genres bg on b.id = bg.book_id 
                join genres g on bg.genre_id = g.id
                where b.id = :id
            """.trimIndent(),
            params,
            BookResultSetExtractor()
        )
    }

    override fun findAll(): List<Book> {
        val genres = genreRepository.findAll()
        val relations = getAllGenreRelations()
        val books = getAllBooksWithoutGenres()
        mergeBooksInfo(books, genres, relations)
        return books
    }

    override fun save(book: Book): Book {
        return if (book.id == 0L) {
            insert(book)
        } else {
            update(book)
        }
    }

    override fun deleteById(id: Long) {
        val params = mapOf("id" to id)
        jdbc.update("delete from books where id = :id", params)
    }

    private fun getAllBooksWithoutGenres(): List<Book>  = jdbc.query(
        """
            select b.id as bookId, b.title as title, a.id as authorId, a.full_name as authorFullName
            from books b 
            join authors a on b.author_id = a.id
        """.trimIndent(),
            BookRowMapper()
    )

    private fun getAllGenreRelations(): List<BookGenreRelation> = jdbc.query(
        """
            select b.id as bookId, g.id as genreId
            from books b 
            join books_genres bg on b.id = bg.book_id 
            join genres g on bg.genre_id = g.id
        """.trimIndent(),
        BookGenreRelationRowMapper()
    )

    private fun mergeBooksInfo(
        booksWithoutGenres: List<Book>,
        genres: List<Genre>,
        relations: List<BookGenreRelation>
    ) {
        val relationsByBook = relations.groupBy { it.bookId }
            .mapValues { relation ->
                relation.value.map { it.genreId }.toSet()
            }

        val genresById = genres.associateBy { it.id }

        return booksWithoutGenres.forEach { book ->
            val genreIds = relationsByBook[book.id].orEmpty()
            book.genres = genreIds.mapNotNull { genresById[it] }
        }
    }

    private fun insert(book: Book): Book {
        val keyHolder = GeneratedKeyHolder()
        val perams = MapSqlParameterSource()
            .addValue("title", book.title)
            .addValue("authorId", book.author.id)

        jdbc.update(
            "insert into books (title, author_id) values (:title, :authorId)",
            perams,
            keyHolder,
            arrayOf("id")
        )

        book.id = keyHolder.key?.toLong() ?: 0L
        batchInsertGenresRelationsFor(book)

        return book
    }

    private fun update(book: Book): Book {
        val params = mapOf("title" to book.title, "authorId" to book.author.id, "id" to book.id)
        val updatedRows = jdbc.update(
            "update books set title = :title, author_id = :authorId where id = :id",
            params
        )

        if (updatedRows == 0) throw EntityNotFoundException("No books found to update")

        removeGenresRelationsFor(book)
        batchInsertGenresRelationsFor(book)

        return book
    }

    private fun batchInsertGenresRelationsFor(book: Book) {
        jdbc.batchUpdate(
            "insert into books_genres (book_id, genre_id) values (:bookId, :genreId)",
            SqlParameterSourceUtils.createBatch(
                book.genres.map { BookGenreRelation(book.id, it.id) }
            )
        )
    }

    private fun removeGenresRelationsFor(book: Book) {
        val params = mapOf("id" to book.id)
        jdbc.update("delete from books_genres where book_id = :id", params)
    }

    private class BookRowMapper : RowMapper<Book> {
        override fun mapRow(rs: ResultSet, rowNum: Int) = Book(
            id = rs.getLong("bookId"),
            title = rs.getString("title"),
            author = Author(
                id = rs.getLong("authorId"),
                fullName = rs.getString("authorFullName")
            ),
            genres = emptyList()
        )
    }

    private class BookResultSetExtractor : ResultSetExtractor<Book> {
        @Throws(SQLException::class, DataAccessException::class)
        override fun extractData(rs: ResultSet): Book? {
            val booksMap = mutableMapOf<Long, Book>()

            while (rs.next()) {
                val bookId = rs.getLong("bookId")
                val bookTitle = rs.getString("title")
                val author = Author(
                    id = rs.getLong("authorId"),
                    fullName = rs.getString("authorFullName")
                )
                val genre = Genre(
                    id = rs.getLong("genreId"),
                    name = rs.getString("genreName")
                )

                val book = booksMap.getOrPut(bookId) {
                    Book(
                        id = bookId,
                        title = bookTitle,
                        author = author,
                        genres = emptyList()
                    )
                }
                book.genres += genre
            }
            return if (booksMap.size == 1) booksMap.values.toList().first() else null
        }
    }

    private data class BookGenreRelation(val bookId: Long, val genreId: Long)

    private class BookGenreRelationRowMapper : RowMapper<BookGenreRelation> {
        override fun mapRow(rs: ResultSet, rowNum: Int) = BookGenreRelation(
            bookId = rs.getLong("bookId"),
            genreId = rs.getLong("genreId")
        )
    }
}
