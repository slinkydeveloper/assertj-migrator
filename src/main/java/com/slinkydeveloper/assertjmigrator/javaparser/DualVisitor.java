/*
 * Copyright (C) 2007-2010 Júlio Vilmar Gesser.
 * Copyright (C) 2011, 2013-2021 The JavaParser Team.
 *
 * This file is part of JavaParser.
 *
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * b) the terms of the Apache License
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 */
package com.slinkydeveloper.assertjmigrator.javaparser;

import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.modules.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.visitor.VoidVisitor;

/**
 * A visitor that calculates deep node equality by comparing all properties and child nodes of the node.
 * <p>
 * This is copy-pasted from javaparser but makes the constructor public so we can extend it.
 *
 * @author Julio Vilmar Gesser
 */
public class DualVisitor implements VoidVisitor<Visitable> {

  <T extends Node> void visitNodes(final List<T> nodes1, final List<T> nodes2) {
    if (nodes1 == null || nodes2 == null) {
      return;
    }
    for (int i = 0; i < Math.max(nodes1.size(), nodes2.size()); i++) {
      try {
        visitNode(nodes1.get(i), nodes2.get(i));
      } catch (IndexOutOfBoundsException e) {
        return;
      }
    }
  }

  private <T extends Node> void visitNode(final T n, final T n2) {
    if (n == null || n2 == null) {
      return;
    }
    if (n.getClass() != AssertStmt.class && n.getClass() != n2.getClass()) {
      return;
    }
    n.accept(this, n2);
  }

  <T extends Node> void visitNode(final Optional<T> n, final Optional<T> n2) {
    visitNode(n.orElse(null), n2.orElse(null));
  }

  private <T extends Node> void visitNodes(final Optional<NodeList<T>> n, final Optional<NodeList<T>> n2) {
    visitNodes(n.orElse(null), n2.orElse(null));
  }

