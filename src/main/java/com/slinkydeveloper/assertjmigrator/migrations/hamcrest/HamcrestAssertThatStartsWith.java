package com.slinkydeveloper.assertjmigrator.migrations.hamcrest;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;

public class HamcrestAssertThatStartsWith extends BaseHamcrestAssertThat {

  @Override
  String matcherName() {
    return "startsWith";
  }

  @Override
  int matcherArity() {
    return 1;
  }

  @Override
  void fillBuilder(AssertJBuilder builder, Expression actual, MethodCallExpr matcher) {
    builder.assertThat(actual).startsWith(matcher.getArgument(0));
  }

}
