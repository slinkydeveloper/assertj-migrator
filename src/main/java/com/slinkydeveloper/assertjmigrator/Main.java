package com.slinkydeveloper.assertjmigrator;

import com.github.javaparser.ParseResult;
import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.printer.configuration.DefaultConfigurationOption;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import com.github.javaparser.printer.configuration.Indentation;
import com.github.javaparser.printer.configuration.PrinterConfiguration;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;
import com.slinkydeveloper.assertjmigrator.migrations.MigrationRules;
import com.slinkydeveloper.assertjmigrator.migrations.NodeMatch;
import picocli.CommandLine;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
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

    @Override
    public Integer call() throws Exception {
        final MigrationRules migrationRules = new MigrationRules();

        final Path rootPath = rootDirectory.toPath();

        // Use the SymbolSolverCollectionStrategy to infer the symbol resolved
        final ProjectRoot projectRoot = new SymbolSolverCollectionStrategy()
                .collect(rootPath);

        final Map<Path, Throwable> processingErrors = new HashMap<>();

        projectRoot.getSourceRoots()
                .stream()
                .filter(source -> source.getRoot().endsWith(Path.of("test", "java")))
                .forEach(source -> {
                    try {
                        source.parseParallelized((localPath, absolutePath, result) -> {
                            try {
                                processMatch(result, migrationRules, absolutePath);
                            } catch (Throwable e) {
                                processingErrors.put(source.getRoot(), e);
                            }
                            return SourceRoot.Callback.Result.DONT_SAVE;
                        });
                    } catch (Throwable e) {
                        processingErrors.put(source.getRoot(), e);
                    }
                });

        System.out.println("---- Processing completed ----");

        if (!processingErrors.isEmpty()) {
            System.out.println("---- Errors: " + processingErrors.size() + " ----");
            processingErrors.forEach((path, throwable) -> {
                System.out.println("-- File: " + path);
                throwable.printStackTrace(System.out);
                System.out.flush();
            });
        }

        return 0;
    }

    private void processMatch(ParseResult<CompilationUnit> parseResult, MigrationRules migrationRules, Path javaFile) throws Throwable {
        // Parse the file
        CompilationUnit cu = parseResult.getResult().get();
        CompilationUnit originalCu = cu.clone();

        List<NodeMatch> matchedMigrationsForCompilationUnit = migrationRules.findMatches(cu);
        if (matchedMigrationsForCompilationUnit.isEmpty()) {
            return;
        }

        MatchedCompilationUnit match = new MatchedCompilationUnit(javaFile, cu, matchedMigrationsForCompilationUnit);

        if (verbose) {
            System.out.println("--- " + match.getPath());
            match.getMatchedMigrations()
                    .stream()
                    .collect(Collectors.groupingBy(NodeMatch::getDescription))
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
