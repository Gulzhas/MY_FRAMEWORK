package com.swift.digitalwallet.page;

import java.io.IOException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.selenium.endpoint.SeleniumBrowser;
import com.consol.citrus.selenium.model.PageValidator;
import com.consol.citrus.selenium.model.WebPage;
import com.swift.utils.GenericComponents;
import com.swift.utils.GlobalVariables;
import com.swift.utils.GlobalVariables.Constants;
import com.swift.utils.GlobalVariables.CreateWallet;

public class RedemptionPage extends TestNGCitrusTestRunner implements WebPage, PageValidator<RedemptionPage> {
	@Autowired(required = true)
	public SeleniumBrowser browser;
	
	// select card options
	@FindBy(css = "[class='col-md-4 col-sm-12 col-xs-12 top10 pad10 zero-left-padding ']:nth-of-type(2) [type]")
	public WebElement btnSelect;
	@FindBy(name = "TermsAccepted")
	public static WebElement chckBoxTerms;
	@FindBy(xpath = "//button[@type='submit']")
	public WebElement btnConfirm;
	@FindBy(linkText = "View Details")
	public WebElement btnViewDetails;
	@FindBy(css = "[class='col-md-4 col-sm-12 col-xs-12 top10 pad10 zero-left-padding ']:nth-of-type(3) [type]")
	public WebElement btnSelectSaveForLater;
	@FindBy(xpath = "//div[@class='content']/div[2]/form[@action='/RedemptionOptions']/div/div[4]/div/div[5]//button[@type='submit']")
	public WebElement btnReloadableCardSelection;
	@FindBy(xpath = "//div[@class='content']/div[2]/form[@action='/RedemptionOptions']/div/div[4]/div/div[4]//button[@type='submit']")
	public WebElement btnCheckOption;
	@FindBy(xpath = "//div[@class='content']/div[2]/form[@action='/RedemptionOptions']/div/div[4]/div/div[3]//button[@type='submit']")
	public WebElement btnSelectPhysicalCard;
	@FindBy(linkText = "Go Back")
	public WebElement btnGoBack;
	// get saved Tokens
	@FindBy(className = "tdBankedCardInfo")
	public WebElement tblBankedCardInfo;
	@FindBy(name = "BankedTokenDetailList[0].Checked")
	public WebElement firstSavedTokenChkBx;
	@FindBy(name = "BankedTokenDetailList[1].Checked")
	public WebElement scndSavedTokenChkBx;
	@FindBy(id = "btnGetNow")
	public WebElement btnGetNow;
	@FindBy(css = ".bootstrap-dialog-message #btnRedeemBankCodeAgree")
	public static WebElement btnAgree;
	// get saved tokens with different level banks
	@FindBy(css = ".ancMyWallet:nth-of-type(1) [class='col-md-11 col-sm-11 col-xs-11 zero-margin zero-padding']")
	public WebElement tblFirstSavedToken;
	@FindBy(css = ".ancMyWallet:nth-of-type(2) [class='col-md-11 col-sm-11 col-xs-11 zero-margin zero-padding']")
	public WebElement tblSecondSavedToken;
	@FindBy(id = "SecurityCode")
	public WebElement txtCVV;
	@FindBy(id = "CardNumber")
	public WebElement txtCardNum;
	@FindBy(className = "ValidationSummaryCustom")
	public static WebElement errorMsg;
	@FindBy(id = "errorMsg")
	public WebElement siteErrorMsg;
	@FindBy(id = "ancLostThisCard")
	public WebElement lnkLostCard;
	@FindBy(linkText = "Click here")
	public WebElement haveReloadableCardLink;
	// itzCash confirmation
	@FindBy(id = "Profile_Address3")
	public WebElement txtAddress3;
	@FindBy(name = "Profile.Address4")
	public WebElement txtAddress4;
	@FindBy(name = "Profile.DOB")
	public WebElement dtpProfileDOB;
	@FindBy(name = "Profile.Salutation")
	public WebElement ddlProfileSalutation;
	@FindBy(name = "Profile.Gender")
	public WebElement ddlProfileGender;
	@FindBy(name = "Profile.MobileNumber")
	public WebElement txtMobilePhone;
	@FindBy(name = "Profile.MotherMaidenName")
	public WebElement txtProfileMotherMaidenName;
	//T2B
	@FindBy(xpath = "//div[@class='content']/div[2]/form[@action='/RedemptionOptions']/div/div[4]/div/div[3]//button[@type='submit']")
	public WebElement btnPhysicalReloadable;

