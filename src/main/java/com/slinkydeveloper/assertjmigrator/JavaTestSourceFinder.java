package com.slinkydeveloper.assertjmigrator;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Consumer;

import static java.nio.file.FileVisitResult.CONTINUE;

public class JavaTestSourceFinder
        extends SimpleFileVisitor<Path> {

    private final Consumer<Path> fileConsumer;

    public JavaTestSourceFinder(Consumer<Path> fileConsumer) {
        this.fileConsumer = fileConsumer;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (dir.endsWith("main") || dir.endsWith("target")) {
            return FileVisitResult.SKIP_SUBTREE;
        }
        return super.preVisitDirectory(dir, attrs);
    }

    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attr) throws IOException {
        if (file.getFileName().toString().endsWith(".java")) {
            fileConsumer.accept(file);
        }

        return CONTINUE;
    }

}
