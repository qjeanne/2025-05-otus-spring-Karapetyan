package ru.otus.hw.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.Locale

@ConfigurationProperties(prefix = "test")
data class AppProperties(
    override val rightAnswersCountToPass: Int,
    override var locale: Locale,
    private val fileNameByLocaleTag: Map<String, String>
) : TestConfig, TestFileNameProvider, LocaleConfig {
    init {
        locale = Locale.forLanguageTag(locale.language)
    }

    override val testFileName: String =
        fileNameByLocaleTag[locale.toLanguageTag()] ?: error("No question file for locale ${locale.toLanguageTag()}")
}
