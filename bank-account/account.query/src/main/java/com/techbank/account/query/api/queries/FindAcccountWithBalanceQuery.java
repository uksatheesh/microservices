package com.techbank.account.query.api.queries;

import com.techbank.account.query.api.dto.EqualityType;
import com.techbank.cqrs.core.queries.BaseQuery;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAcccountWithBalanceQuery extends BaseQuery {

	private EqualityType equalityType;
	private double balance;
	
}
