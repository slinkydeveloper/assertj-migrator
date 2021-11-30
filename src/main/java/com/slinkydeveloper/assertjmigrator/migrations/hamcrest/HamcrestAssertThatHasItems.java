package com.slinkydeveloper.assertjmigrator.migrations.hamcrest;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;

public class HamcrestAssertThatHasItems extends BaseHamcrestAssertThat {

    @Override
    String matcherName() {
        return "hasItems";
    }

    @Override
    boolean matcherVariadic() {
        return true;
    }

    @Override
    int matcherArity() {
        return 0;
    }

    @Override
    void fillBuilder(AssertJBuilder builder, Expression actual, MethodCallExpr matcher) {
        builder.assertThat(actual).contains(matcher.getArguments());
    }

}
