package com.slinkydeveloper.assertjmigrator.migrations.common;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;
import com.slinkydeveloper.assertjmigrator.nodes.Predicates;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static com.slinkydeveloper.assertjmigrator.nodes.Predicates.*;

public class PredicateMigrator {

    private final static List<PredicateMigration> migrations = Arrays.asList(
            new Equality(),
            new NotEquality(),
            new InstanceOf(),
            new IsPresent(),
            new StringContains(),
            new StringStartsWith(),
            new StringEndsWith(),
            new CollectionContains(),
            new MapContainsKey(),
            new MapContainsValue(),
            new GreaterThan(),
            new GreaterThanOrEqualTo(),
            new LessThan(),
            new LessThanOrEqualTo(),
            new IsEmpty()
    );

    private PredicateMigrator() {
    }

    public static AssertJBuilder migrateTrue(AssertJBuilder builder, Expression actual) {
        for (PredicateMigration predicateMigration : migrations) {
            try {
                if (predicateMigration.actualPredicate().test(actual)) {
                    predicateMigration.migrateTrue(builder, actual);
                    return builder;
                }
            } catch (Exception e) {
                System.err.println("Error while trying to match migration " + predicateMigration.getClass() + " for expression " + actual);
            }
        }
        return builder.assertThat(actual).isTrue();
    }

    public static AssertJBuilder migrateFalse(AssertJBuilder builder, Expression actual) {
        for (PredicateMigration equalityMigration : migrations) {
            try {
                if (equalityMigration.actualPredicate().test(actual)) {
                    equalityMigration.migrateFalse(builder, actual);
                    return builder;
                }
            } catch (Exception e) {
                System.err.println("Error while trying to match migration " + equalityMigration.getClass() + " for expression " + actual);
            }
        }
        return builder.assertThat(actual).isFalse();
    }

    /**
     * Definition of equality migration
     */
    private interface PredicateMigration {
        Predicate<Expression> actualPredicate();

        void migrateTrue(AssertJBuilder builder, Expression actual);

        void migrateFalse(AssertJBuilder builder, Expression actual);
    }

    // --- Equality migrations

