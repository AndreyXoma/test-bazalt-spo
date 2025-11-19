package org.example.testbazaltspo.controller;

import org.example.testbazaltspo.exception.NotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public String handleException(NotFoundException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "file-not-found";
    }

    @ExceptionHandler(Exception.class)
    public String handleAll(Exception exception, Model model) {
        model.addAttribute("message", "Unexpected error: " + exception.getMessage());
        return "file-not-found";
    }
}
