package listeners

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.testng.ITestContext
import org.testng.ITestListener
import org.testng.ITestResult
import io.github.bonigarcia.wdm.WebDriverManager
import java.util.logging.Logger

object WebDriverHolder {
    val driver: ThreadLocal<WebDriver> = ThreadLocal()
}

class TestListener : ITestListener {
    private val logger = Logger.getLogger(TestListener::class.java.name)

    override fun onStart(context: ITestContext) {
        logger.info("Starting test suite: ${context.name}")
        // Initialize WebDriver at suite start to ensure it's available
        setupWebDriver()
    }

    override fun onTestStart(result: ITestResult) {
        // Ensure WebDriver is available for each test
        if (WebDriverHolder.driver.get() == null) {
            setupWebDriver()
        }
    }

    override fun onTestSuccess(result: ITestResult) {
        // Don't clean up after each test - let it run for the suite
    }

    override fun onTestFailure(result: ITestResult) {
        // Optional: Take screenshot on failure
        logger.warning("Test failed: ${result.name}")
    }

    override fun onTestSkipped(result: ITestResult) {
        // No action needed
    }

    override fun onFinish(context: ITestContext) {
        cleanupWebDriver()
        logger.info("Finished test suite: ${context.name}")
    }

    private fun setupWebDriver() {
        try {
            WebDriverManager.chromedriver().setup()
            val driver = ChromeDriver().apply {
                manage().window().maximize()
            }
            WebDriverHolder.driver.set(driver)
            logger.info("WebDriver initialized successfully")
        } catch (e: Exception) {
            logger.severe("Failed to set up WebDriver: ${e.message}")
            throw e
        }
    }

    private fun cleanupWebDriver() {
        try {
            WebDriverHolder.driver.get()?.quit()
        } catch (e: Exception) {
            logger.warning("Error while quitting WebDriver: ${e.message}")
        } finally {
            WebDriverHolder.driver.remove()
        }
    }
}