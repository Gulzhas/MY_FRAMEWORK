package com.swift.digitalwallet.page;

import static org.testng.Assert.assertEquals;
import java.io.IOException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.testng.Assert;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.selenium.endpoint.SeleniumBrowser;
import com.consol.citrus.selenium.model.*;
import com.swift.utils.GenericComponents;
import com.swift.utils.GlobalVariables;
import com.swift.utils.GlobalVariables.*;

public class ProfilePage extends TestNGCitrusTestRunner implements WebPage, PageValidator<ProfilePage> {
	@Autowired(required = true)
	public SeleniumBrowser browser;	
	@Autowired
	Environment testProp;
	
	//global web element
	@FindBy(linkText = "Home")
	public static WebElement lnkHome;
	// edit card details
	@FindBy(className = "tblCardInfo")
	public static WebElement firstCardInfoTd;
	@FindBy(css = ".digitalMyWalletInfo .ancMyWallet:nth-of-type(2) [class='col-md-11 col-sm-11 col-xs-11 zero-margin zero-padding']")
	public WebElement secondCardInfoTd;
	@FindBy(xpath = "//div[contains(text(), 'PIN')]/a[contains(text(), 'Edit')]")
	public WebElement lnkEditPin;
	@FindBy(name = "NewPIN")
	public WebElement txtNewPin;
	@FindBy(name = "ConfirmPIN")
	public WebElement txtConfirmPin;
	@FindBy(xpath = "//button[contains(text(), 'Cancel')]")
	public WebElement btnCancel;
	@FindBy(xpath = "//button[contains(text(), 'Submit')]")
	public WebElement btnSubmit;
	@FindBy(xpath = "//b[contains(text(),'PIN update')]")
	public static WebElement msgSuccess;
	@FindBy(className = "close")
	public WebElement btnClose;
	// profile update
	@FindBy(linkText = "Profile")
	public WebElement lnkProfile;
	@FindBy(id = "btnDesktopEdit")
	public WebElement btnProfileEdit;
	@FindBy(id = "btnEditSubmit")
	public WebElement btnEditSubmit;
	@FindBy(xpath = "//p[contains(text(), 'Profile update')]")
	public static WebElement updateMsg;
	@FindBy(name = "Address2")
	public WebElement txtAddress2;
	// update password
	@FindBy(linkText = "Update")
	public WebElement lnkUpdatePassword;
	@FindBy(name = "CurrentPassword")
	public WebElement txtCurrentPassword;
	@FindBy(name = "NewPassword")
	public WebElement txtNewPassword;
	@FindBy(name = "ConfirmPassword")
	public WebElement txtConfirmPassword;
	@FindBy(xpath = "//button[@type='submit']")
	public WebElement btnUpdatePasswordSubmit;
	@FindBy(xpath = "//p[contains(text(), 'Password update')]")
	public static WebElement passwordUpdateMsg;
	// update challenge question
	@FindBy(css = "[class='col-sm-6 col-xs-6']:nth-of-type(2) .form-control")
	public WebElement lnkUpdateQuestion;
	@FindBy(name = "Password")
	public WebElement txtPasswordForUpdate;
	@FindBy(name = "Questions[0].QuestionId")
	public WebElement ddlNewQuestion;
	@FindBy(name = "Questions[0].Answer")
	public WebElement txtNewAnswer;
	@FindBy(id = "btnEditSubmit")
	public WebElement btnUpdateQuestionSubmit;
	// authenticated contact us
	@FindBy(linkText = "Contact Us")
	public WebElement lnkContactUs;
	@FindBy(name = "ReasonId")
	public WebElement ddlReason;
	@FindBy(name = "QuestionComments")
	public WebElement txtQuestion;
	@FindBy(xpath = "//button[@name='Submit']")
	public WebElement btnContactUsSubmit;
	@FindBy(xpath = "//span[contains(text(), 'Continue')]")
	public WebElement btnContinue;	
	// wallet optional|not creating wallet|profile
	@FindBy(name = "Profile.FirstName")
	public WebElement txtProfileFirstName;
	@FindBy(name = "Profile.LastName")
	public WebElement txtProfileLastName;
	@FindBy(name = "Profile.CountryCode")
	public WebElement ddlProfileCountryCode;
	@FindBy(name = "Profile.Address1")
	public WebElement txtProfileAddress1;
	@FindBy(name = "Profile.Address2")
	public WebElement txtProfileAddress2;
	@FindBy(name = "Profile.City")
	public WebElement txtProfileCity;
	@FindBy(name = "Profile.State")
	public WebElement selectProfileState;
	@FindBy(name = "Profile.Zip")
	public WebElement txtProfileZip;
	@FindBy(name = "Profile.Email")
	public WebElement txtProfileEmail;
	@FindBy(name = "TermsAccepted")
	public WebElement profileTermsAccepted;
	@FindBy(xpath = "//button[@type='submit']")
	public WebElement btnProfileSubmit;
	@FindBy(linkText = "View Details")
	public WebElement lnkViewDetails;
	// dynamic generated card details
	@FindBy(xpath = "//form[@id='formVirtualCard']/div/div[2]/div[1]/div[@class='col-md-7 col-xs-7 rightcol zero-left-padding']")
	public WebElement dynamicCardNumber;
	@FindBy(xpath = "//form[@id='formVirtualCard']/div/div[2]/div[3]/div[@class='col-md-7 col-xs-7 rightcol zero-left-padding']")
	public WebElement dynamicCVV;
	// update card profile
	@FindBy(xpath = "//div[contains(text(), 'Profile')]/a[contains(text(), 'Edit')]")
	public WebElement lnkCardProfileEdit;
	@FindBy(name = "LastName")
	public WebElement txtLastName;
	@FindBy(xpath = "//button[contains(text(), 'Submit')]")
	public WebElement btnUpdateCardProfileInfo;
	@FindBy(xpath = "//p[contains(text(), ' Profile update successful.')]")
	public WebElement profileUpdateSuccessMsg;
	@FindBy(xpath = "//p[contains(text(), ' Challenge Question update successful.')]")
	public WebElement challengeQuestionUpdateSuccessMsg;
	@FindBy(xpath = "//div[contains(text(), 'PIN')]/a[contains(text(), 'Edit')]")
	public WebElement lnkCardPinEdit;
	@FindBy(xpath = "//button[contains(text(), 'Log Out')]")
	public WebElement btnCardLogout;
	//INR serial number
	@FindBy(css = "[class] .row:nth-of-type(1) [class='cardProfile col-md-7 col-xs-7 rightcol zero-left-padding']:nth-of-type(2)")
	public WebElement serialNumberINR;
	
