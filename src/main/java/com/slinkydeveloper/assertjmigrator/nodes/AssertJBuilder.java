package com.slinkydeveloper.assertjmigrator.nodes;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ReferenceType;

public class AssertJBuilder {

  private Expression assertThat;
  private Expression assertThatThrownBy;
  private Expression message;

  private boolean size;
  private boolean isTrue;
  private boolean isFalse;
  private boolean isNull;
  private boolean isNotNull;
  private Expression isEqualTo;
  private Expression isNotEqualTo;
  private Expression isInstanceOf;
  private Expression isNotInstanceOf;
  private Expression isSameAs;
  private Expression isNotSameAs;

  // String and collections
  private Expression startsWith;
  private Expression doesNotStartWith;
  private Expression endsWith;
  private Expression doesNotEndWith;
  private boolean isEmpty;
  private boolean isNotEmpty;
  private Expression contains;
  private Expression doesNotContain;

  // Collections
  private Expression hasSize;
  private List<Expression> containsList;
  private Expression containsExactly;
  private List<Expression> containsExactlyList;
  private List<Expression> doesNotContainList;
  private Expression containsExactlyInAnyOrder;
  private List<Expression> containsExactlyInAnyOrderList;

  // Map
  private Expression containsKey;
  private Expression doesNotContainKey;
  private Expression containsValue;
  private Expression doesNotContainValue;

  // Hamcrest
  private Expression satisfiesHamcrestMatcher;

  // Numeric bounds
  private Expression isGreaterThan;
  private Expression isGreaterThanOrEqualTo;
  private Expression isLessThan;
  private Expression isLessThanOrEqualTo;
  private Expression isCloseTo;
  private Expression isCloseToWithin;
  private Expression isNotCloseTo;
  private Expression isNotCloseToWithin;

  // Throwable
  private Expression hasMessage;

  // Optional
  private boolean isPresent;
  private boolean isNotPresent;

  public static AssertJBuilder create() {
    return new AssertJBuilder();
  }

  public AssertJBuilder assertThat(Expression actual) {
    this.assertThat = actual.clone();
    return this;
  }

  public AssertJBuilder assertThatThrownBy(Expression actual) {
    this.assertThatThrownBy = actual.clone();
    return this;
  }

  public AssertJBuilder as(Expression message) {
    this.message = message.clone();
    return this;
  }

  public AssertJBuilder isTrue() {
    this.isTrue = true;
    return this;
  }

  public AssertJBuilder isFalse() {
    this.isFalse = true;
    return this;
  }

  public AssertJBuilder isNull() {
    this.isNull = true;
    return this;
  }

  public AssertJBuilder isNotNull() {
    this.isNotNull = true;
    return this;
  }

  public AssertJBuilder isEqualTo(Expression expected) {
    this.isEqualTo = expected.clone();
    return this;
  }

  public AssertJBuilder isNotEqualTo(Expression expected) {
    this.isNotEqualTo = expected.clone();
    return this;
  }

  public AssertJBuilder isInstanceOf(ReferenceType type) {
    this.isInstanceOf = new ClassExpr(removeTypeArguments(type));
    return this;
  }

  private ReferenceType removeTypeArguments(ReferenceType type) {
    if (type.isClassOrInterfaceType()) {
      // Prune it from <> brackets
      return type.asClassOrInterfaceType().clone().removeTypeArguments();
    }
    if (type.isArrayType() && (type.asArrayType().getComponentType().isClassOrInterfaceType()
                               || type.asArrayType().getComponentType().isArrayType())) {
      return type.asArrayType().clone()
                 .setComponentType(removeTypeArguments((ReferenceType) type.asArrayType().getComponentType()));
    }
    return type;
  }

  public AssertJBuilder isInstanceOf(Expression expected) {
    this.isInstanceOf = expected.clone();
    return this;
  }

  public AssertJBuilder isNotInstanceOf(ReferenceType type) {
    this.isNotInstanceOf = new ClassExpr(removeTypeArguments(type));
    return this;
  }

  public AssertJBuilder isNotInstanceOf(Expression expected) {
    this.isNotInstanceOf = expected.clone();
    return this;
  }

  public AssertJBuilder isCloseTo(Expression expected, Expression delta) {
    this.isCloseTo = expected.clone();
    this.isCloseToWithin = delta.clone();
    return this;
  }

  public AssertJBuilder isNotCloseTo(Expression expected, Expression delta) {
    this.isNotCloseTo = expected.clone();
    this.isNotCloseToWithin = delta.clone();
    return this;
  }

  public AssertJBuilder isSameAs(Expression expected) {
    this.isSameAs = expected.clone();
    return this;
  }

  public AssertJBuilder isNotSameAs(Expression expected) {
    this.isNotSameAs = expected.clone();
    return this;
  }

  public AssertJBuilder contains(Expression argument) {
    this.contains = argument.clone();
    return this;
  }

