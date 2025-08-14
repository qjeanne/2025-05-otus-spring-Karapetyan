package ru.otus.hw.service

interface LocalizedIOService : IOService, LocalizedMessagesService {
    fun printLineLocalized(code: String)
    fun printFormattedLineLocalized(code: String, vararg args: Any)
    fun readStringWithPromptLocalized(promptCode: String): String
    fun readIntForRangeLocalized(min: Int, max: Int, errorMessageCode: String): Int
    fun readIntForRangeWithPromptLocalized(min: Int, max: Int, promptCode: String, errorMessageCode: String): Int
}
