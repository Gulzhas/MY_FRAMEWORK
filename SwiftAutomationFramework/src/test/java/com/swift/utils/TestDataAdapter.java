package com.swift.utils;

import java.io.File;
import java.util.List;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.excel.ExcelDataContext;
import org.apache.metamodel.query.OperatorType;
import org.apache.metamodel.query.Query;
import org.apache.metamodel.schema.Column;
import org.apache.metamodel.schema.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.aventstack.extentreports.Status;
import com.swift.utils.GlobalVariables.CardData;
import com.swift.utils.GlobalVariables.ContactUs;
import com.swift.utils.GlobalVariables.CreateWallet;
import com.swift.utils.GlobalVariables.ForgotPassword;
import com.swift.utils.GlobalVariables.ProfileData;
import com.swift.utils.GlobalVariables.PromocodeAPIData;
import com.swift.utils.GlobalVariables.TokenData;

public class TestDataAdapter {
	protected static Logger log = LoggerFactory.getLogger(TestDataAdapter.class);

	public static void getTestDataLocation(String envValue, String appValue, String suiteTypeValue) {
		GlobalVariables.testDataLocation = System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\"
				+ appValue + "\\" + envValue + "\\" + appValue + "_" + suiteTypeValue + ".xlsx";
	}

	public static void connectDataDriver() {
		GlobalVariables.excel = new ExcelDataContext(new File(GlobalVariables.testDataLocation));
		GlobalVariables.schema = GlobalVariables.excel.getDefaultSchema();
		GlobalVariables.dataDriver = GlobalVariables.schema.getTableByName("DataDriver");
		GlobalVariables.testName = GlobalVariables.dataDriver.getColumnByName("TestName");
		GlobalVariables.testCoverage = GlobalVariables.dataDriver.getColumnByName("TestCoverage");
		GlobalVariables.testDescription = GlobalVariables.dataDriver.getColumnByName("TestDescription");
		GlobalVariables.executionFlag = GlobalVariables.dataDriver.getColumnByName("ExecutionFlag");
		GlobalVariables.testComponentFlow = GlobalVariables.dataDriver.getColumnByName("TestComponentFlow");
		List<Table> tables = GlobalVariables.schema.getTables();
		if (!tables.isEmpty()) {
			Assert.assertTrue(tables.size() != 0, "Excel Connect Success");
			GlobalVariables.testInfo.log(Status.INFO, "Test Data Connection Successful");
		} else {
			GlobalVariables.testInfo
					.fatal("EXCEL SHEET ERROR << Check if the Excel sheet set up for expected data format");
			System.err.println("EXCEL SHEET ERROR << Check if the Excel sheet set up for expected data format");
			System.exit(0);
		}
		Query testCases = new Query().from(GlobalVariables.dataDriver)
				.select(GlobalVariables.testName, GlobalVariables.testCoverage, GlobalVariables.testDescription,
						GlobalVariables.testComponentFlow)
				.where(GlobalVariables.executionFlag, OperatorType.EQUALS_TO, 'Y');
		DataSet ds = GlobalVariables.excel.executeQuery(testCases);
		List<Row> dsRows = ds.toRows();
		if (dsRows.size() == 0) {
			log.error("No Test Cases with Execution Flag Yes", "Test Data Sheet in ${testDataLocation}");
			System.err.println("THERE ARE 0 TESTS WITH EXECUTION FLAG AS YES");
			GlobalVariables.testInfo.fatal("THERE ARE 0 TESTS WITH EXECUTION FLAG AS YES");
			System.exit(0);
		}
		GlobalVariables.testCasesList = dsRows;
	}

	public static List<Row> connectCardData() throws NullPointerException {
		CardData.cardData = GlobalVariables.schema.getTableByName("cardLogin");
		Column testCaseName = CardData.cardData.getColumnByName("TestName");
		CardData.cardValue = CardData.cardData.getColumnByName("CardNumber");
		CardData.codeValue = CardData.cardData.getColumnByName("SecurityCode");
		Query cardDetail = new Query().from(CardData.cardData).select(CardData.cardValue, CardData.codeValue)
				.where(testCaseName, OperatorType.EQUALS_TO, GlobalVariables.execTestCase);
		DataSet ds = GlobalVariables.excel.executeQuery(cardDetail);
		List<Row> dsRows = ds.toRows();
		if (dsRows.size() == 0) {
			GlobalVariables.testInfo
					.fatal("THERE IS NO MATCHING TEST DATA IN cardLogin TABLE FOR THE CURRENT CASE IN EXECUTION");
			log.error("No Test Data exist in cardLogin sheet for the current test",
					"Test Data Sheet in ${testDataLocation}");
			System.err.println("THERE IS NO MATCHING TEST DATA IN cardLogin TABLE FOR THE CURRENT CASE IN EXECUTION");
			System.exit(0);
		}
		return dsRows;
	}

