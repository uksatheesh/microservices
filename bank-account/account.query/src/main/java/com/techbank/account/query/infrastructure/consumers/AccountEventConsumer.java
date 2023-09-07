package com.techbank.account.query.infrastructure.consumers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import com.techbank.account.query.infrastructure.handlers.EventHandler;

@Service
public class AccountEventConsumer implements EventConsumer{
	
	@Autowired
	EventHandler eventHandler;

	//get groupId from application.yml file. if other consumer which consume the same topic, then 
	//kafka wont move offset in the group here.Hence, group id to be pulled from application yml.
	@KafkaListener(topics="AccountOpenedEvent",groupId = "${spring.kafka.consumer.group-id}")
	@Override
	public void consume(AccountOpenedEvent event, Acknowledgment ack) {
		this.eventHandler.on(event);
		ack.acknowledge();// commit the offset to kafka and ensure we always consume the latest event that are produced to kafka.
	}

	@KafkaListener(topics="FundsDepositedEvent",groupId = "${spring.kafka.consumer.group-id}")
	@Override
	public void consume(FundsDepositedEvent event, Acknowledgment ack) {
		this.eventHandler.on(event);
		ack.acknowledge();
	}

	@KafkaListener(topics="FundsWithdrawnEvent",groupId = "${spring.kafka.consumer.group-id}")
	@Override
	public void consume(FundsWithdrawnEvent event, Acknowledgment ack) {
		this.eventHandler.on(event);
		ack.acknowledge();	
	}

	@KafkaListener(topics="AccountClosedEvent",groupId = "${spring.kafka.consumer.group-id}")
	@Override
	public void consume(AccountClosedEvent event, Acknowledgment ack) {
		this.eventHandler.on(event);
		ack.acknowledge();
	}

	
}
