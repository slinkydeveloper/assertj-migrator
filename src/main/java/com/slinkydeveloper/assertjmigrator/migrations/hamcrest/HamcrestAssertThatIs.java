package com.slinkydeveloper.assertjmigrator.migrations.hamcrest;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.migrations.common.EqualityMigrator;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;

public class HamcrestAssertThatIs extends BaseHamcrestAssertThat {

    @Override
    String matcherName() {
        return "is";
    }

    @Override
    int matcherArity() {
        return 1;
    }

    @Override
    void fillBuilder(AssertJBuilder builder, Expression actual, MethodCallExpr matcher) {
        EqualityMigrator.migrateEquals(builder, actual, matcher.getArgument(0));
    }

}
