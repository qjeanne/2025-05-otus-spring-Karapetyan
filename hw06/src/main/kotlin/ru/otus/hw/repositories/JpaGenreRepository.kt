package ru.otus.hw.repositories

import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import ru.otus.hw.models.Genre

@Repository
open class JpaGenreRepository(
    private val em: EntityManager
) : GenreRepository {
    override fun findAll(): List<Genre> =
        em.createQuery("select g from Genre g", Genre::class.java).resultList

    override fun findAllByIds(ids: Set<Long>): List<Genre> =
        em.createQuery("select g from Genre g where g.id in :ids", Genre::class.java)
            .setParameter("ids", ids)
            .resultList
}