  @Override
  public void visit(final CompilationUnit n, final Visitable arg) {
    final CompilationUnit n2 = (CompilationUnit) arg;
    visitNodes(n.getImports(), n2.getImports());
    visitNode(n.getModule(), n2.getModule());
    visitNode(n.getPackageDeclaration(), n2.getPackageDeclaration());
    visitNodes(n.getTypes(), n2.getTypes());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final PackageDeclaration n, final Visitable arg) {
    final PackageDeclaration n2 = (PackageDeclaration) arg;
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getName(), n2.getName());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final TypeParameter n, final Visitable arg) {
    final TypeParameter n2 = (TypeParameter) arg;
    visitNode(n.getName(), n2.getName());
    visitNodes(n.getTypeBound(), n2.getTypeBound());
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final LineComment n, final Visitable arg) {
    final LineComment n2 = (LineComment) arg;
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final BlockComment n, final Visitable arg) {
    final BlockComment n2 = (BlockComment) arg;
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final ClassOrInterfaceDeclaration n, final Visitable arg) {
    final ClassOrInterfaceDeclaration n2 = (ClassOrInterfaceDeclaration) arg;
    visitNodes(n.getExtendedTypes(), n2.getExtendedTypes());
    visitNodes(n.getImplementedTypes(), n2.getImplementedTypes());
    visitNodes(n.getTypeParameters(), n2.getTypeParameters());
    visitNodes(n.getMembers(), n2.getMembers());
    visitNodes(n.getModifiers(), n2.getModifiers());
    visitNode(n.getName(), n2.getName());
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final EnumDeclaration n, final Visitable arg) {
    final EnumDeclaration n2 = (EnumDeclaration) arg;
    visitNodes(n.getEntries(), n2.getEntries());
    visitNodes(n.getImplementedTypes(), n2.getImplementedTypes());
    visitNodes(n.getMembers(), n2.getMembers());
    visitNodes(n.getModifiers(), n2.getModifiers());
    visitNode(n.getName(), n2.getName());
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final EnumConstantDeclaration n, final Visitable arg) {
    final EnumConstantDeclaration n2 = (EnumConstantDeclaration) arg;
    visitNodes(n.getArguments(), n2.getArguments());
    visitNodes(n.getClassBody(), n2.getClassBody());
    visitNode(n.getName(), n2.getName());
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final AnnotationDeclaration n, final Visitable arg) {
    final AnnotationDeclaration n2 = (AnnotationDeclaration) arg;
    visitNodes(n.getMembers(), n2.getMembers());
    visitNodes(n.getModifiers(), n2.getModifiers());
    visitNode(n.getName(), n2.getName());
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final AnnotationMemberDeclaration n, final Visitable arg) {
    final AnnotationMemberDeclaration n2 = (AnnotationMemberDeclaration) arg;
    visitNode(n.getDefaultValue(), n2.getDefaultValue());
    visitNodes(n.getModifiers(), n2.getModifiers());
    visitNode(n.getName(), n2.getName());
    visitNode(n.getType(), n2.getType());
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final FieldDeclaration n, final Visitable arg) {
    final FieldDeclaration n2 = (FieldDeclaration) arg;
    visitNodes(n.getModifiers(), n2.getModifiers());
    visitNodes(n.getVariables(), n2.getVariables());
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final VariableDeclarator n, final Visitable arg) {
    final VariableDeclarator n2 = (VariableDeclarator) arg;
    visitNode(n.getInitializer(), n2.getInitializer());
    visitNode(n.getName(), n2.getName());
    visitNode(n.getType(), n2.getType());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final ConstructorDeclaration n, final Visitable arg) {
    final ConstructorDeclaration n2 = (ConstructorDeclaration) arg;
    visitNode(n.getBody(), n2.getBody());
    visitNodes(n.getModifiers(), n2.getModifiers());
    visitNode(n.getName(), n2.getName());
    visitNodes(n.getParameters(), n2.getParameters());
    visitNode(n.getReceiverParameter(), n2.getReceiverParameter());
    visitNodes(n.getThrownExceptions(), n2.getThrownExceptions());
    visitNodes(n.getTypeParameters(), n2.getTypeParameters());
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final MethodDeclaration n, final Visitable arg) {
    final MethodDeclaration n2 = (MethodDeclaration) arg;
    visitNode(n.getBody(), n2.getBody());
    visitNode(n.getType(), n2.getType());
    visitNodes(n.getModifiers(), n2.getModifiers());
    visitNode(n.getName(), n2.getName());
    visitNodes(n.getParameters(), n2.getParameters());
    visitNode(n.getReceiverParameter(), n2.getReceiverParameter());
    visitNodes(n.getThrownExceptions(), n2.getThrownExceptions());
    visitNodes(n.getTypeParameters(), n2.getTypeParameters());
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final Parameter n, final Visitable arg) {
    final Parameter n2 = (Parameter) arg;
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNodes(n.getModifiers(), n2.getModifiers());
    visitNode(n.getName(), n2.getName());
    visitNode(n.getType(), n2.getType());
    visitNodes(n.getVarArgsAnnotations(), n2.getVarArgsAnnotations());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final InitializerDeclaration n, final Visitable arg) {
    final InitializerDeclaration n2 = (InitializerDeclaration) arg;
    visitNode(n.getBody(), n2.getBody());
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final JavadocComment n, final Visitable arg) {
    final JavadocComment n2 = (JavadocComment) arg;
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final ClassOrInterfaceType n, final Visitable arg) {
    final ClassOrInterfaceType n2 = (ClassOrInterfaceType) arg;
    visitNode(n.getName(), n2.getName());
    visitNode(n.getScope(), n2.getScope());
    visitNodes(n.getTypeArguments(), n2.getTypeArguments());
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final PrimitiveType n, final Visitable arg) {
    final PrimitiveType n2 = (PrimitiveType) arg;
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final ArrayType n, final Visitable arg) {
    final ArrayType n2 = (ArrayType) arg;
    visitNode(n.getComponentType(), n2.getComponentType());
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final ArrayCreationLevel n, final Visitable arg) {
    final ArrayCreationLevel n2 = (ArrayCreationLevel) arg;
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getDimension(), n2.getDimension());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final IntersectionType n, final Visitable arg) {
    final IntersectionType n2 = (IntersectionType) arg;
    visitNodes(n.getElements(), n2.getElements());
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final UnionType n, final Visitable arg) {
    final UnionType n2 = (UnionType) arg;
    visitNodes(n.getElements(), n2.getElements());
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final VoidType n, final Visitable arg) {
    final VoidType n2 = (VoidType) arg;
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final WildcardType n, final Visitable arg) {
    final WildcardType n2 = (WildcardType) arg;
    visitNode(n.getExtendedType(), n2.getExtendedType());
    visitNode(n.getSuperType(), n2.getSuperType());
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final UnknownType n, final Visitable arg) {
    final UnknownType n2 = (UnknownType) arg;
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final ArrayAccessExpr n, final Visitable arg) {
    final ArrayAccessExpr n2 = (ArrayAccessExpr) arg;
    visitNode(n.getIndex(), n2.getIndex());
    visitNode(n.getName(), n2.getName());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final ArrayCreationExpr n, final Visitable arg) {
    final ArrayCreationExpr n2 = (ArrayCreationExpr) arg;
    visitNode(n.getElementType(), n2.getElementType());
    visitNode(n.getInitializer(), n2.getInitializer());
    visitNodes(n.getLevels(), n2.getLevels());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final ArrayInitializerExpr n, final Visitable arg) {
    final ArrayInitializerExpr n2 = (ArrayInitializerExpr) arg;
    visitNodes(n.getValues(), n2.getValues());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final AssignExpr n, final Visitable arg) {
    final AssignExpr n2 = (AssignExpr) arg;
    visitNode(n.getTarget(), n2.getTarget());
    visitNode(n.getValue(), n2.getValue());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final BinaryExpr n, final Visitable arg) {
    final BinaryExpr n2 = (BinaryExpr) arg;
    visitNode(n.getLeft(), n2.getLeft());
    visitNode(n.getRight(), n2.getRight());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final CastExpr n, final Visitable arg) {
    final CastExpr n2 = (CastExpr) arg;
    visitNode(n.getExpression(), n2.getExpression());
    visitNode(n.getType(), n2.getType());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final ClassExpr n, final Visitable arg) {
    final ClassExpr n2 = (ClassExpr) arg;
    visitNode(n.getType(), n2.getType());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final ConditionalExpr n, final Visitable arg) {
    final ConditionalExpr n2 = (ConditionalExpr) arg;
    visitNode(n.getCondition(), n2.getCondition());
    visitNode(n.getElseExpr(), n2.getElseExpr());
    visitNode(n.getThenExpr(), n2.getThenExpr());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final EnclosedExpr n, final Visitable arg) {
    final EnclosedExpr n2 = (EnclosedExpr) arg;
    visitNode(n.getInner(), n2.getInner());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final FieldAccessExpr n, final Visitable arg) {
    final FieldAccessExpr n2 = (FieldAccessExpr) arg;
    visitNode(n.getName(), n2.getName());
    visitNode(n.getScope(), n2.getScope());
    visitNodes(n.getTypeArguments(), n2.getTypeArguments());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final InstanceOfExpr n, final Visitable arg) {
    final InstanceOfExpr n2 = (InstanceOfExpr) arg;
    visitNode(n.getExpression(), n2.getExpression());
    visitNode(n.getPattern(), n2.getPattern());
    visitNode(n.getType(), n2.getType());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final StringLiteralExpr n, final Visitable arg) {
    final StringLiteralExpr n2 = (StringLiteralExpr) arg;
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final IntegerLiteralExpr n, final Visitable arg) {
    final IntegerLiteralExpr n2 = (IntegerLiteralExpr) arg;
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final LongLiteralExpr n, final Visitable arg) {
    final LongLiteralExpr n2 = (LongLiteralExpr) arg;
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final CharLiteralExpr n, final Visitable arg) {
    final CharLiteralExpr n2 = (CharLiteralExpr) arg;
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final DoubleLiteralExpr n, final Visitable arg) {
    final DoubleLiteralExpr n2 = (DoubleLiteralExpr) arg;
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final BooleanLiteralExpr n, final Visitable arg) {
    final BooleanLiteralExpr n2 = (BooleanLiteralExpr) arg;
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final NullLiteralExpr n, final Visitable arg) {
    final NullLiteralExpr n2 = (NullLiteralExpr) arg;
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final MethodCallExpr n, final Visitable arg) {
    final MethodCallExpr n2 = (MethodCallExpr) arg;
    visitNodes(n.getArguments(), n2.getArguments());
    visitNode(n.getName(), n2.getName());
    visitNode(n.getScope(), n2.getScope());
    visitNodes(n.getTypeArguments(), n2.getTypeArguments());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final NameExpr n, final Visitable arg) {
    final NameExpr n2 = (NameExpr) arg;
    visitNode(n.getName(), n2.getName());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final ObjectCreationExpr n, final Visitable arg) {
    final ObjectCreationExpr n2 = (ObjectCreationExpr) arg;
    visitNodes(n.getAnonymousClassBody(), n2.getAnonymousClassBody());
    visitNodes(n.getArguments(), n2.getArguments());
    visitNode(n.getScope(), n2.getScope());
    visitNode(n.getType(), n2.getType());
    visitNodes(n.getTypeArguments(), n2.getTypeArguments());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final Name n, final Visitable arg) {
    final Name n2 = (Name) arg;
    visitNode(n.getQualifier(), n2.getQualifier());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final SimpleName n, final Visitable arg) {
    final SimpleName n2 = (SimpleName) arg;
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final ThisExpr n, final Visitable arg) {
    final ThisExpr n2 = (ThisExpr) arg;
    visitNode(n.getTypeName(), n2.getTypeName());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final SuperExpr n, final Visitable arg) {
    final SuperExpr n2 = (SuperExpr) arg;
    visitNode(n.getTypeName(), n2.getTypeName());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final UnaryExpr n, final Visitable arg) {
    final UnaryExpr n2 = (UnaryExpr) arg;
    visitNode(n.getExpression(), n2.getExpression());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final VariableDeclarationExpr n, final Visitable arg) {
    final VariableDeclarationExpr n2 = (VariableDeclarationExpr) arg;
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNodes(n.getModifiers(), n2.getModifiers());
    visitNodes(n.getVariables(), n2.getVariables());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final MarkerAnnotationExpr n, final Visitable arg) {
    final MarkerAnnotationExpr n2 = (MarkerAnnotationExpr) arg;
    visitNode(n.getName(), n2.getName());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final SingleMemberAnnotationExpr n, final Visitable arg) {
    final SingleMemberAnnotationExpr n2 = (SingleMemberAnnotationExpr) arg;
    visitNode(n.getMemberValue(), n2.getMemberValue());
    visitNode(n.getName(), n2.getName());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final NormalAnnotationExpr n, final Visitable arg) {
    final NormalAnnotationExpr n2 = (NormalAnnotationExpr) arg;
    visitNodes(n.getPairs(), n2.getPairs());
    visitNode(n.getName(), n2.getName());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final MemberValuePair n, final Visitable arg) {
    final MemberValuePair n2 = (MemberValuePair) arg;
    visitNode(n.getName(), n2.getName());
    visitNode(n.getValue(), n2.getValue());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final ExplicitConstructorInvocationStmt n, final Visitable arg) {
    final ExplicitConstructorInvocationStmt n2 = (ExplicitConstructorInvocationStmt) arg;
    visitNodes(n.getArguments(), n2.getArguments());
    visitNode(n.getExpression(), n2.getExpression());
    visitNodes(n.getTypeArguments(), n2.getTypeArguments());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final LocalClassDeclarationStmt n, final Visitable arg) {
    final LocalClassDeclarationStmt n2 = (LocalClassDeclarationStmt) arg;
    visitNode(n.getClassDeclaration(), n2.getClassDeclaration());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final LocalRecordDeclarationStmt n, final Visitable arg) {
    final LocalRecordDeclarationStmt n2 = (LocalRecordDeclarationStmt) arg;
    visitNode(n.getRecordDeclaration(), n2.getRecordDeclaration());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final AssertStmt n, final Visitable arg) {
    final AssertStmt n2 = (AssertStmt) arg;
    visitNode(n.getCheck(), n2.getCheck());
    visitNode(n.getMessage(), n2.getMessage());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final BlockStmt n, final Visitable arg) {
    final BlockStmt n2 = (BlockStmt) arg;
    visitNodes(n.getStatements(), n2.getStatements());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final LabeledStmt n, final Visitable arg) {
    final LabeledStmt n2 = (LabeledStmt) arg;
    visitNode(n.getLabel(), n2.getLabel());
    visitNode(n.getStatement(), n2.getStatement());
    visitNode(n.getComment(), n2.getComment());
  }

  @Override
  public void visit(final EmptyStmt n, final Visitable arg) {
    final EmptyStmt n2 = (EmptyStmt) arg;
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final ExpressionStmt n, final Visitable arg) {
    final ExpressionStmt n2 = (ExpressionStmt) arg;
    visitNode(n.getExpression(), n2.getExpression());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final SwitchStmt n, final Visitable arg) {
    final SwitchStmt n2 = (SwitchStmt) arg;
    visitNodes(n.getEntries(), n2.getEntries());
    visitNode(n.getSelector(), n2.getSelector());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final SwitchEntry n, final Visitable arg) {
    final SwitchEntry n2 = (SwitchEntry) arg;
    visitNodes(n.getLabels(), n2.getLabels());
    visitNodes(n.getStatements(), n2.getStatements());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final BreakStmt n, final Visitable arg) {
    final BreakStmt n2 = (BreakStmt) arg;
    visitNode(n.getLabel(), n2.getLabel());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final ReturnStmt n, final Visitable arg) {
    final ReturnStmt n2 = (ReturnStmt) arg;
    visitNode(n.getExpression(), n2.getExpression());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final IfStmt n, final Visitable arg) {
    final IfStmt n2 = (IfStmt) arg;
    visitNode(n.getCondition(), n2.getCondition());
    visitNode(n.getElseStmt(), n2.getElseStmt());
    visitNode(n.getThenStmt(), n2.getThenStmt());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final WhileStmt n, final Visitable arg) {
    final WhileStmt n2 = (WhileStmt) arg;
    visitNode(n.getBody(), n2.getBody());
    visitNode(n.getCondition(), n2.getCondition());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final ContinueStmt n, final Visitable arg) {
    final ContinueStmt n2 = (ContinueStmt) arg;
    visitNode(n.getLabel(), n2.getLabel());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final DoStmt n, final Visitable arg) {
    final DoStmt n2 = (DoStmt) arg;
    visitNode(n.getBody(), n2.getBody());
    visitNode(n.getCondition(), n2.getCondition());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final ForEachStmt n, final Visitable arg) {
    final ForEachStmt n2 = (ForEachStmt) arg;
    visitNode(n.getBody(), n2.getBody());
    visitNode(n.getIterable(), n2.getIterable());
    visitNode(n.getVariable(), n2.getVariable());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final ForStmt n, final Visitable arg) {
    final ForStmt n2 = (ForStmt) arg;
    visitNode(n.getBody(), n2.getBody());
    visitNode(n.getCompare(), n2.getCompare());
    visitNodes(n.getInitialization(), n2.getInitialization());
    visitNodes(n.getUpdate(), n2.getUpdate());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final ThrowStmt n, final Visitable arg) {
    final ThrowStmt n2 = (ThrowStmt) arg;
    visitNode(n.getExpression(), n2.getExpression());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final SynchronizedStmt n, final Visitable arg) {
    final SynchronizedStmt n2 = (SynchronizedStmt) arg;
    visitNode(n.getBody(), n2.getBody());
    visitNode(n.getExpression(), n2.getExpression());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final TryStmt n, final Visitable arg) {
    final TryStmt n2 = (TryStmt) arg;
    visitNodes(n.getCatchClauses(), n2.getCatchClauses());
    visitNode(n.getFinallyBlock(), n2.getFinallyBlock());
    visitNodes(n.getResources(), n2.getResources());
    visitNode(n.getTryBlock(), n2.getTryBlock());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final CatchClause n, final Visitable arg) {
    final CatchClause n2 = (CatchClause) arg;
    visitNode(n.getBody(), n2.getBody());
    visitNode(n.getParameter(), n2.getParameter());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final LambdaExpr n, final Visitable arg) {
    final LambdaExpr n2 = (LambdaExpr) arg;
    visitNode(n.getBody(), n2.getBody());
    visitNodes(n.getParameters(), n2.getParameters());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final MethodReferenceExpr n, final Visitable arg) {
    final MethodReferenceExpr n2 = (MethodReferenceExpr) arg;
    visitNode(n.getScope(), n2.getScope());
    visitNodes(n.getTypeArguments(), n2.getTypeArguments());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final TypeExpr n, final Visitable arg) {
    final TypeExpr n2 = (TypeExpr) arg;
    visitNode(n.getType(), n2.getType());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final ImportDeclaration n, final Visitable arg) {
    final ImportDeclaration n2 = (ImportDeclaration) arg;
    visitNode(n.getName(), n2.getName());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(NodeList n, Visitable arg) {
    visitNodes((NodeList<Node>) n, (NodeList<Node>) arg);
  }

  @Override
  public void visit(final ModuleDeclaration n, final Visitable arg) {
    final ModuleDeclaration n2 = (ModuleDeclaration) arg;
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNodes(n.getDirectives(), n2.getDirectives());
    visitNode(n.getName(), n2.getName());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final ModuleRequiresDirective n, final Visitable arg) {
    final ModuleRequiresDirective n2 = (ModuleRequiresDirective) arg;
    visitNodes(n.getModifiers(), n2.getModifiers());
    visitNode(n.getName(), n2.getName());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override()
  public void visit(final ModuleExportsDirective n, final Visitable arg) {
    final ModuleExportsDirective n2 = (ModuleExportsDirective) arg;
    visitNodes(n.getModuleNames(), n2.getModuleNames());
    visitNode(n.getName(), n2.getName());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override()
  public void visit(final ModuleProvidesDirective n, final Visitable arg) {
    final ModuleProvidesDirective n2 = (ModuleProvidesDirective) arg;
    visitNode(n.getName(), n2.getName());
    visitNodes(n.getWith(), n2.getWith());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override()
  public void visit(final ModuleUsesDirective n, final Visitable arg) {
    final ModuleUsesDirective n2 = (ModuleUsesDirective) arg;
    visitNode(n.getName(), n2.getName());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final ModuleOpensDirective n, final Visitable arg) {
    final ModuleOpensDirective n2 = (ModuleOpensDirective) arg;
    visitNodes(n.getModuleNames(), n2.getModuleNames());
    visitNode(n.getName(), n2.getName());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final UnparsableStmt n, final Visitable arg) {
    final UnparsableStmt n2 = (UnparsableStmt) arg;
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final ReceiverParameter n, final Visitable arg) {
    final ReceiverParameter n2 = (ReceiverParameter) arg;
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getName(), n2.getName());
    visitNode(n.getType(), n2.getType());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final VarType n, final Visitable arg) {
    final VarType n2 = (VarType) arg;
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final Modifier n, final Visitable arg) {
    final Modifier n2 = (Modifier) arg;
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final SwitchExpr n, final Visitable arg) {
    final SwitchExpr n2 = (SwitchExpr) arg;
    visitNodes(n.getEntries(), n2.getEntries());
    visitNode(n.getSelector(), n2.getSelector());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final YieldStmt n, final Visitable arg) {
    final YieldStmt n2 = (YieldStmt) arg;
    visitNode(n.getExpression(), n2.getExpression());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final TextBlockLiteralExpr n, final Visitable arg) {
    final TextBlockLiteralExpr n2 = (TextBlockLiteralExpr) arg;
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final PatternExpr n, final Visitable arg) {
    final PatternExpr n2 = (PatternExpr) arg;
    visitNode(n.getName(), n2.getName());
    visitNode(n.getType(), n2.getType());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final RecordDeclaration n, final Visitable arg) {
    final RecordDeclaration n2 = (RecordDeclaration) arg;
    visitNodes(n.getImplementedTypes(), n2.getImplementedTypes());
    visitNodes(n.getParameters(), n2.getParameters());
    visitNode(n.getReceiverParameter(), n2.getReceiverParameter());
    visitNodes(n.getTypeParameters(), n2.getTypeParameters());
    visitNodes(n.getMembers(), n2.getMembers());
    visitNodes(n.getModifiers(), n2.getModifiers());
    visitNode(n.getName(), n2.getName());
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());

  }

  @Override
  public void visit(final CompactConstructorDeclaration n, final Visitable arg) {
    final CompactConstructorDeclaration n2 = (CompactConstructorDeclaration) arg;
    visitNode(n.getBody(), n2.getBody());
    visitNodes(n.getModifiers(), n2.getModifiers());
    visitNode(n.getName(), n2.getName());
    visitNodes(n.getThrownExceptions(), n2.getThrownExceptions());
    visitNodes(n.getTypeParameters(), n2.getTypeParameters());
    visitNodes(n.getAnnotations(), n2.getAnnotations());
    visitNode(n.getComment(), n2.getComment());

  }
}
