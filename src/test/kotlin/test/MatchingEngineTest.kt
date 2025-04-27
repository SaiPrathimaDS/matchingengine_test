import listeners.TestListener
import listeners.WebDriverHolder
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.testng.Assert
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Listeners
import org.testng.annotations.Test
import pages.RepertoirePage
import java.net.URL

@Listeners(TestListener::class)
class MatchingEngineTest {

    private lateinit var page: RepertoirePage

    // TestNG will automatically call the setup method before each test method
    @BeforeMethod
    fun setUp() {
        // Add null check and initialization if needed
        if (WebDriverHolder.driver.get() == null) {
            // Dynamically configure WebDriver based on environment
            WebDriverHolder.driver.set(createDriver())
        }

        // Initialize the RepertoirePage using the WebDriver from WebDriverHolder
        page = RepertoirePage(WebDriverHolder.driver.get())
    }

    // Function to create the WebDriver with appropriate options
    private fun createDriver(): ChromeDriver {
        val options = ChromeOptions()

        // Check if the environment is Docker (headless)
        val isDocker = System.getenv("DOCKER_ENV")?.toBoolean() ?: false

        // Configure the driver for headless mode if running in Docker
        if (isDocker) {
            options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage", "--remote-debugging-port=9222")
        } else {
            // Local browser configuration (non-headless mode)
            options.addArguments("--start-maximized")
        }

        // Ensure the ChromeDriver path is set correctly for both environments
        return ChromeDriver(options)
    }

    // Test method to verify the Repertoire Management Module link
    @Test
    fun `verify Repertoire Management Module link`() {
        page.openHomePage()
        page.hoverModulesAndClickRepertoire()

        // Assert that the current URL matches the expected URL
        Assert.assertEquals(
            WebDriverHolder.driver.get().currentUrl,
            "https://www.matchingengine.com/repertoire-management-module/",
            "The URL does not match the expected Repertoire Management Module link!"
        )
    }

    // Test method to verify the functionality of the "Products Supported" section
    @Test
    fun `verify supported product list`() {
        page.openHomePage()
        page.hoverModulesAndClickRepertoire()
        page.scrollToAdditionalFeatures()
        page.clickProductsSupported()

        // Expected list of supported products
        val expectedProducts = listOf(
            "Cue Sheet / AV Work",
            "Recording",
            "Bundle",
            "Advertisement"
        )

        // Assert that the actual product list matches the expected list
        Assert.assertEquals(
            page.getSupportedProducts(),
            expectedProducts,
            "Product list does not match expected!"
        )
    }

    @AfterMethod
    fun tearDown() {
        // Clean up is handled by the TestListener
    }
}
