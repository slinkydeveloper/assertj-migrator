package com.slinkydeveloper.assertjmigrator.migrations.junit;

import static com.slinkydeveloper.assertjmigrator.nodes.Predicates.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;
import com.slinkydeveloper.assertjmigrator.nodes.Predicates;

public class JUnitAssertEqualsWithDelta extends BaseJUnitAssertion {

  @Override
  protected String assertionName() {
    return "assertEquals";
  }

  @Override
  protected int assertionArity() {
    return 3;
  }

  @Override
  protected Predicate<Expression> additionalPredicate(int offset) {
    return methodArgMatches(offset + 2, or(Predicates::isFloat, Predicates::isDouble, Expression::isIntegerLiteralExpr));
  }

  @Override
  protected void fillBuilder(AssertJBuilder builder, MethodCallExpr node) {
    builder.assertThat(node.getArgument(1));
    Expression offsetArg = node.getArgument(2);
    if (isLiteralEqualToZero(offsetArg)) {
      // Assert equals is enough in this case
      builder.isEqualTo(node.getArgument(0));
      return;
    }
    builder.isCloseTo(node.getArgument(0), node.getArgument(2));
  }

  @Override
  public List<String> requiredImports() {
    return Arrays.asList("org.assertj.core.api.Assertions.assertThat", "org.assertj.core.api.Assertions.within");
  }

  @Override
  public String toString() {
    return "JUnit 4/5 assertEquals with delta";
  }
}
