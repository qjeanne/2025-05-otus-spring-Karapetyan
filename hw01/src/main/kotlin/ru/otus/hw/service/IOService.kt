package ru.otus.hw.service


interface IOService {
    fun printLine(s: String)
    fun printFormattedLine(s: String, vararg args: Any)
}
