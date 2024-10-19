package com.contact.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(OutOfMemoryError.class)
    public String handleOutOfMemoryError(Model model) {
        // Optionally, log the error or alert your team
        System.err.println("Out of memory error occurred");

        // Return a user-friendly message or redirect to an error page
        return "redirect:/error-page";  // or refresh the page
    }
}
