package com.slinkydeveloper.assertjmigrator.io;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.javaparser.Position;
import com.github.javaparser.Range;

public class StringReplacerTest {

  private static Range range(int beginLineIndex, int beginColumnIndex, int endLineIndex, int endColumnIndex) {
    return new Range(
                     new Position(beginLineIndex + Position.FIRST_LINE, beginColumnIndex + Position.FIRST_COLUMN),
                     new Position(endLineIndex + Position.FIRST_LINE, endColumnIndex + Position.FIRST_COLUMN));
  }

  @Test
  void replaceLinesTest() {
    StringReplacer replacer = new StringReplacer(List.of(
                                                         "0",
                                                         "1",
                                                         "2",
                                                         "3",
                                                         "4",
                                                         "5",
                                                         "6",
                                                         "7"));

    replacer.replaceLines(
                          1,
                          3,
                          List.of(
                                  "1.1",
                                  "2.2"));
    replacer.replaceLines(
                          4,
                          4,
                          List.of(
                                  "4.4"));
    replacer.replaceLines(
                          5,
                          7,
                          List.of(
                                  "5.5",
                                  "6.6",
                                  "7.7",
                                  "8"));

    assertThat(replacer.getLines()).containsExactly(
                                                    "0",
                                                    "1.1",
                                                    "2.2",
                                                    "4.4",
                                                    "5.5",
                                                    "6.6",
                                                    "7.7",
                                                    "8");
  }

  @Test
  void replaceLinesAndNodesTest() {
    StringReplacer replacer = new StringReplacer(List.of(
                                                         "import a",
                                                         "import b",
                                                         "import c",
                                                         "",
                                                         "assert \"123\" instanceof String;",
                                                         "assertEquals(2f, 1f, 2f);assertFalse(Integer.valueOf(123) instanceof Integer);",
                                                         "assertEquals(123, \"abc\");",
                                                         "",
                                                         "Assert.assertThrows(\"my error msg\", IndexOutOfBoundsException.class, () -> {",
                                                         "    throw new IndexOutOfBoundsException();",
                                                         "});",
                                                         "assertDoesNotThrow(() -> \"null\");Assert.assertThrows(\"my error msg\", IndexOutOfBoundsException.class, () -> {",
                                                         "    throw new IndexOutOfBoundsException();",
                                                         "});"));

    replacer.replaceLines(
                          0,
                          2,
                          List.of(
                                  "import assertThat"));
    replacer.replaceNode(
                         range(4, 0, 4, "assert \"123\" instanceof String".length() - 1),
                         "assertThat(\"123\").isInstanceOf(String.class)");
    replacer.replaceNode(
                         range(5, 0, 5, "assertEquals(2f, 1f, 2f)".length() - 1),
                         "assertThat(2f).isCloseTo(1f, within(2f))");
    replacer.replaceNode(
                         range(5, "assertEquals(2f, 1f, 2f);".length(), 5,
                               "assertEquals(2f, 1f, 2f);assertFalse(Integer.valueOf(123) instanceof Integer)".length() - 1),
                         "assertThat(Integer.valueOf(123)).isNotInstanceOf(Integer.class)");
    replacer.replaceNode(
                         range(6, 0, 6, "assertEquals(123, \"abc\")".length() - 1),
                         "assertThat(123).isEqualTo(\"abc\")");
    replacer.replaceNode(
                         range(8, 0, 10, "})".length() - 1),
                         "assertThatThrownBy(() -> { throw new IndexOutOfBoundsException(); }).isInstanceOf(IndexOutOfBoundsException.class)");
    replacer.replaceNode(
                         range(11, 0, 11, "assertDoesNotThrow(() -> \"null\")".length() - 1),
                         "assertThatThrownBy(() -> \"null\").isNull()");
    replacer.replaceNode(
                         range(11, "assertDoesNotThrow(() -> \"null\");".length(), 13, "})".length() - 1),
                         "assertThatThrownBy(() -> { throw new IndexOutOfBoundsException(); }).isInstanceOf(IndexOutOfBoundsException.class)");

    assertThat(replacer.getLines()).containsExactly(
                                                    "import assertThat",
                                                    "",
                                                    "assertThat(\"123\").isInstanceOf(String.class);",
                                                    "assertThat(2f).isCloseTo(1f, within(2f));assertThat(Integer.valueOf(123)).isNotInstanceOf(Integer.class);",
                                                    "assertThat(123).isEqualTo(\"abc\");",
                                                    "",
                                                    "assertThatThrownBy(() -> { throw new IndexOutOfBoundsException(); }).isInstanceOf(IndexOutOfBoundsException.class)",
                                                    ";",
                                                    "assertThatThrownBy(() -> \"null\").isNull();assertThatThrownBy(() -> { throw new IndexOutOfBoundsException(); }).isInstanceOf(IndexOutOfBoundsException.class)",
                                                    ";");
  }

}
