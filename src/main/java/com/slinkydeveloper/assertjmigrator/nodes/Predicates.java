package com.slinkydeveloper.assertjmigrator.nodes;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.function.Predicate;

public class Predicates {

    private static final ReflectionTypeSolver TYPE_SOLVER = new ReflectionTypeSolver();
    private static final ResolvedType THROWABLE = new ReferenceTypeImpl(TYPE_SOLVER.solveType(Throwable.class.getName()), TYPE_SOLVER);

    private Predicates() {
    }

    public static Predicate<Expression> methodNameIs(String name) {
        return expression -> expression.isMethodCallExpr() && name.equals(expression.asMethodCallExpr().getName().toString());
    }

    public static Predicate<Expression> methodArgsAre(int count) {
        return expression -> expression.isMethodCallExpr() && expression.asMethodCallExpr().getArguments().size() == count;
    }

    public static Predicate<Expression> methodArgMatches(int i, Predicate<Expression> predicate) {
        return expression -> expression.isMethodCallExpr() && predicate.test(expression.asMethodCallExpr().getArgument(i));
    }

    public static boolean isJUnit5Assertion(Expression expr) {
        return Assertions.class.getName().equals(expr.asMethodCallExpr().resolve().declaringType().getQualifiedName());
    }

    public static boolean isJUnit4Assertion(Expression expr) {
        return Assert.class.getName().equals(expr.asMethodCallExpr().resolve().declaringType().getQualifiedName());
    }

    public static boolean isString(Expression expr) {
        if (expr.isStringLiteralExpr()) {
            return true;
        }
        ResolvedType resolvedType = expr.calculateResolvedType();
        if (!resolvedType.isReferenceType()) {
            return false;
        }
        return resolvedType.asReferenceType().getQualifiedName().equals(String.class.getName());
    }

    public static boolean isThrowable(Expression expr) {
        ResolvedType resolvedType = expr.calculateResolvedType();
        if (!resolvedType.isReferenceType()) {
            return false;
        }
        return THROWABLE.isAssignableBy(resolvedType.asReferenceType());
    }

    public static boolean isDouble(Expression expr) {
        if (expr.isDoubleLiteralExpr()) {
            return true;
        }
        ResolvedType resolvedType = expr.calculateResolvedType();
        return !resolvedType.isTypeVariable() && ResolvedPrimitiveType.DOUBLE.equals(resolvedType);
    }

    public static boolean isFloat(Expression expr) {
        ResolvedType resolvedType = expr.calculateResolvedType();
        return !resolvedType.isTypeVariable() && ResolvedPrimitiveType.FLOAT.equals(resolvedType);
    }

    @SafeVarargs
    public static <T> Predicate<T> and(Predicate<T>... predicates) {
        return arg -> Arrays.stream(predicates).allMatch(p -> p.test(arg));
    }

    @SafeVarargs
    public static <T> Predicate<T> or(Predicate<T>... predicates) {
        return arg -> Arrays.stream(predicates).anyMatch(p -> p.test(arg));
    }

}
