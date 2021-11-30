package com.slinkydeveloper.assertjmigrator;

import com.github.javaparser.Position;
import com.github.javaparser.Range;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringReplacer {

    private final List<String> fileLines;

    private int lineOffset = 0;
    private int columnOffset = 0;
    private int lastEndLineIndexInclusive = -1;
    private int lastEndColumnIndexInclusive = -1;

    public StringReplacer(List<String> fileLines) {
        this.fileLines = new ArrayList<>(fileLines);
    }

    public StringReplacer(Path javaFile) throws IOException {
        this(Files.readAllLines(javaFile));
    }

    public void replaceLines(int beginLineIndex, int endLineIndexInclusive, List<String> newLines) {
        assert (beginLineIndex > lastEndLineIndexInclusive);

        int oldLinesNumber = endLineIndexInclusive + 1 - beginLineIndex;

        // Adjust to the offset
        beginLineIndex += lineOffset;

        // Remove lines
        for (int i = 0; i < oldLinesNumber; i++) {
            lastEndColumnIndexInclusive = Math.max(0, fileLines.remove(beginLineIndex).length() - 1);
        }

        // Add lines
        fileLines.addAll(beginLineIndex, newLines);

        // Update the offset
        lineOffset += newLines.size() - oldLinesNumber;

        lastEndLineIndexInclusive = endLineIndexInclusive;
    }

    public void replaceNode(Range oldNodeRange, String newString) {
        int beginLineIndex = oldNodeRange.begin.line - Position.FIRST_LINE;
        int endLineIndexInclusive = oldNodeRange.end.line - Position.FIRST_LINE;
        int beginColumnIndex = oldNodeRange.begin.column - Position.FIRST_COLUMN;
        int endColumnIndexInclusive = oldNodeRange.end.column - Position.FIRST_COLUMN;

        // Make sure only "next" strings are replaced
        assert (beginLineIndex > lastEndLineIndexInclusive) || (beginLineIndex == lastEndLineIndexInclusive && beginColumnIndex > lastEndColumnIndexInclusive);

        // Reset column offset if necessary
        if (beginLineIndex != lastEndLineIndexInclusive) {
            columnOffset = 0;
        }
        lastEndLineIndexInclusive = beginLineIndex; // This is because a multiline replace is collapsed in a single line
        lastEndColumnIndexInclusive = endColumnIndexInclusive;

        if (beginLineIndex == endLineIndexInclusive) {
            singleLineReplace(beginLineIndex, beginColumnIndex, endColumnIndexInclusive + 1, newString);
        } else {
            multilineReplace(beginLineIndex, endLineIndexInclusive + 1, beginColumnIndex, endColumnIndexInclusive + 1, newString);
        }
    }

    private void singleLineReplace(int lineIndex, int beginColumnIndex, int endColumnIndex, String newString) {
        int oldColumnsNumber = endColumnIndex - beginColumnIndex;

        // Adjust to the offset
        lineIndex += lineOffset;
        beginColumnIndex += columnOffset;
        endColumnIndex += columnOffset;

        updateLine(lineIndex, beginColumnIndex, endColumnIndex, newString);

        // Update the columnOffset
        columnOffset += newString.length() - oldColumnsNumber;
    }

    private void multilineReplace(int beginLineIndex, int endLineIndex, int beginColumnIndex, int endColumnIndex, String newString) {
        int oldLinesNumber = endLineIndex - beginLineIndex;

        // Adjust to the offset
        beginLineIndex += lineOffset;
        beginColumnIndex += columnOffset;

        // Replace first line
        updateLine(beginLineIndex, beginColumnIndex, Integer.MAX_VALUE, newString);

        // Remove intermediate lines
        if (oldLinesNumber > 2) {
            for (int i = 0; i < oldLinesNumber - 2; i++) {
                this.fileLines.remove(beginLineIndex + 1);
            }
        }

        // Replace last line
        updateLine(beginLineIndex + 1, 0, endColumnIndex, "");

        lineOffset += 2 - oldLinesNumber;
        columnOffset = -endColumnIndex;
    }

    private void updateLine(int lineIndex, int beginColumnIndex, int endColumnIndex, String newString) {
        String oldLine = this.fileLines.get(lineIndex);
        StringBuilder builder = new StringBuilder();
        if (beginColumnIndex > 0) {
            builder.append(oldLine, 0, beginColumnIndex);
        }
        builder.append(newString);
        if (endColumnIndex < oldLine.length()) {
            builder.append(oldLine, endColumnIndex, oldLine.length());
        }
        this.fileLines.set(lineIndex, builder.toString());
    }

    protected List<String> getLines() {
        return Collections.unmodifiableList(this.fileLines);
    }

    public void writeOut(PrintStream printStream) {
        for (String line : fileLines) {
            printStream.print(line);
            printStream.print('\n');
        }
    }

    @Override
    public String toString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        writeOut(printStream);
        return byteArrayOutputStream.toString();
    }
}
