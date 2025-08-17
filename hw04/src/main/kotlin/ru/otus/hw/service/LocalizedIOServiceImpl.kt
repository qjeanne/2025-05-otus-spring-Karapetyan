package ru.otus.hw.service

import org.springframework.stereotype.Service

@Service
class LocalizedIOServiceImpl(
    private val localizedMessagesService: LocalizedMessagesService,
    private val ioService: IOService
) : LocalizedIOService {
    override fun printLineLocalized(code: String) {
        ioService.printLine(localizedMessagesService.getMessage(code))
    }

    override fun printFormattedLineLocalized(code: String, vararg args: Any) {
        ioService.printLine(localizedMessagesService.getMessage(code, *args))
    }

    override fun readStringWithPromptLocalized(promptCode: String): String =
        ioService.readStringWithPrompt(localizedMessagesService.getMessage(promptCode))

    override fun readIntForRangeLocalized(min: Int, max: Int, errorMessageCode: String): Int =
        ioService.readIntForRange(min, max, localizedMessagesService.getMessage(errorMessageCode))

    override fun readIntForRangeWithPromptLocalized(
        min: Int,
        max: Int,
        promptCode: String,
        errorMessageCode: String
    ): Int = ioService.readIntForRangeWithPrompt(
        min,
        max,
        localizedMessagesService.getMessage(promptCode),
        localizedMessagesService.getMessage(errorMessageCode)
    )

    override fun printLine(s: String) {
        ioService.printLine(s)
    }

    override fun printFormattedLine(s: String, vararg args: Any) {
        ioService.printFormattedLine(s, *args)
    }

    override fun readString(): String = ioService.readString()

    override fun readStringWithPrompt(prompt: String): String = ioService.readStringWithPrompt(prompt)

    override fun readIntForRange(min: Int, max: Int, errorMessage: String): Int =
        ioService.readIntForRange(min, max, errorMessage)

    override fun readIntForRangeWithPrompt(min: Int, max: Int, prompt: String, errorMessage: String): Int =
        ioService.readIntForRangeWithPrompt(min, max, prompt, errorMessage)

    override fun getMessage(code: String, vararg args: Any): String = localizedMessagesService.getMessage(code, *args)
}
