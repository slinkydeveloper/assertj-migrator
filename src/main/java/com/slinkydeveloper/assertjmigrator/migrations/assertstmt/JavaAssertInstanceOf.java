package com.slinkydeveloper.assertjmigrator.migrations.assertstmt;

import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.slinkydeveloper.assertjmigrator.Migration;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;

import java.util.Collections;
import java.util.List;

public class JavaAssertInstanceOf implements Migration<AssertStmt> {

    @Override
    public Class<AssertStmt> matchedNode() {
        return AssertStmt.class;
    }

    @Override
    public boolean matches(AssertStmt assertStmt) {
        return assertStmt.getCheck().isInstanceOfExpr();
    }

    @Override
    public void migrate(AssertStmt node) {
        InstanceOfExpr instanceOfExpr = node.getCheck().asInstanceOfExpr();
        node.replace(new ExpressionStmt(
                AssertJBuilder.create().assertThat(instanceOfExpr.getExpression())
                        .isInstanceOf(instanceOfExpr.getType())
                        .build()
        ));
    }

    @Override
    public List<String> requiredImports() {
        return Collections.singletonList("org.assertj.core.api.Assertions.assertThat");
    }

    @Override
    public String toString() {
        return "Java assert instanceof";
    }
}
