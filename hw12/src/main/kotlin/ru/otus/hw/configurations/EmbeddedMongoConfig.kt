package ru.otus.hw.configurations

import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion
import de.flapdoodle.embed.mongo.distribution.Version
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class EmbeddedMongoConfig {
    @Bean
    open fun embeddedMongoVersion(): IFeatureAwareVersion = Version.Main.V7_0
}
