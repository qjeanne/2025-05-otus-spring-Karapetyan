package ru.otus.hw.domain

data class Student(
    val firstName: String,
    val lastName: String
) {
    fun getFullName() = "$firstName $lastName"
}