	public void selectCardOption(String cardType) throws IOException, InterruptedException {
		if (cardType.equalsIgnoreCase("virtual")) {
			btnSelect.click();
		} else if (cardType.equalsIgnoreCase("reloadable")) {
			btnReloadableCardSelection.click();
		} else if (cardType.equalsIgnoreCase("physicalCardSaveForLater")) {
			btnSelectSaveForLater.click();
		} else if (cardType.equalsIgnoreCase("virtualCardSaveForLater")) {
			btnSelect.click();
		} else if (cardType.equalsIgnoreCase("check")) {
			btnCheckOption.click();
		} else if (cardType.equalsIgnoreCase("physical")) {
			btnSelectPhysicalCard.click();
		} else if (cardType.equalsIgnoreCase("physicalReloadable")){
			btnPhysicalReloadable.click();
		}
		btnConfirm.click();
		Assert.assertEquals(errorMsg.getText(), Constants.errorMsg);
		GlobalVariables.testInfo.pass(
				"Select Card Options Negative case Error message Popup comes up as expected -- " + errorMsg.getText());
		if (!chckBoxTerms.isSelected()) {
			chckBoxTerms.click();
		}
		btnConfirm.click();
		Assert.assertEquals(browser.getWebDriver().getCurrentUrl(), Constants.successRedemptionURL);
		GlobalVariables.testInfo.pass(
				"Select Card Options Page loads as expected, Browser URL -- " + browser.getWebDriver().getCurrentUrl());
		GenericComponents.takeScreenShot(browser, "Select Card Option Page");
		btnViewDetails.click();
	}
	
	public void selectSaveForLater() throws IOException {
		btnSelectSaveForLater.click();
		btnConfirm.click();
		GenericComponents.takeScreenShot(browser, "Select Card Option Page");
		Assert.assertEquals(Constants.successRedemptionURL, browser.getWebDriver().getCurrentUrl());
		GlobalVariables.testInfo.pass(
				"Select Card Options Page loads as expected, Browser URL -- " + browser.getWebDriver().getCurrentUrl());
		btnViewDetails.click();
		btnGoBack.click();
	}

	public void getSavedTokens() throws IOException, InterruptedException {
		tblBankedCardInfo.click();
		if (!firstSavedTokenChkBx.isSelected()) {
			firstSavedTokenChkBx.click();
		}
		if (!scndSavedTokenChkBx.isSelected()) {
			scndSavedTokenChkBx.click();
		}
		GlobalVariables.testInfo.pass("Saved Tokens CheckBox are clickable & Tokens are selected successfully");
		GenericComponents.takeScreenShot(browser, "Get Saved Tokens");
		btnGetNow.click();
		GenericComponents.waitFor(2);
		btnAgree.click();
		GlobalVariables.testInfo.pass("Saved Tokens are selected successfully");
	}

	public void getSavedTokenWithDifferentLevelBanks() throws IOException, InterruptedException {
		tblFirstSavedToken.click();
		if (!firstSavedTokenChkBx.isSelected()) {
			GlobalVariables.testInfo.pass("Saved Tokens CheckBox are clickable & Tokens are selected successfully");
			firstSavedTokenChkBx.click();
		}
		GlobalVariables.testInfo.pass("Saved Tokens CheckBox are clickable & Tokens are selected successfully");
		GenericComponents.takeScreenShot(browser, "Get Saved Tokens with Different Level Banks");
		btnGetNow.click();
		selectCardOption("virtual");
		GlobalVariables.testInfo.pass("Saved Tokens are selected successfully");
	}

	public void loadCardNegative(String CVV) throws IOException {
		btnReloadableCardSelection.click();
		txtCVV.sendKeys(CVV);
		if (!chckBoxTerms.isSelected()) {
			chckBoxTerms.click();
		}
		GlobalVariables.testInfo.pass("CheckBox is clickable & CVV is entered successfully");
		GenericComponents.takeScreenShot(browser, "Load Card for Re-loadable Cards");
		btnConfirm.click();
		Assert.assertEquals(errorMsg.getText(), Constants.errorMsg);
		GlobalVariables.testInfo.pass("Load Card Link Test Case Error message Popup comes up as expected -- "+ errorMsg.getText());
	}

	public void loadCard(String CVV) throws IOException {
		btnReloadableCardSelection.click();
		txtCVV.sendKeys(CVV);
		if (!chckBoxTerms.isSelected()) {
			chckBoxTerms.click();
		}
		GlobalVariables.testInfo.pass("CheckBox is clickable & CVV is entered successfully");
		GenericComponents.takeScreenShot(browser, "Load Card for Re-loadable Cards");
		btnConfirm.click();
		Assert.assertEquals(Constants.successRedemptionURL, browser.getWebDriver().getCurrentUrl());
		GlobalVariables.testInfo.pass("Load Card Options Page loads as expected, Browser URL -- " + browser.getWebDriver().getCurrentUrl());
	}
	
	 public void lostCard() throws IOException, NullPointerException, InterruptedException {
		btnReloadableCardSelection.click();
		lnkLostCard.click(); 
		GenericComponents.waitFor(3);
		if (!chckBoxTerms.isSelected()) {
			chckBoxTerms.click();
		}
		GlobalVariables.testInfo.pass("CheckBox is clickable");
		GenericComponents.takeScreenShot(browser, "Load Card for Re-loadable Cards");
		btnConfirm.click();
		Assert.assertEquals(browser.getWebDriver().getCurrentUrl(), Constants.successRedemptionURL);
		GlobalVariables.testInfo.pass(
				"Select Card Options Page loads as expected, Browser URL -- " + browser.getWebDriver().getCurrentUrl());
		GenericComponents.takeScreenShot(browser, "Lost This Card Page");
		btnViewDetails.click();
	}