	public void createCardProfile() throws NullPointerException, IOException {
		txtProfileFirstName.sendKeys(CardProfileData.firstName);
		txtProfileLastName.sendKeys(CardProfileData.lastName);
		GenericComponents.selectDropDown(ddlProfileCountryCode, CardProfileData.country, "Text");
		txtProfileAddress1.sendKeys(CardProfileData.address1);
		txtProfileAddress2.sendKeys(CardProfileData.address2);
		txtProfileCity.sendKeys(CardProfileData.city);
		GenericComponents.selectDropDown(selectProfileState, CardProfileData.state, "Text");
		txtProfileZip.sendKeys(CardProfileData.zip);
		txtProfileEmail.clear();
		txtProfileEmail.sendKeys(CardProfileData.email);
		if (!profileTermsAccepted.isSelected()) {
			profileTermsAccepted.click();
		}
		GlobalVariables.testInfo.info("Create Card Profile - All Input Boxes are editable & User Informations are entered successfully");
		GenericComponents.takeScreenShot(browser, "Create Card Profile");
	    btnProfileSubmit.click();
	    Assert.assertEquals(browser.getWebDriver().getCurrentUrl(),
				Constants.successRedemptionURL);
	    GlobalVariables.testInfo.pass("Card Profile has been created succesfully. Success redemption URL >> "+Constants.successRedemptionURL);
	    lnkViewDetails.click();	    
	}
	
	public void getINRSerialNumber(){
		GlobalVariables.serialNumber = serialNumberINR.getText();
		Assert.assertNotNull(GlobalVariables.serialNumber);
	}
	
