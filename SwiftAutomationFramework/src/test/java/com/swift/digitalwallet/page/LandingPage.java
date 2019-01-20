package com.swift.digitalwallet.page;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import java.io.IOException;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.selenium.endpoint.SeleniumBrowser;
import com.consol.citrus.selenium.model.*;
import com.swift.utils.GenericComponents;
import com.swift.utils.GlobalVariables;
import com.swift.utils.GlobalVariables.*;
/**
 * @author skrishnan
 */
public class LandingPage extends TestNGCitrusTestRunner implements WebPage, PageValidator<LandingPage> {
	@Autowired(required = true)
	public SeleniumBrowser browser;
	@Autowired
	Environment testProp;
	
	// Card login
	@FindBy(id = "Login.Tab.CardNum")
	public static WebElement tabCard;
	@FindBy(name = "CardNumber")
	public static WebElement txtCardNumber;
	@FindBy(id = "SecurityCode")
	public static WebElement txtSecurityCode;
	@FindBy(name = "Login")
	public static WebElement btnCardLogin;
	// token entry
	@FindBy(id = "Login.Tab.Token")
	public static WebElement tabToken;
	@FindBy(name = "Code")
	public  static WebElement txtTokenCode;
	@FindBy(id = "Login.Btn.Token")
	public static WebElement btnTokenSubmit;
	@FindBy(name = "ConfirmPassword")
	public static WebElement txtConfirmPassword;
	// login with username
	@FindBy(id = "Login.Tab.Username")
	public static WebElement tabUsername;
	@FindBy(name = "UserName")
	public  static WebElement txtUsername;
	@FindBy(name = "Password")
	public static WebElement txtPassword;
	@FindBy(id = "Login.Btn.Username")
	public static WebElement btnLoginUsername;
	// Unauthenticated - forgot password
	@FindBy(linkText = "Click here")
	public static WebElement lnkForgotPassword;
	@FindBy(name = "Username")
	public static WebElement txtRecoveryUsername;
	@FindBy(xpath = "//button[@type='submit']")
	public static WebElement btnRecoverySubmit;
	@FindBy(name = "Questions[0].SecutiryQuestionAnswer")
	public static WebElement txtSecurityQuestion;
	@FindBy(xpath = "//button[.='Done']")
	public static WebElement btnDone;
	// Unauthenticated - Contact us
	@FindBy(linkText = "Contact Us")
	public static WebElement lnkContactUs;
	@FindBy(name = "FirstName")
	public static WebElement txtFirstName;
	@FindBy(name = "LastName")
	public static WebElement txtLastName;
	@FindBy(name = "EmailAddress")
	public static WebElement txtEmail;
	@FindBy(name = "PhoneNumber")
	public static WebElement txtPhoneNumber;
	@FindBy(name = "ReasonId")
	public static WebElement ddlReason;
	@FindBy(name = "QuestionComments")
	public static WebElement txtQuestionsComments;
	@FindBy(name = "Submit")
	public static WebElement btnSubmit;
	@FindBy(id = "divSuccessMsg")
	public static WebElement msgSuccess;
	@FindBy(linkText = "Continue")
	public static WebElement btnContactUsContinue;
	// logout     
	@FindBy(linkText = "Log Out")
	public static WebElement lnkLogout;
	@FindBy(xpath = "//button[contains(text(), 'Log Out')]")
	public static WebElement btnLogout;

	public void entertokenData(String tokenValue) throws NullPointerException, IOException {
		tabToken.click();
		GlobalVariables.testInfo.pass("Token Tab is Clickable");
		txtTokenCode.sendKeys(tokenValue);
		assertNotNull(txtTokenCode);
		GlobalVariables.testInfo.pass("Token Input Box is editable & Auto Generated Token is entered successfully");
		GenericComponents.takeScreenShot(browser, "EnterToken");	
		btnTokenSubmit.click();
		if(browser.getWebDriver().getCurrentUrl().equals(Constants.loginURL)){
			txtPassword.sendKeys(GlobalVariables.password);
			btnLoginUsername.click();
		}
	}
	
	public void loginWithUsername(String txtUserName) throws NullPointerException, IOException {		
		tabUsername.click();
		GlobalVariables.testInfo.pass("Username Tab is Clickable");
		txtUsername.sendKeys(txtUserName);
		txtPassword.sendKeys(GlobalVariables.password);
		GlobalVariables.testInfo.pass("Username and Password Input Boxes are editable & User Credentials are entered successfully");
		GenericComponents.takeScreenShot(browser, "User Login");
		btnLoginUsername.click();
	}

	public void cardLogin(String cardValue, String securityCode) throws NullPointerException, IOException {
		tabCard.click();
		GlobalVariables.testInfo.pass("Card Tab is Clickable");
		txtCardNumber.sendKeys(cardValue);
		txtSecurityCode.sendKeys(securityCode);
		Assert.assertNotNull(cardValue);
		Assert.assertNotNull(securityCode);
		GlobalVariables.testInfo.pass("Card Number & Security Code Input Boxes are editable & Card Data is entered successfully");
		GenericComponents.takeScreenShot(browser, "Card Login");
		btnCardLogin.click();
	}

