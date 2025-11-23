package ru.otus.hw.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.HiddenHttpMethodFilter

@Configuration
open class HiddenMethodFilterConf {
    @Bean
    open fun hiddenHttpMethodFilter() = HiddenHttpMethodFilter()
}
