package com.epam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class ExceptionResponse {
	private String timeStamp;
	private String error;
	private String status;
	private String path;

}
