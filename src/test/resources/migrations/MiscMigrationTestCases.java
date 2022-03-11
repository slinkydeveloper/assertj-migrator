import org.assertj.core.api.Assertions;

import java.util.*;

public class MiscMigrationTestCases {

    Object x = new Object();
    Object[] arr = new Object[]{x};

    List<Object> list = new ArrayList<>();
    Set<Object> set = new HashSet<>();
    Map<Object, Object> map = new HashMap<>();
    Optional<Object> optional = Optional.of(x);

    String str = "abc";
    
    int num = 10;

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

    void assertListSize0_input() {
        org.junit.jupiter.api.Assertions.assertEquals(0, list.size());
    }

    void assertListSize0_expected() {
        assertThat(list).isEmpty();
    }

    void assertSetSize0_input() {
        org.junit.jupiter.api.Assertions.assertEquals(0, set.size());
    }

    void assertSetSize0_expected() {
        assertThat(set).isEmpty();
    }

    void assertMapSize0_input() {
        org.junit.jupiter.api.Assertions.assertEquals(0, map.size());
    }

    void assertMapSize0_expected() {
        assertThat(map).isEmpty();
    }

    void assertNotListSize0_input() {
        org.junit.jupiter.api.Assertions.assertNotEquals(0, list.size());
    }

    void assertNotListSize0_expected() {
        assertThat(list).isNotEmpty();
    }

    void assertNotSetSize0_input() {
        org.junit.jupiter.api.Assertions.assertNotEquals(0, set.size());
    }

    void assertNotSetSize0_expected() {
        assertThat(set).isNotEmpty();
    }

    void assertNotMapSize0_input() {
        org.junit.jupiter.api.Assertions.assertNotEquals(0, map.size());
    }

    void assertNotMapSize0_expected() {
        assertThat(map).isNotEmpty();
    }


    void assertEmptyList_input() {
        org.junit.jupiter.api.Assertions.assertEquals(Collections.emptyList(), list);
    }

    void assertEmptyList_expected() {
        assertThat(list).isEmpty();
    }

    void assertEmptySet_input() {
        org.junit.jupiter.api.Assertions.assertEquals(Collections.emptySet(), set);
    }

    void assertEmptySet_expected() {
        assertThat(set).isEmpty();
    }

    void assertEmptyMap_input() {
        org.junit.jupiter.api.Assertions.assertEquals(Collections.emptyMap(), map);
    }

    void assertEmptyMap_expected() {
        assertThat(map).isEmpty();
    }

    void assertNotEmptyList_input() {
        org.junit.jupiter.api.Assertions.assertNotEquals(Collections.emptyList(), list);
    }

    void assertNotEmptyList_expected() {
        assertThat(list).isNotEmpty();
    }

    void assertNotEmptySet_input() {
        org.junit.jupiter.api.Assertions.assertNotEquals(Collections.emptySet(), set);
    }

    void assertNotEmptySet_expected() {
        assertThat(set).isNotEmpty();
    }

    void assertNotEmptyMap_input() {
        org.junit.jupiter.api.Assertions.assertNotEquals(Collections.emptyMap(), map);
    }

    void assertNotEmptyMap_expected() {
        assertThat(map).isNotEmpty();
    }

    void assertListContains_input() {
        org.junit.jupiter.api.Assertions.assertTrue(list.contains(x));
    }

    void assertListContains_expected() {
        assertThat(list).contains(x);
    }

    void assertNotListContains_input() {
        org.junit.jupiter.api.Assertions.assertFalse(list.contains(x));
    }

    void assertNotListContains_expected() {
        assertThat(list).doesNotContain(x);
    }

    void assertMapContainsKey_input() {
        org.junit.jupiter.api.Assertions.assertTrue(map.containsKey(x));
    }

    void assertMapContainsKey_expected() {
        assertThat(map).containsKey(x);
    }

    void assertNotMapContainsKey_input() {
        org.junit.jupiter.api.Assertions.assertFalse(map.containsKey(x));
    }

    void assertNotMapContainsKey_expected() {
        assertThat(map).doesNotContainKey(x);
    }

    void assertMapContainsValue_input() {
        org.junit.jupiter.api.Assertions.assertTrue(map.containsValue(x));
    }

    void assertMapContainsValue_expected() {
        assertThat(map).containsValue(x);
    }

    void assertNotMapContainsValue_input() {
        org.junit.jupiter.api.Assertions.assertFalse(map.containsValue(x));
    }

    void assertNotMapContainsValue_expected() {
        assertThat(map).doesNotContainValue(x);
    }

    void assertArrayLengthEquals_input() {
        org.junit.jupiter.api.Assertions.assertEquals(2, arr.length);
    }

    void assertArrayLengthEquals_expected() {
        assertThat(arr).hasSize(2);
    }

    void assertArrayLengthEquality_input() {
        org.junit.jupiter.api.Assertions.assertTrue(arr.length == 2);
    }

    void assertArrayLengthEquality_expected() {
        assertThat(arr).hasSize(2);
    }

    void assertArrayLengthEqualityWithNotEquals_input() {
        org.junit.jupiter.api.Assertions.assertNotEquals(true, arr.length == 2);
    }

    void assertArrayLengthEqualityWithNotEquals_expected() {
        assertThat(arr).size().isNotEqualTo(2);
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

    // Strings

    void assertEmptyString_input() {
        org.junit.jupiter.api.Assertions.assertEquals("", str);
    }

    void assertEmptyString_expected() {
        assertThat(str).isEmpty();
    }

    void assertStringContains_input() {
        org.junit.jupiter.api.Assertions.assertTrue(str.contains("abc"));
    }

    void assertStringContains_expected() {
        assertThat(str).contains("abc");
    }
    
    // Numeric bounds

    void assertGreater_input() {
        org.junit.jupiter.api.Assertions.assertTrue(num > 10);
    }

    void assertGreater_expected() {
        assertThat(num).isGreaterThan(10);
    }

    void assertGreaterEquals_input() {
        org.junit.jupiter.api.Assertions.assertTrue(num >= 10);
    }

    void assertGreaterEquals_expected() {
        assertThat(num).isGreaterThanOrEqualTo(10);
    }

    void assertLess_input() {
        org.junit.jupiter.api.Assertions.assertTrue(num < 10);
    }

    void assertLess_expected() {
        assertThat(num).isLessThan(10);
    }

    void assertLessEquals_input() {
        org.junit.jupiter.api.Assertions.assertTrue(num <= 10);
    }

    void assertLessEquals_expected() {
        assertThat(num).isLessThanOrEqualTo(10);
    }

}
