import static org.assertj.core.api.Assertions.assertThat;

public class AssertMigrationTestCases {

  Integer xInt = 1;
  int yInt = 2;
  Object x = new Object();
  Object y = new Object();

  // Assert equality

  void assertEquality_input() {
    assert x == y;
  }

  void assertEquality_expected() {
    assertThat(x).isSameAs(y);
  }

  void assertEqualityPrimitive_input() {
    assert xInt == yInt;
  }

  void assertEqualityPrimitive_expected() {
    assertThat(xInt).isEqualTo(yInt);
  }

  void assertNotEquality_input() {
    assert x != y;
  }

  void assertNotEquality_expected() {
    assertThat(x).isNotSameAs(y);
  }

  void assertNotEqualityPrimitive_input() {
    assert xInt != yInt;
  }

  void assertNotEqualityPrimitive_expected() {
    assertThat(xInt).isNotEqualTo(yInt);
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
