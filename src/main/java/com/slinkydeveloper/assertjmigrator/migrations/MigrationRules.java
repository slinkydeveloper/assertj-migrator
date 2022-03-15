package com.slinkydeveloper.assertjmigrator.migrations;

import com.github.javaparser.ast.Node;
import com.slinkydeveloper.assertjmigrator.migrations.assertstmt.JavaAssertFallback;
import com.slinkydeveloper.assertjmigrator.migrations.hamcrest.*;
import com.slinkydeveloper.assertjmigrator.migrations.junit.*;

import java.util.*;
import java.util.stream.Collectors;

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

                        new HamcrestAssertThatFallbackMigration()
                )
        );
    }

    private final List<MigrationRule<?>> supportedMigrationRules;

    public MigrationRules() {
        this(DEFAULT_MIGRATION_RULES);
    }

    private MigrationRules(List<MigrationRule<?>> supportedMigrationRules) {
        this.supportedMigrationRules = supportedMigrationRules;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Map.Entry<MigrationRule<Node>, Node>> match(Node rootNodeToMatch) {
        List<Map.Entry<MigrationRule<Node>, Node>> matchedMigrationsForCompilationUnit = new ArrayList<>();

        Map<Class, List<MigrationRule<?>>> nodesToMatch = supportedMigrationRules
                .stream()
                .collect(Collectors.groupingBy(MigrationRule::matchedNode));

        for (Map.Entry<Class, List<MigrationRule<?>>> entry : nodesToMatch.entrySet()) {
            List<Node> nodes = rootNodeToMatch.findAll(entry.getKey());

            for (Node node : nodes) {
                entry.getValue()
                        .stream()
                        .filter(migration -> {
                            try {
                                return ((MigrationRule<Node>) migration).predicate().test(node);
                            } catch (Throwable e) {
                                throw new RuntimeException(
                                        String.format("Error while trying to match migration '%s' on code:\n%s", migration, node.toString()), e);
                            }
                        })
                        .findFirst()
                        .ifPresent(migration ->
                                matchedMigrationsForCompilationUnit.add(new AbstractMap.SimpleImmutableEntry<>(((MigrationRule<Node>) migration), node)));
            }
        }

        return matchedMigrationsForCompilationUnit;
    }


}
