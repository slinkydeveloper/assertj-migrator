package com.slinkydeveloper.assertjmigrator.migrations.hamcrest;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.migrations.MigrationMatchingExpressionPredicate;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;
import com.slinkydeveloper.assertjmigrator.nodes.Predicates;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static com.slinkydeveloper.assertjmigrator.nodes.Predicates.*;

public abstract class BaseHamcrestAssertThat implements MigrationMatchingExpressionPredicate<MethodCallExpr> {

    abstract String matcherName();

    boolean matcherVariadic() {
        return false;
    }

    abstract int matcherArity();

    abstract void fillBuilder(AssertJBuilder builder, Expression actual, MethodCallExpr matcher);

    Predicate<Expression> matcherPredicate() {
        return v -> true;
    }

    protected Predicate<Expression> resolveMatcherPredicate() {
        if (matcherVariadic()) {
            return and(
                    methodNameIs(matcherName()),
                    expr -> expr.asMethodCallExpr().getArguments().size() >= matcherArity(),
                    matcherPredicate()
            );
        }
        return and(
                methodNameIs(matcherName()),
                methodArgsAre(matcherArity()),
                matcherPredicate()
        );
    }

    @Override
    public Class<MethodCallExpr> matchedNode() {
        return MethodCallExpr.class;
    }

    @Override
    public Predicate<Expression> predicate() {
        Predicate<Expression> matcherPredicate = resolveMatcherPredicate();
        return and(
                methodNameIs("assertThat"),
                or(
                        and(
                                methodArgsAre(2),
                                methodArgMatches(1, matcherPredicate)
                        ),
                        and(
                                methodArgsAre(3),
                                methodArgMatches(0, Predicates::isString),
                                methodArgMatches(2, matcherPredicate)
                        )
                )
        );
    }

    @Override
    public void migrate(MethodCallExpr node) {
        AssertJBuilder builder = AssertJBuilder.create();
        if (node.getArguments().size() == 3) {
            builder.as(node.getArgument(0));
            fillBuilder(builder, node.getArgument(1), node.getArgument(2).asMethodCallExpr());
        } else {
            fillBuilder(builder, node.getArgument(0), node.getArgument(1).asMethodCallExpr());
        }

        node.replace(builder.build());
    }

    @Override
    public List<String> requiredImports() {
        return Collections.singletonList("org.assertj.core.api.Assertions.assertThat");
    }

    @Override
    public String toString() {
        return "Hamcrest assertThat " + matcherName();
    }
}
