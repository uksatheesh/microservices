package com.techbank.account.cmd.infrastructure;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.account.cmd.domain.EventStoreRepository;
import com.techbank.cqrs.core.events.BaseEvent;
import com.techbank.cqrs.core.events.EventModel;
import com.techbank.cqrs.core.exceptions.AggregateException;
import com.techbank.cqrs.core.exceptions.ConcurrencyException;
import com.techbank.cqrs.core.infrastructure.EventStore;
import com.techbank.cqrs.core.producers.EventProducer;

@Service
public class AccountEventStore implements EventStore{
	
	@Autowired
	private EventProducer eventProducer; 
	
	@Autowired
	private EventStoreRepository objEventStoreRepository; 

	@Override
	public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {

		List<EventModel> eventStream= objEventStoreRepository.findByAggregateIdentifier(aggregateId);
		if(expectedVersion!=-1 && eventStream.get(eventStream.size()-1).getVersion()!=expectedVersion) {
			throw new ConcurrencyException();
		}
		
		int version=expectedVersion;
		for (BaseEvent event : events) {
			version++;
			event.setVersion(version);
			EventModel eventModel=EventModel.builder().
					timeStamp(new Date())
					.aggregateIdentifier(aggregateId)
					.aggregateType(AccountAggregate.class.getTypeName())
					.version(version)
					.eventType(event.getClass().getTypeName())
					.eventData(event)
					.build();
			
			EventModel persistedEvent=objEventStoreRepository.save(eventModel);
			if(!persistedEvent.getId().isEmpty()) {
				eventProducer.produce(event.getClass().getSimpleName(), event);
			}
			
		}
		
	}

	@Override
	public List<BaseEvent> getEvents(String aggregateId) {
		
		List<EventModel> eventStream=objEventStoreRepository.findByAggregateIdentifier(aggregateId);
		if(eventStream==null || eventStream.isEmpty()) {
			throw new AggregateException("Incorrect aggregateId is provided");
		}	
		
		return eventStream.stream().map(x-> x.getEventData()).collect(Collectors.toList());
	}

	@Override
	public List<String> getAggregateIds() {
		var eventStream=objEventStoreRepository.findAll();
		if(eventStream==null || eventStream.isEmpty()) {
			throw new IllegalStateException("Could not retrieve event stream from event store!");
		}
		return eventStream.stream().map(EventModel::getAggregateIdentifier).distinct().collect(Collectors.toList());
	}

}
