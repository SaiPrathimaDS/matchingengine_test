package pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class RepertoirePage(private val driver: WebDriver) {

    private val wait = WebDriverWait(driver, Duration.ofSeconds(10))

    // Open the home page of the Matching Engine website
    fun openHomePage() {
        driver.get("https://www.matchingengine.com/")
    }
    // Hover over the "Modules" link and click on "Repertoire Management Module" link to navigate to the Repertoire Management Module page.
    fun hoverModulesAndClickRepertoire() {
        try {
            // Locate the "Modules" link
            val modules = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@href, '#Modules')]")))

            // Hover over the "Modules" link
            Actions(driver).moveToElement(modules).perform()

            // Locate and click the "Repertoire Management Module" link
            val repertoire = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, 'repertoire-management-module')]")))
            repertoire.click()
        } catch (e: Exception) {
            throw RuntimeException("Failed to hover and click on Repertoire Management Module: ${e.message}")
        }
    }
    // Scroll to the "Additional Features" section of the page.
    fun scrollToAdditionalFeatures() {
        try {
            val section = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h2[contains(@class, 'vc_custom_heading') and contains(text(), 'Additional Features')]")))
            (driver as org.openqa.selenium.JavascriptExecutor).executeScript("arguments[0].scrollIntoView(true);", section)
        } catch (e: Exception) {
            throw RuntimeException("Failed to scroll to Additional Features section: ${e.message}")
        }
    }
    // Click on the "Products Supported" link to view the list of supported products.
    fun clickProductsSupported() {
        try {
            val link = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(@class, 'vc_tta-title-text') and text()='Products Supported']")))
            link.click()
        } catch (e: Exception) {
            throw RuntimeException("Failed to click on Products Supported: ${e.message}")
        }
    }
    // Retrieve the list of supported products from the "Products Supported" section.
    fun getSupportedProducts(): List<String> {
        return try {
            val ul = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h3[contains(text(), 'Product Supported:')]/following::ul[1]")
            ))
            ul.findElements(By.tagName("li")).map { it.text.trim() }.filter { it.isNotEmpty() }
        } catch (e: Exception) {
            throw RuntimeException("Failed to retrieve supported products: ${e.message}")
        }
    }
}