package com.slinkydeveloper.assertjmigrator.migrations;

import com.github.javaparser.ast.Node;

import java.util.List;
import java.util.function.Predicate;

public interface MigrationRule<T extends Node> {

    Class<T> matchedNode();

    Predicate<? super T> predicate();

    void migrate(T node);

    List<String> requiredImports();

}
