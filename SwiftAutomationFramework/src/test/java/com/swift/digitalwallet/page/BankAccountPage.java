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
import com.swift.utils.GlobalVariables.CardProfileData;
import com.swift.utils.GlobalVariables.Constants;

public class BankAccountPage extends TestNGCitrusTestRunner implements WebPage, PageValidator<RedemptionPage> {
	@Autowired(required = true)
	public SeleniumBrowser browser;

	// T2B
	@FindBy(linkText = "Transfer To Bank")
	public WebElement t2bLink;
	@FindBy(id = "AccountTypeID")
	public WebElement txtAccountType;
	@FindBy(id = "RoutingNumber")
	public WebElement txtRoutingNumber;
	@FindBy(id = "AccountNumber")
	public WebElement txtAccountNumber;
	@FindBy(id = "ConfirmAccountNumber")
	public WebElement txtConfirmAccountNumber;
	@FindBy(name = "Continue")
	public WebElement btnT2BContinue;
	@FindBy(name = "cancel")
	public WebElement btnTransferFundsLater;
	@FindBy(name = "AddNewBank")
	public WebElement btnTransferFundsNow;
	@FindBy(id = "AmountToTransfer")
	public WebElement txtAmountToTransfer;
	@FindBy(id = "EmailAddress")
	public WebElement txtEmailAddressT2B;
	@FindBy(name = "ConfirmTransfer")
	public WebElement btnConfirmTransfer;
	//remove bank
	@FindBy(id = "lnkRemoveBank")
	public WebElement lnkRemoveBank;
	@FindBy(css = ".bootstrap-dialog-message [class='col-md-6 col-sm-6 col-xs-6 zero-right-padding'] [type]")
	public WebElement btnRemoveBank;
	@FindBy(className = "close")
	public WebElement btnClose;
	@FindBy(css = "#divAddBankSuccess [class='col-md-12 row opaqueformheader viewprofileheader zero-margin pad10']")
	public WebElement addBankSuccessMsg;
	@FindBy(css = ".bootstrap-dialog-message [class='col-md-12 row opaqueformheader viewprofileheader zero-margin pad10'] font:nth-of-type(1) font")
	public WebElement removeBankMsg;
	@FindBy(css = ".bootstrap-dialog-message li")
	public WebElement errorMsgT2BConfirm;
	@FindBy(css = ".bootstrap-dialog-message .errRoutingNumber")
	public WebElement errRoutingNumber;
	@FindBy(css = ".bootstrap-dialog-message ul li:nth-of-type(1)")
	public WebElement errInsufficientFunds;
	@FindBy(css = ".digitalMyWalletInfo .ancMyWallet:nth-of-type(1) [class='col-md-11 col-sm-11 col-xs-11 zero-margin zero-padding']")
	public WebElement cardInfo;

	public void addBank() throws IOException, InterruptedException {
		GenericComponents.waitFor(2);
		cardInfo.click();
		t2bLink.click();
		GenericComponents.selectDropDown(txtAccountType, CardProfileData.accountType, "Text");
		txtRoutingNumber.sendKeys("031101169");
		txtRoutingNumber.sendKeys(Keys.ENTER);
		txtAccountNumber.sendKeys("131030270");
		txtConfirmAccountNumber.sendKeys("131030270");
		btnT2BContinue.click();
		Assert.assertEquals(addBankSuccessMsg.getText(), Constants.addBankSuccessMsg);
		GenericComponents.takeScreenShot(browser, "Bank Account Page");
		GlobalVariables.testInfo.pass("Transfer to Bank Link is clickable & Bank Account Informations are entered successfully");
		btnTransferFundsLater.click();
	}

	public void removeBank() throws IOException {
		cardInfo.click();
		lnkRemoveBank.click();
		btnRemoveBank.click();
		GenericComponents.takeScreenShot(browser, "Bank Account Page");
		Assert.assertEquals(removeBankMsg.getText(), Constants.removeBankMsg);
		GlobalVariables.testInfo.pass("Remove Bank Link is clickable & Bank Account Information has been removed successfully, Success Message PopUp comes up as expected>>" + removeBankMsg.getText());
		btnClose.click();
	}

	public void addBankInvalidAccounting() throws IOException {
		ProfilePage.firstCardInfoTd.click();
		t2bLink.click();
		GenericComponents.selectDropDown(txtAccountType, CardProfileData.accountType, "Text");
		txtRoutingNumber.sendKeys(GlobalVariables.routingNumber);
		txtRoutingNumber.sendKeys(Keys.ENTER);
		txtAccountNumber.sendKeys(CardProfileData.invalidAccountNumber);
		txtConfirmAccountNumber.sendKeys(CardProfileData.invalidAccountNumber);
		btnT2BContinue.click();
		Assert.assertEquals(addBankSuccessMsg.getText(), Constants.addBankSuccessMsg);
		GenericComponents.takeScreenShot(browser, "Bank Account Page");
		GlobalVariables.testInfo.pass("Transfer to Bank Link is clickable & Success Message PopUp comes up as expected>>" + addBankSuccessMsg.getText());
		btnTransferFundsLater.click();
	}

	public void addBankInvalidRouting() throws IOException {
		ProfilePage.firstCardInfoTd.click();
		t2bLink.click();
		GenericComponents.selectDropDown(txtAccountType, CardProfileData.accountType, "Text");
		txtRoutingNumber.sendKeys(CardProfileData.invalidRoutingNumber);
		Assert.assertEquals(errRoutingNumber.getText(), Constants.invalidRoutingErr);
		GenericComponents.takeScreenShot(browser, "Bank Account Page");
		GlobalVariables.testInfo.pass("Error message is displayed as expected>>" + errRoutingNumber.getText());
	}

	public void initiateTransfer() throws IOException {
		ProfilePage.firstCardInfoTd.click();
		t2bLink.click();
		txtAmountToTransfer.sendKeys(CardProfileData.transferAmount);
		txtEmailAddressT2B.sendKeys(CardProfileData.email);
		if(!RedemptionPage.chckBoxTerms.isSelected()){
			RedemptionPage.chckBoxTerms.click();
		}
		btnConfirmTransfer.click();
		GenericComponents.takeScreenShot(browser, "Bank Account Page");
		//TODO Asertions
	}

	public void initiateTransferInsufficientFund() throws IOException {
		ProfilePage.firstCardInfoTd.click();
		t2bLink.click();
		txtAmountToTransfer.sendKeys(CardProfileData.transferAmount);
		txtEmailAddressT2B.sendKeys(CardProfileData.email);
		if(!RedemptionPage.chckBoxTerms.isSelected()){
			RedemptionPage.chckBoxTerms.click();
		}
		btnConfirmTransfer.click();
		GenericComponents.takeScreenShot(browser, "Bank Account Page");
		Assert.assertEquals(errInsufficientFunds.getText(), Constants.insufficientFundsErr);
		GlobalVariables.testInfo.pass("Error message is displayed as expected>>" + errInsufficientFunds.getText());
	}

	@Override
	public void validate(RedemptionPage bankAccountPage, SeleniumBrowser browser, TestContext context) {
		Assert.assertNotNull(bankAccountPage);
		Assert.assertEquals(Constants.browserTitle, browser.getWebDriver().getTitle());
		GlobalVariables.testInfo.pass(
				"BankAccount Page loaded successfully. BankAccount Page Browser Title >>" + browser.getWebDriver().getTitle());
	}
}
