package com.swift.digitalwallet.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.metamodel.data.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.*;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.selenium.endpoint.SeleniumBrowser;
import com.consol.citrus.ws.client.WebServiceClient;
import com.swift.digitalwallet.page.*;
import com.swift.secure.utils.CryptionAlgorithm;
import com.swift.utils.GenericComponents;
import com.swift.utils.GlobalVariables;
import com.swift.utils.GlobalVariables.*;
import com.swift.utils.TestDataAdapter;

/**
 * @authors skrishnan, gmailybayeva
 * @category SmokeSuite
 * Digital Wallet Smoke Suite 
 */
public class SmokeTestDriver extends TestNGCitrusTestRunner {
	protected Logger log = LoggerFactory.getLogger(SmokeTestDriver.class);
	@Autowired
	Environment testProp;
	@Autowired(required = true)
	public SeleniumBrowser browser;
	@Autowired(required = true)
	public LandingPage landingPage;
	@Autowired(required = true)
	public ProfilePage profilePage;
	@Autowired(required = true)
	public WalletPage walletPage;
	@Autowired(required = true)
	public RedemptionPage redemptionPage;
	@Autowired(required = true)
	public BankAccountPage bankAccountPage;
	@Autowired(required = true)
	public WebServiceClient getPromocodeClient;
	@Autowired(required = true)
	public HttpClient fisClient;

	
	@Test
	@CitrusTest
	public void testLogin() {
		try {		
			for (Row testName : GlobalVariables.testCasesList) {
				GlobalVariables.pin = CryptionAlgorithm.decryptString(testProp.getProperty("web.digitalwallet.userpin"));
				GlobalVariables.password = CryptionAlgorithm.decryptString(testProp.getProperty("web.digitalwallet.userPassword"));	
				GlobalVariables.newPassword = CryptionAlgorithm.decryptString(testProp.getProperty("web.digitalwallet.userNewPassword"));
				GlobalVariables.challengeAnswer = CryptionAlgorithm.decryptString(testProp.getProperty("web.digitalwallet.challengeanswer"));
				GlobalVariables.execTestCase = testName.getValue(GlobalVariables.testName).toString();
				GlobalVariables.execTestCoverage = testName.getValue(GlobalVariables.testCoverage).toString()+ " -- " +testName.getValue(GlobalVariables.testDescription).toString();
				GlobalVariables.testInfo = GlobalVariables.reports.createTest(GlobalVariables.execTestCase);
				GlobalVariables.testInfo.info("Test Description --"+GlobalVariables.execTestCoverage);
				log.debug("Current Test Case in Execution >> " + GlobalVariables.execTestCase);
				String testComponents = testName.getValue(GlobalVariables.testComponentFlow).toString();
				System.out.println(GlobalVariables.execTestCase);
				log.debug(testComponents);
				String[] testFlow = testComponents.split("&");
				for (String testModule : testFlow) {
					System.out.println(testModule);
					log.debug("Components under Execution >> " + testModule + GlobalVariables.execTestCase);
					switch (testModule) {
					case "getToken":
						List<Row> tokenNumber = TestDataAdapter.enterTokenData();
						GlobalVariables.newToken = tokenNumber.get(0).getValue(TokenData.tokenValue).toString();
						GlobalVariables.testInfo
							.info("Getting Token from Test Data Sheet");
						break;
					case "getPromocodeAPI":
						GlobalVariables.testInfo
						.info(testModule + " -- Prepare and Send Request for Automatic Token Generation -- ");
						getPromocodeAPITest();
						break;
					case "getPersonAPI":
						GlobalVariables.testInfo
						.info(testModule + " -- Prepare and Send Request for getting Person's Info -- ");
						getPersonAPI("${newToken}");
						break;
					case "createWallet":					
						CreateWallet.walletRows = TestDataAdapter.connectCreateWallet();				
						variable("txtUserName", "AUTO" + "citrus:randomString(4, UPPERCASE)" + "citrus:randomNumber(4)");
						selenium(action -> action
								.browser(browser)
								.page(walletPage)
								.argument("${txtUserName}")
								.execute("createWallet"));				
						selenium(action -> action
								.browser(browser)
								.page(walletPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Create Wallet Test Action -- completed Successfully", ExtentColor.GREEN));
						break;
					case "enterToken":	
						selenium(action -> action
								.browser(browser)
								.page(landingPage)
								.argument("${newToken}")
								.execute("entertokenData"));			
						selenium(action -> action
								.browser(browser)
								.page(landingPage)
								.validate());	
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Token Entry Test Action -- completed Successfully", ExtentColor.GREEN));
						break;
					case "logout":			
						selenium(action -> action.browser(browser).page(landingPage).execute("logout"));				
						selenium(action -> action
								.browser(browser)
								.page(landingPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("User logged out successfully", ExtentColor.GREEN));
						break;
					case "cardLogin":					
						variable("txtCardNumber", GlobalVariables.cardNumber);
						variable("txtSecurityCode", GlobalVariables.securityCode);
						selenium(action -> action
								.browser(browser)
								.page(landingPage)
								.arguments("${txtCardNumber}", "${txtSecurityCode}")
								.execute("cardLogin"));				
						selenium(action -> action
								.browser(browser)
								.page(landingPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Card Login Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "usernameLogin":									
						selenium(action -> action
								.browser(browser)
								.page(landingPage)
								.argument("${txtUserName}")
								.execute("loginWithUsername"));					
						selenium(action -> action.browser(browser).page(landingPage).validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Username Login Test Action -- Successful", ExtentColor.GREEN));
						break;						
					case "editPin":							
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.argument(GlobalVariables.pin)
								.execute("editPIN"));			
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Edit Pin Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "editPinSecondCard":									
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.validate());
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.argument(GlobalVariables.pin)
								.execute("editPINSecondCard"));
						GenericComponents.takeScreenShot(browser, testModule);
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Edit Second Card Pin Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "editProfile":					
						CreateWallet.walletRows = TestDataAdapter.connectCreateWallet();
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.execute("editProfile"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Edit Profile Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "updatePassword":		
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.execute("updatePassword"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Update Password Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "updateChallengeQuestion":		
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.execute("updateChallengeQuestion"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Update Challenge Question Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "forgotPassword":	
						ForgotPassword.passwordRows = TestDataAdapter.connectForgotPasswordData();
						selenium(action -> action
								.browser(browser)
								.page(landingPage)								
								.execute("recoverPassword"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(landingPage)
								.validate());
						GenericComponents.takeScreenShot(browser, testModule);
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Forgot Password Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "contactUs":						
						ContactUs.contactRows = TestDataAdapter.connectContactUs();
						selenium(action -> action
								.browser(browser)
								.page(landingPage)
								.execute("contactUs"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(landingPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Contact Us Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "contactUsAuth":						
						ContactUs.contactRows = TestDataAdapter.connectContactUs();
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.execute("contactUsAuth"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Authenticated Contact Us Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "selectCardOption":					
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.argument("virtual")
								.execute("selectCardOption"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Select Card Option Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "selectPhysicalCard":					
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.argument("physical")
								.execute("selectCardOption"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Select Physical Card Option Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "saveForLater":					
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.execute("selectSaveForLater"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Select Save for Later Card Option Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "addNewCard":						 
						if (GlobalVariables.newToken == null) {
							selenium(action -> action
									.browser(browser)
									.page(walletPage)
									.argument("${newToken}")
									.execute("addNewCard"));
						} else {
							selenium(action -> action
									.browser(browser)
									.page(walletPage)
									.argument(GlobalVariables.newToken)
									.execute("addNewCard"));
						}
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(walletPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Add New Card Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "createCardProfile":						
						ProfileData.profileRows = TestDataAdapter.connectProfileData();
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.execute("createCardProfile"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Create Card Profile Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "updateCardProfile":					
						ProfileData.profileRows = TestDataAdapter.connectProfileData();
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.execute("updateCardProfile"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Update Card Profile Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "updateCardPin":						
						ProfileData.profileRows = TestDataAdapter.connectProfileData();
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.arguments(GlobalVariables.pin)
								.execute("updateCardPIN"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Update Card Pin Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "getCardNumberAndCvv":						 
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.execute("getCardNumberAndCVV"));
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Get Card Number and CVV Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "cardLogout":					
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.execute("cardLogout"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Card Logout Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "tokenLogin":			
						selenium(action -> action
								.browser(browser)
								.page(landingPage)
								.argument("${newToken}")
								.execute("entertokenData"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(landingPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Login with Token Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "addToWallet":				
						ProfileData.profileRows = TestDataAdapter.connectProfileData();
						variable("txtProfileUserName",
								"AUTO" + "citrus:randomString(3, UPPERCASE)" + "citrus:randomNumber(3)");
						selenium(action -> action
								.browser(browser)
								.page(walletPage)
								.argument("${txtProfileUserName}")
								.execute("addToWallet"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(walletPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Add to Wallet Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "cardContactUs":				
						ProfileData.profileRows = TestDataAdapter.connectProfileData();
						selenium(action -> action
								.browser(browser)
								.page(landingPage)
								.execute("cardContactUs"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(landingPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Card Contact Us Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "getSavedTokens":						
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.execute("getSavedTokens"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Get Saved Tokens Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "userCardLogin":						
						selenium(action -> action
								.browser(browser)
								.page(landingPage)
								.argument("${txtProfileUserName}")
								.execute("loginWithUsername"));
						selenium(action -> action
								.browser(browser)
								.page(landingPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Login with Card Number & CVV Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "selectVirtualCardForSaveForLater":				
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.argument("virtualCardSaveForLater")
								.execute("selectCardOption"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Select Virtual Card for Save for Later Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "selectPhysicalCardForSaveForLater":					
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.argument("physicalCardSaveForLater")
								.execute("selectCardOption"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Select Physical Card for Save for Later Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "getSavedTokenWithDifferentLevelBanks":					
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.execute("getSavedTokenWithDifferentLevelBanks"));
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Get Saved Tokens with different level banks Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "openWebsite":
						goToHomePage();
						break;
					case "selectReloadableCard":
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.argument("reloadable")
								.execute("selectCardOption"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Select Reloadable Card Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "selectCheckCard":
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.argument("check")
								.execute("selectCardOption"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Select Check Card Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "itzCashConfirmation":
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.execute("itzCashConfirmation"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Select ITzCash Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "selectCertificate":
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.argument("certificate")
								.execute("selectCardOption"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Select Certificate Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "selectPhysicalReloadable":
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.argument("physicalReloadable")
								.execute("selectCardOption"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Select Physical Reloadable Card Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "loadCardInvalidCVV":
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.argument("989")
								.execute("loadCardNegative"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Load Card Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "loadCard":
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.argument("${cvv}") 
								.execute("loadCard"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Load Card Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "lostThisCard":
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.execute("lostCard"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Lost This Card Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "createProfile":
						ProfileData.profileRows = TestDataAdapter.connectProfileData();
						selenium(action -> action
								.browser(browser)
								.page(walletPage)
								.execute("createProfile"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(walletPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Create Profile Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "getSerialNumber":
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.execute("getINRSerialNumber"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(profilePage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Get INR Serial Number Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "itzCashInvalidDOB":
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.execute("itzCashInvalidDOB"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Get INR Serial Number Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "clickReloadableCardLink":
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.arguments("1234567896548965", "112")
								.execute("clickReloadableCardLink"));
						GenericComponents.takeScreenShot(browser, testModule);
						selenium(action -> action
								.browser(browser)
								.page(redemptionPage)
								.validate());
						GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Select Have a reloadable Card Link Test Action -- Successful", ExtentColor.GREEN));
						break;
					case "addBank":
							selenium(action -> action
									.browser(browser)
									.page(bankAccountPage)
									.execute("addBank"));
							GenericComponents.takeScreenShot(browser, testModule);
							selenium(action -> action
									.browser(browser)
									.page(bankAccountPage)
									.validate());
							GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Transfer To Bank|Add Bank Test Action -- Successful", ExtentColor.GREEN));
							break;
					default:
						break;
					}
				} 
				GlobalVariables.newToken = null; 
			} 
		} catch (NullPointerException e) {
			e.printStackTrace();
			GlobalVariables.testInfo.log(Status.ERROR, ExceptionUtils.getRootCause(e));
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
			GlobalVariables.testInfo.log(Status.ERROR, ExceptionUtils.getRootCause(e1));
		} catch (Exception e2) {
			e2.printStackTrace();
			GlobalVariables.testInfo.log(Status.ERROR, ExceptionUtils.getRootCause(e2));
		} finally {
			
		}
	}
	
	/**
	 * Get Promocode API Call 
	 * This method sends Soap Request - GetPromocodeAPI to receive Token in response
	 * @throws Exception 
	 */
	public void getPromocodeAPITest() throws Exception {
		List<Row> promocodeAPIRow = TestDataAdapter.getPromocodeAPIData();
		variable("amount", promocodeAPIRow.get(0).getValue(PromocodeAPIData.Amount).toString());
		variable("clientId", promocodeAPIRow.get(0).getValue(PromocodeAPIData.ClientID).toString());
		variable("address1", promocodeAPIRow.get(0).getValue(PromocodeAPIData.Address1).toString());
		variable("address2", promocodeAPIRow.get(0).getValue(PromocodeAPIData.Address2).toString());
		variable("city", promocodeAPIRow.get(0).getValue(PromocodeAPIData.City).toString());
		variable("countryCode", promocodeAPIRow.get(0).getValue(PromocodeAPIData.CountryCode).toString());
		variable("emailAddress", promocodeAPIRow.get(0).getValue(PromocodeAPIData.EmailAddress).toString());
		variable("firstName", promocodeAPIRow.get(0).getValue(PromocodeAPIData.FirstName).toString());
		variable("lastName", promocodeAPIRow.get(0).getValue(PromocodeAPIData.LastName).toString());
		variable("phoneNumber", promocodeAPIRow.get(0).getValue(PromocodeAPIData.PhoneNumber).toString());
		variable("endClientId", promocodeAPIRow.get(0).getValue(PromocodeAPIData.EndClientID).toString());
		variable("issuanceProductId", promocodeAPIRow.get(0).getValue(PromocodeAPIData.IssuanceProductID).toString());
		variable("locationId", promocodeAPIRow.get(0).getValue(PromocodeAPIData.LocationID).toString());
		variable("password", CryptionAlgorithm.decryptString(testProp.getProperty("web.omsi.encryptedPassword")));
		variable("promocodeProgramId", promocodeAPIRow.get(0).getValue(PromocodeAPIData.PromocodeProgramID).toString());
		variable("userId", CryptionAlgorithm.decryptString(testProp.getProperty("web.omsi.encryptedUserId")));	
		variable("distributionTemplate",
				promocodeAPIRow.get(0).getValue(PromocodeAPIData.DistributionTemplate).toString());		
		String participantData = promocodeAPIRow.get(0).getValue(PromocodeAPIData.ParticipantID).toString();			
		if(participantData.equalsIgnoreCase(GlobalVariables.execTestCase)){
			variable("participantId", "AUTOPARTID" + "citrus:randomNumber(3)");				
		} else if(participantData.equalsIgnoreCase(" ")){		
			variable("participantId", promocodeAPIRow.get(0).getValue(PromocodeAPIData.ParticipantID).toString());				
		} 
		sleep(10);
		soap(action -> action
				.client(getPromocodeClient)
				.send()
				.fork(true)
				.name("GetPromocodeRequest")				
				.payload(new ClassPathResource("APITemplates/getPromocodeRequest.xml")));
		sleep(35);
		soap(action -> action
				.client(getPromocodeClient)
				.receive()
				.name("GetPromocodeResponse")
				.payload(new ClassPathResource("APITemplates/getPromocodeResponse.xml"))
				.validationCallback((message, context) -> {
					context.setVariable("password", "******");					
					GlobalVariables.testInfo
						.info("Payload Variables:" + "<textarea>" + context.getVariables() + "</textarea>");
					GlobalVariables.testInfo
						.info("Get Promocode Response:" + "<textarea>"+ message.getPayload(String.class) + "</textarea>");
					GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Token Generation Successful >> "+context.getVariable("newToken"), ExtentColor.GREEN));
				})
				.extractFromPayload("(//*[3])/text()", "newToken"));		
		System.out.println("DONE");
	}
	
	/**
	 * GetPersonInfo API Call 
	 * This method sends HTTP/REST Request - GetPersonAPI to receive Security Code in response
	 * @throws Exception 
	 */
	public void getPersonAPI(String newToken) throws Exception{
		http(action -> {
			try {
				action
						.client(fisClient)
						.send()
						.post("/CO_GetPersonInfo.asp")
						.fork(true)			
						.queryParam("userid", CryptionAlgorithm.decryptString(testProp.getProperty("api.fis.userId")))			
						.queryParam("pwd", CryptionAlgorithm.decryptString(testProp.getProperty("api.fis.password")))	
						.queryParam("sourceid", CryptionAlgorithm.decryptString(testProp.getProperty("api.fis.sourceId")))				
						.queryParam("ClientUniqueID", newToken +"_00" )
						.queryParam("ClientID", "262428")
						.queryParam("SkipCreateRec", "1")
						.queryParam("CardStatus_List", "1,2,3,4,5,6,7,8,9");
			} catch (Exception e) {			
				e.printStackTrace();
			}
		});
		sleep(20);
		http(action -> action.client(fisClient).receive().response(HttpStatus.OK)
				.extractFromHeader("citrus_http_status_code", "status_code")
				.extractFromHeader("citrus_http_reason_phrase", "status_phrase")
				.validationCallback((message, context) -> {
				String[] personInfo = message.getPayload(String.class).replace("|", " ").split(" ");
				List<String> cvv = new ArrayList<>();
					for(int i=0; i<personInfo.length; i++){
						if(personInfo[i].length()==3){
							cvv.add(personInfo[i]);
							}
							}
					for(int j=0; j<cvv.size(); j++){
						variable("cvv", cvv.get(2));
					}
				GlobalVariables.testInfo.pass(MarkupHelper.createLabel("Get Person Info API  Successful & Got Security Code from FIS API Call ", ExtentColor.GREEN));
							}
				)
				);
	}

	/**
	 * Navigate to Digital Wallet Main Page
	 * 
	 * @throws IOException
	 */
	public void goToHomePage() throws IOException {
		selenium(builder -> builder
				.browser(browser)
				.start());
		selenium(builder -> builder
				.browser(browser)
				.clearCache());
		browser.getWebDriver()
			    .manage()
			    .window()
			    .maximize();
		selenium(action -> action
			    .browser(browser)
				.navigate(testProp.getProperty("web.digitalwallet.url")));
		GlobalVariables.testInfo.pass(MarkupHelper.createLabel(
				"Entered Home Page. URL >> " + testProp.getProperty("web.digitalwallet.url"), ExtentColor.GREEN));
		browser.getWebDriver()
		       .manage()
		       .timeouts()
		       .pageLoadTimeout(30, TimeUnit.SECONDS);
		browser.getWebDriver()
		       .manage()
		       .timeouts()
		       .implicitlyWait(25, TimeUnit.SECONDS);
	}
}