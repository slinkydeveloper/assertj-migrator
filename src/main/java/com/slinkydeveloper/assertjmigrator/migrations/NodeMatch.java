package com.slinkydeveloper.assertjmigrator.migrations;

import com.github.javaparser.ast.Node;
import org.assertj.core.api.ThrowableAssert;

import java.util.Objects;
import java.util.Set;

public class NodeMatch {

    private final Node node;
    private final ThrowableAssert.ThrowingCallable migrationExecutor;
    private final String description;
    private final Set<String> requiredImports;

    public NodeMatch(Node node, ThrowableAssert.ThrowingCallable migrationExecutor, String description, Set<String> requiredImports) {
        this.node = node;
        this.migrationExecutor = migrationExecutor;
        this.description = description;
        this.requiredImports = requiredImports;
    }

    public Node getNode() {
        return node;
    }

    public void executeMigration() throws Throwable {
        this.migrationExecutor.call();
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getRequiredImports() {
        return requiredImports;
    }

    @Override
    public String toString() {
        return "NodeMatch{" +
                "node=" + node +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeMatch nodeMatch = (NodeMatch) o;
        return Objects.equals(node, nodeMatch.node) &&
                Objects.equals(migrationExecutor, nodeMatch.migrationExecutor) &&
                Objects.equals(description, nodeMatch.description) &&
                Objects.equals(requiredImports, nodeMatch.requiredImports);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, migrationExecutor, description, requiredImports);
    }
}
