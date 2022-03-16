package com.slinkydeveloper.assertjmigrator.migrations.junit;

import java.util.Collections;
import java.util.List;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;

public class JUnit5AssertDoesNotThrow extends BaseJUnitAssertion {

  @Override
  protected String assertionName() {
    return "assertDoesNotThrow";
  }

  @Override
  protected int assertionArity() {
    return 1;
  }

  @Override
  protected void fillBuilder(AssertJBuilder builder, MethodCallExpr expr) {
    builder.assertThatThrownBy(expr.getArgument(0)).isNull();
  }

  @Override
  public List<String> requiredImports() {
    return Collections.singletonList("org.assertj.core.api.Assertions.assertThatThrownBy");
  }
}
