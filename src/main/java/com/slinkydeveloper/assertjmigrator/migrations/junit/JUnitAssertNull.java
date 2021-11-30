package com.slinkydeveloper.assertjmigrator.migrations.junit;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;

public class JUnitAssertNull extends BaseJUnitAssertion {

    @Override
    protected String assertionName() {
        return "assertNull";
    }

    @Override
    protected int assertionArity() {
        return 1;
    }

    @Override
    protected void fillBuilder(AssertJBuilder builder, MethodCallExpr expr) {
        builder.assertThat(expr.getArgument(0)).isNull();
    }

}