	public void updateCardPIN(String newPin) throws InterruptedException, IOException {
		lnkCardPinEdit.click();
		GenericComponents.waitFor(2);
		txtNewPin.sendKeys(newPin);
		txtConfirmPin.sendKeys(newPin);
		GlobalVariables.testInfo.info("Update Card PIN - New Pin & Confirm Pin fields are editable");
		GenericComponents.takeScreenShot(browser, "Update Card PIN");
		btnSubmit.click();
		Assert.assertEquals(Constants.pinUpdateMessage, msgSuccess.getText());
		GlobalVariables.testInfo.pass("Update Card Pin Success Message Popup comes up as Expected >> "+msgSuccess.getText());
		btnClose.click();
	}

	public void updateCardProfile() throws NullPointerException, InterruptedException, IOException {
		String txtNewLastName = ProfileData.profileRows.get(0).getValue(ProfileData.NewLastName).toString();		
		GenericComponents.waitFor(2);
		lnkCardProfileEdit.click();
		txtLastName.clear();
		txtLastName.sendKeys(txtNewLastName);
		GlobalVariables.testInfo.info("Update Card Profile - LastName & New LastName fields are editable");
		GenericComponents.takeScreenShot(browser, "Update Card Profile");
		Assert.assertNotNull(txtLastName.getText());
		GlobalVariables.testInfo.pass("New LastName is displayed as Expected >> "+ txtLastName.getText());
		btnSubmit.click();
	}

	public void getCardNumberAndCVV() throws Exception, NullPointerException {
		GlobalVariables.cardNumber = dynamicCardNumber.getText().replace("-", "");
		GlobalVariables.securityCode = dynamicCVV.getText();
		GlobalVariables.testInfo.info("Dynamicly Generates Card Number and CVV >> " + GlobalVariables.cardNumber + "  " +GlobalVariables.securityCode);
		GenericComponents.takeScreenShot(browser, "Get Card Number and CVV");	
	}

   public void contactUsAuth() throws NullPointerException, InterruptedException, IOException {
	    lnkContactUs.click(); 
		GenericComponents.waitFor(1);
		GenericComponents.selectDropDown(ddlReason, "How do I log in?", "Text");
		txtQuestion.sendKeys("test");
		GlobalVariables.testInfo.info("Authenticated Contact Us - All Input Boxes are editable & User Informations are entered successfully");
		GenericComponents.takeScreenShot(browser, "Authenticated Contact Us");
		btnContactUsSubmit.click();
		if(testProp.getProperty("${web.browser.type}") != "FIREFOX") { 
			assertEquals(Constants.contactUsSuccessMessage, LandingPage.msgSuccess.getText());
			GlobalVariables.testInfo.pass("Authenticated Contact Us Success Message Popup comes up as Expected >> "+ LandingPage.msgSuccess.getText());
			btnContinue.click();
	   }
   }
   
	public void updateChallengeQuestion() throws NullPointerException, IOException {
		String newChallengeQuestion = CreateWallet.walletRows.get(0).getValue(CreateWallet.NewChallengeQuestion)
				.toString();
		lnkProfile.click();
		btnProfileEdit.click();
		lnkUpdateQuestion.click();
		txtPasswordForUpdate.sendKeys(GlobalVariables.newPassword);
		GenericComponents.selectDropDown(ddlNewQuestion, newChallengeQuestion, "Text");
		txtNewAnswer.sendKeys(GlobalVariables.challengeAnswer);
		GlobalVariables.testInfo.info("Update Challenge Question Page - Challenge Question & New Challenge Question fields are editable");
		btnEditSubmit.click();
		GenericComponents.takeScreenShot(browser, "Update Challenge Question");
		Assert.assertEquals(challengeQuestionUpdateSuccessMsg.getText(), Constants.updateChallengeQuestionMessage);
		GlobalVariables.testInfo.pass("Challenge Question Success Message Popup comes up as Expected>> "+ Constants.updateChallengeQuestionMessage);
	}

