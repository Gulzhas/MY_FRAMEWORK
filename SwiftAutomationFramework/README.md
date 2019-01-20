	Swift Automation Framework 

	This Project contains QA Automation architecture/framework and scripts. The ReadMe document will be updated soon. 
	This is a maven project. The Framework has capabilities to support system, integration, functional and non-functional testing types
        
    References and useful links
	• Programming Language: Java 8 SE - https://docs.oracle.com/javase/tutorial/
	• Integrated Development Environment (IDE): Eclipse Oxygen - https://help.eclipse.org/2018-09/index.jsp
	• Software Project Management Tool: Maven - https://maven.apache.org/what-is-maven.html
	• Web Automation Framework: Selenium WebDriver - https://www.seleniumhq.org/docs/03_webdriver.jsp
	• Design Pattern: Page Object Model (POM) 
	• Testing Framework: TestNG - https://testng.org/doc/documentation-main.html
	• Framework Type: Hybrid-driven Framework, CITRUS framework - https://citrusframework.org/, https://www.guru99.com/creating-keyword-hybrid-frameworks-with-selenium.html
	• Data Access Framework: Apache Metamodel - http://metamodel.apache.org/
	• Version Control Tool: git  - https://git-scm.com/docs
	• Behavior-Driven Development (BDD) Framework: Cucumber - https://docs.cucumber.io/guides/
	• Reporting: Extent Report - http://extentreports.com/docs/versions/3/java/
	• Cross Browser and Cross Platform Testing: BrowserStack.com - https://www.browserstack.com/automate/capabilities
	• Spring Framework - https://docs.spring.io/spring-boot/docs/2.0.6.RELEASE/reference/htmlsingle/
	• Automation Software -  J:\QAAutomation\AutomationSoftware\windows

Page Objects

com.swift.digitalwallet.pages

LandingPage.java

ProfilePage.java

WalletPage.java
	• createCardProfile() 
	• updateCardPIN()
	• updateCardProfile() 
	• getCardNumberAndCVV() 
	• contactUsAuth() 
	• updateChallengeQuestion()
	• updatePassword() 
	• editPIN()
	• editPINSecondCard()
	• editProfile() 
	• cardLogout()
	• entertokenData()
	• loginWithUsername()
	• cardLogin()
	• logout()
	• recoverPassword()
	• contactUs()
	• cardContactUs()
	• getDynamicUsername()
	• createWallet()
	• addToWallet()
	• selectCardOption() 
	• selectSaveForLater()
	• selectVirtualCardForSaveForLater()
	• selectPhysicalCardForSaveForLater()
	• addNewCard()
	• getSavedTokens()
	• getSavedTokenWithDifferentLevelBanks() 

Page Object Model is a design pattern to create Object Repository for web UI elements. It helps make the code more readable, maintainable, and reusable.
Page Factory is an inbuilt Page Object Model concept for Selenium WebDriver. With the help of PageFactory class, we use annotations @FindBy to find Web Elements. 
For example: 
@FindBy(id = "Login.Tab.CardNum")
public static WebElement tabCard;

How to locate Web Elements in Selenium WebDriver?
There are some browser tools that you can use in order to identify web elements in the DOM (Document Object Model) easier. These are:
	• Google Developer Tools for Chrome
	• Web Inspector for Safari
	• Right click of mouse and select Inspect
	• Shortcut for Chrome FN+F12
	
Selenium WebDriver API supports different possibilities to identify elements:
	• By id
	• By name
	• By className
	• By linkText and By Partial linkText
	• By tagName
	• By xpath
	• By css
NOTE: It is common way and good to use IDs and NAMEs for locating web elements. 
ID’s are supposed to be unique on a page, but NAMEs are NOT always unique, and it makes ID’s are the most reliable locator. If there are multiple elements with the same NAME locator then the first element on the page is selected. ID and NAME locators are the fastest and safest locators out of all locators. 
For dynamic web elements and elements with no id, name, links and etc. we use XPATH and CSS selectors, but xpath and css locators are known as Performance Killers!

In test automation, we write scripts. Scripting is basically about three ‘A’s:
	ARRANGEMENT
	ACTION
	ASSERTION
	
	#1. ARRANGEMENT or Object Identification
	We identify objects (buttons, dropdowns etc.) either by their ids, names or by their Window Titles etc.
	In case of web application, we identify by user ID, or By XPath or By CSS or By Class Name etc.
	For Example: @FindBy(id = "Login.Tab.CardNum")
	                         public static WebElement tabCard;
	
	#2. ACTION on the Identified Object
	When the objects are identified, we perform some kind of actions on it either by mouse or by keyboard. For example, either we click, or we double-click, or we mouse hover over it or sometimes we drag-drop.
	For example: tabUsername.click();
                               txtUsername.sendKeys(txtUserName);
	
	#3. ASSERTION
	The assertion is basically checking the object with some expected result. For example, if we press 2+3 on the calculator, the screen should show 5. In this case, our expected result is 5.
	For example: Assert.assertEquals(Constants.contactUsSuccessMessage, msgSuccess.getText()); 
