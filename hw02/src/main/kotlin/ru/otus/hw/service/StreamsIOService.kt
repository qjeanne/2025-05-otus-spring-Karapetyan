package ru.otus.hw.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.InputStream
import java.io.PrintStream
import java.util.*

@Service
class StreamsIOService(
    @Value("#{T(System).out}")
    private val printStream: PrintStream,

    @Value("#{T(System).in}")
    private val inputStream: InputStream
) : IOService {

    companion object {
        const val MAX_ATTEMPTS = 10
    }

    private val scanner = Scanner(inputStream)

    override fun printLine(s: String) {
        printStream.println(s)
    }

    override fun printFormattedLine(s: String, vararg args: Any) {
        printStream.printf("$s%n", *args)
    }

    override fun readString(): String = scanner.nextLine()

    override fun readStringWithPrompt(prompt: String): String {
        printLine(prompt)
        return scanner.nextLine()
    }

    override fun readIntForRange(min: Int, max: Int, errorMessage: String): Int {
        for (i in 0..MAX_ATTEMPTS) {
            try {
                val stringValue = scanner.nextLine()
                val intValue = stringValue.toInt()
                require(intValue in min..max)
                return intValue
            } catch (e: IllegalArgumentException) {
                printLine(errorMessage)
            }
        }
        throw IllegalArgumentException("Error during reading int value")
    }

    override fun readIntForRangeWithPrompt(min: Int, max: Int, prompt: String, errorMessage: String): Int {
        printLine(prompt)
        return readIntForRange(min, max, errorMessage)
    }
}
