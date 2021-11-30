package com.slinkydeveloper.assertjmigrator.migrations.junit;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;

public class JUnit5AssertInstanceOf extends BaseJUnitAssertion {

    @Override
    protected String assertionName() {
        return "assertInstanceOf";
    }

    @Override
    protected int assertionArity() {
        return 2;
    }

    @Override
    protected void fillBuilder(AssertJBuilder builder, MethodCallExpr expr) {
        builder.assertThat(expr.getArgument(1)).isInstanceOf(expr.getArgument(0));
    }

    @Override
    public String toString() {
        return "JUnit 5 assertInstanceOf";
    }
}
