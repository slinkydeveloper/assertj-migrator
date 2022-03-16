package com.slinkydeveloper.assertjmigrator.migrations.junit;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;

public class JUnitAssertArrayEquals extends BaseJUnitAssertion {

  @Override
  protected String assertionName() {
    return "assertArrayEquals";
  }

  @Override
  protected int assertionArity() {
    return 2;
  }

  @Override
  protected void fillBuilder(AssertJBuilder builder, MethodCallExpr expr) {
    builder.assertThat(expr.getArgument(1)).isEqualTo(expr.getArgument(0));
  }

}
