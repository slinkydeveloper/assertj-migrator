package com.slinkydeveloper.assertjmigrator.migrations.hamcrest;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;
import com.slinkydeveloper.assertjmigrator.nodes.Predicates;

import java.util.function.Predicate;

import static com.slinkydeveloper.assertjmigrator.nodes.Predicates.*;

public abstract class BaseHamcrestAssertThatNot extends BaseHamcrestAssertThat {

    abstract void fillBuilder(AssertJBuilder builder, Expression actual, MethodCallExpr matcher);

    Predicate<Expression> matcherPredicate() {
        return v -> true;
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
                                methodArgMatches(1, and(
                                        methodNameIs("not"),
                                        methodArgsAre(1),
                                        methodArgMatches(0, matcherPredicate)
                                ))
                        ),
                        and(
                                methodArgsAre(3),
                                methodArgMatches(0, Predicates::isString),
                                methodArgMatches(2, and(
                                        methodNameIs("not"),
                                        methodArgsAre(1),
                                        methodArgMatches(0, matcherPredicate)
                                ))
                        )
                )
        );
    }

    @Override
    public void migrate(MethodCallExpr node) {
        AssertJBuilder builder = AssertJBuilder.create();
        if (node.getArguments().size() == 3) {
            builder.as(node.getArgument(0));
            fillBuilder(builder, node.getArgument(1), node.getArgument(2).asMethodCallExpr().getArgument(0).asMethodCallExpr());
        } else {
            fillBuilder(builder, node.getArgument(0), node.getArgument(1).asMethodCallExpr().getArgument(0).asMethodCallExpr());
        }

        node.replace(builder.build());
    }

    @Override
    public String toString() {
        return "Hamcrest assertThat not " + matcherName();
    }
}