    private static class Equality implements PredicateMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return expr -> expr.isBinaryExpr() && expr.asBinaryExpr().getOperator() == BinaryExpr.Operator.EQUALS;
        }

        @Override
        public void migrateTrue(AssertJBuilder builder, Expression actualBinaryExpr) {
            Expression actual = actualBinaryExpr.asBinaryExpr().getLeft();
            Expression expected = actualBinaryExpr.asBinaryExpr().getRight();
            if (expected.isNullLiteralExpr() || (isPrimitive(actual) || isPrimitive(expected))) {
                EqualityMigrator.migrateEquals(builder, actual, expected);
            } else {
                builder.assertThat(actual).isSameAs(expected);
            }
        }

        @Override
        public void migrateFalse(AssertJBuilder builder, Expression actualBinaryExpr) {
            Expression actual = actualBinaryExpr.asBinaryExpr().getLeft();
            Expression expected = actualBinaryExpr.asBinaryExpr().getRight();
            if (expected.isNullLiteralExpr() || (isPrimitive(actual) || isPrimitive(expected))) {
                EqualityMigrator.migrateNotEquals(builder, actual, expected);
            } else {
                builder.assertThat(actual).isNotSameAs(expected);
            }
        }
    }

    private static class NotEquality extends Equality {

        @Override
        public Predicate<Expression> actualPredicate() {
            return expr -> expr.isBinaryExpr() && expr.asBinaryExpr().getOperator() == BinaryExpr.Operator.NOT_EQUALS;
        }

        @Override
        public void migrateTrue(AssertJBuilder builder, Expression actual) {
            super.migrateFalse(builder, actual);
        }

        @Override
        public void migrateFalse(AssertJBuilder builder, Expression actual) {
            super.migrateTrue(builder, actual);
        }
    }

    private static class InstanceOf implements PredicateMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return Expression::isInstanceOfExpr;
        }

        @Override
        public void migrateTrue(AssertJBuilder builder, Expression actual) {
            InstanceOfExpr instanceOfExpr = actual.asInstanceOfExpr();
            builder.assertThat(instanceOfExpr.getExpression())
                    .isInstanceOf(instanceOfExpr.getType());
        }

        @Override
        public void migrateFalse(AssertJBuilder builder, Expression actual) {
            InstanceOfExpr instanceOfExpr = actual.asInstanceOfExpr();
            builder.assertThat(instanceOfExpr.getExpression())
                    .isNotInstanceOf(instanceOfExpr.getType());
        }
    }

    private static class IsPresent implements PredicateMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return and(
                    methodScopeMatches(Predicates::isOptional),
                    methodNameIs("isPresent")
            );
        }

        @Override
        public void migrateTrue(AssertJBuilder builder, Expression actual) {
            builder.assertThat(actual.asMethodCallExpr().getScope().get()).isPresent();
        }

        @Override
        public void migrateFalse(AssertJBuilder builder, Expression actual) {
            builder.assertThat(actual.asMethodCallExpr().getScope().get()).isNotPresent();
        }
    }

    private static class StringContains implements PredicateMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return and(
                    methodScopeMatches(Predicates::isString),
                    methodNameIs("contains")
            );
        }

        @Override
        public void migrateTrue(AssertJBuilder builder, Expression actual) {
            MethodCallExpr callExpr = actual.asMethodCallExpr();
            builder.assertThat(callExpr.getScope().get())
                    .contains(callExpr.getArgument(0));
        }

        @Override
        public void migrateFalse(AssertJBuilder builder, Expression actual) {
            MethodCallExpr callExpr = actual.asMethodCallExpr();
            builder.assertThat(callExpr.getScope().get())
                    .doesNotContain(callExpr.getArgument(0));
        }
    }

    private static class StringStartsWith implements PredicateMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return and(
                    methodScopeMatches(Predicates::isString),
                    methodNameIs("startsWith")
            );
        }

        @Override
        public void migrateTrue(AssertJBuilder builder, Expression actual) {
            MethodCallExpr callExpr = actual.asMethodCallExpr();
            builder.assertThat(callExpr.getScope().get())
                    .startsWith(callExpr.getArgument(0));
        }

        @Override
        public void migrateFalse(AssertJBuilder builder, Expression actual) {
            MethodCallExpr callExpr = actual.asMethodCallExpr();
            builder.assertThat(callExpr.getScope().get())
                    .doesNotStartWith(callExpr.getArgument(0));
        }
    }

    private static class StringEndsWith implements PredicateMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return and(
                    methodScopeMatches(Predicates::isString),
                    methodNameIs("endsWith")
            );
        }

        @Override
        public void migrateTrue(AssertJBuilder builder, Expression actual) {
            MethodCallExpr callExpr = actual.asMethodCallExpr();
            builder.assertThat(callExpr.getScope().get())
                    .endsWith(callExpr.getArgument(0));
        }

        @Override
        public void migrateFalse(AssertJBuilder builder, Expression actual) {
            MethodCallExpr callExpr = actual.asMethodCallExpr();
            builder.assertThat(callExpr.getScope().get())
                    .doesNotEndWith(callExpr.getArgument(0));
        }
    }

    private static class CollectionContains implements PredicateMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return and(
                    methodScopeMatches(Predicates::isCollection),
                    methodNameIs("contains")
            );
        }

        @Override
        public void migrateTrue(AssertJBuilder builder, Expression actual) {
            MethodCallExpr callExpr = actual.asMethodCallExpr();
            builder.assertThat(callExpr.getScope().get())
                    .contains(callExpr.getArgument(0));
        }

        @Override
        public void migrateFalse(AssertJBuilder builder, Expression actual) {
            MethodCallExpr callExpr = actual.asMethodCallExpr();
            builder.assertThat(callExpr.getScope().get())
                    .doesNotContain(callExpr.getArgument(0));
        }
    }

    private static class MapContainsKey implements PredicateMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return and(
                    methodScopeMatches(Predicates::isMap),
                    methodNameIs("containsKey")
            );
        }

        @Override
        public void migrateTrue(AssertJBuilder builder, Expression actual) {
            MethodCallExpr callExpr = actual.asMethodCallExpr();
            builder.assertThat(callExpr.getScope().get())
                    .containsKey(callExpr.getArgument(0));
        }

        @Override
        public void migrateFalse(AssertJBuilder builder, Expression actual) {
            MethodCallExpr callExpr = actual.asMethodCallExpr();
            builder.assertThat(callExpr.getScope().get())
                    .doesNotContainKey(callExpr.getArgument(0));
        }
    }

    private static class MapContainsValue implements PredicateMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return and(
                    methodScopeMatches(Predicates::isMap),
                    methodNameIs("containsValue")
            );
        }

        @Override
        public void migrateTrue(AssertJBuilder builder, Expression actual) {
            MethodCallExpr callExpr = actual.asMethodCallExpr();
            builder.assertThat(callExpr.getScope().get())
                    .containsValue(callExpr.getArgument(0));
        }

        @Override
        public void migrateFalse(AssertJBuilder builder, Expression actual) {
            MethodCallExpr callExpr = actual.asMethodCallExpr();
            builder.assertThat(callExpr.getScope().get())
                    .doesNotContainValue(callExpr.getArgument(0));
        }
    }

    private static abstract class BinaryComparison implements PredicateMigration {

        abstract BinaryExpr.Operator getOperator();

        abstract BiConsumer<AssertJBuilder, Expression> trueAssert();

        abstract BiConsumer<AssertJBuilder, Expression> falseAssert();

        @Override
        public Predicate<Expression> actualPredicate() {
            return binaryOperatorMatches(getOperator());
        }

        @Override
        public void migrateTrue(AssertJBuilder builder, Expression actual) {
            BinaryExpr binaryExpr = actual.asBinaryExpr();
            trueAssert().accept(builder.assertThat(binaryExpr.getLeft()), binaryExpr.getRight());
        }

        @Override
        public void migrateFalse(AssertJBuilder builder, Expression actual) {
            BinaryExpr binaryExpr = actual.asBinaryExpr();
            falseAssert().accept(builder.assertThat(binaryExpr.getLeft()), binaryExpr.getRight());
        }

    }

    private static class GreaterThan extends BinaryComparison {

        @Override
        BinaryExpr.Operator getOperator() {
            return BinaryExpr.Operator.GREATER;
        }

        @Override
        BiConsumer<AssertJBuilder, Expression> trueAssert() {
            return AssertJBuilder::isGreaterThan;
        }

        @Override
        BiConsumer<AssertJBuilder, Expression> falseAssert() {
            return AssertJBuilder::isLessThanOrEqualTo;
        }

    }

    private static class GreaterThanOrEqualTo extends BinaryComparison {

        @Override
        BinaryExpr.Operator getOperator() {
            return BinaryExpr.Operator.GREATER_EQUALS;
        }

        @Override
        BiConsumer<AssertJBuilder, Expression> trueAssert() {
            return AssertJBuilder::isGreaterThanOrEqualTo;
        }

        @Override
        BiConsumer<AssertJBuilder, Expression> falseAssert() {
            return AssertJBuilder::isLessThan;
        }

    }

    private static class LessThan extends BinaryComparison {

        @Override
        BinaryExpr.Operator getOperator() {
            return BinaryExpr.Operator.LESS;
        }

        @Override
        BiConsumer<AssertJBuilder, Expression> trueAssert() {
            return AssertJBuilder::isLessThan;
        }

        @Override
        BiConsumer<AssertJBuilder, Expression> falseAssert() {
            return AssertJBuilder::isGreaterThanOrEqualTo;
        }

    }

    private static class LessThanOrEqualTo extends BinaryComparison {

        @Override
        BinaryExpr.Operator getOperator() {
            return BinaryExpr.Operator.LESS_EQUALS;
        }

        @Override
        BiConsumer<AssertJBuilder, Expression> trueAssert() {
            return AssertJBuilder::isLessThanOrEqualTo;
        }

        @Override
        BiConsumer<AssertJBuilder, Expression> falseAssert() {
            return AssertJBuilder::isGreaterThan;
        }

    }

    private static class IsEmpty implements PredicateMigration {

        @Override
        public Predicate<Expression> actualPredicate() {
            return and(
                    methodScopeMatches(or(Predicates::isCollection, Predicates::isMap)),
                    methodNameIs("isEmpty")
            );
        }

        @Override
        public void migrateTrue(AssertJBuilder builder, Expression actual) {
            builder.assertThat(actual.asMethodCallExpr().getScope().get()).isEmpty();
        }

        @Override
        public void migrateFalse(AssertJBuilder builder, Expression actual) {
            builder.assertThat(actual.asMethodCallExpr().getScope().get()).isNotEmpty();
        }
    }

}
