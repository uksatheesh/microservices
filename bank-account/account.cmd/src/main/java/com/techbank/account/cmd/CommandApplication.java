package com.techbank.account.cmd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.techbank.account.cmd.api.commands.CloseAccountCommand;
import com.techbank.account.cmd.api.commands.CommandHandler;
import com.techbank.account.cmd.api.commands.DepositFundsCommand;
import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.cmd.api.commands.RestoreDbCommand;
import com.techbank.account.cmd.api.commands.WithdrawFundsCommand;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class CommandApplication {

	@Autowired
	private CommandHandler objCommandHandler;
	
	@Autowired
	private CommandDispatcher objCommandDispatcher;
	
	
	public static void main(String[] args) {
		SpringApplication.run(CommandApplication.class, args);
	}
	

	@PostConstruct
	public void registerHandler() {
		objCommandDispatcher.registerHandler(OpenAccountCommand.class, objCommandHandler::handle);
		objCommandDispatcher.registerHandler(DepositFundsCommand.class, objCommandHandler::handle);
		objCommandDispatcher.registerHandler(WithdrawFundsCommand.class, objCommandHandler::handle);
		objCommandDispatcher.registerHandler(CloseAccountCommand.class, objCommandHandler::handle);
		objCommandDispatcher.registerHandler(RestoreDbCommand.class, objCommandHandler::handle);
	}
}
