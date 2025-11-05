package ru.otus.hw.controllers

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.ModelAndView

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun handle(e: Exception): ModelAndView {
        val mv = ModelAndView("error")
        mv.addObject("message", e.message)
        return mv
    }
}
