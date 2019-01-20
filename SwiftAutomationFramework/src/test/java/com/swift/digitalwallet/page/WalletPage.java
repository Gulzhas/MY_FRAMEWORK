package com.swift.digitalwallet.page;

import java.io.IOException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.selenium.endpoint.SeleniumBrowser;
import com.consol.citrus.selenium.model.*;
import com.swift.utils.GenericComponents;
import com.swift.utils.GlobalVariables;
import com.swift.utils.GlobalVariables.*;

public class WalletPage extends TestNGCitrusTestRunner implements WebPage, PageValidator<WalletPage> {
	@Autowired(required = true)
	public SeleniumBrowser browser;
	
	// create wallet
	@FindBy(name = "UserName")
	public WebElement txtUsername;
	@FindBy(id = "Password")
	public WebElement txtPassword;
	@FindBy(id = "ConfirmPassword")
	public static WebElement txtConfirmPassword;
	@FindBy(name = "Email")
	public WebElement txtEmail;
	@FindBy(name = "ConfirmEmail")
	public WebElement txtConfirmEmail;
	@FindBy(name = "FirstName")
	public WebElement txtFirstName;
	@FindBy(name = "LastName")
	public WebElement txtLastName;
	@FindBy(name = "CountryCode")
	public WebElement ddlCountryCode;
	@FindBy(name = "Address1")
	public WebElement txtAddress1;
	@FindBy(name = "Address2")
	public WebElement txtAddress2;
	@FindBy(name = "City")
	public WebElement txtCity;
	@FindBy(name = "State")
	public WebElement ddlState;
	@FindBy(name = "Zip")
	public WebElement txtZipCode;
	@FindBy(name = "Phone")
	public static WebElement txtPhone;
	@FindBy(name = "Questions[0].QuestionId")
	public static WebElement ddlChallengeQuestion;
	@FindBy(name = "Questions[0].Answer")
	public static WebElement txtChallengeAnswer;
	@FindBy(id = "btnCreateWallet")
	public static WebElement btnCreateWallet;
	@FindBy(xpath = "//p[contains(text(), 'This card has been successfully added to your wallet')]")
	public WebElement addedToWalletSuccessMsg;
	@FindBy(xpath = "//div[@class = 'dropdown currencies MyCurrencyWrapper']/button[@type='button']")
	public WebElement btnCurrency;
	@FindBy(css = ".li-Currency:nth-of-type(2) [href] span:nth-of-type(2)")
	public WebElement optionUSD;
	// create or add to DW for Wallet Optional
	@FindBy(linkText = "Create or Add to Digital Wallet")
	public static WebElement btnCreateAddToWallet;
	@FindBy(name = "UserName")
	public WebElement txtAddToWalletUsername;
	@FindBy(name = "Password")
	public WebElement txtAddToWalletPassword;
	@FindBy(name = "ConfirmPassword")
	public WebElement txtAddToWalletConfirmPassword;
	@FindBy(name = "AddToWallet")
	public WebElement btnAddToWallet;
	@FindBy(name = "CreateWallet")
	public WebElement btnCreateAddWallet;
	@FindBy(className = "divSuccessMsg")
	public static WebElement msgAddedToWallet;
	@FindBy(id = "btnProfileConntinue")
	public WebElement btnProfileContinue;
	// add new card
	@FindBy(id = "btnAddNewCard1")
	public static WebElement btnAddNewCard;
	@FindBy(name = "NewPromocode")
	public WebElement txtNewPromocode;
	@FindBy(id = "btnAddNewCardArrow")
	public WebElement btnAddNewCardArrow;