  public AssertJBuilder contains(List<Expression> argumentList) {
    this.containsList = new NodeList<>(argumentList.stream().map(Expression::clone).collect(Collectors.toList()));
    return this;
  }

  public AssertJBuilder containsExactly(Expression argument) {
    this.containsExactly = argument.clone();
    return this;
  }

  public AssertJBuilder containsExactly(List<Expression> argumentList) {
    this.containsExactlyList = new NodeList<>(argumentList.stream().map(Expression::clone).collect(Collectors.toList()));
    return this;
  }

  public AssertJBuilder containsExactlyInAnyOrder(Expression argument) {
    this.containsExactlyInAnyOrder = argument.clone();
    return this;
  }

  public AssertJBuilder containsExactlyInAnyOrder(List<Expression> argumentList) {
    this.containsExactlyInAnyOrderList = new NodeList<>(argumentList.stream().map(Expression::clone)
                                                                    .collect(Collectors.toList()));
    return this;
  }

  public AssertJBuilder doesNotContain(Expression argument) {
    this.doesNotContain = argument.clone();
    return this;
  }

  public AssertJBuilder doesNotContain(List<Expression> argumentList) {
    this.doesNotContainList = new NodeList<>(argumentList.stream().map(Expression::clone).collect(Collectors.toList()));
    return this;
  }

  public AssertJBuilder startsWith(Expression argument) {
    this.startsWith = argument.clone();
    return this;
  }

  public AssertJBuilder doesNotStartWith(Expression argument) {
    this.doesNotStartWith = argument.clone();
    return this;
  }

  public AssertJBuilder endsWith(Expression argument) {
    this.endsWith = argument.clone();
    return this;
  }

  public AssertJBuilder doesNotEndWith(Expression argument) {
    this.doesNotEndWith = argument.clone();
    return this;
  }

  public AssertJBuilder satisfiesHamcrestMatcher(Expression matcher) {
    this.satisfiesHamcrestMatcher = matcher.clone();
    return this;
  }

  public AssertJBuilder isGreaterThan(Expression isGreaterThan) {
    this.isGreaterThan = isGreaterThan.clone();
    return this;
  }

  public AssertJBuilder isGreaterThanOrEqualTo(Expression isGreaterThanOrEqualTo) {
    this.isGreaterThanOrEqualTo = isGreaterThanOrEqualTo.clone();
    return this;
  }

  public AssertJBuilder isLessThan(Expression isLessThan) {
    this.isLessThan = isLessThan.clone();
    return this;
  }

  public AssertJBuilder isLessThanOrEqualTo(Expression isLessThanOrEqualTo) {
    this.isLessThanOrEqualTo = isLessThanOrEqualTo.clone();
    return this;
  }

  public AssertJBuilder hasMessage(Expression hasMessage) {
    this.hasMessage = hasMessage.clone();
    return this;
  }

  public AssertJBuilder hasSize(Expression hasSize) {
    this.hasSize = hasSize.clone();
    return this;
  }

  public AssertJBuilder size() {
    this.size = true;
    return this;
  }

  public AssertJBuilder isPresent() {
    this.isPresent = true;
    return this;
  }

  public AssertJBuilder isNotPresent() {
    this.isNotPresent = true;
    return this;
  }

  public AssertJBuilder isEmpty() {
    this.isEmpty = true;
    return this;
  }

  public AssertJBuilder isNotEmpty() {
    this.isNotEmpty = true;
    return this;
  }

  public AssertJBuilder containsKey(Expression argument) {
    this.containsKey = argument.clone();
    return this;
  }

  public AssertJBuilder doesNotContainKey(Expression argument) {
    this.doesNotContainKey = argument.clone();
    return this;
  }

  public AssertJBuilder containsValue(Expression argument) {
    this.containsValue = argument.clone();
    return this;
  }

  public AssertJBuilder doesNotContainValue(Expression argument) {
    this.doesNotContainValue = argument.clone();
    return this;
  }

