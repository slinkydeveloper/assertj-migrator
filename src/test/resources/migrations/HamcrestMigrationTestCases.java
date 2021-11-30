import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.Assertions.fail;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.*;

public class HamcrestMigrationTestCases {

    Object x = new Object();
    Class xClass = Object.class;
    Object y = new Object();

    Object[] xArray = new Object[]{};
    Object[] yArray = new Object[]{};

    List<Object> xList = new ArrayList<>();

    Boolean xBoolean = false;
    Float xFloat = 10f;
    Float yFloat = 10f;
    float deltaFloat = 10f;
    Double xDouble = 10d;
    Double yDouble = 10d;
    double deltaDouble = 10d;

    boolean b = true;

    Matcher<?> myMatcher = new BaseMatcher<Iterable<? super Object>>() {
        @Override
        public boolean matches(Object o) {
            return true;
        }

        @Override
        public void describeTo(Description description) {}
    };

    // Assert is/isEqualTo/not is/not is equal to
    
    void assertIs_input() {
        assertThat(x, is(y));
    }

    void assertIs_expected() {
        assertThat(x).isEqualTo(y);
    }

    void assertIsWithMessage_input() {
        assertThat("some msg", x, is(y));
    }

    void assertIsWithMessage_expected() {
        assertThat(x).as("some msg").isEqualTo(y);
    }

    void assertIsAllStringsWithMessage_input() {
        assertThat("some msg", "x", is("y"));
    }

    void assertIsAllStringsWithMessage_expected() {
        assertThat("x").as("some msg").isEqualTo("y");
    }

    void assertNotIs_input() {
        assertThat(x, not(is(y)));
    }

    void assertNotIs_expected() {
        assertThat(x).isNotEqualTo(y);
    }

    void assertNotIsWithMessage_input() {
        assertThat("some msg", x, not(is(y)));
    }

    void assertNotIsWithMessage_expected() {
        assertThat(x).as("some msg").isNotEqualTo(y);
    }

    void assertIsEqualTo_input() {
        assertThat(x, equalTo(y));
    }

    void assertIsEqualTo_expected() {
        assertThat(x).isEqualTo(y);
    }

    void assertIsEqualToWithMessage_input() {
        assertThat("some msg", x, equalTo(y));
    }

    void assertIsEqualToWithMessage_expected() {
        assertThat(x).as("some msg").isEqualTo(y);
    }

    void assertIsEqualToAllStringsWithMessage_input() {
        assertThat("some msg", "x", equalTo("y"));
    }

    void assertIsEqualToAllStringsWithMessage_expected() {
        assertThat("x").as("some msg").isEqualTo("y");
    }

    void assertNotIsEqualTo_input() {
        assertThat(x, not(equalTo(y)));
    }

    void assertNotIsEqualTo_expected() {
        assertThat(x).isNotEqualTo(y);
    }

    void assertNotIsEqualToWithMessage_input() {
        assertThat("some msg", x, not(equalTo(y)));
    }

    void assertNotIsEqualToWithMessage_expected() {
        assertThat(x).as("some msg").isNotEqualTo(y);
    }

    // Assert null/not null

    void assertNullValue_input() {
        assertThat(x, nullValue());
    }

    void assertNullValue_expected() {
        assertThat(x).isNull();
    }

    void assertNullValueWithMessage_input() {
        assertThat("some msg", x, nullValue());
    }

    void assertNullValueWithMessage_expected() {
        assertThat(x).as("some msg").isNull();
    }

    void assertNotNullValue_input() {
        assertThat(x, notNullValue());
    }

    void assertNotNullValue_expected() {
        assertThat(x).isNotNull();
    }

    void assertNotNullValueWithMessage_input() {
        assertThat("some msg", x, not(nullValue()));
    }

    void assertNotNullValueWithMessage_expected() {
        assertThat(x).as("some msg").isNotNull();
    }

    void assertNotNullValueNested_input() {
        assertThat(x, notNullValue());
    }

    void assertNotNullValueNested_expected() {
        assertThat(x).isNotNull();
    }

    void assertNotNullValueNestedWithMessage_input() {
        assertThat("some msg", x, not(nullValue()));
    }

    void assertNotNullValueNestedWithMessage_expected() {
        assertThat(x).as("some msg").isNotNull();
    }

    // Assert same instance/not same instance

    void assertSameInstance_input() {
        assertThat(x, sameInstance(y));
    }

    void assertSameInstance_expected() {
        assertThat(x).isSameAs(y);
    }

    void assertSameInstanceWithMessage_input() {
        assertThat("some msg", x, sameInstance(y));
    }

    void assertSameInstanceWithMessage_expected() {
        assertThat(x).as("some msg").isSameAs(y);
    }

    void assertNotSameInstance_input() {
        assertThat(x, not(sameInstance(y)));
    }

    void assertNotSameInstance_expected() {
        assertThat(x).isNotSameAs(y);
    }

    void assertNotSameInstanceWithMessage_input() {
        assertThat("some msg", x, not(sameInstance(y)));
    }

    void assertNotSameInstanceWithMessage_expected() {
        assertThat(x).as("some msg").isNotSameAs(y);
    }

    // Assert instanceOf/not instance of

    void assertInstanceOfWithClassLiteral_input() {
        assertThat(x, instanceOf(Object.class));
    }

