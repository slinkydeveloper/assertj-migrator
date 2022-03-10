package com.slinkydeveloper.assertjmigrator.migrations.common;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;
import com.slinkydeveloper.assertjmigrator.nodes.Predicates;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static com.slinkydeveloper.assertjmigrator.nodes.Predicates.*;

public class EqualityMigrator {

    private final static List<EqualityMigration> migrations = Arrays.asList(
            new CollectionOrMapSize(),
            new SingletonList(),
            new Singleton(),
            new ThrowableMessage(),
            new Null(),
            new Fallback()
    );

    private EqualityMigrator() {
    }

    public static AssertJBuilder migrateEquals(AssertJBuilder builder, Expression actual, Expression expected) {
        for (EqualityMigration equalityMigration : migrations) {
            if (equalityMigration.actualPredicate().test(actual) && equalityMigration.expectedPredicate().test(expected)) {
                equalityMigration.migrateEquals(builder, actual, expected);
                return builder;
            }
        }
        throw new IllegalStateException("Requested a migration on an unsupported equality predicate");
    }

    public static AssertJBuilder migrateNotEquals(AssertJBuilder builder, Expression actual, Expression expected) {
        for (EqualityMigration equalityMigration : migrations) {
            if (equalityMigration.actualPredicate().test(actual) && equalityMigration.expectedPredicate().test(expected)) {
                equalityMigration.migrateNotEquals(builder, actual, expected);
                return builder;
            }
        }
        throw new IllegalStateException("Requested a migration on an unsupported equality predicate");
    }

    /**
     * Definition of equality migration
     */
    private interface EqualityMigration {
        Predicate<Expression> actualPredicate();

        Predicate<Expression> expectedPredicate();

        void migrateEquals(AssertJBuilder builder, Expression actual, Expression expected);

        void migrateNotEquals(AssertJBuilder builder, Expression actual, Expression expected);
    }

    // --- Equality migrations

    private static class Fallback implements EqualityMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return e -> true;
        }

        @Override
        public Predicate<Expression> expectedPredicate() {
            return e -> true;
        }

        @Override
        public void migrateEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual).isEqualTo(expected);
        }

        @Override
        public void migrateNotEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual).isNotEqualTo(expected);
        }
    }

    private static class Null implements EqualityMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return e -> true;
        }

        @Override
        public Predicate<Expression> expectedPredicate() {
            return Expression::isNullLiteralExpr;
        }

        @Override
        public void migrateEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual)
                    .isNull();
        }

        @Override
        public void migrateNotEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual)
                    .isNotNull();
        }
    }

    private static class CollectionOrMapSize implements EqualityMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return and(
                    Expression::isMethodCallExpr,
                    or(methodScopeMatches(Predicates::isCollection), methodScopeMatches(Predicates::isMap)),
                    methodNameIs("size")
            );
        }

        @Override
        public Predicate<Expression> expectedPredicate() {
            return Predicates::isIntegral;
        }

        @Override
        public void migrateEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual.asMethodCallExpr().getScope().get())
                    .hasSize(expected);
        }

        @Override
        public void migrateNotEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual.asMethodCallExpr().getScope().get())
                    .size()
                    .isNotEqualTo(expected);
        }
    }

    private static class SingletonList implements EqualityMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return Predicates::isList;
        }

        @Override
        public Predicate<Expression> expectedPredicate() {
            return and(
                    Predicates.methodDeclaredIn(Collections.class),
                    Predicates.methodNameIs("singletonList")
            );
        }

        @Override
        public void migrateEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual)
                    .containsExactly(expected.asMethodCallExpr().getArgument(0));
        }

        @Override
        public void migrateNotEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual)
                    .hasSize(new IntegerLiteralExpr("1"))
                    .doesNotContain(expected.asMethodCallExpr().getArgument(0));
        }
    }

    private static class Singleton implements EqualityMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return Predicates::isSet;
        }

        @Override
        public Predicate<Expression> expectedPredicate() {
            return and(
                    Predicates.methodDeclaredIn(Collections.class),
                    Predicates.methodNameIs("singleton")
            );
        }

        @Override
        public void migrateEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual)
                    .containsExactly(expected.asMethodCallExpr().getArgument(0));
        }

        @Override
        public void migrateNotEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual)
                    .hasSize(new IntegerLiteralExpr("1"))
                    .doesNotContain(expected.asMethodCallExpr().getArgument(0));
        }
    }

    private static class ThrowableMessage implements EqualityMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return and(
                    Expression::isMethodCallExpr,
                    methodScopeMatches(Predicates::isThrowable),
                    methodNameIs("getMessage")
            );
        }

        @Override
        public Predicate<Expression> expectedPredicate() {
            return Predicates::isString;
        }

        @Override
        public void migrateEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual.asMethodCallExpr().getScope().get())
                    .hasMessage(expected);
        }

        @Override
        public void migrateNotEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual).isNotEqualTo(expected);
        }
    }

}
