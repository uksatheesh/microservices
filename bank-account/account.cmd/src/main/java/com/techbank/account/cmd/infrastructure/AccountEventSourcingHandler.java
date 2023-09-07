package com.techbank.account.cmd.infrastructure;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.cqrs.core.domain.AggregateRoot;
import com.techbank.cqrs.core.events.BaseEvent;
import com.techbank.cqrs.core.handlers.EventSourcinghandler;
import com.techbank.cqrs.core.infrastructure.EventStore;
import com.techbank.cqrs.core.producers.EventProducer;

@Service
public class AccountEventSourcingHandler implements EventSourcinghandler<AccountAggregate> {

	@Autowired
	private EventStore objEventStore;
	
	@Autowired
	private EventProducer eventProducer;
	
	@Override
	public void save(AggregateRoot aggregate) {
		objEventStore.saveEvents(aggregate.getId(), aggregate.getUncommittedChanges(), aggregate.getVersion());
		aggregate.getUncommittedChanges();
	}

	@Override
	public AccountAggregate getById(String id) {
		AccountAggregate objAccAggregate=new AccountAggregate();
		List<BaseEvent> events=objEventStore.getEvents(id);
		if(events!=null && !events.isEmpty()) {
			objAccAggregate.replayEvents(events);
		Optional<Integer> latestVersion=events.stream().map(x->x.getVersion()).max(Comparator.naturalOrder());
		objAccAggregate.setVersion(latestVersion.get());
		}
			
		return objAccAggregate;
	}

	@Override
	public void republishEvents() {
		var aggregateIds=objEventStore.getAggregateIds();
		for (var aggregateId : aggregateIds) {
			var aggregate=getById(aggregateId);
			if(aggregate==null || !aggregate.getActive()) {
				continue;
			}
			var events=objEventStore.getEvents(aggregateId);
			for (var event : events) {
				eventProducer.produce(event.getClass().getSimpleName(), event);
			}
			
		}
		
	}

}
