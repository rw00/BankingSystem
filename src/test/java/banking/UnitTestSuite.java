package banking;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        SampleTest.class,
        HiddenTest.class,
        ConcurrentTest.class
})
public class UnitTestSuite {
}
