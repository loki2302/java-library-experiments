package me.loki2302.expectations.element.statement.dowhile;

import static org.junit.Assert.assertNotNull;
import me.loki2302.dom.DOMDoWhileStatement;
import me.loki2302.dom.DOMExpression;
import me.loki2302.expectations.element.expression.ExpressionExpectation;

public class DoWhileStatementHasConditionExpressionExpectation implements DoWhileStatementExpectation {
    private final ExpressionExpectation[] expectations;
    
    public DoWhileStatementHasConditionExpressionExpectation(ExpressionExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMDoWhileStatement domDoWhileStatement) {
        DOMExpression conditionExpression = domDoWhileStatement.getConditionExpression();
        assertNotNull(conditionExpression);
        for(ExpressionExpectation expectation : expectations) {
            expectation.check(conditionExpression);
        }
    }        
}