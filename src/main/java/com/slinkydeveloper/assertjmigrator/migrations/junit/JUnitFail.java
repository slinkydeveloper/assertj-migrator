package com.slinkydeveloper.assertjmigrator.migrations.junit;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.slinkydeveloper.assertjmigrator.migrations.MigrationMatchingExpressionPredicate;
import com.slinkydeveloper.assertjmigrator.nodes.Predicates;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static com.slinkydeveloper.assertjmigrator.nodes.Predicates.*;

public class JUnitFail implements MigrationMatchingExpressionPredicate<MethodCallExpr> {

    private static final Predicate<Expression> FAIL = methodArgsAre(0);
    private static final Predicate<Expression> FAIL_STRING = and(methodArgsAre(1), methodArgMatches(0, Predicates::isString));
    private static final Predicate<Expression> FAIL_THROWABLE = and(methodArgsAre(1), methodArgMatches(0, Predicates::isThrowable));
    private static final Predicate<Expression> FAIL_STRING_THROWABLE = and(methodArgsAre(2), methodArgMatches(0, Predicates::isString), methodArgMatches(1, Predicates::isThrowable));

    @Override
    public Class<MethodCallExpr> matchedNode() {
        return MethodCallExpr.class;
    }

    @Override
    public final Predicate<Expression> predicate() {
        return and(
                methodNameIs("fail"),
                or(
                        FAIL,
                        FAIL_STRING,
                        FAIL_THROWABLE,
                        FAIL_STRING_THROWABLE
                )
        );
    }

    @Override
    public final void migrate(MethodCallExpr methodCallExpr) {
        if (FAIL.test(methodCallExpr)) {
            methodCallExpr.replace(
                    new MethodCallExpr("fail", new StringLiteralExpr("unknown failure"))
            );
        } else if (FAIL_STRING.test(methodCallExpr)) {
            methodCallExpr.replace(
                    new MethodCallExpr("fail", methodCallExpr.getArgument(0))
            );
        } else if (FAIL_THROWABLE.test(methodCallExpr)) {
            methodCallExpr.replace(
                    new MethodCallExpr(
                            "fail",
                            new StringLiteralExpr("unknown failure"),
                            methodCallExpr.getArgument(0)
                    )
            );
        } else {
            methodCallExpr.replace(
                    new MethodCallExpr(
                            "fail",
                            methodCallExpr.getArgument(0),
                            methodCallExpr.getArgument(1)
                    )
            );
        }
    }

    @Override
    public List<String> requiredImports() {
        return Collections.singletonList("org.assertj.core.api.Assertions.fail");
    }

    @Override
    public String toString() {
        return "JUnit 4/5 fail";
    }

}
