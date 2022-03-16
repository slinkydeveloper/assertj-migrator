import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.*;

public class JUnit5MigrationTestCases {

  Object x = new Object();
  Class xClass = Object.class;
  Object y = new Object();

  Object[] xArray = new Object[] {};
  Object[] yArray = new Object[] {};

  Boolean xBoolean = false;
  Float xFloat = 10f;
  Float yFloat = 10f;
  float deltaFloat = 10f;
  Double xDouble = 10d;
  Double yDouble = 10d;
  double deltaDouble = 10d;

  boolean b = true;

  // Assert equals/not equals

  void assertEquals_input() {
    assertEquals(x, y);
  }

  void assertEquals_expected() {
    assertThat(y).isEqualTo(x);
  }

  void assertEqualsWithMessage_input() {
    assertEquals(x, y, "some msg");
  }

  void assertEqualsWithMessage_expected() {
    assertThat(y).as("some msg").isEqualTo(x);
  }

  void assertEqualsAllStringsWithMessage_input() {
    assertEquals("x", "y", "some msg");
  }

  void assertEqualsAllStringsWithMessage_expected() {
    assertThat("y").as("some msg").isEqualTo("x");
  }

  void assertNotEquals_input() {
    assertNotEquals(x, y);
  }

  void assertNotEquals_expected() {
    assertThat(y).isNotEqualTo(x);
  }

  void assertNotEqualsWithMessage_input() {
    assertNotEquals(x, y, "some msg");
  }

  void assertNotEqualsWithMessage_expected() {
    assertThat(y).as("some msg").isNotEqualTo(x);
  }

  // Assert equals/not equals with delta

  void assertEqualsFloatDelta_input() {
    assertEquals(xFloat, yFloat, deltaFloat);
  }

  void assertEqualsFloatDelta_expected() {
    assertThat(yFloat).isCloseTo(xFloat, within(deltaFloat));
  }

  void assertEqualsFloatDeltaWithMessage_input() {
    assertEquals(xFloat, yFloat, deltaFloat, "some msg");
  }

  void assertEqualsFloatDeltaWithMessage_expected() {
    assertThat(yFloat).as("some msg").isCloseTo(xFloat, within(deltaFloat));
  }

  void assertNotEqualsFloatDelta_input() {
    assertNotEquals(xFloat, yFloat, deltaFloat);
  }

  void assertNotEqualsFloatDelta_expected() {
    assertThat(yFloat).isNotCloseTo(xFloat, within(deltaFloat));
  }

  void assertNotEqualsFloatDeltaWithMessage_input() {
    assertNotEquals(xFloat, yFloat, deltaFloat, "some msg");
  }

  void assertNotEqualsFloatDeltaWithMessage_expected() {
    assertThat(yFloat).as("some msg").isNotCloseTo(xFloat, within(deltaFloat));
  }

  void assertEqualsDoubleDelta_input() {
    assertEquals(xDouble, yDouble, deltaDouble);
  }

  void assertEqualsDoubleDelta_expected() {
    assertThat(yDouble).isCloseTo(xDouble, within(deltaDouble));
  }

  void assertEqualsDoubleDeltaWithMessage_input() {
    assertEquals(xDouble, yDouble, deltaDouble, "some msg");
  }

  void assertEqualsDoubleDeltaWithMessage_expected() {
    assertThat(yDouble).as("some msg").isCloseTo(xDouble, within(deltaDouble));
  }

  void assertNotEqualsDoubleDelta_input() {
    assertNotEquals(xDouble, yDouble, deltaDouble);
  }

  void assertNotEqualsDoubleDelta_expected() {
    assertThat(yDouble).isNotCloseTo(xDouble, within(deltaDouble));
  }

  void assertNotEqualsDoubleDeltaWithMessage_input() {
    assertNotEquals(xDouble, yDouble, deltaDouble, "some msg");
  }

  void assertNotEqualsDoubleDeltaWithMessage_expected() {
    assertThat(yDouble).as("some msg").isNotCloseTo(xDouble, within(deltaDouble));
  }

  // Assert array equals

  void assertArrayEquals_input() {
    assertArrayEquals(xArray, yArray);
  }

  void assertArrayEquals_expected() {
    assertThat(yArray).isEqualTo(xArray);
  }

  // Assert true/false

  void assertTrue_input() {
    assertTrue(xBoolean);
  }

  void assertTrue_expected() {
    assertThat(xBoolean).isTrue();
  }

  void assertTrueWithMessage_input() {
    assertTrue(xBoolean, "some msg");
  }

  void assertTrueWithMessage_expected() {
    assertThat(xBoolean).as("some msg").isTrue();
  }

  void assertFalse_input() {
    assertFalse(xBoolean);
  }

  void assertFalse_expected() {
    assertThat(xBoolean).isFalse();
  }

