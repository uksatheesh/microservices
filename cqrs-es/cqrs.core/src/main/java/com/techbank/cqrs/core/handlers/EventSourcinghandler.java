package com.techbank.cqrs.core.handlers;

import com.techbank.cqrs.core.domain.AggregateRoot;

public interface EventSourcinghandler<T> {
	
	void save(AggregateRoot aggregate);
	T getById(String id);//aggregateId - latest state of aggregrate
	
	void republishEvents();

}
