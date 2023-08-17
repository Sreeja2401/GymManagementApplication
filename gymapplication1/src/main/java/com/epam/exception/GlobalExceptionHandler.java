package com.epam.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest w) {
		List<String> errors = new ArrayList<>();
		e.getAllErrors().forEach(e1 -> errors.add(e1.getDefaultMessage()));

		return new ExceptionResponse(new Date().toString(), errors.toString(), HttpStatus.BAD_REQUEST.toString(),
				w.getDescription(false));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ExceptionResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
			WebRequest w) {

		return new ExceptionResponse(new Date().toString(), e.getMessage(), HttpStatus.BAD_REQUEST.toString(),
				w.getDescription(false));
	}

	@ExceptionHandler(GymException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public ExceptionResponse handleAssociateException(GymException e, WebRequest w) {

		return new ExceptionResponse(new Date().toString(), e.getMessage(), HttpStatus.NOT_FOUND.toString(),
				w.getDescription(false));
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public ExceptionResponse handleRuntimeException(RuntimeException e, WebRequest w) {

		return new ExceptionResponse(new Date().toString(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),
				w.getDescription(false));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ExceptionResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e, WebRequest w) {

		return new ExceptionResponse(new Date().toString(), e.getMessage(), HttpStatus.BAD_REQUEST.toString(),
				w.getDescription(false));
	}

}
