package com.slinkydeveloper.assertjmigrator.migrations.hamcrest;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.migrations.common.EqualityMigrator;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;

public class HamcrestAssertThatNotEqualTo extends BaseHamcrestAssertThatNot {

  @Override
  String matcherName() {
    return "equalTo";
  }

  @Override
  int matcherArity() {
    return 1;
  }

  @Override
  void fillBuilder(AssertJBuilder builder, Expression actual, MethodCallExpr matcher) {
    EqualityMigrator.migrateNotEquals(builder, actual, matcher.getArgument(0));
  }

}
