package com.slinkydeveloper.assertjmigrator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Name;
import com.slinkydeveloper.assertjmigrator.migrations.MigrationRule;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class MatchedCompilationUnit {

    private final Path path;
    private final CompilationUnit compilationUnit;
    private final List<Map.Entry<MigrationRule<Node>, Node>> matchedMigrations;

    public MatchedCompilationUnit(Path path, CompilationUnit compilationUnit, List<Map.Entry<MigrationRule<Node>, Node>> matchedMigrations) {
        this.path = path;
        this.compilationUnit = compilationUnit;
        this.matchedMigrations = matchedMigrations;
    }

    public Path getPath() {
        return path;
    }

    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public List<Map.Entry<MigrationRule<Node>, Node>> getMatchedMigrations() {
        return matchedMigrations;
    }

    /**
     * Execute the migration on this assertion match.
     */
    public void migrate() {
        getMatchedMigrations().forEach(migrationEntry -> {
            try {
                migrationEntry.getKey().migrate(migrationEntry.getValue());
            } catch (Throwable e) {
                throw new RuntimeException(
                        String.format("Error while trying to execute migration '%s' in file '%s' on code:\n%s", migrationEntry.getKey(), path, migrationEntry.getValue().toString()), e);
            }
        });

        getMatchedMigrations()
                .stream()
                .flatMap(migrationEntry -> migrationEntry.getKey().requiredImports().stream())
                .distinct()
                .forEach(methodIdentifier -> addStaticImport(compilationUnit, methodIdentifier));

        // Remove imports
        compilationUnit.getImports().removeIf(importDeclaration -> {
            // Asterisk imports
            if (importDeclaration.isStatic() && importDeclaration.isAsterisk() && importDeclaration.getNameAsString().equals(org.junit.Assert.class.getName())) {
                return true;
            }
            if (importDeclaration.isStatic() && importDeclaration.isAsterisk() && importDeclaration.getNameAsString().equals(org.junit.jupiter.api.Assertions.class.getName())) {
                return true;
            }
            // assert methods
            if (importDeclaration.isStatic() && importDeclaration.getNameAsString().startsWith(org.junit.Assert.class.getName() + ".assert")) {
                return true;
            }
            if (importDeclaration.isStatic() && importDeclaration.getNameAsString().startsWith(org.junit.jupiter.api.Assertions.class.getName() + ".assert")) {
                return true;
            }
            if (importDeclaration.isStatic() && importDeclaration.getNameAsString().startsWith(org.hamcrest.MatcherAssert.class.getName() + ".assertThat")) {
                return true;
            }
            // fail methods
            if (importDeclaration.isStatic() && importDeclaration.getNameAsString().startsWith(org.junit.Assert.class.getName() + ".fail")) {
                return true;
            }
            if (importDeclaration.isStatic() && importDeclaration.getNameAsString().startsWith(org.junit.jupiter.api.Assertions.class.getName() + ".fail")) {
                return true;
            }
            return false;
        });

    }

    private void addStaticImport(CompilationUnit cu, String methodIdentifier) {
        int lastDotIndex = methodIdentifier.lastIndexOf('.');
        Name className = new Name(methodIdentifier.substring(0, lastDotIndex));
        Name methodName = new Name(className, methodIdentifier.substring(lastDotIndex + 1));

        cu.getImports().addFirst(new ImportDeclaration(methodName, true, false));
    }

}
