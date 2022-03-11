package com.slinkydeveloper.assertjmigrator.nodes;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Predicates {

    private static final ReflectionTypeSolver TYPE_SOLVER = new ReflectionTypeSolver();
    private static final ResolvedType THROWABLE = new ReferenceTypeImpl(TYPE_SOLVER.solveType(Throwable.class.getName()), TYPE_SOLVER);
    private static final ResolvedType COLLECTION = new ReferenceTypeImpl(TYPE_SOLVER.solveType(Collection.class.getName()), TYPE_SOLVER);
    private static final ResolvedType LIST = new ReferenceTypeImpl(TYPE_SOLVER.solveType(List.class.getName()), TYPE_SOLVER);
    private static final ResolvedType SET = new ReferenceTypeImpl(TYPE_SOLVER.solveType(Set.class.getName()), TYPE_SOLVER);
    private static final ResolvedType MAP = new ReferenceTypeImpl(TYPE_SOLVER.solveType(Map.class.getName()), TYPE_SOLVER);
    private static final ResolvedType OPTIONAL = new ReferenceTypeImpl(TYPE_SOLVER.solveType(Optional.class.getName()), TYPE_SOLVER);

    private Predicates() {
    }

    public static Predicate<Expression> methodNameIs(String name) {
        return expression -> expression.isMethodCallExpr() && name.equals(expression.asMethodCallExpr().getName().toString());
    }

    public static Predicate<Expression> methodArgsAre(int count) {
        return expression -> expression.isMethodCallExpr() && expression.asMethodCallExpr().getArguments().size() == count;
    }

    public static Predicate<Expression> methodScopeMatches(Predicate<Expression> predicate) {
        return expression -> expression.isMethodCallExpr() && expression.asMethodCallExpr().getScope().map(predicate::test).orElse(false);
    }

    public static Predicate<Expression> methodArgMatches(int i, Predicate<Expression> predicate) {
        return expression -> expression.isMethodCallExpr() && predicate.test(expression.asMethodCallExpr().getArgument(i));
    }

    public static Predicate<Expression> methodDeclaredIn(Class<?> clazz) {
        return expr -> expr.isMethodCallExpr() && clazz.getName().equals(expr.asMethodCallExpr().resolve().declaringType().getQualifiedName());
    }

    public static Predicate<Expression> binaryOperatorMatches(BinaryExpr.Operator operator) {
        return binaryOperatorMatches(operator, expr -> true, expr -> true);
    }

    public static Predicate<Expression> binaryOperatorMatches(BinaryExpr.Operator operator, Predicate<Expression> left, Predicate<Expression> right) {
        return expression -> expression.isBinaryExpr() &&
                expression.asBinaryExpr().getOperator() == operator &&
                left.test(expression.asBinaryExpr().getLeft()) &&
                right.test(expression.asBinaryExpr().getRight());
    }

    public static Predicate<Expression> fieldAccessMatches(Predicate<Expression> scopePredicate, String fieldName) {
        return expression -> expression.isFieldAccessExpr() &&
                scopePredicate.test(expression.asFieldAccessExpr().getScope()) &&
                fieldName.equals(expression.asFieldAccessExpr().getName().toString());
    }

    public static Predicate<Expression> isJUnit5Assertion() {
        return methodDeclaredIn(Assertions.class);
    }

    public static Predicate<Expression> isJUnit4Assertion() {
        return methodDeclaredIn(Assert.class);
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

    public static boolean isCollection(Expression expr) {
        ResolvedType resolvedType = expr.calculateResolvedType();
        if (!resolvedType.isReferenceType()) {
            return false;
        }
        return COLLECTION.isAssignableBy(resolvedType.asReferenceType());
    }

    public static boolean isList(Expression expr) {
        ResolvedType resolvedType = expr.calculateResolvedType();
        if (!resolvedType.isReferenceType()) {
            return false;
        }
        return LIST.isAssignableBy(resolvedType.asReferenceType());
    }

    public static boolean isSet(Expression expr) {
        ResolvedType resolvedType = expr.calculateResolvedType();
        if (!resolvedType.isReferenceType()) {
            return false;
        }
        return SET.isAssignableBy(resolvedType.asReferenceType());
    }

    public static boolean isMap(Expression expr) {
        ResolvedType resolvedType = expr.calculateResolvedType();
        if (!resolvedType.isReferenceType()) {
            return false;
        }
        return MAP.isAssignableBy(resolvedType.asReferenceType());
    }

    public static boolean isArray(Expression expr) {
        ResolvedType resolvedType = expr.calculateResolvedType();
        return resolvedType.isArray();
    }

    public static boolean isOptional(Expression expr) {
        ResolvedType resolvedType = expr.calculateResolvedType();
        if (!resolvedType.isReferenceType()) {
            return false;
        }
        return OPTIONAL.isAssignableBy(resolvedType.asReferenceType());
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

    public static boolean isIntegralPrimitive(Expression expr) {
        ResolvedType resolvedType = expr.calculateResolvedType();
        return !resolvedType.isTypeVariable() && Stream.of(
                ResolvedPrimitiveType.BYTE,
                ResolvedPrimitiveType.SHORT,
                ResolvedPrimitiveType.INT,
                ResolvedPrimitiveType.LONG
        ).anyMatch(p -> p.equals(resolvedType));
    }

    public static boolean isIntegral(Expression expr) {
        ResolvedType resolvedType = expr.calculateResolvedType();
        if (resolvedType.isTypeVariable()) {
            return false;
        }
        ResolvedType unboxed = ResolvedPrimitiveType.unp(resolvedType);
        return Stream.of(
                ResolvedPrimitiveType.BYTE,
                ResolvedPrimitiveType.SHORT,
                ResolvedPrimitiveType.INT,
                ResolvedPrimitiveType.LONG
        ).anyMatch(p -> p.equals(unboxed));
    }

    public static boolean isPrimitive(Expression expr) {
        ResolvedType resolvedType = expr.calculateResolvedType();
        return !resolvedType.isTypeVariable() && resolvedType.isPrimitive();
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
