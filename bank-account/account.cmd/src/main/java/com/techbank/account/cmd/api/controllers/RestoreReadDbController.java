package com.techbank.account.cmd.api.controllers;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techbank.account.cmd.api.commands.DepositFundsCommand;
import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.cmd.api.commands.RestoreDbCommand;
import com.techbank.account.cmd.api.dto.OpenAccountResponse;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;

@RestController
@RequestMapping(path="/api/v1/restoreReadDb")
public class RestoreReadDbController {

	private final Logger logger=Logger.getLogger(RestoreReadDbController.class.getName());
	
	@Autowired
	private CommandDispatcher commandDispatcher;
	
	@PostMapping
	public ResponseEntity<BaseResponse> restoreReadDb(){
		try {
			commandDispatcher.send(new RestoreDbCommand());
			return new ResponseEntity<>(new BaseResponse("Restore read Db request completed Successfully"), HttpStatus.CREATED);
		}catch(IllegalStateException e) {
			logger.log(Level.WARNING, MessageFormat.format("Client made a bad Request- {0}.", e.toString()));
			return new ResponseEntity<>(new BaseResponse(e.toString()),HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			var safeErrMsg="Error while processing request to restore read Db";
			logger.log(Level.SEVERE,safeErrMsg,e);
			return new ResponseEntity<>(new BaseResponse(safeErrMsg),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}
	
	
}
