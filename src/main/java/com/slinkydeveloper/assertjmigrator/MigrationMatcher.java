package com.slinkydeveloper.assertjmigrator;

import com.github.javaparser.ast.Node;
import com.slinkydeveloper.assertjmigrator.migrations.assertstmt.JavaAssertFallback;
import com.slinkydeveloper.assertjmigrator.migrations.hamcrest.*;
import com.slinkydeveloper.assertjmigrator.migrations.junit.*;

import java.util.*;
import java.util.stream.Collectors;

public class MigrationMatcher {

    private static final List<Migration<?>> DEFAULT_MIGRATIONS = new ArrayList<>();

    static {
        DEFAULT_MIGRATIONS.addAll(
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

    private final List<Migration<?>> supportedMigrations;

    public MigrationMatcher() {
        this(DEFAULT_MIGRATIONS);
    }

    private MigrationMatcher(List<Migration<?>> supportedMigrations) {
        this.supportedMigrations = supportedMigrations;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Map.Entry<Migration<Node>, Node>> match(Node rootNodeToMatch) {
        List<Map.Entry<Migration<Node>, Node>> matchedMigrationsForCompilationUnit = new ArrayList<>();

        Map<Class, List<Migration<?>>> nodesToMatch = supportedMigrations
                .stream()
                .collect(Collectors.groupingBy(Migration::matchedNode));

        for (Map.Entry<Class, List<Migration<?>>> entry : nodesToMatch.entrySet()) {
            List<Node> nodes = rootNodeToMatch.findAll(entry.getKey());

            for (Node node : nodes) {
                entry.getValue()
                        .stream()
                        .filter(migration -> {
                            try {
                                return ((Migration<Node>) migration).matches(node);
                            } catch (Throwable e) {
                                throw new RuntimeException(
                                        String.format("Error while trying to match migration '%s' on code:\n%s", migration, node.toString()), e);
                            }
                        })
                        .findFirst()
                        .ifPresent(migration ->
                                matchedMigrationsForCompilationUnit.add(new AbstractMap.SimpleImmutableEntry<>(((Migration<Node>) migration), node)));
            }
        }

        return matchedMigrationsForCompilationUnit;
    }


}
