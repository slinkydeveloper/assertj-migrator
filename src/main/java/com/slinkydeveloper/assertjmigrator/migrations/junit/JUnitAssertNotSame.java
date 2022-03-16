package com.slinkydeveloper.assertjmigrator.migrations.junit;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;

public class JUnitAssertNotSame extends BaseJUnitAssertion {

  @Override
  protected String assertionName() {
    return "assertNotSame";
  }

  @Override
  protected int assertionArity() {
    return 2;
  }

  @Override
  protected void fillBuilder(AssertJBuilder builder, MethodCallExpr expr) {
    builder.assertThat(expr.getArgument(1)).isNotSameAs(expr.getArgument(0));
  }

}
