package ru.otus.hw.service


interface IOService {
    fun printLine(s: String)
    fun printFormattedLine(s: String, vararg args: Any)
    fun readString(): String
    fun readStringWithPrompt(prompt: String): String
    fun readIntForRange(min: Int, max: Int, errorMessage: String): Int
    fun readIntForRangeWithPrompt(min: Int, max: Int, prompt: String, errorMessage: String): Int
}
