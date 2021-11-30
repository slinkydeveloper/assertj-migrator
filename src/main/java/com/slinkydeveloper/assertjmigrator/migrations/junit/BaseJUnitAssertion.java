package com.slinkydeveloper.assertjmigrator.migrations.junit;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.migrations.MigrationMatchingExpressionPredicate;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;
import com.slinkydeveloper.assertjmigrator.nodes.Predicates;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static com.slinkydeveloper.assertjmigrator.nodes.Predicates.*;

/**
 * This abstract class contains the logic for handling most of JUnit 4/5 assertions, including the message.
 */
public abstract class BaseJUnitAssertion implements MigrationMatchingExpressionPredicate<MethodCallExpr> {

    protected abstract String assertionName();

    protected abstract int assertionArity();

    protected abstract void fillBuilder(AssertJBuilder builder, MethodCallExpr expr);

    protected Predicate<Expression> additionalPredicate(int offset) {
        return v -> true;
    }

    @Override
    public Class<MethodCallExpr> matchedNode() {
        return MethodCallExpr.class;
    }

    @Override
    public final Predicate<Expression> predicate() {
        return and(
                methodNameIs(assertionName()),
                or(
                        methodArgsAre(assertionArity()),
                        and(
                                methodArgsAre(assertionArity() + 1),
                                or(
                                        methodArgMatches(0, Predicates::isString),
                                        methodArgMatches(assertionArity(), Predicates::isString)
                                )
                        )
                ),
                matchWithoutMessage()
        );
    }

    @Override
    public final void migrate(MethodCallExpr methodCallExpr) {
        int messageArgumentIndex = messageIndex(methodCallExpr);
        if (messageArgumentIndex == -1) {
            AssertJBuilder builder = AssertJBuilder.create();
            fillBuilder(builder, methodCallExpr);
            methodCallExpr.replace(builder.build());
            return;
        }

        // We need to extract the message and modify the arguments
        Expression message = methodCallExpr.getArgument(messageArgumentIndex);
        methodCallExpr.getArguments().remove(messageArgumentIndex);
        AssertJBuilder builder = AssertJBuilder.create()
                .as(message);
        fillBuilder(builder, methodCallExpr);
        methodCallExpr.replace(builder.build());
    }

    @Override
    public List<String> requiredImports() {
        return Collections.singletonList("org.assertj.core.api.Assertions.assertThat");
    }

    @Override
    public String toString() {
        return "JUnit 4/5 " + assertionName();
    }

    private int messageIndex(MethodCallExpr methodCallExpr) {
        if (methodArgsAre(assertionArity() + 1).test(methodCallExpr)) {
            if (Predicates.isJUnit4Assertion(methodCallExpr)) {
                return 0;
            }
            if (Predicates.isJUnit5Assertion(methodCallExpr)) {
                return assertionArity();
            }
        }
        return -1;
    }

    private Predicate<Expression> matchWithoutMessage() {
        return expression -> {
            MethodCallExpr methodCallExpr = expression.asMethodCallExpr();
            int messageArgumentIndex = messageIndex(methodCallExpr);

            if (messageArgumentIndex == -1) {
                return additionalPredicate(0).test(methodCallExpr);
            }
            int offset = messageArgumentIndex == 0 ? 1 : 0;
            return additionalPredicate(offset).test(methodCallExpr);
        };
    }
}
