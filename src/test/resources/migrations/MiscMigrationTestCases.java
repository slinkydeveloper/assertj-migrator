import org.assertj.core.api.Assertions;

public class MiscMigrationTestCases {

    Object x = new Object();

    // Assert true/false instanceOf junit 4

    void assertTrueWithInstanceOf_junit4_input() {
        org.junit.Assert.assertTrue(x instanceof Object);
    }

    void assertTrueWithInstanceOf_junit4_expected() {
        assertThat(x).isInstanceOf(Object.class);
    }

    void assertTrueWithInstanceOfWithMessage_junit4_input() {
        org.junit.Assert.assertTrue("some msg", x instanceof Object);
    }

    void assertTrueWithInstanceOfWithMessage_junit4_expected() {
        assertThat(x).as("some msg").isInstanceOf(Object.class);
    }

    void assertFalseWithInstanceOf_junit4_input() {
        org.junit.Assert.assertFalse(x instanceof Object);
    }

    void assertFalseWithInstanceOf_junit4_expected() {
        assertThat(x).isNotInstanceOf(Object.class);
    }

    void assertFalseWithInstanceOfWithMessage_junit4_input() {
        org.junit.Assert.assertFalse("some msg", x instanceof Object);
    }

    void assertFalseWithInstanceOfWithMessage_junit4_expected() {
        assertThat(x).as("some msg").isNotInstanceOf(Object.class);
    }
    
    // Assert true/false instanceOf junit 5

    void assertTrueWithInstanceOf_junit5_input() {
        org.junit.jupiter.api.Assertions.assertTrue(x instanceof Object);
    }

    void assertTrueWithInstanceOf_junit5_expected() {
        assertThat(x).isInstanceOf(Object.class);
    }

    void assertTrueWithInstanceOfWithMessage_junit5_input() {
        org.junit.jupiter.api.Assertions.assertTrue(x instanceof Object, "some msg");
    }

    void assertTrueWithInstanceOfWithMessage_junit5_expected() {
        assertThat(x).as("some msg").isInstanceOf(Object.class);
    }

    void assertFalseWithInstanceOf_junit5_input() {
        org.junit.jupiter.api.Assertions.assertFalse(x instanceof Object);
    }

    void assertFalseWithInstanceOf_junit5_expected() {
        assertThat(x).isNotInstanceOf(Object.class);
    }

    void assertFalseWithInstanceOfWithMessage_junit5_input() {
        org.junit.jupiter.api.Assertions.assertFalse(x instanceof Object, "some msg");
    }

    void assertFalseWithInstanceOfWithMessage_junit5_expected() {
        assertThat(x).as("some msg").isNotInstanceOf(Object.class);
    }

}
