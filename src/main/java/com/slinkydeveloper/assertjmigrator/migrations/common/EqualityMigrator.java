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
            new EmptyString(),
            new EmptyList(),
            new EmptySet(),
            new EmptyMap(),
            new ArrayLength(),
            new EqualsBoolean()
    );

    private EqualityMigrator() {
    }

    public static AssertJBuilder migrateEquals(AssertJBuilder builder, Expression actual, Expression expected) {
        for (EqualityMigration equalityMigration : migrations) {
            try {
                if (equalityMigration.actualPredicate().test(actual) && equalityMigration.expectedPredicate().test(expected)) {
                    equalityMigration.migrateEquals(builder, actual, expected);
                    return builder;
                }
            } catch (Exception e) {
                System.err.println("Error while trying to match migration " + equalityMigration.getClass() + " for expression " + actual);
            }
        }
        return builder.assertThat(actual).isEqualTo(expected);
    }

    public static AssertJBuilder migrateNotEquals(AssertJBuilder builder, Expression actual, Expression expected) {
        for (EqualityMigration equalityMigration : migrations) {
            try {
                if (equalityMigration.actualPredicate().test(actual) && equalityMigration.expectedPredicate().test(expected)) {
                    equalityMigration.migrateNotEquals(builder, actual, expected);
                    return builder;
                }
            } catch (Exception e) {
                System.err.println("Error while trying to match migration " + equalityMigration.getClass() + " for expression " + actual);
            }
        }
        return builder.assertThat(actual).isNotEqualTo(expected);
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
            builder.assertThat(actual.asMethodCallExpr().getScope().get());
            if (expected.isIntegerLiteralExpr() && expected.asIntegerLiteralExpr().asNumber().intValue() == 0) {
                builder.isEmpty();
            } else {
                builder.hasSize(expected);
            }
        }

        @Override
        public void migrateNotEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual.asMethodCallExpr().getScope().get());
            if (expected.isIntegerLiteralExpr() && expected.asIntegerLiteralExpr().asNumber().intValue() == 0) {
                builder.isNotEmpty();
            } else {
                builder.size().isNotEqualTo(expected);
            }
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

    private static class EmptyString implements EqualityMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return Predicates::isString;
        }

        @Override
        public Predicate<Expression> expectedPredicate() {
            return exp -> exp.isStringLiteralExpr() && exp.asStringLiteralExpr().getValue().isEmpty();
        }

        @Override
        public void migrateEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual)
                    .isEmpty();
        }

        @Override
        public void migrateNotEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual)
                    .isNotEmpty();
        }
    }

    private static class EmptyList implements EqualityMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return Predicates::isList;
        }

        @Override
        public Predicate<Expression> expectedPredicate() {
            return and(
                    Predicates.methodDeclaredIn(Collections.class),
                    Predicates.methodNameIs("emptyList")
            );
        }

        @Override
        public void migrateEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual)
                    .isEmpty();
        }

        @Override
        public void migrateNotEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual)
                    .isNotEmpty();
        }
    }

    private static class EmptySet implements EqualityMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return Predicates::isSet;
        }

        @Override
        public Predicate<Expression> expectedPredicate() {
            return and(
                    Predicates.methodDeclaredIn(Collections.class),
                    Predicates.methodNameIs("emptySet")
            );
        }

        @Override
        public void migrateEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual)
                    .isEmpty();
        }

        @Override
        public void migrateNotEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual)
                    .isNotEmpty();
        }
    }

    private static class EmptyMap implements EqualityMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return Predicates::isMap;
        }

        @Override
        public Predicate<Expression> expectedPredicate() {
            return and(
                    Predicates.methodDeclaredIn(Collections.class),
                    Predicates.methodNameIs("emptyMap")
            );
        }

        @Override
        public void migrateEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual)
                    .isEmpty();
        }

        @Override
        public void migrateNotEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual)
                    .isNotEmpty();
        }
    }

    private static class ArrayLength implements EqualityMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return fieldAccessMatches(Predicates::isArray, "length");
        }

        @Override
        public Predicate<Expression> expectedPredicate() {
            return Predicates::isIntegral;
        }

        @Override
        public void migrateEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual.asFieldAccessExpr().getScope());
            if (expected.isIntegerLiteralExpr() && expected.asIntegerLiteralExpr().asNumber().intValue() == 0) {
                builder.isEmpty();
            } else {
                builder.hasSize(expected);
            }
        }

        @Override
        public void migrateNotEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            builder.assertThat(actual.asFieldAccessExpr().getScope());
            if (expected.isIntegerLiteralExpr() && expected.asIntegerLiteralExpr().asNumber().intValue() == 0) {
                builder.isNotEmpty();
            } else {
                builder.size().isNotEqualTo(expected);
            }
        }
    }

    private static class EqualsBoolean implements EqualityMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return expr -> true;
        }

        @Override
        public Predicate<Expression> expectedPredicate() {
            return Expression::isBooleanLiteralExpr;
        }

        @Override
        public void migrateEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            if (expected.asBooleanLiteralExpr().getValue()) {
                PredicateMigrator.migrateTrue(builder, actual);
            } else {
                PredicateMigrator.migrateFalse(builder, actual);
            }
        }

        @Override
        public void migrateNotEquals(AssertJBuilder builder, Expression actual, Expression expected) {
            if (expected.asBooleanLiteralExpr().getValue()) {
                PredicateMigrator.migrateFalse(builder, actual);
            } else {
                PredicateMigrator.migrateTrue(builder, actual);
            }
        }
    }

}
