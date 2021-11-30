package com.slinkydeveloper.assertjmigrator.migrations.assertstmt;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.slinkydeveloper.assertjmigrator.Migration;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;

import java.util.Collections;
import java.util.List;

public class JavaAssertEquality implements Migration<AssertStmt> {

    @Override
    public Class<AssertStmt> matchedNode() {
        return AssertStmt.class;
    }

    @Override
    public boolean matches(AssertStmt assertStmt) {
        return assertStmt.getCheck().isBinaryExpr() &&
                assertStmt.getCheck().asBinaryExpr().getOperator() == BinaryExpr.Operator.EQUALS;
    }

    @Override
    public void migrate(AssertStmt assertStmt) {
        BinaryExpr expr = assertStmt.getCheck().asBinaryExpr();
        assertStmt.replace(new ExpressionStmt(
                AssertJBuilder.create().assertThat(expr.getLeft()).isEqualTo(expr.getRight()).build()
        ));
    }

    @Override
    public List<String> requiredImports() {
        return Collections.singletonList("org.assertj.core.api.Assertions.assertThat");
    }

    @Override
    public String toString() {
        return "Java assert equality";
    }
}
