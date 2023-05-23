import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DebitCardTest {

    WebDriver driver;
    ChromeOptions options;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void teardown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldOrderSuccess() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Георгий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button_theme_alfa-on-white")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldInputNameFailed() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Georgiy Ivanov");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name] [class=input__sub]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldInputPhoneFailed() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Георгий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("89211112233");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone] [class=input__sub]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldUncheckedCheckboxTextChangedToRedColor() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Георгий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("button")).click();

        String expected = "rgba(255, 92, 92, 1)";
        String actual = driver.findElement(By.cssSelector("[data-test-id=agreement]")).getCssValue("color");
        assertEquals(expected, actual);
    }
}