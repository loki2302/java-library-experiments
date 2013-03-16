package com.loki2302.expectations;

import static org.junit.Assert.assertEquals;

import com.loki2302.DOMLiteralExpression;
import com.loki2302.DOMLiteralType;


public class HasDOMLiteralTypeExpectation implements DOMLiteralExpressionExpectation {
	private final DOMLiteralType literalType;
	
	public HasDOMLiteralTypeExpectation(DOMLiteralType literalType) {
		this.literalType = literalType;
	}

	@Override
	public void check(DOMLiteralExpression domLiteralExpression) {
		assertEquals(literalType, domLiteralExpression.getLiteralType());			
	}		
}