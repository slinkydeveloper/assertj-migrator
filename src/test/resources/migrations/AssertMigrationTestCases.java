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

public class AssertMigrationTestCases {

    Object x = new Object();
    Object y = new Object();

    // Assert equality
    
    void assertEquality_input() {
        assert x == y;
    }

    void assertEquality_expected() {
        assertThat(x).isEqualTo(y);
    }

    // Assert instanceof

    void assertInstanceOf_input() {
        assert x instanceof Object;
    }

    void assertInstanceOf_expected() {
        assertThat(x).isInstanceOf(Object.class);
    }

    // Assert fallback

    void assertFallback_input() {
        assert java.util.function.Predicate.isEqual(x).test(y);
    }

    void assertFallback_expected() {
        assertThat(java.util.function.Predicate.isEqual(x).test(y)).isTrue();
    }

}
