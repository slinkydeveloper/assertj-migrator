package com.slinkydeveloper.assertjmigrator;

import java.nio.file.Path;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.expr.Name;
import com.slinkydeveloper.assertjmigrator.migrations.NodeMatch;

class MatchedCompilationUnit {

  private final Path path;
  private final CompilationUnit compilationUnit;
  private final List<NodeMatch> matchedMigrations;

  public MatchedCompilationUnit(Path path, CompilationUnit compilationUnit, List<NodeMatch> matchedMigrations) {
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

  public List<NodeMatch> getMatchedMigrations() {
    return matchedMigrations;
  }

  /**
   * Execute the migration on this assertion match.
   */
  public void migrate() {
    getMatchedMigrations().forEach(migrationEntry -> {
      try {
        // Migrations always mutate the compilation unit, they don't return new nodes
        migrationEntry.executeMigration();
      } catch (Throwable e) {
        throw new RuntimeException(
                                   String.format("Error while trying to execute migration '%s' in file '%s' on code:\n%s",
                                                 migrationEntry.toString(), path, migrationEntry.getNode()),
                                   e);
      }
    });

    getMatchedMigrations()
                          .stream()
                          .flatMap(migrationEntry -> migrationEntry.getRequiredImports().stream())
                          .distinct()
                          .forEach(methodIdentifier -> addStaticImport(compilationUnit, methodIdentifier));

    // Remove imports
    compilationUnit.getImports().removeIf(importDeclaration -> {
      if (importDeclaration.isStatic()) {
        String importName = importDeclaration.getNameAsString();
        if (importDeclaration.isAsterisk()) {
          if (importName.equals(org.junit.Assert.class.getName())) {
            return true;
          }
          if (importName.equals(org.junit.jupiter.api.Assertions.class.getName())) {
            return true;
          }
        }
        // assert methods
        if (importName.startsWith(org.junit.Assert.class.getName() + ".assert")) {
          return true;
        }
        if (importName.startsWith(org.junit.jupiter.api.Assertions.class.getName() + ".assert")) {
          return true;
        }
        if (importName.startsWith(org.hamcrest.MatcherAssert.class.getName() + ".assertThat")) {
          return true;
        }
        // fail methods
        if (importName.startsWith(org.junit.Assert.class.getName() + ".fail")) {
          return true;
        }
        if (importName.startsWith(org.junit.jupiter.api.Assertions.class.getName() + ".fail")) {
          return true;
        }
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
