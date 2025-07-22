package ru.otus.hw

import io.kotest.assertions.withClue
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeEmpty
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import ru.otus.hw.config.AppProperties

class CommonHwTest {

    private val CONFIGURATION_ANNOTATION_NAME = "org.springframework.context.annotation.Configuration"

    @Test
    fun shouldNotContainConfigurationAnnotationAboveItSelf() {
        withClue(
            "Класс свойств не является конфигурацией, т.к. конфигурация для создания бинов, а тут просто компонент группирующий свойства приложения"
        ) {
            AppProperties::class.java.isAnnotationPresent(Configuration::class.java).shouldBeFalse()
        }
    }

    @Test
    fun shouldNotContainPropertySourceAnnotationAboveItSelf() {
        withClue(
            "Аннотацию @PropertySource лучше вешать над конфигурацией, а класс свойств ей не является"
        ) {
            AppProperties::class.java.isAnnotationPresent(PropertySource::class.java).shouldBeFalse()
        }
    }

    @Test
    fun shouldNotContainFieldInjectedDependenciesOrProperties() {
        val provider = ClassPathScanningCandidateComponentProvider(false)
        provider.addIncludeFilter { mr, _ ->
            val metaData = mr.classMetadata
            val annotationMetaData = mr.annotationMetadata
            val isTest = metaData.className.endsWith("Test")
            val isInterface = metaData.isInterface
            val isConfiguration = annotationMetaData.hasAnnotation(CONFIGURATION_ANNOTATION_NAME)
            val clazz = getBeanClassByName(metaData.className)
            val hasFieldInjection = clazz.declaredFields.any {
                it.isAnnotationPresent(Autowired::class.java) || it.isAnnotationPresent(Value::class.java)
            }
            !isTest && !isInterface && !isConfiguration && hasFieldInjection
        }

        val violatingClasses = provider.findCandidateComponents(Application::class.java.`package`.name)

        withClue(
            "На курсе все внедрение рекомендовано осуществлять через конструктор (в т.ч. @Value). Следующие классы нарушают это правило:\n" +
                    violatingClasses.joinToString("\n") { it.beanClassName ?: "" }
        ) {
            violatingClasses.shouldBeEmpty()
        }
    }

    private fun getBeanClassByName(beanClassName: String): Class<*> =
        try {
            Class.forName(beanClassName)
        } catch (e: ClassNotFoundException) {
            throw RuntimeException(e)
        }
}
