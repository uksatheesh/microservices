package com.techbank.account.cmd.api.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.cqrs.core.handlers.EventSourcinghandler;

@Service
public class AccountCommandHandler implements CommandHandler{
	
	@Autowired
	EventSourcinghandler<AccountAggregate> eventSourcingHandler;

	@Override
	public void handle(OpenAccountCommand command) {
		AccountAggregate objAccAggregate=new AccountAggregate(command);
		eventSourcingHandler.save(objAccAggregate);
	}

	@Override
	public void handle(DepositFundsCommand command) {
		AccountAggregate objAccAggregate= eventSourcingHandler.getById(command.getId());
		objAccAggregate.depositFunds(command.getAmount());
		eventSourcingHandler.save(objAccAggregate);
	}

	@Override
	public void handle(WithdrawFundsCommand command) {
		AccountAggregate objAccAggregate= eventSourcingHandler.getById(command.getId());
		if(command.getAmount()>=objAccAggregate.getBalance()) {
			throw new IllegalStateException("Insufficient Funds to Withdraw");
		}
		objAccAggregate.withdrawFunds(command.getAmount());
		eventSourcingHandler.save(objAccAggregate);
	}

	@Override
	public void handle(CloseAccountCommand command) {
		AccountAggregate objAccAggregate= eventSourcingHandler.getById(command.getId());
		objAccAggregate.closeAccount();
		eventSourcingHandler.save(objAccAggregate);
	}

	@Override
	public void handle(RestoreDbCommand command) {
		eventSourcingHandler.republishEvents();
	}

}
