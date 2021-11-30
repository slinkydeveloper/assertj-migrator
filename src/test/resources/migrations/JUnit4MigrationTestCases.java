import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.Assertions.fail;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class JUnit4MigrationTestCases {

    Object x = new Object();
    Class xClass = Object.class;
    Object y = new Object();

    Object[] xArray = new Object[]{};
    Object[] yArray = new Object[]{};

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
        assertEquals("some msg", x, y);
    }

    void assertEqualsWithMessage_expected() {
        assertThat(y).as("some msg").isEqualTo(x);
    }

    void assertEqualsAllStringsWithMessage_input() {
        assertEquals("some msg", "x", "y");
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
        assertNotEquals("some msg", x, y);
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
        assertEquals("some msg", xFloat, yFloat, deltaFloat);
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
        assertNotEquals("some msg", xFloat, yFloat, deltaFloat);
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
        assertEquals("some msg", xDouble, yDouble, deltaDouble);
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
        assertNotEquals("some msg", xDouble, yDouble, deltaDouble);
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
        assertTrue("some msg", xBoolean);
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
        assertFalse("some msg", xBoolean);
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
        assertNull("some msg", x);
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
        assertNotNull("some msg", x);
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
        assertSame("some msg", x, y);
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
        assertNotSame("some msg", x, y);
    }

    void assertNotSameWithMessage_expected() {
        assertThat(y).as("some msg").isNotSameAs(x);
    }

    // Assert throws

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
        assertThrows("some msg", IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException();
        });
    }

    void assertThrowsWithMessage_expected() {
        assertThatThrownBy(() -> {
            throw new IllegalArgumentException();
        }).as("some msg").isInstanceOf(IllegalArgumentException.class);
    }

    // Fail

    void fail_input() {
        org.junit.Assert.fail();
    }

    void fail_expected() {
        fail("unknown failure");
    }

    void failWithMessage_input() {
        org.junit.Assert.fail("a specific failure");
    }

    void failWithMessage_expected() {
        fail("a specific failure");
    }

}