  void assertFalseWithMessage_input() {
    assertFalse(xBoolean, "some msg");
  }

  void assertFalseWithMessage_expected() {
    assertThat(xBoolean).as("some msg").isFalse();
  }

  // Assert null/not null

  void assertNull_input() {
    assertNull(x);
  }

  void assertNull_expected() {
    assertThat(x).isNull();
  }

  void assertNullWithMessage_input() {
    assertNull(x, "some msg");
  }

  void assertNullWithMessage_expected() {
    assertThat(x).as("some msg").isNull();
  }

  void assertNotNull_input() {
    assertNotNull(x);
  }

  void assertNotNull_expected() {
    assertThat(x).isNotNull();
  }

  void assertNotNullWithMessage_input() {
    assertNotNull(x, "some msg");
  }

  void assertNotNullWithMessage_expected() {
    assertThat(x).as("some msg").isNotNull();
  }

  // Assert same/not same

  void assertSame_input() {
    assertSame(x, y);
  }

  void assertSame_expected() {
    assertThat(y).isSameAs(x);
  }

  void assertSameWithMessage_input() {
    assertSame(x, y, "some msg");
  }

  void assertSameWithMessage_expected() {
    assertThat(y).as("some msg").isSameAs(x);
  }

  void assertNotSame_input() {
    assertNotSame(x, y);
  }

  void assertNotSame_expected() {
    assertThat(y).isNotSameAs(x);
  }

  void assertNotSameWithMessage_input() {
    assertNotSame(x, y, "some msg");
  }

  void assertNotSameWithMessage_expected() {
    assertThat(y).as("some msg").isNotSameAs(x);
  }

  // Assert instanceOf

  void assertInstanceOfWithClassLiteral_input() {
    assertInstanceOf(Object.class, x);
  }

  void assertInstanceOfWithClassLiteral_expected() {
    assertThat(x).isInstanceOf(Object.class);
  }

  void assertInstanceOfWithClassLiteralWithMessage_input() {
    assertInstanceOf(Object.class, x, "some msg");
  }

  void assertInstanceOfWithClassLiteralWithMessage_expected() {
    assertThat(x).as("some msg").isInstanceOf(Object.class);
  }

  void assertInstanceOfWithClassVariable_input() {
    assertInstanceOf(xClass, x);
  }

  void assertInstanceOfWithClassVariable_expected() {
    assertThat(x).isInstanceOf(xClass);
  }

  void assertInstanceOfWithClassVariableWithMessage_input() {
    assertInstanceOf(xClass, x, "some msg");
  }

  void assertInstanceOfWithClassVariableWithMessage_expected() {
    assertThat(x).as("some msg").isInstanceOf(xClass);
  }

  // Assert throws/does not throw

  void assertThrows_input() {
    assertThrows(IllegalArgumentException.class, () -> {
      throw new IllegalArgumentException();
    });
  }

  void assertThrows_expected() {
    assertThatThrownBy(() -> {
      throw new IllegalArgumentException();
    }).isInstanceOf(IllegalArgumentException.class);
  }

  void assertThrowsWithMessage_input() {
    assertThrows(IllegalArgumentException.class, () -> {
      throw new IllegalArgumentException();
    }, "some msg");
  }

  void assertThrowsWithMessage_expected() {
    assertThatThrownBy(() -> {
      throw new IllegalArgumentException();
    }).as("some msg").isInstanceOf(IllegalArgumentException.class);
  }

  void assertDoesNotThrow_input() {
    assertDoesNotThrow(() -> {
      throw new IllegalArgumentException();
    });
  }

  void assertDoesNotThrow_expected() {
    assertThatThrownBy(() -> {
      throw new IllegalArgumentException();
    }).isNull();
  }

  void assertDoesNotThrowWithMessage_input() {
    assertDoesNotThrow(() -> {
      throw new IllegalArgumentException();
    }, "some msg");
  }

  void assertDoesNotThrowWithMessage_expected() {
    assertThatThrownBy(() -> {
      throw new IllegalArgumentException();
    }).as("some msg").isNull();
  }

  // Fail

  void fail_input() {
    org.junit.jupiter.api.Assertions.fail();
  }

  void fail_expected() {
    fail("unknown failure");
  }

  void failWithMessage_input() {
    org.junit.jupiter.api.Assertions.fail("a specific failure");
  }

  void failWithMessage_expected() {
    fail("a specific failure");
  }

  void failWithThrowable_input() {
    org.junit.jupiter.api.Assertions.fail(new IllegalArgumentException());
  }

  void failWithThrowable_expected() {
    fail("unknown failure", new IllegalArgumentException());
  }

  void failWithMessageAndThrowable_input() {
    org.junit.jupiter.api.Assertions.fail("a specific failure", new IllegalArgumentException());
  }

  void failWithMessageAndThrowable_expected() {
    fail("a specific failure", new IllegalArgumentException());
  }

}
