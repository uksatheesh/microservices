package com.techbank.cqrs.core.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AggregateException extends RuntimeException {

	public AggregateException(String msg) {
		super(msg);
	}
	
}
