package cucumber.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Steps {

    WebDriver webDriver;
    WebElement webElement;
    String selectedPath;
    static final String FILE_BASE_PATH = "src/test/resources/";

    public static void main(String[] args) throws IOException {

    }

    @Given("browser {string}")
    public void browser(String browserName) {

        switch (browserName) {
            case "Google Chrome":
                System.setProperty("webdriver.chrome.driver", new File("src/cucumberTest/resources/chromedriver").getAbsolutePath());
                webDriver = new ChromeDriver();
                webDriver.manage().window().maximize();
                webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                break;
        }

    }

    @When("I open {string}")
    public void i_open(String envUrl) {
        webDriver.get(envUrl);
    }

    @And("I set {string} value to {string}")
    public void i_set_value_to(String value, String field) {
        switch (field) {
            case "File description placeholder":
                webDriver.findElement(By.xpath("//input[@placeholder='Enter description here...']")).sendKeys(value);
                break;

            case "File description filter placeholder":
                webDriver.findElement(By.xpath("//*[@id=\"descriptionRadio\"]")).click();
                webDriver.findElement(By.xpath("//*[@id=\"searchText\"]")).sendKeys(value);
                break;
        }
    }

    @When("I click on {string}")
    public void i_click_on(String element) throws InterruptedException {
        switch (element) {
            case "Choose File button":
                webElement = webDriver.findElement(By.xpath("//input[@name='image']"));
                break;

            case "image.jpeg":
                selectedPath = new File(FILE_BASE_PATH + element).getAbsolutePath();
                break;

            case "Submit image button":
                webElement = webDriver.findElement(By.xpath("//button[contains(text(),'Upload')]"));
                webElement.click();
                break;

            case "Submit search":
                Thread.sleep(1000);
                webElement = webDriver.findElement(By.xpath("//*[@id=\"submitSearch\"]"));
                webElement.click();
                break;

        }
    }

    @When("I press on {string}")
    public void i_press_on(String key) {
        switch (key) {
            case "ENTER":
                webElement.sendKeys(selectedPath);
                break;
        }
    }

    @Then("I should see text {string} in {string}")
    public void i_should_see_text_in(String value, String field) {
        switch (field) {
            case "Response message placeholder":
                webElement = webDriver.findElement(By.xpath("//span[@id='responseMessagePlaceholder']"));
                break;
            case "File description error placeholder":
                webElement = webDriver.findElement(By.xpath("//span[@id='fileDescriptionErrorPlaceholder']"));
                break;
            case "search-results 1st row file description placeholder":
                webElement = webDriver.findElement(By.xpath("//*[@id=\"dataTable\"]/tbody"));
                webElement = webElement.findElements(By.tagName("tr")).get(0).findElements(By.tagName("td")).get(1);
        }
        assertEquals(value, webElement.getText());
    }

    @Then("existing records should be displayed")
    public void existing_records_should_be_displayed() {
        webElement = webDriver.findElement(By.xpath("//*[@id=\"dataTable\"]/tbody"));

        //prepare expected data
        String[] expectedDescriptionFields = new String[20];
        for (int i = 0; i < 20; i++) {
            expectedDescriptionFields[i] = "This image contains text : image " + (i + 1);
        }

        List<WebElement> tableRows = webElement.findElements(By.tagName("tr"));
        int rows_count = tableRows.size();
        for (int row = 0; row < rows_count; row++) {
            List<WebElement> columns = tableRows.get(row).findElements(By.tagName("td"));
            assertEquals(expectedDescriptionFields[row], columns.get(1).getText());
        }
    }

    @Then("the application shows up to {int} records")
    public void the_application_shows_up_to_records(Integer size) {
        assertEquals((long) size, webElement.findElements(By.tagName("tr")).size());
    }

    @Then("{string} should be displayed")
    public void should_be_displayed(String element) {
        switch (element) {
            case "search-results":
                webElement = webDriver.findElement(By.xpath("//*[@id=\"dataTable\"]/tbody"));
                assertTrue(!webElement.findElements(By.tagName("tr")).isEmpty());
                break;
        }
    }

    @Then("{string} has {int} result")
    public void has_result(String element, Integer value) throws InterruptedException {
        switch (element) {
            case "search-results":
                Thread.sleep(3000);
                webElement = webDriver.findElement(By.xpath("//*[@id=\"dataTable\"]/tbody"));
                assertEquals((long) value, webElement.findElements(By.tagName("tr")).size());
                break;
        }
    }

    @Then("I should see the image description, image size, and image file type")
    public void i_should_see_the_image_description_image_size_and_image_file_type() {
        webElement = webDriver.findElement(By.xpath("//*[@id=\"dataTable\"]/tbody"));
        WebElement row = webElement.findElements(By.tagName("tr")).get(0);
        List<WebElement> columns = row.findElements(By.tagName("td"));
        assertEquals("https://image-bucket-1.s3.us-east-2.amazonaws.com/image.jpeg", columns.get(0).getText());
        assertEquals("file description", columns.get(1).getText());
        assertEquals("JPEG", columns.get(2).getText());
        assertEquals("43592", columns.get(3).getText());

    }
}