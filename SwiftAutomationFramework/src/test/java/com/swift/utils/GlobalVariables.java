package com.swift.utils;

import java.util.List;
import org.apache.metamodel.DataContext;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.schema.*;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;


/**
 * @author skrishnan
 */

public class GlobalVariables {
	
	public static String execEnvironment;
	// Extent Report Variables
	public static ExtentReports reports;
	public static ExtentTest testInfo;
	public static ExtentTest testStepInfo;
	public static ExtentTestInterruptedException testexception;
	public static ExtentHtmlReporter htmlReporter;
	public static String reportScreenshots;
	// Total TestCases in Execution
	public static List<Row> testCasesList;
	public static int totalCases;
	public static String execTestCase;
	public static String execTestCoverage;
	public static String module;
	// Test Data Location
	public static String testDataLocation;
	// Token ID
	public static String newToken;
	// cardNumber
	public static String cardNumber;
	public static String securityCode;
	public static String CVV;
	public static String serialNumber;
	// LoginUserName
	public static String userName;
	public static String password;
	public static String newPassword; 
	public static String pin;  
	public static String challengeAnswer;
	public static String routingNumber;
	public static String accountNumber;
	// Test Data Sheet
	public static DataContext excel;
	public static Schema schema;

	// Data Driver
	public static Table dataDriver;
	public static Column testName;
	public static Column testDescription;
	public static Column testCoverage;
	public static Column executionFlag;
	public static Column testComponentFlow;
	
	// Card Login Data
	public static class CardData {
		public static Table cardData;
		public static Column cardValue;
		public static Column codeValue;
	}
	// login with username
	public static class UsernameData {
		public static Table usernameData;
		public static Column Username;
		public static List<Row> usernameRows;
	}
	// Token Entry Data
	public static class TokenData {
		public static Table tokenData;
		public static Column tokenValue;
		public static Column Token2;
		public static Column Token3;
		public static List<Row> tokenRows;
	}
	// Unauthenticated - Forgot Password
	public static class ForgotPassword {
		public static Table forgotPasswordData;
		public static Column UserName;
		public static List<Row> passwordRows;
	}
	// Contact Us
	public static class ContactUs {
		public static Table contactUs;
		public static Column FirstName;
		public static Column LastName;
		public static Column EmailAddress;
		public static Column PhoneNumber;
		public static Column ReasonId;
		public static Column QuestionComments;
		public static List<Row> contactRows;
	}
	// Create Wallet Data
	public static class CreateWallet {
		public static Table createWallet;
		public static Column Username;
		public static Column Email;
		public static Column ConfirmEmail;
		public static Column FirstName;
		public static Column LastName;
		public static Column Country;
		public static Column Address1;
		public static Column Address2;
		public static Column Address3;
		public static Column Address4;
		public static Column City;
		public static Column State;
		public static Column Zip;
		public static Column Phone;
		public static Column ChallengeQuestion;
		public static Column NewAddress2;
		public static Column NewChallengeQuestion;
		public static Column profileDOB;
		public static Column salutation;
		public static Column gender;
		public static Column mobilePhone;
		public static Column mothersMaidenName;
		public static List<Row> walletRows;
		public static Column invalidDOB;
	}
	// card profile data
	public static class ProfileData {
		public static Table profileData;
		public static Column FirstName;
		public static Column LastName;
		public static Column Country;
		public static Column Address1;
		public static Column Address2;
		public static Column City;
		public static Column State;
		public static Column Zip;
		public static Column Email;
		public static Column NewLastName;
		public static Column ConfirmEmail;
		public static Column ChallengeQuestion;
		public static Column PhoneNumber;
		public static Column AccountType;
		public static Column TransferAmount;
		public static Column InvalidAccountNumber;
		public static Column InvalidRoutingNumber;
		public static List<Row> profileRows;
	}
	// Promocode API Data
	public static class PromocodeAPIData {
		public static Table promocodeAPIData;
		public static Column Amount;
		public static Column ClientID;
		public static Column Address1;
		public static Column Address2;
		public static Column City;
		public static Column CountryCode;
		public static Column EmailAddress;
		public static Column FirstName;
		public static Column LastName;
		public static Column PhoneNumber;
		public static Column EndClientID;
		public static Column IssuanceProductID;
		public static Column LocationID;
		public static Column PromocodeProgramID;
		public static Column DistributionTemplate;
		public static Column ParticipantID;
		public static List<Row> promocodeAPIRow;
	}

	public static class Constants {
		public static String errorMsg = "Oops, there was a problem.";
		public static String successRedemptionURL = "https://"+execEnvironment+".prepaiddigitalsolutions.com/SuccessRedemption";
		public static String confirmRedemptionURL = "https://"+execEnvironment+".prepaiddigitalsolutions.com/ConfirmRedemptionOption";
		public static String forgotPasswordURL = "https://"+execEnvironment+".prepaiddigitalsolutions.com/ForgotPassword";
		public static String createProfileURL = "https://"+execEnvironment+".prepaiddigitalsolutions.com/CreateProfile";
		public static String baseURL = "https://"+execEnvironment+".prepaiddigitalsolutions.com/";
		public static String loginURL = "https://"+execEnvironment+".prepaiddigitalsolutions.com/Login";
		public static String browserTitle = "Digital Wallet";
		public static String pinUpdateMessage = "PIN update successful.";
		public static String profileUpdateMessage = "Profile updated successfully.";
		public static String updatePasswordMessage = "Password update successful.";
		public static String updateChallengeQuestionMessage = "Challenge Question update successful.";
		public static String contactUsSuccessMessage = "Your request has been submitted to Member Support. You will be contacted shortly for further assistance."
				+ "\n" + "Continue";
		public static String invalidRoutingErr = "Error: Invalid routing number";
		public static String insufficientFundsErr = "Your card does not have sufficient funds to complete a transfer.";
		public static String addBankSuccessMsg = "Conguratulations!";
		public static String removeBankMsg = "Remove Bank";
	}
	
	   //create profile data
	public static class CardProfileData {
		public static String firstName = ProfileData.profileRows.get(0).getValue(ProfileData.FirstName).toString();
		public static String lastName = ProfileData.profileRows.get(0).getValue(ProfileData.LastName).toString();
		public static String country = ProfileData.profileRows.get(0).getValue(ProfileData.Country).toString();
		public static String address1  = ProfileData.profileRows.get(0).getValue(ProfileData.Address1).toString();
		public static String address2 = ProfileData.profileRows.get(0).getValue(ProfileData.Address2).toString();
		public static String city = ProfileData.profileRows.get(0).getValue(ProfileData.City).toString();
		public static String state = ProfileData.profileRows.get(0).getValue(ProfileData.State).toString();
		public static String zip = ProfileData.profileRows.get(0).getValue(ProfileData.Zip).toString();
		public static String email = ProfileData.profileRows.get(0).getValue(ProfileData.Email).toString();
		public static String invalidAccountNumber = ProfileData.profileRows.get(0).getValue(ProfileData.InvalidAccountNumber).toString();
		public static String invalidRoutingNumber = ProfileData.profileRows.get(0).getValue(ProfileData.InvalidRoutingNumber).toString();
		public static String accountType = ProfileData.profileRows.get(0).getValue(ProfileData.AccountType).toString();
		public static String transferAmount = ProfileData.profileRows.get(0).getValue(ProfileData.TransferAmount).toString();
		}
}