	public static List<Row> getPromocodeAPIData() throws NullPointerException {
		PromocodeAPIData.promocodeAPIData = GlobalVariables.schema.getTableByName("getPromocodeAPI");
		Column testCaseName = PromocodeAPIData.promocodeAPIData.getColumnByName("TestName");
		PromocodeAPIData.Amount = PromocodeAPIData.promocodeAPIData.getColumnByName("Amount");
		PromocodeAPIData.ClientID = PromocodeAPIData.promocodeAPIData.getColumnByName("ClientID");
		PromocodeAPIData.Address1 = PromocodeAPIData.promocodeAPIData.getColumnByName("Address1");
		PromocodeAPIData.Address2 = PromocodeAPIData.promocodeAPIData.getColumnByName("Address2");
		PromocodeAPIData.City = PromocodeAPIData.promocodeAPIData.getColumnByName("City");
		PromocodeAPIData.CountryCode = PromocodeAPIData.promocodeAPIData.getColumnByName("CountryCode");
		PromocodeAPIData.EmailAddress = PromocodeAPIData.promocodeAPIData.getColumnByName("EmailAddress");
		PromocodeAPIData.FirstName = PromocodeAPIData.promocodeAPIData.getColumnByName("FirstName");
		PromocodeAPIData.LastName = PromocodeAPIData.promocodeAPIData.getColumnByName("LastName");
		PromocodeAPIData.PhoneNumber = PromocodeAPIData.promocodeAPIData.getColumnByName("PhoneNumber");
		PromocodeAPIData.EndClientID = PromocodeAPIData.promocodeAPIData.getColumnByName("EndClientID");
		PromocodeAPIData.IssuanceProductID = PromocodeAPIData.promocodeAPIData.getColumnByName("IssuanceProductID");
		PromocodeAPIData.LocationID = PromocodeAPIData.promocodeAPIData.getColumnByName("LocationID");
		PromocodeAPIData.PromocodeProgramID = PromocodeAPIData.promocodeAPIData.getColumnByName("PromocodeProgramID");
		PromocodeAPIData.DistributionTemplate = PromocodeAPIData.promocodeAPIData
				.getColumnByName("DistributionTemplate");
		PromocodeAPIData.ParticipantID = PromocodeAPIData.promocodeAPIData.getColumnByName("ParticipantID");
		Query cardDetail = new Query().from(PromocodeAPIData.promocodeAPIData).selectAll().where(testCaseName,
				OperatorType.EQUALS_TO, GlobalVariables.execTestCase);
		DataSet ds = GlobalVariables.excel.executeQuery(cardDetail);
		List<Row> dsRows = ds.toRows();
		if (dsRows.size() == 0) {
			GlobalVariables.testInfo
					.fatal("THERE IS NO MATCHING TEST DATA IN getPromocodeAPI TABLE FOR THE CURRENT CASE IN EXECUTION");
			log.error("No Test Data exist in getPromocodeAPI sheet for the current test",
					"Test Data Sheet in " + GlobalVariables.testDataLocation);
			System.err.println(
					"THERE IS NO MATCHING TEST DATA IN getPromocodeAPI TABLE FOR THE CURRENT CASE IN EXECUTION");
			System.exit(0);
		}
		return dsRows;
	}

	public static List<Row> enterTokenData() throws NullPointerException {
		TokenData.tokenData = GlobalVariables.schema.getTableByName("tokenData");
		Column testCaseName = TokenData.tokenData.getColumnByName("TestName");
		TokenData.tokenValue = TokenData.tokenData.getColumnByName("Token1");
		TokenData.Token2 = TokenData.tokenData.getColumnByName("Token2");
		Query tokenDetail = new Query().from(TokenData.tokenData).selectAll().where(testCaseName,
				OperatorType.EQUALS_TO, GlobalVariables.execTestCase);
		DataSet ds = GlobalVariables.excel.executeQuery(tokenDetail);
		List<Row> dsRows = ds.toRows();
		if (dsRows.size() == 0) {
			GlobalVariables.testInfo
					.fatal("THERE IS NO MATCHING TEST DATA IN tokenData TABLE FOR THE CURRENT CASE IN EXECUTION");
			log.error("No Test Data exist in tokenData sheet for the current test",
					"Test Data Sheet in ${testDataLocation}");
			System.err.println("THERE IS NO MATCHING TEST DATA IN tokenData TABLE FOR THE CURRENT CASE IN EXECUTION");
			System.exit(0);
		}
		return dsRows;
	}	
	
