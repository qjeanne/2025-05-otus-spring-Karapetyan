package ru.otus.hw.repositories

import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import ru.otus.hw.models.Comment

@Repository
open class JpaCommentRepository(
    private val em: EntityManager
): CommentRepository {
    override fun findById(id: Long): Comment? = em.find(Comment::class.java, id)

    override fun findByBookId(id: Long): List<Comment> =
        em.createQuery("select c from Comment c where c.bookId = :id", Comment::class.java)
            .setParameter("id", id)
            .resultList

    override fun save(comment: Comment): Comment =
        if (comment.id == 0L) {
            em.persist(comment)
            comment
        } else {
            em.merge(comment)
        }

    override fun deleteById(id: Long) {
        val comment = em.find(Comment::class.java, id)
        em.remove(comment)
    }
}
