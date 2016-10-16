package SportsDirect;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import java.util.logging.Logger;


/**
 * Create web test which would check whether item filter
 * on sportsdirect.com is working properly.
 * Open mens shoes section and filter by brands "Skechers" and "Firetrap"
 * and price in range 30 - 60 EUR. Check items are correctly filtered!
 */

public class SportsDirectTest {
    private static final Logger LOGGER = Logger.getLogger(SportsDirectTest.class.getName());
    private static final By MENU_BTN = By.id("trigger");
    private static final By MENS_SECTION = By.xpath(".//*[@class='has-dropdown']//*[@class='menuitemtext' and text()='Mens']");
    private static final By MENS_SHOES = By.xpath(".//*[@href='/mens/mens-shoes']//*[@class='menuitemtext' and text()='Shoes']");
    private static final By FILTER = By.id("filterByMob");
    private static final By BRAND_FILTER = By.id("dnn_ctr16744878_ViewTemplate_ctl00_ctl13_lstFilters_CollapseDiv_0");
    private static final By SKECHERS = By.xpath(".//*[@data-productname='Skechers']");
    private static final By FIRETRAP = By.xpath(".//*[@data-productname='Firetrap']");
    private static final By PRICE_FILTER = By.id("dnn_ctr16744878_ViewTemplate_ctl00_ctl13_lstFilters_CollapseDiv_4");
    private static final By PRICE_MIN = By.id("PriceFilterTextEntryMin");
    private static final By PRICE_MAX = By.id("PriceFilterTextEntryMax");
    private static final By PRICE_FLTR_BTN = By.id("PriceFilterTextEntryApply");
    private static final By APPLY_FLTR_BTN = By.id("mobappfltrs");
    private static final By CONTAINER = By.xpath(".//*[@id='ProductContainer' and @style='opacity: 1;']");

    private static final By ITEM_CARD = By.className("s-productthumbbox");
    private static final By ITEM_TEXT = By.className("productdescriptionbrand");
    private static final By ITEM_PRICE = By.xpath(".//*[contains(@class, 'curprice')]");

    private WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, 10);

    @Test
    public void filterCheck(){

        LOGGER.info("User is going to SportsDirect website");
        driver.navigate().to("http://lv.sportsdirect.com//");
        LOGGER.info("User navigated to SportsDirect website");

        setAdvCookie();
        menuClick();
        setFilter();
        checkFilter();
    }

    private void setAdvCookie() {
        //Set cookie to avoid advert popup
        Cookie advCookie = new Cookie("AdvertCookie", "true");
        driver.manage().addCookie(advCookie);
        LOGGER.info("Cookie to avoid advert is set");
    }

    private void menuClick() {
        driver.findElement(MENU_BTN).click();
        LOGGER.info("User clicked Menu button");

        wait.until(ExpectedConditions.visibilityOfElementLocated(MENS_SECTION)).click();
        LOGGER.info("User clicked Mens section menu");
        //Pause used for waiting until Shoes menu is visible _in viewport_ and clickable
        safeSleep(500);
        driver.findElement(MENS_SHOES).click();
        LOGGER.info("User clicked Mens shoes menu");
    }

    private void setFilter() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(FILTER)).click();
        LOGGER.info("User clicked Filter button");
        wait.until(ExpectedConditions.visibilityOfElementLocated(BRAND_FILTER)).click();
        LOGGER.info("User clicked Brand filter");
        wait.until(ExpectedConditions.visibilityOfElementLocated(SKECHERS)).click();
        LOGGER.info("User clicked SKECHERS checkbox");
        wait.until(ExpectedConditions.visibilityOfElementLocated(FIRETRAP)).click();
        LOGGER.info("User clicked FIRETRAP checkbox");
        driver.findElement(PRICE_FILTER).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(PRICE_MIN)).sendKeys("30");
        driver.findElement(PRICE_MAX).sendKeys("60");
        driver.findElement(PRICE_FLTR_BTN).click();
        driver.findElement(APPLY_FLTR_BTN).click();
        LOGGER.info("User entered price range");
        //wait until filter has applied
        wait.until(ExpectedConditions.visibilityOfElementLocated(CONTAINER));
    }

    private void checkFilter() {
        //checking items brands and prices
        List<WebElement> items = driver.findElements(ITEM_CARD);
        for (WebElement item : items){
            String priceText = item.findElement(ITEM_PRICE).getText().replace(",", ".");
            String text = item.findElement(ITEM_TEXT).getText();
            Assert.assertTrue("Brand filter worked incorrectly. Brand found: " + text, text.contains("Skechers") || text.contains("Firetrap"));

            Float price = Float.parseFloat(priceText.substring(0, priceText.length()-2));
            Assert.assertTrue("Price filter worked incorrectly. Price found: "+price, price>=30 && price <=60);
        }
        LOGGER.info("Brand and price filter worked correctly");
    }

    //pause for waiting
    private void safeSleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            LOGGER.warning("Interrupted!");
            throw new RuntimeException(e);
        }
    }

    @After
    public void closeDriver(){
          driver.close();
    }
}
