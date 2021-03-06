package io.agibalov;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@ExtendWith({
        DummyTest.TestPrinterExtension.class,
        // extracted these to meta-annotation EnableDummyParameterResolverExtensionAndDummyTestInstancePostProcessor
        // DummyTest.DummyParameterResolverExtension.class,
        // DummyTest.DummyTestInstancePostProcessor.class
})
@DummyTest.EnableDummyParameterResolverExtensionAndDummyTestInstancePostProcessor
public class DummyTest {
    public int injectedValue;

    @BeforeEach
    void init(TestInfo testInfo) {
        System.out.printf("testInfo: %s::%s - %s\n", testInfo.getTestClass(), testInfo.getTestMethod(), testInfo.getDisplayName());
    }

    @Test
    @DisplayName("1+2 should be 3")
    void oneAndTwoShouldBeThree() {
        assertEquals(3, 1 + 2);
    }

    @TestFactory
    List<DynamicTest> allTheseWordsShouldBe3LetterLong() {
        List<DynamicTest> tests = new ArrayList<>();
        for(String word : new String[] { "age", "you", "hey" }) {
            tests.add(dynamicTest(String.format("%s should be %d letters long", word, 3), () -> {
                assertEquals(3, word.length());
            }));
        }
        return tests;
    }

    @Test
    void shouldInject123(@PleaseInject123Here int x, @PleaseInject123Here int y) {
        assertEquals(123, x);
        assertEquals(123, y);
    }

    @Test
    void injectedValueShouldBe222() {
        assertEquals(222, injectedValue);
    }

    @Nested
    class NestedLevelOneTest {
        @Test
        void levelOneTest() {
        }

        @Nested
        class NestedLevelTwoTest {
            @Test
            void levelTwoTest(TestReporter testReporter) {
                testReporter.publishEntry("hey", "I'm reporting something");
            }
        }
    }

    public static class TestPrinterExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {
        @Override
        public void beforeTestExecution(ExtensionContext context) {
            String displayName = context.getDisplayName();
            System.out.printf("Executing %s...\n", displayName);
        }

        @Override
        public void afterTestExecution(ExtensionContext context) {
            String displayName = context.getDisplayName();
            System.out.printf("Executed %s\n", displayName);
        }
    }

    public static class DummyParameterResolverExtension implements ParameterResolver {
        @Override
        public boolean supportsParameter(
                ParameterContext parameterContext,
                ExtensionContext extensionContext) throws ParameterResolutionException {

            return parameterContext.getParameter().getAnnotation(PleaseInject123Here.class) != null;
        }

        @Override
        public Object resolveParameter(
                ParameterContext parameterContext,
                ExtensionContext extensionContext) throws ParameterResolutionException {

            return 123;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface PleaseInject123Here {
    }

    public static class DummyTestInstancePostProcessor implements TestInstancePostProcessor {
        @Override
        public void postProcessTestInstance(
                Object testInstance, // test class object is injected here
                ExtensionContext context) throws Exception {

            if(!(testInstance instanceof DummyTest)) {
                return;
            }

            ((DummyTest)testInstance).injectedValue = 222;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @ExtendWith({
            DummyTest.DummyParameterResolverExtension.class,
            DummyTest.DummyTestInstancePostProcessor.class
    })
    public @interface EnableDummyParameterResolverExtensionAndDummyTestInstancePostProcessor {
    }
}
