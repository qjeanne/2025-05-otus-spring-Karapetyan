package ru.otus.hw.repositories

import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import ru.otus.hw.models.Author

@Repository
open class JpaAuthorRepository(
    private val em: EntityManager
) : AuthorRepository {
    override fun findAll(): List<Author> =
        em.createQuery("select a from Author a", Author::class.java).resultList

    override fun findById(id: Long): Author? = em.find(Author::class.java, id)
}