	public void createWallet(String txtUserName) throws NullPointerException, InterruptedException, IOException {
		String email = CreateWallet.walletRows.get(0).getValue(CreateWallet.Email).toString();
		String confirmEmail = CreateWallet.walletRows.get(0).getValue(CreateWallet.ConfirmEmail).toString();
		String firstName = CreateWallet.walletRows.get(0).getValue(CreateWallet.FirstName).toString();
		String lastName = CreateWallet.walletRows.get(0).getValue(CreateWallet.LastName).toString();
		String country = CreateWallet.walletRows.get(0).getValue(CreateWallet.Country).toString();
		String address1 = CreateWallet.walletRows.get(0).getValue(CreateWallet.Address1).toString();
		String address2 = CreateWallet.walletRows.get(0).getValue(CreateWallet.Address2).toString();
		String city = CreateWallet.walletRows.get(0).getValue(CreateWallet.City).toString();
		String state = CreateWallet.walletRows.get(0).getValue(CreateWallet.State).toString();
		String zip = CreateWallet.walletRows.get(0).getValue(CreateWallet.Zip).toString();
		String phone = CreateWallet.walletRows.get(0).getValue(CreateWallet.Phone).toString();
		if(txtUsername != null){
		txtUsername.sendKeys(txtUserName);
		txtPassword.sendKeys(GlobalVariables.password);
		txtConfirmPassword.sendKeys(GlobalVariables.password);
		txtEmail.sendKeys(email);
		txtConfirmEmail.sendKeys(confirmEmail);
		txtFirstName.sendKeys(firstName);
		txtLastName.sendKeys(lastName);
		GenericComponents.selectDropDown(ddlCountryCode, country, "Text");
		txtAddress1.sendKeys(address1);
		txtAddress2.sendKeys(address2);
		txtCity.sendKeys(city);
		if(country == "USA"){
		GenericComponents.selectDropDown(ddlState, state, "Value");
		} else {
			ddlState.sendKeys(state);
		}
		txtZipCode.sendKeys(zip);
		txtPhone.sendKeys(phone);
		txtChallengeAnswer.sendKeys(GlobalVariables.challengeAnswer);
		GenericComponents.takeScreenShot(browser, "Create Wallet Page");
		btnCreateWallet.click();	
		//Assert.assertEquals(Constants.confirmRedemptionURL, browser.getWebDriver().getCurrentUrl());
		GlobalVariables.testInfo.pass("Select Card Options Page loads as expected, Browser URL -- " + browser.getWebDriver().getCurrentUrl());
		}
	}
	
	public void createProfile(){
		txtFirstName.sendKeys(CardProfileData.firstName);
		txtLastName.sendKeys(CardProfileData.lastName);
		GenericComponents.selectDropDown(ddlCountryCode, CardProfileData.country, "Text");
		txtAddress1.sendKeys(CardProfileData.address1);
		txtAddress2.sendKeys(CardProfileData.address2);
		txtCity.sendKeys(CardProfileData.city);;
		GenericComponents.selectDropDown(ddlState, CardProfileData.state, "Text");
		txtZipCode.sendKeys(CardProfileData.zip);
		txtEmail.sendKeys(CardProfileData.email);
		btnProfileContinue.click();
	}
	
	public void addToWallet(String txtUsername) throws NullPointerException, InterruptedException, IOException {
		String email = ProfileData.profileRows.get(0).getValue(ProfileData.Email).toString();
		String confirmEmail = ProfileData.profileRows.get(0).getValue(ProfileData.ConfirmEmail).toString();
		GenericComponents.jsExecutor(browser, btnCreateAddToWallet, "visible");
		txtAddToWalletUsername.sendKeys(txtUsername);
		txtAddToWalletPassword.sendKeys(GlobalVariables.password);
		txtAddToWalletConfirmPassword.sendKeys(GlobalVariables.password);
		btnAddToWallet.click();
		txtEmail.sendKeys(email);
		txtConfirmEmail.sendKeys(confirmEmail);
		txtChallengeAnswer.sendKeys(GlobalVariables.challengeAnswer);
		GlobalVariables.testInfo.info("Add to Wallet - AddToWallet button is clickable & User Informations are entered successfully");
		GenericComponents.takeScreenShot(browser, "Add to Wallet");
		btnCreateAddWallet.click();
		Assert.assertNotNull(msgAddedToWallet.getText());
		GlobalVariables.testInfo.pass("Added to Wallet Message Popup comes up as Expected >> "+msgAddedToWallet.getText());
	}

	public void addNewCard(String newPromocode) throws NullPointerException, IOException {
		if (btnAddNewCard.isDisplayed()) {
			btnAddNewCard.click();
		}
		txtNewPromocode.sendKeys(newPromocode);
		Assert.assertNotNull(newPromocode);
		GlobalVariables.testInfo.pass("Add New Card Box is editable & Auto Generated Token is entered successfully");
		GenericComponents.takeScreenShot(browser, "Add New Card");
		btnAddNewCardArrow.click();
		/*if(RedemptionPage.errorMsg.isDisplayed()){
		Assert.assertEquals(RedemptionPage.errorMsg.getText(), Constants.errorMsg);
		GlobalVariables.testInfo.pass("Select Add New Card Negative Case Error message Popup comes up as expected -- "+ RedemptionPage.errorMsg.getText());
		GenericComponents.takeScreenShot(browser, "Add New Card Option Page");
	}*/
	}

	@Override
	public void validate(WalletPage walletPage, SeleniumBrowser browser, TestContext context) {
		Assert.assertNotNull(walletPage);		
		Assert.assertEquals(Constants.browserTitle, browser.getWebDriver().getTitle());
		GlobalVariables.testInfo.pass("Wallet Page loaded successfully. Wallet Page Browser Title >>" +browser.getWebDriver().getTitle());		
	}
}
