package ru.otus.hw.repositories

import jakarta.persistence.EntityManager
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType
import org.springframework.stereotype.Repository
import ru.otus.hw.converters.BookConverter
import ru.otus.hw.dto.BookDto
import ru.otus.hw.models.Book

@Repository
open class JpaBookRepository(
    private val em: EntityManager,
    private val bookConverter: BookConverter
) : BookRepository {
    override fun findById(id: Long): BookDto? {
        val book = em.find(Book::class.java, id)

        return bookConverter.toDto(book)
    }

    override fun findAll(): List<BookDto> {
        val entityGraph = em.getEntityGraph("authors-entity-graph")
        val books = em.createQuery("select b from Book b", Book::class.java)
            .setHint(EntityGraphType.FETCH.key, entityGraph)
            .resultList

        return bookConverter.toDtoList(books)
    }

    override fun save(book: Book): BookDto =
        if (book.id == 0L) {
            em.persist(book)
            bookConverter.toDto(book)
        } else {
            bookConverter.toDto(em.merge(book))
        }

    override fun deleteById(id: Long) {
        val book = em.find(Book::class.java, id)
        em.remove(book)
    }
}
