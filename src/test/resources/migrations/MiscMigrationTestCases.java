import org.assertj.core.api.Assertions;

import java.util.*;

public class MiscMigrationTestCases {

    Object x = new Object();
    
    List<Object> list = new ArrayList<>();
    Set<Object> set = new HashSet<>();
    Map<Object, Object> map = new HashMap<>();
    Optional<Object> optional = Optional.of(x);

    // Asserts on collections

    void assertSingletonList_input() {
        org.junit.jupiter.api.Assertions.assertEquals(Collections.singletonList(x), list);
    }

    void assertSingletonList_expected() {
        assertThat(list).containsExactly(x);
    }

    void assertNotSingletonList_input() {
        org.junit.jupiter.api.Assertions.assertNotEquals(Collections.singletonList(x), list);
    }

    void assertNotSingletonList_expected() {
        assertThat(list).hasSize(1).doesNotContain(x);
    }

    void assertSingletonSet_input() {
        org.junit.jupiter.api.Assertions.assertEquals(Collections.singleton(x), set);
    }

    void assertSingletonSet_expected() {
        assertThat(set).containsExactly(x);
    }

    void assertNotSingletonSet_input() {
        org.junit.jupiter.api.Assertions.assertNotEquals(Collections.singleton(x), set);
    }

    void assertNotSingletonSet_expected() {
        assertThat(set).hasSize(1).doesNotContain(x);
    }

    void assertListSize_input() {
        org.junit.jupiter.api.Assertions.assertEquals(1, list.size());
    }

    void assertListSize_expected() {
        assertThat(list).hasSize(1);
    }

    void assertSetSize_input() {
        org.junit.jupiter.api.Assertions.assertEquals(1, set.size());
    }

    void assertSetSize_expected() {
        assertThat(set).hasSize(1);
    }

    void assertMapSize_input() {
        org.junit.jupiter.api.Assertions.assertEquals(1, map.size());
    }

    void assertMapSize_expected() {
        assertThat(map).hasSize(1);
    }

    void assertNotListSize_input() {
        org.junit.jupiter.api.Assertions.assertNotEquals(1, list.size());
    }

    void assertNotListSize_expected() {
        assertThat(list).size().isNotEqualTo(1);
    }

    void assertNotSetSize_input() {
        org.junit.jupiter.api.Assertions.assertNotEquals(1, set.size());
    }

    void assertNotSetSize_expected() {
        assertThat(set).size().isNotEqualTo(1);
    }

    void assertNotMapSize_input() {
        org.junit.jupiter.api.Assertions.assertNotEquals(1, map.size());
    }

    void assertNotMapSize_expected() {
        assertThat(map).size().isNotEqualTo(1);
    }

    // Equality with null

    void assertNull_input() {
        org.junit.jupiter.api.Assertions.assertTrue(x == null);
    }

    void assertNull_expected() {
        assertThat(x).isNull();
    }

    void assertNotNull_input() {
        org.junit.jupiter.api.Assertions.assertTrue(x != null);
    }

    void assertNotNull_expected() {
        assertThat(x).isNotNull();
    }

    void assertNotNotNull_input() {
        org.junit.jupiter.api.Assertions.assertFalse(x != null);
    }

    void assertNotNotNull_expected() {
        assertThat(x).isNull();
    }

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
    
    // Assert optional

    void assertIsPresent_input() {
        org.junit.jupiter.api.Assertions.assertTrue(optional.isPresent());
    }

    void assertIsPresent_expected() {
        assertThat(optional).isPresent();
    }

    void assertIsNotPresent_input() {
        org.junit.jupiter.api.Assertions.assertFalse(optional.isPresent());
    }

    void assertIsNotPresent_expected() {
        assertThat(optional).isNotPresent();
    }

}
