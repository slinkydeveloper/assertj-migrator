package com.slinkydeveloper.assertjmigrator;

import com.github.javaparser.ast.Node;

import java.util.List;

public interface Migration<T extends Node> {

    Class<T> matchedNode();

    boolean matches(T node);

    void migrate(T node);

    List<String> requiredImports();

}