	public void updatePassword() throws NullPointerException, InterruptedException, IOException {	
		GenericComponents.waitFor(2);
		lnkProfile.click();
		btnProfileEdit.click();
		lnkUpdatePassword.click();
		txtCurrentPassword.sendKeys(GlobalVariables.password);
		txtNewPassword.sendKeys(GlobalVariables.newPassword);
		txtConfirmPassword.sendKeys(GlobalVariables.newPassword);
		GlobalVariables.testInfo.info("Update Password Page - New Password & Confirm Password fields are editable");
		GenericComponents.takeScreenShot(browser, "Update Password");
		btnUpdatePasswordSubmit.click();
		Assert.assertEquals(Constants.updatePasswordMessage, passwordUpdateMsg.getText());
		GlobalVariables.testInfo.pass("Edit Pin Success Message Popup comes up as Expected >> "+passwordUpdateMsg.getText());
	}

	public void editPIN(String newPin) throws NullPointerException, InterruptedException, IOException {
		lnkHome.click();
		firstCardInfoTd.click();
		lnkEditPin.click();
		GenericComponents.waitFor(2);
		txtNewPin.sendKeys(newPin);
		txtConfirmPin.sendKeys(newPin);
		GlobalVariables.testInfo.info("Edit Pin Page - New Pin & Confirm Pin fields are editable");
		GenericComponents.takeScreenShot(browser, "Edit Pin");
		btnSubmit.click();
		Assert.assertEquals(Constants.pinUpdateMessage, msgSuccess.getText());
		GlobalVariables.testInfo.pass("Edit Pin Success Message Popup comes up as Expected >> "+msgSuccess.getText());
		btnClose.click();
	}
	
	public void editPINSecondCard(String newPin) throws NullPointerException, InterruptedException, IOException {
		GenericComponents.waitFor(2);
		lnkHome.click();
		firstCardInfoTd.click();
		secondCardInfoTd.click(); 
		lnkEditPin.click();
		GenericComponents.waitFor(2);
		txtNewPin.sendKeys(newPin);
		txtConfirmPin.sendKeys(newPin);
		GlobalVariables.testInfo.info("Edit Pin Page - New Pin & Confirm Pin fields are editable");
		GenericComponents.takeScreenShot(browser, "Edit Second Card Pin");
		btnSubmit.click();
		Assert.assertEquals(Constants.pinUpdateMessage, msgSuccess.getText());
		GlobalVariables.testInfo.pass("Edit Pin Success Message Popup comes up as Expected >> "+msgSuccess.getText());
		btnClose.click();
	}
	
	public void editProfile() throws NullPointerException, InterruptedException, IOException {
		String address2 = CreateWallet.walletRows.get(0).getValue(CreateWallet.NewAddress2).toString();
		lnkProfile.click();
		btnProfileEdit.click();
		txtAddress2.clear();
		txtAddress2.sendKeys(address2);
		GlobalVariables.testInfo.info("Edit Profile Page - Address & New Address fields are editable");
		GenericComponents.takeScreenShot(browser, "Edit Profile");
		btnEditSubmit.click();
		Assert.assertEquals(Constants.profileUpdateMessage, updateMsg.getText());
		GlobalVariables.testInfo.pass("Edit Pin Success Message Popup comes up as Expected >> "+updateMsg.getText());
	}

	public void cardLogout() throws InterruptedException, IOException {
		GenericComponents.waitFor(2);
		lnkHome.click();
		GenericComponents.takeScreenShot(browser, "Card Logout");
		btnCardLogout.click();
		Assert.assertEquals(Constants.baseURL, browser.getWebDriver().getCurrentUrl());
		GlobalVariables.testInfo.pass("User Logged Out Successfully  and navigated to BaseURL>> "+browser.getWebDriver().getCurrentUrl());
	}

	@Override
	public void validate(ProfilePage profilePage, SeleniumBrowser browser, TestContext context) {
		Assert.assertNotNull(profilePage);
		Assert.assertEquals(Constants.browserTitle, browser.getWebDriver().getTitle());
		GlobalVariables.testInfo.pass("Profile Page loaded successfully. Profile Page Browser Title >>" +browser.getWebDriver().getTitle());	
	}

}
