package com.techbank.account.cmd.api.controllers;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techbank.account.cmd.api.commands.WithdrawFundsCommand;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;

@RestController
@RequestMapping(path="/api/v1/withdrawFunds")
public class WithdrawFundsController {
	@Autowired
	private CommandDispatcher commandDispatcher;
	
	private final Logger logger=Logger.getLogger(WithdrawFundsController.class.getName());
	
	@PutMapping(path="/{id}")
	public ResponseEntity<BaseResponse> withdrawFunds(@PathVariable(value="id")String id,@RequestBody WithdrawFundsCommand command){

		try {
			command.setId(id);
			commandDispatcher.send(command);
			return new ResponseEntity<>(new BaseResponse("WithdrawFund request completed Successfully"), HttpStatus.OK);
		}catch(IllegalStateException e) {
			logger.log(Level.WARNING, MessageFormat.format("Client made a bad Request- {0}.", e.toString()));
			return new ResponseEntity<>(new BaseResponse(e.toString()),HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			var safeErrMsg=MessageFormat.format("Error while processing request to Withdraw Funds in Account for id - {0}", id);
			logger.log(Level.SEVERE,safeErrMsg,e);
			return new ResponseEntity<>(new BaseResponse(safeErrMsg),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}
}
