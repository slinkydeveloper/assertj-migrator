package com.slinkydeveloper.assertjmigrator.migrations.hamcrest;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.migrations.MigrationMatchingExpressionPredicate;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;
import com.slinkydeveloper.assertjmigrator.nodes.Predicates;

import java.util.List;
import java.util.function.Predicate;

import static com.slinkydeveloper.assertjmigrator.nodes.Predicates.*;

public class HamcrestAssertThatFallbackMigration implements MigrationMatchingExpressionPredicate<MethodCallExpr> {

    @Override
    public Class<MethodCallExpr> matchedNode() {
        return MethodCallExpr.class;
    }

    @Override
    public Predicate<Expression> predicate() {
        return and(
                methodNameIs("assertThat"),
                or(
                        methodArgsAre(2),
                        and(
                                methodArgsAre(3),
                                methodArgMatches(0, Predicates::isString)
                        )
                )
        );
    }

    @Override
    public void migrate(MethodCallExpr node) {
        AssertJBuilder builder = AssertJBuilder.create();
        if (node.getArguments().size() == 3) {
            builder.as(node.getArgument(0))
                    .assertThat(node.getArgument(1))
                    .satisfiesHamcrestMatcher(node.getArgument(2));
        } else {
            builder.assertThat(node.getArgument(0))
                    .satisfiesHamcrestMatcher(node.getArgument(1));
        }

        node.replace(builder.build());
    }

    @Override
    public List<String> requiredImports() {
        return List.of("org.assertj.core.api.HamcrestCondition.matching", "org.assertj.core.api.Assertions.assertThat");
    }

    @Override
    public String toString() {
        return "Hamcrest assertThat fallback migration";
    }
}
