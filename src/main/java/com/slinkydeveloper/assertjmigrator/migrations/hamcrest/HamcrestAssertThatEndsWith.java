package com.slinkydeveloper.assertjmigrator.migrations.hamcrest;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;

public class HamcrestAssertThatEndsWith extends BaseHamcrestAssertThat {

  @Override
  String matcherName() {
    return "endsWith";
  }

  @Override
  int matcherArity() {
    return 1;
  }

  @Override
  void fillBuilder(AssertJBuilder builder, Expression actual, MethodCallExpr matcher) {
    builder.assertThat(actual).endsWith(matcher.getArgument(0));
  }

}
