package com.techbank.account.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.techbank.account.query.api.queries.FindAcccountWithBalanceQuery;
import com.techbank.account.query.api.queries.FindAccountByHolderQuery;
import com.techbank.account.query.api.queries.FindAccountByIdQuery;
import com.techbank.account.query.api.queries.FindAllAccountsQuery;
import com.techbank.account.query.api.queries.QueryHandler;
import com.techbank.cqrs.core.infrastructure.QueryDispatcher;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class QueryApplication {

	@Autowired
	private QueryDispatcher queryDispatcher;
	
	@Autowired
	private QueryHandler queryHandler;
	
	public static void main(String[] args) {
		SpringApplication.run(QueryApplication.class, args);
	}

	@PostConstruct
	public void registerHandlers() {
		queryDispatcher.registerHandler(FindAllAccountsQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountByHolderQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountByIdQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindAcccountWithBalanceQuery.class, queryHandler::handle);
		
	}
	
	
	
		
		
}