	public void contactUs() throws NullPointerException, InterruptedException, IOException {
		String txtContactFirstname = ContactUs.contactRows.get(0).getValue(ContactUs.FirstName).toString();
		String txtContactLastName = ContactUs.contactRows.get(0).getValue(ContactUs.LastName).toString();
		String txtContactEmail = ContactUs.contactRows.get(0).getValue(ContactUs.EmailAddress).toString();
		String txtContactPhone = ContactUs.contactRows.get(0).getValue(ContactUs.PhoneNumber).toString();
		String txtReason = ContactUs.contactRows.get(0).getValue(ContactUs.ReasonId).toString();
		String txtComments = ContactUs.contactRows.get(0).getValue(ContactUs.QuestionComments).toString();
		GenericComponents.waitFor(2);
		lnkContactUs.click();
		txtFirstName.sendKeys(txtContactFirstname);
		GenericComponents.waitFor(2);
		txtLastName.sendKeys(txtContactLastName);
		txtEmail.sendKeys(txtContactEmail);
		txtPhoneNumber.sendKeys(txtContactPhone);
		GenericComponents.selectDropDown(ddlReason, txtReason, "Text");
		txtQuestionsComments.sendKeys(txtComments);
		GlobalVariables.testInfo.pass("Contact Us - All Input Boxes are editable & User Information is entered successfully");
		GenericComponents.takeScreenShot(browser, "Contact Us");
		btnSubmit.click();
		if(testProp.getProperty("web.browser.type") != "FIREFOX") { 
			assertEquals(Constants.contactUsSuccessMessage, msgSuccess.getText()); 
			GlobalVariables.testInfo.pass("Contact Us Success Message Popup comes up as Expected >> "+msgSuccess.getText()); 
			btnContactUsContinue.click(); 
		}
	}

	public void recoverPassword() throws NullPointerException, InterruptedException, IOException {
		String txtRecoverUserName = ForgotPassword.passwordRows.get(0).getValue(ForgotPassword.UserName).toString();
		GenericComponents.waitFor(2);
		tabUsername.click();
		lnkForgotPassword.click();
		if(GlobalVariables.userName != null){
		txtRecoveryUsername.sendKeys(GlobalVariables.userName);		
		} else{
			txtRecoveryUsername.sendKeys(txtRecoverUserName);
		}
		btnRecoverySubmit.click();
		txtSecurityQuestion.sendKeys(GlobalVariables.challengeAnswer);
		GlobalVariables.testInfo.pass("Forgot Password - Username and Password Input Boxes are editable & User Credentials are entered successfully");
		GenericComponents.takeScreenShot(browser, "Forgot Password");
		btnRecoverySubmit.click();
		Assert.assertEquals(Constants.forgotPasswordURL, browser.getWebDriver().getCurrentUrl());
		GlobalVariables.testInfo.pass("Forgot Password request has been submitted successfully -- Browser URL >> "+browser.getWebDriver().getCurrentUrl());
		btnDone.click();
	}

	public void cardContactUs() throws NullPointerException, InterruptedException {
		String txtContactPhone = ProfileData.profileRows.get(0).getValue(ProfileData.PhoneNumber).toString();
		GenericComponents.waitFor(2);
		lnkContactUs.click();
		GenericComponents.waitFor(1);
		txtPhoneNumber.sendKeys(txtContactPhone);
		txtQuestionsComments.sendKeys("testing");
		btnSubmit.click();
		if(testProp.getProperty("web.browser.type") != "FIREFOX") { 
			assertEquals(Constants.contactUsSuccessMessage, msgSuccess.getText()); 
			GlobalVariables.testInfo.pass("Contact Us Success Message Popup comes up as Expected >> "+ msgSuccess.getText()); 
			btnContactUsContinue.click(); 
		}	
	}

	public void logout() throws InterruptedException, IOException {
		GenericComponents.waitFor(2);
		lnkLogout.click();
		assertEquals(browser.getWebDriver().getTitle(), Constants.browserTitle);
		GenericComponents.takeScreenShot(browser, "Log out");
		btnLogout.click();
		Assert.assertEquals(browser.getWebDriver().getCurrentUrl(), Constants.baseURL);
		GlobalVariables.testInfo.pass("User Logged Out Successfully and navigated to BaseURL>> "+browser.getWebDriver().getCurrentUrl());
	}

	@Override
	public void validate(LandingPage landingPage, SeleniumBrowser browser, TestContext context) {
		assertNotNull(landingPage);
		assertNotNull(txtUsername);
		assertNotNull(txtPassword);
		assertNotNull(txtCardNumber);
		assertNotNull(txtSecurityCode);
		assertEquals(Constants.browserTitle, browser.getWebDriver().getTitle());
		GlobalVariables.testInfo.pass(
				"Landing Page Title appears as Expected: " + browser.getWebDriver().getTitle());
	}
}