	public static List<Row> connectForgotPasswordData() throws NullPointerException {
		ForgotPassword.forgotPasswordData = GlobalVariables.schema.getTableByName("forgotPasswordData");
		ForgotPassword.UserName = ForgotPassword.forgotPasswordData.getColumnByName("UserName");
		Query forgotPasswordDetail = new Query().from(ForgotPassword.forgotPasswordData).select(ForgotPassword.UserName);
		DataSet ds = GlobalVariables.excel.executeQuery(forgotPasswordDetail);
		List<Row> dsRows = ds.toRows();
		if (dsRows.size() == 0) {
			GlobalVariables.testInfo.fatal(
					"THERE IS NO MATCHING TEST DATA IN forgotPasswordData TABLE FOR THE CURRENT CASE IN EXECUTION");
			log.error("No Test Data exist in forgotPasswordData sheet for the current test",
					"Test Data Sheet in ${testDataLocation}");
			System.err.println(
					"THERE IS NO MATCHING TEST DATA IN forgotPasswordData TABLE FOR THE CURRENT CASE IN EXECUTION");
			System.exit(0);
			System.out.println(dsRows.size());
		}
		return dsRows;
	}	

	public static List<Row> connectContactUs() throws NullPointerException {
		ContactUs.contactUs = GlobalVariables.schema.getTableByName("contactUs");
		ContactUs.FirstName = ContactUs.contactUs.getColumnByName("FirstName");
		ContactUs.LastName = ContactUs.contactUs.getColumnByName("LastName");
		ContactUs.EmailAddress = ContactUs.contactUs.getColumnByName("EmailAddress");
		ContactUs.PhoneNumber = ContactUs.contactUs.getColumnByName("PhoneNumber");
		ContactUs.ReasonId = ContactUs.contactUs.getColumnByName("ReasonId");
		ContactUs.QuestionComments = ContactUs.contactUs.getColumnByName("QuestionComments");
		
		Query contactUsDetail = new Query().from(ContactUs.contactUs).selectAll();
		DataSet ds = GlobalVariables.excel.executeQuery(contactUsDetail);
		List<Row> dsRows = ds.toRows();
		if (dsRows.size() == 0) {
			GlobalVariables.testInfo
					.fatal("THERE IS NO MATCHING TEST DATA IN contactUs TABLE FOR THE CURRENT CASE IN EXECUTION");
			log.error("No Test Data exist in contactUs sheet for the current test",
					"Test Data Sheet in ${testDataLocation}");
			System.err.println("THERE IS NO MATCHING TEST DATA IN contactUs TABLE FOR THE CURRENT CASE IN EXECUTION");
			System.exit(0);
		}
		return dsRows;
	}

