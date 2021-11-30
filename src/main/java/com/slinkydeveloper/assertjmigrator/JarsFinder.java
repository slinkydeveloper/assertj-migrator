package com.slinkydeveloper.assertjmigrator;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;

public class JarsFinder
        extends SimpleFileVisitor<Path> {

    private final List<URL> jars;

    public JarsFinder() {
        this.jars = new ArrayList<>();
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (dir.endsWith("src") || dir.endsWith("classes") || dir.endsWith("test-classes")) {
            return FileVisitResult.SKIP_SUBTREE;
        }
        return super.preVisitDirectory(dir, attrs);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attr) throws IOException {

        if (file.getFileName().toString().endsWith(".jar")) {
            this.jars.add(file.toUri().toURL());
        }

        return CONTINUE;
    }

    public List<URL> getFoundJars() {
        return jars;
    }
}