	public void clickReloadableCardLink(String cardNum, String cvv) throws IOException {
		btnReloadableCardSelection.click();
		haveReloadableCardLink.click();
		Assert.assertEquals(browser.getWebDriver().getCurrentUrl(), Constants.confirmRedemptionURL);
		txtCardNum.sendKeys(cardNum);
		txtCVV.sendKeys(cvv);
		if (!chckBoxTerms.isSelected()) {
			chckBoxTerms.click();
		}
		GlobalVariables.testInfo.pass("CheckBox is clickable & Card Number & CVV are  entered successfully");
		GenericComponents.takeScreenShot(browser, "Load Card for Re-loadable Cards");
		btnConfirm.click();
		Assert.assertEquals(errorMsg.getText(), Constants.errorMsg);
		GlobalVariables.testInfo.pass("Click Reloadable Card Link Test Case Error message Popup comes up as expected -- "+ errorMsg.getText());
	}
	
	public void itzCashConfirmation() throws IOException, InterruptedException {
		String address3 = CreateWallet.walletRows.get(0).getValue(CreateWallet.Address3).toString();
		String address4 = CreateWallet.walletRows.get(0).getValue(CreateWallet.Address4).toString();
		String profileDOB = CreateWallet.walletRows.get(0).getValue(CreateWallet.profileDOB).toString();
		String salutation = CreateWallet.walletRows.get(0).getValue(CreateWallet.salutation).toString();
		String gender = CreateWallet.walletRows.get(0).getValue(CreateWallet.gender).toString();
		String phone = CreateWallet.walletRows.get(0).getValue(CreateWallet.mobilePhone).toString();
		String mothersMaidenName = CreateWallet.walletRows.get(0).getValue(CreateWallet.mothersMaidenName).toString();
		txtAddress3.sendKeys(address3);
		txtAddress4.sendKeys(address4);
		dtpProfileDOB.sendKeys(Keys.TAB);	
		GenericComponents.jsExecutor(browser, dtpProfileDOB, "datePickerInput");
		dtpProfileDOB.sendKeys(profileDOB);
		dtpProfileDOB.sendKeys(Keys.TAB);		
		GenericComponents.selectDropDown(ddlProfileSalutation, salutation, "Value");
		GenericComponents.selectDropDown(ddlProfileGender, gender, "Text");
		txtMobilePhone.sendKeys(phone);
		txtProfileMotherMaidenName.sendKeys(mothersMaidenName);
		if (!chckBoxTerms.isSelected()) {
			chckBoxTerms.click();
		}
		btnConfirm.click();
		Assert.assertEquals(Constants.successRedemptionURL, browser.getWebDriver().getCurrentUrl());
		GlobalVariables.testInfo.pass(
				"Select Card Options Page loads as expected, Browser URL -- " + browser.getWebDriver().getCurrentUrl());
		GenericComponents.takeScreenShot(browser, "Itz Cash Confirmation Page");
		btnViewDetails.click();
	}
	
	public void itzCashInvalidDOB() throws IOException{
		String address3 = CreateWallet.walletRows.get(0).getValue(CreateWallet.Address3).toString();
		String address4 = CreateWallet.walletRows.get(0).getValue(CreateWallet.Address4).toString();
		String invalidDOB = CreateWallet.walletRows.get(0).getValue(CreateWallet.invalidDOB).toString();
		String salutation = CreateWallet.walletRows.get(0).getValue(CreateWallet.salutation).toString();
		String gender = CreateWallet.walletRows.get(0).getValue(CreateWallet.gender).toString();
		String phone = CreateWallet.walletRows.get(0).getValue(CreateWallet.mobilePhone).toString();
		String mothersMaidenName = CreateWallet.walletRows.get(0).getValue(CreateWallet.mothersMaidenName).toString();
		txtAddress3.sendKeys(address3);
		txtAddress4.sendKeys(address4);
		dtpProfileDOB.sendKeys(invalidDOB);
		GenericComponents.selectDropDown(ddlProfileSalutation, salutation, "Value");
		GenericComponents.selectDropDown(ddlProfileGender, gender, "Text");
		txtMobilePhone.sendKeys(phone);
		txtProfileMotherMaidenName.sendKeys(mothersMaidenName);
		if (!chckBoxTerms.isSelected()) {
			chckBoxTerms.click();
		}
		btnConfirm.click();
		Assert.assertEquals(errorMsg.getText(), Constants.errorMsg);
		GlobalVariables.testInfo.pass("ITz Cash Invalid DOB Negative Test Case Error message Popup comes up as expected -- "+ errorMsg.getText());
		GenericComponents.takeScreenShot(browser, "ITz Cash Invalid DOB");
	}

	@Override
	public void validate(RedemptionPage redemptionPage, SeleniumBrowser browser, TestContext context) {
		Assert.assertNotNull(redemptionPage);
		Assert.assertEquals(Constants.browserTitle, browser.getWebDriver().getTitle());
		GlobalVariables.testInfo.pass(
				"Redemption Page loaded successfully. Redemption Page Browser Title >>" + browser.getWebDriver().getTitle());
	}
}