	public static List<Row> connectCreateWallet() throws NullPointerException {
		CreateWallet.createWallet = GlobalVariables.schema.getTableByName("createWallet");
		Column testCaseName = CreateWallet.createWallet.getColumnByName("TestName");
		CreateWallet.Username = CreateWallet.createWallet.getColumnByName("Username");
		CreateWallet.Email = CreateWallet.createWallet.getColumnByName("Email");
		CreateWallet.ConfirmEmail = CreateWallet.createWallet.getColumnByName("ConfirmEmail");
		CreateWallet.FirstName = CreateWallet.createWallet.getColumnByName("FirstName");
		CreateWallet.LastName = CreateWallet.createWallet.getColumnByName("LastName");
		CreateWallet.Country = CreateWallet.createWallet.getColumnByName("Country");
		CreateWallet.Address1 = CreateWallet.createWallet.getColumnByName("Address1");
		CreateWallet.Address2 = CreateWallet.createWallet.getColumnByName("Address2");
		CreateWallet.Address3 = CreateWallet.createWallet.getColumnByName("Address3");
		CreateWallet.Address4 = CreateWallet.createWallet.getColumnByName("Address4");
		CreateWallet.City = CreateWallet.createWallet.getColumnByName("City");
		CreateWallet.State = CreateWallet.createWallet.getColumnByName("State");
		CreateWallet.Zip = CreateWallet.createWallet.getColumnByName("Zip");
		CreateWallet.Phone = CreateWallet.createWallet.getColumnByName("Phone");
		CreateWallet.ChallengeQuestion = CreateWallet.createWallet.getColumnByName("ChallengeQuestion");
		CreateWallet.NewAddress2 = CreateWallet.createWallet.getColumnByName("NewAddress2");
		CreateWallet.NewChallengeQuestion = CreateWallet.createWallet.getColumnByName("NewChallengeQuestion");	
		CreateWallet.profileDOB = CreateWallet.createWallet.getColumnByName("ProfileDOB");
		CreateWallet.invalidDOB = CreateWallet.createWallet.getColumnByName("InvalidDOB");
		CreateWallet.salutation = CreateWallet.createWallet.getColumnByName("Salutation");
		CreateWallet.mobilePhone = CreateWallet.createWallet.getColumnByName("MobilePhone");
		CreateWallet.gender = CreateWallet.createWallet.getColumnByName("Gender");
		CreateWallet.mothersMaidenName = CreateWallet.createWallet.getColumnByName("MothersMaidenName");
		Query createWalletDetail = new Query().from(CreateWallet.createWallet).selectAll().where(testCaseName,
				OperatorType.EQUALS_TO, GlobalVariables.execTestCase);
		DataSet ds = GlobalVariables.excel.executeQuery(createWalletDetail);
		List<Row> dsRows = ds.toRows();
		if (dsRows.size() == 0) {
			GlobalVariables.testInfo
					.fatal("THERE IS NO MATCHING TEST DATA IN createWallet TABLE FOR THE CURRENT CASE IN EXECUTION");
			log.error("No Test Data exist in createWallet sheet for the current test",
					"Test Data Sheet in ${testDataLocation}");
			System.err
					.println("THERE IS NO MATCHING TEST DATA IN createWallet TABLE FOR THE CURRENT CASE IN EXECUTION");
			System.exit(0);
		}
		return dsRows;
	}

	public static List<Row> connectProfileData() throws NullPointerException {
		ProfileData.profileData = GlobalVariables.schema.getTableByName("profileData");
		Column testCaseName = ProfileData.profileData.getColumnByName("TestName");
		ProfileData.FirstName = ProfileData.profileData.getColumnByName("FirstName");
		ProfileData.LastName = ProfileData.profileData.getColumnByName("LastName");
		ProfileData.Country = ProfileData.profileData.getColumnByName("CountryCode");
		ProfileData.Address1 = ProfileData.profileData.getColumnByName("Address1");
		ProfileData.Address2 = ProfileData.profileData.getColumnByName("Address2");
		ProfileData.City = ProfileData.profileData.getColumnByName("City");
		ProfileData.State = ProfileData.profileData.getColumnByName("State");
		ProfileData.Zip = ProfileData.profileData.getColumnByName("Zip");
		ProfileData.Email = ProfileData.profileData.getColumnByName("Email");
		ProfileData.ConfirmEmail = ProfileData.profileData.getColumnByName("ConfirmEmail");
		ProfileData.NewLastName = ProfileData.profileData.getColumnByName("NewLastName");
		ProfileData.ChallengeQuestion = ProfileData.profileData.getColumnByName("ChallengeQuestion");
		ProfileData.PhoneNumber = ProfileData.profileData.getColumnByName("PhoneNumber");
		ProfileData.AccountType = ProfileData.profileData.getColumnByName("AccountType");
		ProfileData.TransferAmount = ProfileData.profileData.getColumnByName("TransferAmount");
		ProfileData.InvalidAccountNumber = ProfileData.profileData.getColumnByName("InvalidAccountNumber");
		ProfileData.InvalidRoutingNumber = ProfileData.profileData.getColumnByName("InvalidRoutingNumber");
		Query profileDetail = new Query().from(ProfileData.profileData).selectAll().where(testCaseName,
				OperatorType.EQUALS_TO, GlobalVariables.execTestCase);
		DataSet ds = GlobalVariables.excel.executeQuery(profileDetail);
		List<Row> dsRows = ds.toRows();
		if (dsRows.size() == 0) {
			GlobalVariables.testInfo
					.fatal("THERE IS NO MATCHING TEST DATA IN profileData TABLE FOR THE CURRENT CASE IN EXECUTION");
			log.error("No Test Data exist in profileData sheet for the current test",
					"Test Data Sheet in ${testDataLocation}");
			System.err.println("THERE IS NO MATCHING TEST DATA IN profileData TABLE FOR THE CURRENT CASE IN EXECUTION");
			System.exit(0);
		}
		return dsRows;
	}
}
