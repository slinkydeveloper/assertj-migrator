package com.slinkydeveloper.assertjmigrator;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.printer.configuration.DefaultConfigurationOption;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import com.github.javaparser.printer.configuration.Indentation;
import com.github.javaparser.printer.configuration.PrinterConfiguration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ClassLoaderTypeSolver;
import org.assertj.core.api.ThrowingConsumer;
import picocli.CommandLine;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@CommandLine.Command(name = "main", mixinStandardHelpOptions = true, version = "0.0.1",
        description = "AssertJ migrator")
public class Main implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", description = "The path where to locate .java files")
    private File rootDirectory;

    @CommandLine.Option(names = {"-v", "--verbose"}, description = "Verbose")
    private boolean verbose = false;

    @CommandLine.Option(names = {"-d", "--dry-run"}, description = "Dry run")
    private boolean dryRun = false;

    public static void main(String[] args) {
        System.exit(new CommandLine(new Main()).execute(args));
    }

    private static <T> Consumer<T> wrapTryConsumer(ThrowingConsumer<T> processFunction, Map<T, Throwable> processingErrors) {
        return f -> {
            try {
                processFunction.accept(f);
            } catch (Throwable e) {
                processingErrors.put(f, e);
            }
        };
    }

    @Override
    public Integer call() throws Exception {
        Path startingDir = rootDirectory.toPath();
        JarsFinder pf = new JarsFinder();
        Files.walkFileTree(startingDir, pf);

        if (verbose) {
            System.out.println(
                    "---- Found jars:\n" + pf.getFoundJars() + "\n----\n"
            );
        }

        ClassLoaderTypeSolver classLoaderTypeSolver = new ClassLoaderTypeSolver(
                new URLClassLoader(pf.getFoundJars().toArray(new URL[0]), Main.class.getClassLoader())
        );

        // Configure JavaParser to use type resolution
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(classLoaderTypeSolver);

        // Parser configuration
        ParserConfiguration configuration = new ParserConfiguration()
                .setSymbolResolver(symbolSolver);
        JavaParser parser = new JavaParser(configuration);

        MigrationMatcher migrationMatcher = new MigrationMatcher();

        Map<Path, Throwable> errors = new HashMap<>();
        JavaTestSourceFinder migrationTargetFinder = new JavaTestSourceFinder(wrapTryConsumer(
                javaFilePath -> processMatch(parser, migrationMatcher, javaFilePath),
                errors
        ));
        Files.walkFileTree(startingDir, migrationTargetFinder);

        System.out.println("---- Processing completed ----");

        if (!errors.isEmpty()) {
            System.out.println("---- Errors: " + errors.size() + " ----");
            errors.forEach((path, throwable) -> {
                System.out.println("-- File: " + path);
                throwable.printStackTrace(System.out);
                System.out.flush();
            });
        }

        return 0;
    }

    private void processMatch(JavaParser parser, MigrationMatcher migrationMatcher, Path javaFile) throws Throwable {
        // Parse the file
        CompilationUnit cu = parser.parse(javaFile).getResult().get();
        CompilationUnit originalCu = cu.clone();

        List<Map.Entry<Migration<Node>, Node>> matchedMigrationsForCompilationUnit = migrationMatcher.match(cu);
        if (matchedMigrationsForCompilationUnit.isEmpty()) {
            return;
        }

        MigrationTarget match = new MigrationTarget(javaFile, cu, matchedMigrationsForCompilationUnit);

        if (verbose) {
            System.out.println("--- " + match.getPath());
            match.getMatchedMigrations()
                    .stream()
                    .collect(Collectors.groupingBy(Map.Entry::getKey))
                    .forEach((migration, entry) -> System.out.println(migration + ": " + entry.size()));
            System.out.println();
        }

        match.migrate();

        // Look out for the differences
        AssertDiffVisitor assertDiffVisitor = new AssertDiffVisitor();
        assertDiffVisitor.visit(originalCu, match.getCompilationUnit());
        List<Map.Entry<Node, Node>> differences = assertDiffVisitor.getDifferentNodes();
        differences.sort(Comparator.comparing(x -> x.getKey().getRange().get().begin));

        // Write the differences back to the original file
        StringReplacer stringReplacer = new StringReplacer(javaFile);

        // Replace imports
        PrinterConfiguration importPrinterConfiguration = new DefaultPrinterConfiguration();
        importPrinterConfiguration.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.INDENTATION, new Indentation(Indentation.IndentType.SPACES, 0)));
        importPrinterConfiguration.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.END_OF_LINE_CHARACTER, ""));

        int importsFirstLine = originalCu.getImports().getFirst().get().getRange().get().begin.line - Position.FIRST_LINE;
        int importsLastLineInclusive = originalCu.getImports().getLast().get().getRange().get().end.line - Position.FIRST_LINE;
        stringReplacer.replaceLines(
                importsFirstLine,
                importsLastLineInclusive,
                match.getCompilationUnit()
                        .getImports()
                        .stream()
                        .map(importDeclaration -> importDeclaration.toString(importPrinterConfiguration))
                        .collect(Collectors.toList())
        );

        // Replace nodes
        PrinterConfiguration nodePrinterConfiguration = new DefaultPrinterConfiguration();

        for (Map.Entry<Node, Node> entry : differences) {
            Node oldNode = entry.getKey();
            Node newNode = entry.getValue();

            stringReplacer.replaceNode(oldNode.getRange().get(), newNode.toString(nodePrinterConfiguration));
        }

        // Write out
        PrintStream printStream = dryRun ? System.out : new PrintStream(match.getPath().toFile());
        stringReplacer.writeOut(printStream);
    }

}
