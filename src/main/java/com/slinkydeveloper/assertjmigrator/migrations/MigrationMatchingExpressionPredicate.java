package com.slinkydeveloper.assertjmigrator.migrations;

import com.github.javaparser.ast.expr.Expression;
import com.slinkydeveloper.assertjmigrator.Migration;

import java.util.function.Predicate;

public interface MigrationMatchingExpressionPredicate<T extends Expression> extends Migration<T> {

    Predicate<Expression> predicate();

    @Override
    default boolean matches(T node) {
        return predicate().test(node);
    }
}
