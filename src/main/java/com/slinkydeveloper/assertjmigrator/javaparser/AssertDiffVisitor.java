package com.slinkydeveloper.assertjmigrator.javaparser;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.visitor.Visitable;

public class AssertDiffVisitor extends DualVisitor {

  private final List<Map.Entry<Node, Node>> differentNodes;

  public AssertDiffVisitor() {
    this.differentNodes = new ArrayList<>();
  }

  @Override
  public void visit(MethodCallExpr n, Visitable arg) {
    if (!n.equals(arg)) {
      this.differentNodes.add(new SimpleEntry<>(n, (Node) arg));
    }
  }

  @Override
  public void visit(AssertStmt n, Visitable arg) {
    if (!n.equals(arg)) {
      this.differentNodes.add(new SimpleEntry<>(n, (Node) arg));
    }
  }

  public List<Map.Entry<Node, Node>> getDifferentNodes() {
    return differentNodes;
  }
}
