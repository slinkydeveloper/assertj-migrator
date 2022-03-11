/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.slinkydeveloper.assertjmigrator;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class MigrationsTests {

    private static final String INPUT_SUFFIX = "_input";
    private static final String EXPECTED_SUFFIX = "_expected";

    private static final MigrationMatcher defaultMigrationMatcher = new MigrationMatcher();

    private static Stream<DynamicTest> parseTestCases(String filename) throws Exception {
        // Configure JavaParser to use type resolution
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(new ReflectionTypeSolver(false));

        // Parser configuration
        ParserConfiguration configuration = new ParserConfiguration()
                .setSymbolResolver(symbolSolver);
        JavaParser parser = new JavaParser(configuration);

        // Parse JUnit5MigrationTestCases
        CompilationUnit cu = parser.parse(Path.of(MigrationsTests.class.getResource(filename).toURI()))
                .getResult()
                .get();

        // Get methods
        List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);
        Map<String, MethodDeclaration> methodsByName = methods.stream().collect(Collectors.toMap(
                MethodDeclaration::getNameAsString,
                Function.identity()
        ));

        return methods.stream().filter(md -> md.getNameAsString().endsWith(INPUT_SUFFIX)).map(inputMd -> {
            // Get the expected method
            String testName = inputMd.getNameAsString().substring(0, inputMd.getNameAsString().length() - INPUT_SUFFIX.length());
            MethodDeclaration expectedMd = methodsByName.get(testName + EXPECTED_SUFFIX);

            return DynamicTest.dynamicTest(testName, executeTestCase(inputMd, expectedMd));
        });
    }

    private static Executable executeTestCase(MethodDeclaration inputMd, MethodDeclaration expectedMd) {
        return () -> {
            assertThat(inputMd.getBody().get().getStatements()).hasSize(1);
            assertThat(expectedMd.getBody().get().getStatements()).hasSize(1);

            Statement inputStatement = inputMd.getBody().get().getStatements().get(0);
            Statement expectedStatement = expectedMd.getBody().get().getStatements().get(0);

            List<Map.Entry<Migration<Node>, Node>> matchResults = defaultMigrationMatcher.match(inputStatement);
            assertThat(matchResults)
                    .as("Expecting one migration")
                    .hasSize(1);

            // Note that this mutates the inputMd
            matchResults.get(0).getKey().migrate(matchResults.get(0).getValue());

            assertThat(inputMd.getBody().get().getStatements()).hasSize(1);
            assertThat(inputMd.getBody().get().getStatements().get(0)).isEqualTo(expectedStatement);
        };
    }

    @TestFactory
    Stream<DynamicTest> assertStatement() throws Exception {
        return parseTestCases("/migrations/AssertMigrationTestCases.java");
    }

    @TestFactory
    Stream<DynamicTest> junit4() throws Exception {
        return parseTestCases("/migrations/JUnit4MigrationTestCases.java");
    }

    @TestFactory
    Stream<DynamicTest> junit5() throws Exception {
        return parseTestCases("/migrations/JUnit5MigrationTestCases.java");
    }

    @TestFactory
    Stream<DynamicTest> hamcrest() throws Exception {
        return parseTestCases("/migrations/HamcrestMigrationTestCases.java");
    }

    @TestFactory
    Stream<DynamicTest> misc() throws Exception {
        return parseTestCases("/migrations/MiscMigrationTestCases.java");
    }

}
