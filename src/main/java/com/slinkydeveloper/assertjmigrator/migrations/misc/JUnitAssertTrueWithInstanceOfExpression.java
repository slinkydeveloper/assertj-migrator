package com.slinkydeveloper.assertjmigrator.migrations.misc;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.migrations.junit.BaseJUnitAssertion;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;

import java.util.function.Predicate;

import static com.slinkydeveloper.assertjmigrator.nodes.Predicates.methodArgMatches;

public class JUnitAssertTrueWithInstanceOfExpression extends BaseJUnitAssertion {

    @Override
    protected String assertionName() {
        return "assertTrue";
    }

    @Override
    protected int assertionArity() {
        return 1;
    }

    @Override
    protected Predicate<Expression> additionalPredicate(int offset) {
        return methodArgMatches(offset, Expression::isInstanceOfExpr);
    }

    @Override
    protected void fillBuilder(AssertJBuilder builder, MethodCallExpr expr) {
        InstanceOfExpr instanceOfExpr = expr.getArguments().get(0).asInstanceOfExpr();
        builder.assertThat(instanceOfExpr.getExpression())
                .isInstanceOf(instanceOfExpr.getType());
    }

    @Override
    public String toString() {
        return "JUnit 4/5 assertTrue with instanceof";
    }

}
