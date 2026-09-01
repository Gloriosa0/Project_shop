package com.academy.shop.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("error", "Некорректный запрос");
        model.addAttribute("message", e.getMessage());

        return "error";
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException e, Model model) {
        model.addAttribute("status", HttpStatus.CONFLICT.value());
        model.addAttribute("error", "Операция невозможна");
        model.addAttribute("message", e.getMessage());

        return "error";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException e, Model model) {
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", "Объект не найден");
        model.addAttribute("message", "Запрашиваемый объект не существует");

        return "error";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException e, Model model) {
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", "Пользователь не найден");
        model.addAttribute("message", e.getMessage());

        return "error";
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException e, Model model) {
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("error", "Некорректный параметр");
        model.addAttribute("message", "Параметр '"
                + e.getName() + "' имеет неверный формат");

        return "error";
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParameter(MissingServletRequestParameterException e, Model model) {
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("error", "Отсутствует параметр");
        model.addAttribute("message", "Не указан обязательный параметр: "
                + e.getParameterName());

        return "error";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolation(DataIntegrityViolationException e, Model model) {
        model.addAttribute("status", HttpStatus.CONFLICT.value());
        model.addAttribute("error", "Ошибка базы данных");
        model.addAttribute("message",
                "Операцию невозможно выполнить из-за ограничений базы данных");

        return "error";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(AccessDeniedException e, Model model) {
        model.addAttribute("status", HttpStatus.FORBIDDEN.value());
        model.addAttribute("error", "Доступ запрещён");
        model.addAttribute("message",
                "У вас недостаточно прав для выполнения этой операции");

        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("error", "Внутренняя ошибка");
        model.addAttribute("message",
                "Произошла непредвиденная ошибка. Попробуйте повторить операцию позже.");

        return "error";
    }
}