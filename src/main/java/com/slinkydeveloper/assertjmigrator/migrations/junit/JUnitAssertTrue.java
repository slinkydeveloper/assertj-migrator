package com.slinkydeveloper.assertjmigrator.migrations.junit;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.migrations.common.PredicateMigrator;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;

public class JUnitAssertTrue extends BaseJUnitAssertion {

  @Override
  protected String assertionName() {
    return "assertTrue";
  }

  @Override
  protected int assertionArity() {
    return 1;
  }

  @Override
  protected void fillBuilder(AssertJBuilder builder, MethodCallExpr expr) {
    PredicateMigrator.migrateTrue(builder, expr.getArgument(0));
  }

}
