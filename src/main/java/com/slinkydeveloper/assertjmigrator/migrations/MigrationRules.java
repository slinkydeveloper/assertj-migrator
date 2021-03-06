package com.slinkydeveloper.assertjmigrator.migrations;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.ast.Node;
import com.slinkydeveloper.assertjmigrator.migrations.assertstmt.JavaAssertFallback;
import com.slinkydeveloper.assertjmigrator.migrations.hamcrest.*;
import com.slinkydeveloper.assertjmigrator.migrations.junit.*;

/**
 * Contains the set of migrations that can be matched.
 */
public class MigrationRules {

  private static final List<MigrationRule<?>> DEFAULT_MIGRATION_RULES = new ArrayList<>();

  static {
    DEFAULT_MIGRATION_RULES.addAll(
                                   Arrays.asList(
                                                 // Java assert statement
                                                 new JavaAssertFallback(),

                                                 // JUnit 4/5 assertions
                                                 new JUnitAssertEqualsWithDelta(),
                                                 new JUnitAssertArrayEquals(),
                                                 new JUnitAssertEquals(),

                                                 new JUnitAssertNotEqualsWithDelta(),
                                                 new JUnitAssertNotEquals(),

                                                 new JUnitAssertTrue(),
                                                 new JUnitAssertFalse(),

                                                 new JUnitAssertNull(),
                                                 new JUnitAssertNotNull(),

                                                 new JUnitAssertSame(),
                                                 new JUnitAssertNotSame(),

                                                 new JUnitAssertThrows(),

                                                 // JUnit 5 specific
                                                 new JUnit5AssertDoesNotThrow(),
                                                 new JUnit5AssertInstanceOf(),

                                                 // Fail
                                                 new JUnitFail(),

                                                 // Hamcrest
                                                 new HamcrestAssertThatEqualTo(),
                                                 new HamcrestAssertThatNotEqualTo(),
                                                 new HamcrestAssertThatIs(),
                                                 new HamcrestAssertThatNotIs(),

                                                 new HamcrestAssertThatSameInstance(),
                                                 new HamcrestAssertThatNotSameInstance(),

                                                 new HamcrestAssertThatContainsString(),
                                                 new HamcrestAssertThatNotContainsString(),
                                                 new HamcrestAssertThatStartsWith(),
                                                 new HamcrestAssertThatNotStartsWith(),
                                                 new HamcrestAssertThatEndsWith(),
                                                 new HamcrestAssertThatNotEndsWith(),

                                                 new HamcrestAssertThatNotNullValueWithNestedNot(),
                                                 new HamcrestAssertThatNotNullValue(),
                                                 new HamcrestAssertThatNullValue(),

                                                 new HamcrestAssertThatHasItem(),
                                                 new HamcrestAssertThatNotHasItem(),
                                                 new HamcrestAssertThatHasItems(),
                                                 new HamcrestAssertThatNotHasItems(),

                                                 new HamcrestAssertThatInstanceOf(),
                                                 new HamcrestAssertThatNotInstanceOf(),

                                                 new HamcrestAssertThatGreaterThan(),
                                                 new HamcrestAssertThatGreaterThanOrEqualTo(),
                                                 new HamcrestAssertThatLessThan(),
                                                 new HamcrestAssertThatLessThanOrEqualTo(),

                                                 new HamcrestAssertThatFallbackMigration()));
  }

  private final Map<Class<? extends Node>, List<MigrationRule<? extends Node>>> nodeClassToRules;

  public MigrationRules() {
    this(DEFAULT_MIGRATION_RULES);
  }

  private MigrationRules(List<MigrationRule<?>> rules) {
    this.nodeClassToRules = rules
                                 .stream()
                                 .collect(Collectors.groupingBy(MigrationRule::matchedNode));
  }

  @SuppressWarnings({ "unchecked" })
  public List<NodeMatch> findMatches(Node rootNodeToMatch) {
    return nodeClassToRules.keySet()
                           .stream()
                           .flatMap(nodeClazz -> rootNodeToMatch.findAll(nodeClazz).stream())
                           .flatMap(this::computeNodeMatches)
                           .collect(Collectors.toList());
  }

  private Stream<? extends NodeMatch> computeNodeMatches(Node node) {
    return nodeClassToRules.get(node.getClass()).stream()
                           .filter(migration -> {
                             try {
                               return ((MigrationRule<Node>) migration).predicate().test(node);
                             } catch (Throwable e) {
                               throw new RuntimeException(
                                                          String.format("Error while trying to match migration '%s' on code:\n%s",
                                                                        migration, node),
                                                          e);
                             }
                           })
                           .findFirst()
                           .map(rule -> new NodeMatch(
                                                      node,
                                                      () -> ((MigrationRule<Node>) rule).migrate(node),
                                                      rule.toString(),
                                                      new HashSet<>(rule.requiredImports())))
                           .stream();
  }

}