    void assertInstanceOfWithClassLiteral_expected() {
        assertThat(x).isInstanceOf(Object.class);
    }

    void assertInstanceOfWithClassLiteralWithMessage_input() {
        assertThat("some msg", x, instanceOf(Object.class));
    }

    void assertInstanceOfWithClassLiteralWithMessage_expected() {
        assertThat(x).as("some msg").isInstanceOf(Object.class);
    }

    void assertInstanceOfWithClassVariable_input() {
        assertThat(x, instanceOf(xClass));
    }

    void assertInstanceOfWithClassVariable_expected() {
        assertThat(x).isInstanceOf(xClass);
    }

    void assertInstanceOfWithClassVariableWithMessage_input() {
        assertThat("some msg", x, instanceOf(xClass));
    }

    void assertInstanceOfWithClassVariableWithMessage_expected() {
        assertThat(x).as("some msg").isInstanceOf(xClass);
    }

    void assertNotInstanceOfWithClassLiteral_input() {
        assertThat(x, not(instanceOf(Object.class)));
    }

    void assertNotInstanceOfWithClassLiteral_expected() {
        assertThat(x).isNotInstanceOf(Object.class);
    }

    void assertNotInstanceOfWithClassLiteralWithMessage_input() {
        assertThat("some msg", x, not(instanceOf(Object.class)));
    }

    void assertNotInstanceOfWithClassLiteralWithMessage_expected() {
        assertThat(x).as("some msg").isNotInstanceOf(Object.class);
    }

    void assertNotInstanceOfWithClassVariable_input() {
        assertThat(x, not(instanceOf(xClass)));
    }

    void assertNotInstanceOfWithClassVariable_expected() {
        assertThat(x).isNotInstanceOf(xClass);
    }

    void assertNotInstanceOfWithClassVariableWithMessage_input() {
        assertThat("some msg", x, not(instanceOf(xClass)));
    }

    void assertNotInstanceOfWithClassVariableWithMessage_expected() {
        assertThat(x).as("some msg").isNotInstanceOf(xClass);
    }

    // Assert less/less or equal/greater/greater or equal

    void assertLess_input() {
        assertThat(xFloat, lessThan(yFloat));
    }

    void assertLess_expected() {
        assertThat(xFloat).isLessThan(yFloat);
    }

    void assertLessOrEqual_input() {
        assertThat(xFloat, lessThanOrEqualTo(yFloat));
    }

    void assertLessOrEqual_expected() {
        assertThat(xFloat).isLessThanOrEqualTo(yFloat);
    }

    void assertGreater_input() {
        assertThat(xFloat, greaterThan(yFloat));
    }

    void assertGreater_expected() {
        assertThat(xFloat).isGreaterThan(yFloat);
    }

    void assertGreaterOrEqual_input() {
        assertThat(xFloat, greaterThanOrEqualTo(yFloat));
    }

    void assertGreaterOrEqual_expected() {
        assertThat(xFloat).isGreaterThanOrEqualTo(yFloat);
    }
    
    // Assert has item/has itmes/not has item/not has items

    void assertHasItem_input() {
        assertThat(xList, hasItem(y));
    }

    void assertHasItem_expected() {
        assertThat(xList).contains(y);
    }

    void assertNotHasItem_input() {
        assertThat(xList, not(hasItem(y)));
    }

    void assertNotHasItem_expected() {
        assertThat(xList).doesNotContain(y);
    }

    void assertHasItems_input() {
        assertThat(xList, hasItems(x, y));
    }

    void assertHasItems_expected() {
        assertThat(xList).contains(x, y);
    }

    void assertNotHasItems_input() {
        assertThat(xList, not(hasItems(x, y)));
    }

    void assertNotHasItems_expected() {
        assertThat(xList).doesNotContain(x, y);
    }

    // Assert contains string/not contains string/starts with/not starts with/ends with/not ends with

    void assertContainsString_input() {
        assertThat("abc", containsString("b"));
    }

    void assertContainsString_expected() {
        assertThat("abc").contains("b");
    }

    void assertNotContainsString_input() {
        assertThat("abc", not(containsString("d")));
    }

    void assertNotContainsString_expected() {
        assertThat("abc").doesNotContain("d");
    }

    void assertStartsWith_input() {
        assertThat("abc", startsWith("a"));
    }

    void assertStartsWith_expected() {
        assertThat("abc").startsWith("a");
    }

    void assertNotStartsWith_input() {
        assertThat("abc", not(startsWith("b")));
    }

    void assertNotStartsWith_expected() {
        assertThat("abc").doesNotStartWith("b");
    }

    void assertEndsWith_input() {
        assertThat("abc", endsWith("c"));
    }

    void assertEndsWith_expected() {
        assertThat("abc").endsWith("c");
    }

    void assertNotEndsWith_input() {
        assertThat("abc", not(endsWith("b")));
    }

    void assertNotEndsWith_expected() {
        assertThat("abc").doesNotEndWith("b");
    }
    
    // Fallback

    void assertFallback_input() {
        assertThat(xList, allOf(hasItem(x), myMatcher));
    }

    void assertFallback_expected() {
        assertThat(xList).satisfies(matching(allOf(hasItem(x), myMatcher)));
    }

}
