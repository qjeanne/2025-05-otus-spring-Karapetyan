package ru.otus.hw.models

import io.kotest.matchers.shouldBe
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToOne
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.reflections.Reflections
import java.lang.reflect.Field

class ModelsCommonTest {

    companion object {
        private lateinit var entitiesClasses: Set<Class<*>>

        @JvmStatic
        @BeforeAll
        fun setUpAll() {
            entitiesClasses = Reflections("ru.otus.hw.models")
                .getTypesAnnotatedWith(Entity::class.java)
        }

        fun <T> getRelationAnnotationArgumentValue(
            field: Field,
            argumentName: String,
            returnType: Class<T>
        ): T? {
            return field.annotations
                .flatMap { a ->
                    a.annotationClass.java.declaredMethods.map { m -> m to a }
                }
                .firstOrNull { (method, _) -> method.name == argumentName }
                ?.let { (method, ann) -> method.invoke(ann) as T }
        }

        fun findAllRelationsEntry(
            entityClass: Class<*>,
            entitiesClasses: Set<Class<*>>
        ): Map<Class<*>, Field> {
            return entityClass.declaredFields
                .filter { !it.type.isPrimitive }
                .map { f -> fieldToClass(f) to f }
                .filter { entitiesClasses.contains(it.first) }
                .toMap()
        }

        private fun fieldToClass(field: Field): Class<*> {
            var className = field.type.name
            if (Collection::class.java.isAssignableFrom(field.type)) {
                className = field.genericType.typeName
                    .substringAfter("<")
                    .substringBefore(">")
            }
            return Class.forName(className)
        }
    }

    @Test
    fun `should be no OneToOne relationships`() {
        entitiesClasses.forEach { entityClass ->
            val oneToOneExists = entityClass.declaredFields.any {
                it.isAnnotationPresent(OneToOne::class.java)
            }
            oneToOneExists shouldBe false
        }
    }

    @Test
    fun `should be no EAGER links`() {
        entitiesClasses.forEach { entityClass ->
            val eagerExists = entityClass.declaredFields
                .mapNotNull { f ->
                    getRelationAnnotationArgumentValue(f, "fetch", FetchType::class.java)
                }
                .any { it == FetchType.EAGER }
            eagerExists shouldBe false
        }
    }

    @Test
    fun `bidirectional relationships should be mappedBy`() {
        entitiesClasses.forEach { entityClass ->
            val relationsEntries = findAllRelationsEntry(entityClass, entitiesClasses)
            val hasProblem = relationsEntries.entries.any { relationEntry ->
                val reverseRelations = findAllRelationsEntry(relationEntry.key, entitiesClasses)
                val reverseRelationField = reverseRelations[entityClass] ?: return@any false

                val relationFieldName = relationEntry.value.name
                val reverseFieldName = reverseRelationField.name

                val mappedBy = getRelationAnnotationArgumentValue(
                    relationEntry.value, "mappedBy", String::class.java
                )
                val reverseMappedBy = getRelationAnnotationArgumentValue(
                    reverseRelationField, "mappedBy", String::class.java
                )

                mappedBy != reverseFieldName && reverseMappedBy != relationFieldName
            }
            hasProblem shouldBe false
        }
    }
}
