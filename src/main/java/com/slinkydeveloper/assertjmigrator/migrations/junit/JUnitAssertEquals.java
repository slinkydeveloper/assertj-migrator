package com.slinkydeveloper.assertjmigrator.migrations.junit;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.migrations.common.EqualityMigrator;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;

public class JUnitAssertEquals extends BaseJUnitAssertion {

  @Override
  protected String assertionName() {
    return "assertEquals";
  }

  @Override
  protected int assertionArity() {
    return 2;
  }

  @Override
  protected void fillBuilder(AssertJBuilder builder, MethodCallExpr expr) {
    EqualityMigrator.migrateEquals(builder, expr.getArgument(1), expr.getArgument(0));
  }

}
