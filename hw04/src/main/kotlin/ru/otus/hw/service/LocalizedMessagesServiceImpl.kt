package ru.otus.hw.service

import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import ru.otus.hw.config.LocaleConfig

@Service
class LocalizedMessagesServiceImpl(
    private val localeConfig: LocaleConfig,
    private val messageSource: MessageSource
) : LocalizedMessagesService {
    override fun getMessage(code: String, vararg args: Any): String =
        messageSource.getMessage(code, args, localeConfig.locale)
}
