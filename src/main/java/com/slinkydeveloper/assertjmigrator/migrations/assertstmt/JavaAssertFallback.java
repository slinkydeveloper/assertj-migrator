package com.slinkydeveloper.assertjmigrator.migrations.assertstmt;

import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.slinkydeveloper.assertjmigrator.migrations.MigrationRule;
import com.slinkydeveloper.assertjmigrator.migrations.common.PredicateMigrator;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class JavaAssertFallback implements MigrationRule<AssertStmt> {

    @Override
    public Class<AssertStmt> matchedNode() {
        return AssertStmt.class;
    }

    @Override
    public Predicate<AssertStmt> predicate() {
        return x -> true;
    }

    @Override
    public void migrate(AssertStmt node) {
        node.replace(new ExpressionStmt(
                PredicateMigrator.migrateTrue(new AssertJBuilder(), node.getCheck())
                        .build()
        ));
    }

    @Override
    public List<String> requiredImports() {
        return Collections.singletonList("org.assertj.core.api.Assertions.assertThat");
    }

    @Override
    public String toString() {
        return "Java assert fallback";
    }
}