  public Expression build() {
    Objects.requireNonNullElse(this.assertThat, this.assertThatThrownBy);
    Expression root;
    if (this.assertThat != null) {
      root = new MethodCallExpr("assertThat", this.assertThat);
    } else {
      root = new MethodCallExpr("assertThatThrownBy", this.assertThatThrownBy);
    }

    if (message != null) {
      root = new MethodCallExpr("as", message).setScope(root);
    }
    if (size) {
      root = new MethodCallExpr("size").setScope(root);
    }

    if (isTrue) {
      root = new MethodCallExpr(root, "isTrue");
    }
    if (isFalse) {
      root = new MethodCallExpr(root, "isFalse");
    }
    if (isNull) {
      root = new MethodCallExpr(root, "isNull");
    }
    if (isNotNull) {
      root = new MethodCallExpr(root, "isNotNull");
    }
    if (isEqualTo != null) {
      root = new MethodCallExpr("isEqualTo", isEqualTo).setScope(root);
    }
    if (isNotEqualTo != null) {
      root = new MethodCallExpr("isNotEqualTo", isNotEqualTo).setScope(root);
    }
    if (isInstanceOf != null) {
      root = new MethodCallExpr("isInstanceOf", isInstanceOf).setScope(root);
    }
    if (isNotInstanceOf != null) {
      root = new MethodCallExpr("isNotInstanceOf", isNotInstanceOf).setScope(root);
    }
    if (isCloseTo != null) {
      root = new MethodCallExpr("isCloseTo", isCloseTo, new MethodCallExpr("within", isCloseToWithin)).setScope(root);
    }
    if (isNotCloseTo != null) {
      root = new MethodCallExpr("isNotCloseTo", isNotCloseTo, new MethodCallExpr("within", isNotCloseToWithin)).setScope(root);
    }
    if (isSameAs != null) {
      root = new MethodCallExpr("isSameAs", isSameAs).setScope(root);
    }
    if (isNotSameAs != null) {
      root = new MethodCallExpr("isNotSameAs", isNotSameAs).setScope(root);
    }
    if (hasSize != null) {
      root = new MethodCallExpr("hasSize", hasSize).setScope(root);
    }
    if (contains != null) {
      root = new MethodCallExpr("contains", contains).setScope(root);
    }
    if (containsList != null) {
      root = new MethodCallExpr("contains").setArguments(new NodeList<>(containsList)).setScope(root);
    }
    if (containsExactly != null) {
      root = new MethodCallExpr("containsExactly", containsExactly).setScope(root);
    }
    if (containsExactlyList != null) {
      root = new MethodCallExpr("containsExactly").setArguments(new NodeList<>(containsExactlyList)).setScope(root);
    }
    if (containsExactlyInAnyOrder != null) {
      root = new MethodCallExpr("containsExactlyInAnyOrder", containsExactlyInAnyOrder).setScope(root);
    }
    if (containsExactlyInAnyOrderList != null) {
      root = new MethodCallExpr("containsExactlyInAnyOrder").setArguments(new NodeList<>(containsExactlyInAnyOrderList))
                                                            .setScope(root);
    }
    if (doesNotContain != null) {
      root = new MethodCallExpr("doesNotContain", doesNotContain).setScope(root);
    }
    if (doesNotContainList != null) {
      root = new MethodCallExpr("doesNotContain").setArguments(new NodeList<>(doesNotContainList)).setScope(root);
    }
    if (startsWith != null) {
      root = new MethodCallExpr("startsWith", startsWith).setScope(root);
    }
    if (doesNotStartWith != null) {
      root = new MethodCallExpr("doesNotStartWith", doesNotStartWith).setScope(root);
    }
    if (endsWith != null) {
      root = new MethodCallExpr("endsWith", endsWith).setScope(root);
    }
    if (doesNotEndWith != null) {
      root = new MethodCallExpr("doesNotEndWith", doesNotEndWith).setScope(root);
    }
    if (satisfiesHamcrestMatcher != null) {
      root = new MethodCallExpr("satisfies", new MethodCallExpr("matching", satisfiesHamcrestMatcher)).setScope(root);
    }
    if (isGreaterThan != null) {
      root = new MethodCallExpr("isGreaterThan", isGreaterThan).setScope(root);
    }
    if (isGreaterThanOrEqualTo != null) {
      root = new MethodCallExpr("isGreaterThanOrEqualTo", isGreaterThanOrEqualTo).setScope(root);
    }
    if (isLessThan != null) {
      root = new MethodCallExpr("isLessThan", isLessThan).setScope(root);
    }
    if (isLessThanOrEqualTo != null) {
      root = new MethodCallExpr("isLessThanOrEqualTo", isLessThanOrEqualTo).setScope(root);
    }
    if (hasMessage != null) {
      root = new MethodCallExpr("hasMessage", hasMessage).setScope(root);
    }
    if (isPresent) {
      root = new MethodCallExpr(root, "isPresent");
    }
    if (isNotPresent) {
      root = new MethodCallExpr(root, "isNotPresent");
    }
    if (isEmpty) {
      root = new MethodCallExpr(root, "isEmpty");
    }
    if (isNotEmpty) {
      root = new MethodCallExpr(root, "isNotEmpty");
    }
    if (containsKey != null) {
      root = new MethodCallExpr("containsKey", containsKey).setScope(root);
    }
    if (doesNotContainKey != null) {
      root = new MethodCallExpr("doesNotContainKey", doesNotContainKey).setScope(root);
    }
    if (containsValue != null) {
      root = new MethodCallExpr("containsValue", containsValue).setScope(root);
    }
    if (doesNotContainValue != null) {
      root = new MethodCallExpr("doesNotContainValue", doesNotContainValue).setScope(root);
    }

    return root;
  }
}
