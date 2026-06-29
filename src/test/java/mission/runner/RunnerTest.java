package mission.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/java",
        glue = {"mission.steps", "mission.hooks"},
        plugin = {
                "pretty:test-output/cucumber-reports/cucumber-pretty.txt",
                "html:test-output/cucumber-reports/cucumber-report.html",
                "json:test-output/cucumber-reports/CucumberTestReport.json",
                "rerun:test-output/cucumber-reports/rerun.txt"
        }
)
public class RunnerTest extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
