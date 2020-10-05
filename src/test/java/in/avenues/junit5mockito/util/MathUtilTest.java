package in.avenues.junit5mockito.util;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Running MathUtil")
class MathUtilTest {

    private MathUtil mathUtil;

    @BeforeAll
        // if class instance is created just once then static in not required.
    void beforeAllInit() {
        System.out.println("before all init method is called!");

    }

    @BeforeEach
    void init() {
        mathUtil = new MathUtil();
    }

    @AfterEach
    void cleanUp() {
        System.out.println("clean up oparetions!");
    }


    @Nested
    @DisplayName("add method")
    class AddTest {
        @Test
        @DisplayName("when adding two positive numbers")
        void testAddPositive() {
            //	fail("fucked up");

            boolean isServerUp = false;
            //assumeTrue(isServerUp);

            int expected = 2;
            int actual = mathUtil.add(1, 1);
            assertEquals(expected, actual, () -> "should return sum" + expected + " but returned " + actual);
        }

        @Test
        @DisplayName("when adding two negative numbers")
        void testAddNegative() {
            assertEquals(-2, mathUtil.add(-1, -1), "should return right sum");
        }
    }


    @Test
    void testDevice() {
        assertThrows(ArithmeticException.class, () -> mathUtil.divide(5, 0), "Divide by zero should throw");
    }

    @Test
    @DisplayName("Multiplication Method: ")
    void testMultiply() {
        assertAll(
                () -> assertEquals(4, mathUtil.multiply(2, 2)),
                () -> assertEquals(0, mathUtil.multiply(0, 2)),
                () -> assertEquals(-6, mathUtil.multiply(2, -3))
        );
    }

    @RepeatedTest(2)
    void testCoumputeCircleRadius(RepetitionInfo repetitionInfo) {
        System.out.println(repetitionInfo.getCurrentRepetition());
        assertEquals(314.1592653589793, mathUtil.computeCircleRadius(10), "Should return circle area");
    }

    @Test
    @Disabled
    @DisplayName("TDD method. Should not run")
    void testDisable() {
        fail("This test should be desable!");
    }

}