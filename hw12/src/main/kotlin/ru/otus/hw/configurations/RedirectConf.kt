package ru.otus.hw.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import java.net.URI


@Configuration
open class RedirectConf : WebFluxConfigurer {
    @Bean
    open fun apiRouter() = router {
        GET("/") {
            ServerResponse
                .temporaryRedirect(URI.create("/books"))
                .build()
        }

        GET("/books") {
            ServerResponse.ok()
                .contentType(MediaType.TEXT_HTML)
                .bodyValue(ClassPathResource("static/books.html"))
        }
    }
}
