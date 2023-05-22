package utils;

import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DriverUtils {

    static WebDriver driver;

    public static void createDriver(Scenario scenario){

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        if(ConfigReader.getProperty("app.host").equalsIgnoreCase("local")) {

            if (driver == null) {
                switch (ConfigReader.getProperty("app.browser")) {

                    case "edge" -> {
                        WebDriverManager.edgedriver().setup();
                        driver = new EdgeDriver();
                    }
                    case "safari" -> {
                        WebDriverManager.safaridriver().setup();
                        driver = new SafariDriver();
                    }
                    case "firefox" -> {
                        WebDriverManager.firefoxdriver().setup();
                        driver = new FirefoxDriver();
                    }
                    default -> {

                        WebDriverManager.chromedriver().setup();
                        driver = new ChromeDriver(options);
                    }
                }
            }

        } else if (ConfigReader.getProperty("app.host").equalsIgnoreCase("saucelabs")){

            MutableCapabilities sauceOptions = new MutableCapabilities();
            sauceOptions.setCapability("username", ConfigReader.getProperty("sauce.username"));
            sauceOptions.setCapability("accessKey", ConfigReader.getProperty("sauce.accessKey"));

            MutableCapabilities capabilities = new MutableCapabilities();
            capabilities.setCapability("browserName", ConfigReader.getProperty("sauce.browserName"));
            capabilities.setCapability("browserVersion", ConfigReader.getProperty("sauce.browserVersion"));
            capabilities.setCapability("platformName", ConfigReader.getProperty("sauce.platformName"));
            capabilities.setCapability("sauce:options", sauceOptions);

            try {
                driver = new RemoteWebDriver(new URL(ConfigReader.getProperty("sauce.urlWest")), capabilities); // URL Constructor throws the MalformedURLException
                ((JavascriptExecutor)driver).executeScript("sauce:job-name=" + scenario.getName());
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.get(ConfigReader.getProperty("app.baseurl"));
    }

    public static void quitDriver(Scenario scenario){
        // below is the outer "if" statement, which is needed to check whether the run is even happening on the Sauce Labs (basically checking: is scenario running or not?)
        if (ConfigReader.getProperty("dbank.host").equalsIgnoreCase("saucelabs")){

            // and this inner "if" statement is needed to determine whether the scenario being run is failed, or is passed and providing the scenario's data to Sauce Labs
            if(scenario.isFailed()){
                ((JavascriptExecutor)driver).executeScript("sauce:job-result=failed");
            } else {
                ((JavascriptExecutor)driver).executeScript("sauce:job-result=passed");
            }
        }
        driver.quit();
    }

    public static WebDriver getDriver() {
        return driver;
    }
}
