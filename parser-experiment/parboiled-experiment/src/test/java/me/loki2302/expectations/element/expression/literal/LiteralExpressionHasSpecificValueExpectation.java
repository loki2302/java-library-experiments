package me.loki2302.expectations.element.expression.literal;

import static org.junit.Assert.assertEquals;
import me.loki2302.dom.DOMLiteralExpression;

public class LiteralExpressionHasSpecificValueExpectation implements LiteralExpressionExpectation {
	private final String stringValue;
	
	public LiteralExpressionHasSpecificValueExpectation(String stringValue) {
		this.stringValue = stringValue;
	}

	@Override
	public void check(DOMLiteralExpression domLiteralExpression) {
		assertEquals(stringValue, domLiteralExpression.getStringValue());			
	}		
}