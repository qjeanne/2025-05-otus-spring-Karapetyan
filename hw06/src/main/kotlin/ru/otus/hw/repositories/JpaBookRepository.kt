package ru.otus.hw.repositories

import jakarta.persistence.EntityManager
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType
import org.springframework.stereotype.Repository
import ru.otus.hw.converters.BookConverter
import ru.otus.hw.models.Book

@Repository
open class JpaBookRepository(
    private val em: EntityManager,
    private val bookConverter: BookConverter
) : BookRepository {
    override fun findById(id: Long): Book? {
        val book = em.find(Book::class.java, id)

        bookConverter.toDto(book)

        return book
    }

    override fun findAll(): List<Book> {
        val entityGraph = em.getEntityGraph("authors-entity-graph")
        val books = em.createQuery("select b from Book b", Book::class.java)
            .setHint(EntityGraphType.FETCH.key, entityGraph)
            .resultList

        bookConverter.toDtoList(books)

        return books
    }

    override fun save(book: Book): Book =
        if (book.id == 0L) {
            em.persist(book)
            book
        } else {
            em.merge(book)
        }

    override fun deleteById(id: Long) {
        val book = em.find(Book::class.java, id)
        em.remove(book)
    }
}
