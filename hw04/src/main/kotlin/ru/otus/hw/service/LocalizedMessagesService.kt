package ru.otus.hw.service

fun interface LocalizedMessagesService {
    fun getMessage(code: String, vararg args: Any): String
}
