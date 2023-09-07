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

import com.techbank.account.cmd.api.commands.DepositFundsCommand;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;

@RestController
@RequestMapping(path="/api/v1/depositFunds")
public class DepositFundsController {
	@Autowired
	private CommandDispatcher commandDispatcher;
	
	private Logger logger=Logger.getLogger(DepositFundsController.class.getName());
	
	@PutMapping(path="/{id}")
	public ResponseEntity<BaseResponse> depositFund(@PathVariable(value="id")String id,@RequestBody DepositFundsCommand command){

		try {
			command.setId(id);
			commandDispatcher.send(command);
			return new ResponseEntity<>(new BaseResponse("DepositFund request completed Successfully"), HttpStatus.OK);
		}catch(IllegalStateException e) {
			logger.log(Level.WARNING, MessageFormat.format("Client made a bad Request- {0}.", e.toString()));
			return new ResponseEntity<>(new BaseResponse(e.toString()),HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			var safeErrMsg=MessageFormat.format("Error while processing request to Deposit Funds in Account for id - {0}", id);
			logger.log(Level.SEVERE,safeErrMsg,e);
			return new ResponseEntity<>(new BaseResponse(safeErrMsg),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}
}
