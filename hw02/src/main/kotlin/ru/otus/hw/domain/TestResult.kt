package ru.otus.hw.domain

class TestResult(
    val student: Student
) {
    var answeredQuestions: List<Question> = emptyList()
        private set
    var rightAnswersCount: Int = 0
        private set

    fun applyAnswer(question: Question, isRightAnswer: Boolean) {
        answeredQuestions += question
        if (isRightAnswer) rightAnswersCount++
    }
}
