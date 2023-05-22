package stepdefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.CommonMethods;
import utils.ConfigReader;
import utils.DatabaseUtils;
import utils.DriverUtils;

public class Hooks {

    @Before("@UI")
    public void setUp (Scenario scenario) {
        ConfigReader.initializeProperties();
        DriverUtils.createDriver(scenario);
        DatabaseUtils.initializeDBProperties();
    }

    @After("@UI")
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()){
            scenario.attach(CommonMethods.takeScreenshot(), "image/png", scenario.getName());
        }
        CommonMethods.takeScreenshot(scenario);
        DatabaseUtils.closeDataBaseConnection();
        DriverUtils.quitDriver(scenario);
    }
}
