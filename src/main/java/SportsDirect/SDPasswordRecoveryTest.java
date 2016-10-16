package SportsDirect;

import org.junit.Test;
import org.junit.After;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.logging.Logger;


/**
 * Write web test for password recovery on sportsdirect.com
 */
public class SDPasswordRecoveryTest {
    private static final Logger LOGGER = Logger.getLogger(SDPasswordRecoveryTest.class.getName());
    private static final By SIGNIN= By.id("dnn_dnnLOGIN_loginLink");
    private static final By LOGIN_FIELD= By.id("dnn_ctr16744866_Login_Login_AuthenticationProvider_registerLogin_txtExistingCustomerEmailAddress");
    private static final By PWD_FIELD= By.id("dnn_ctr16744866_Login_Login_AuthenticationProvider_registerLogin_txtPassword");
    private static final By LOGIN_BTN= By.id("dnn_ctr16744866_Login_Login_AuthenticationProvider_registerLogin_btnRegisteredCustomer");
    private static final By FRGT_PWD = By.id("dnn_ctr16744866_Login_Login_AuthenticationProvider_registerLogin_cmdForgottenPassword");
    private static final String EMAIL = "sdprcheck@gmail.com";
    private static final By EMAIL_FIELD= By.id("dnn_ctr16745005_PasswordReset_UserName");
    private static final By SEND_BTN = By.id("dnn_ctr16745005_PasswordReset_cmdSendPassword");
    private static final By SENT_CONFIRM_TEXT = By.id("dnn_ctr16745005_ctl00_lblMessage");

    private static final By GMAIL_FIELD = By.id("Email");
    private static final By GMAIL_NEXT_BTN = By.id("next");
    private static final By GMAIL_PWD = By.id("Passwd");
    private static final String PASSWORD = "Test1199";
    private static final By SIGNIN_BTN = By.id("signIn");
    private static final By LETTER_ID = By.xpath(".//*[@id=':3t']//*[contains(text(), 'Sports Direct')]");
    private static final By RECOVERY_LINK = By.xpath(".//*[contains(@href, 'PasswordReset')]");

    private static final By NEW_PWD = By.id("dnn_ctr16745005_PasswordReset_txtNewPassword");
    private static final By NEW_PWD_CONFIRM = By.id("dnn_ctr16745005_PasswordReset_txtConfirmNewPassword");
    private static final By CHANGE_PWD_BTN = By.id("dnn_ctr16745005_PasswordReset_lnkbtnChangePassword");
    private static final By CHANGED_CONFIRM_TEXT = By.id("dnn_ctr16745005_PasswordReset_SuccessText");
    private static final By ACCOUNT_MENU = By.id("topLinkMenu");

    private WebDriver driver = new ChromeDriver();


    @Test
    public void recovery(){
        LOGGER.info("User is going to SportsDirect website");
        driver.navigate().to("https://lv.sportsdirect.com/Login?returnurl=/");
        LOGGER.info("User navigated to SportsDirect SignIN page");

        //Set cookie to avoid advert popup
        Cookie advCookie = new Cookie("AdvertCookie", "true");
        driver.manage().addCookie(advCookie);
        LOGGER.info("Cookie to avoid advert is set");


        driver.findElement(FRGT_PWD).click();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_FIELD)).sendKeys(EMAIL);
        driver.findElement(SEND_BTN).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(SENT_CONFIRM_TEXT));
        LOGGER.info("User is going to SportsDirect website");
        driver.navigate().to("https://gmail.com");
        wait.until(ExpectedConditions.visibilityOfElementLocated(GMAIL_FIELD)).sendKeys(EMAIL);
        driver.findElement(GMAIL_NEXT_BTN).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(GMAIL_PWD)).sendKeys(PASSWORD);
        driver.findElement(SIGNIN_BTN).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(LETTER_ID)).click();
        String recoveryLink = wait.until(ExpectedConditions.visibilityOfElementLocated(RECOVERY_LINK)).getAttribute("href");
        driver.navigate().to(recoveryLink);

        try{
        Alert alert = driver.switchTo().alert();
        alert.accept();
        }catch(Exception e){}

        wait.until(ExpectedConditions.visibilityOfElementLocated(NEW_PWD)).sendKeys(PASSWORD);
        driver.findElement(NEW_PWD_CONFIRM).sendKeys(PASSWORD);
        driver.findElement(CHANGE_PWD_BTN).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(CHANGED_CONFIRM_TEXT));
        LOGGER.info("Password successfully changed");


        driver.navigate().to("http://lv.sportsdirect.com/?ctl=logoff");
        wait.until(ExpectedConditions.visibilityOfElementLocated(SIGNIN));
        driver.navigate().to("https://lv.sportsdirect.com/Login?returnurl=/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_FIELD)).sendKeys(EMAIL);
        driver.findElement(PWD_FIELD).sendKeys(PASSWORD);
        driver.findElement(LOGIN_BTN).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(ACCOUNT_MENU));
        LOGGER.info("User successfully logged in with changed password");
     }

    @After
    public void closeDriver(){
        driver.close();
    }
}
