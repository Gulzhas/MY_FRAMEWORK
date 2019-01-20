package com.swift.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.consol.citrus.container.SequenceAfterSuite;
import com.consol.citrus.container.SequenceAfterTest;
import com.consol.citrus.container.SequenceBeforeSuite;
import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.dsl.runner.TestRunner;
import com.consol.citrus.dsl.runner.TestRunnerAfterSuiteSupport;
import com.consol.citrus.dsl.runner.TestRunnerAfterTestSupport;
import com.consol.citrus.dsl.runner.TestRunnerBeforeSuiteSupport;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.selenium.endpoint.SeleniumBrowser;
import com.consol.citrus.selenium.endpoint.SeleniumBrowserBuilder;
import com.consol.citrus.ws.addressing.WsAddressingHeaders;
import com.consol.citrus.ws.addressing.WsAddressingVersion;
import com.consol.citrus.ws.client.WebServiceClient;
import com.consol.citrus.ws.message.converter.WebServiceMessageConverter;
import com.consol.citrus.ws.message.converter.WsAddressingMessageConverter;
import com.swift.digitalwallet.page.BankAccountPage;
import com.swift.digitalwallet.page.LandingPage;
import com.swift.digitalwallet.page.ProfilePage;
import com.swift.digitalwallet.page.RedemptionPage;
import com.swift.digitalwallet.page.WalletPage;
import com.swift.fis.api.GetPersonAPI;
import com.swift.secure.utils.CryptionAlgorithm;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

/**
 * @author skrishnan
 */
@Configuration
@PropertySource(value = "file:${test.env.properties}/appconfig-${test.execution.env}.properties")
public class GenericComponents {
	@Autowired
	Environment testProp;
	@Value("${web.browser.type}")
	public String browserType;
	@Value("${web.platform.type}")
	public String platformType;
	@Value("${test.execution.env}")
	public String execEnvironment;
	@Value("${web.hub.url}")
	public String hubUrl;
	@Value("${system.under.test}")
	public String systemUnderTest;
	@Value("${suite.execution.type}")
	public String suiteType;
	protected static Logger log = LoggerFactory.getLogger(GenericComponents.class);
	private WebDriver driver;
	DesiredCapabilities caps = new DesiredCapabilities();

	@Bean
	static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public LandingPage landingPage() {
		return new LandingPage();
	}

	@Bean
	public ProfilePage profilePage() {
		return new ProfilePage();
	}

	@Bean
	public WalletPage walletPage() {
		return new WalletPage();
	}
    
	@Bean
	public RedemptionPage redemptionPage(){
		return new RedemptionPage();
	}
	
	@Bean
	public BankAccountPage bankAccountPage(){
		return new BankAccountPage();
	}
	
	@Bean
	public GetPersonAPI getPersonAPI(){
		return new GetPersonAPI();
	}
	
    @Bean
    public HttpClient fisClient() throws Exception, UnrecoverableKeyException{
        return CitrusEndpoints.http()
                            .client()                      
                            .requestUrl(testProp.getProperty("api.fis.url"))
                            .requestFactory(sslRequestFactory())
                            .build();
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory sslRequestFactory() throws Exception, UnrecoverableKeyException {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public org.apache.http.client.HttpClient httpClient() throws Exception, UnrecoverableKeyException  {
        try {
            SSLContext sslcontext = SSLContexts.custom()            
            		.loadKeyMaterial(new ClassPathResource("keys/fiscert.jks").getFile(),             		
            		CryptionAlgorithm.decryptString("5jaUo+8efY0K0hucTQhWwdd3yVbz2Zb+IBJmVKv0rHk=").toCharArray(), CryptionAlgorithm.decryptString("K7nEGqhg2hEUEKoUzufN+T2wtWwf+Znx6MfsZCPc37E=").toCharArray())
            		.build();
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                    sslcontext, NoopHostnameVerifier.INSTANCE);
            return HttpClients.custom()
                    .setSSLSocketFactory(sslSocketFactory)
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new BeanCreationException("Failed to create FIS client for ssl connection", e);
        }
    }
	
	@Bean
	public SoapMessageFactory soap12MessageFactory() {
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.setSoapVersion(SoapVersion.SOAP_12);
		return messageFactory;
	}

	@Bean
	public WebServiceClient getPromocodeClient() {
		return CitrusEndpoints.soap().client().defaultUri(testProp.getProperty("soap.getPromocode.requestUri"))
				.messageFactory(soap12MessageFactory()).messageConverter(wsAddressingMessageConverter())
				.keepSoapEnvelope(false).build();
	}

	@Bean
	public WebServiceMessageConverter wsAddressingMessageConverter() {
		WsAddressingHeaders addressingHeaders = new WsAddressingHeaders();
		addressingHeaders.setVersion(WsAddressingVersion.VERSION10);
		addressingHeaders.setTo(URI.create(testProp.getProperty("soap.getPromocode.requestUri")));
		addressingHeaders.setAction(URI.create(testProp.getProperty("soap.getPromocode.headerAction")));
		return new WsAddressingMessageConverter(addressingHeaders);
	}

	@Bean
	public SeleniumBrowser browser() throws MalformedURLException {
		SeleniumBrowserBuilder browser = null;
		caps.setCapability("browserstack.local", "true");
		caps.setCapability("browserstack.localIdentifier", "Test123");
		if (browserType.equalsIgnoreCase("CHROME")) {
			if (platformType.equalsIgnoreCase("ANDROID")) {
				caps.setCapability("platformVersion", "4.4");
				caps.setCapability("platformName", "Android");
				driver = new RemoteWebDriver(new URL(hubUrl), DesiredCapabilities.android().merge(caps));
				browser = CitrusEndpoints.selenium().browser().type(BrowserType.CHROME).webDriver(driver);
			} else if (platformType.equalsIgnoreCase("macOS")) {
				caps.setCapability("browser", "Chrome");
				caps.setCapability("browser_version", "70.0");
				caps.setCapability("os", "OS X");
				caps.setCapability("os_version", "High Sierra");
				caps.setCapability("resolution", "1024x768");
				driver = new RemoteWebDriver(new URL(hubUrl), caps);
				browser = CitrusEndpoints.selenium().browser().type(BrowserType.CHROME).webDriver(driver);
			} else {
				browser = CitrusEndpoints.selenium().browser().type(BrowserType.CHROME);
			}
		} else if (browserType.equalsIgnoreCase("SAFARI")) {
			if (platformType.equalsIgnoreCase("OSX")) {
				caps.setCapability("browser", "Safari");
				caps.setCapability("browser_version", "11.1");
				caps.setCapability("os", "OS X");
				caps.setCapability("os_version", "High Sierra");
				caps.setCapability("resolution", "1024x768");
				driver = new RemoteWebDriver(new URL(hubUrl), caps);
				browser = CitrusEndpoints.selenium().browser().type(BrowserType.SAFARI).webDriver(driver);
			} else if (platformType.equalsIgnoreCase("IOS")) {
				caps.setCapability("platformVersion", "9.1");
				caps.setCapability("platformName", "iOS");
				driver = new RemoteWebDriver(new URL(hubUrl), DesiredCapabilities.iphone().merge(caps));
				browser = CitrusEndpoints.selenium().browser().type(BrowserType.SAFARI).webDriver(driver);
			} else {
				browser = CitrusEndpoints.selenium().browser().type(BrowserType.SAFARI);
			}
		} else if (browserType.equalsIgnoreCase("FIREFOX")) {
			browser = CitrusEndpoints.selenium().browser().type(BrowserType.FIREFOX);
		} else if (browserType.equalsIgnoreCase("EDGE")) {
			browser = CitrusEndpoints.selenium().browser().type(BrowserType.EDGE);
		} else if (browserType.equalsIgnoreCase("IE")) {
			browser = CitrusEndpoints.selenium().browser().type(BrowserType.IE);
		} else if (browserType.equalsIgnoreCase("OPERA")) {
			driver = new OperaDriver();
			browser = CitrusEndpoints.selenium().browser().type("custom").webDriver(driver);
		}
		return browser.build();
	}

	@Bean
	@DependsOn("browser")
	public SequenceBeforeSuite beforeSuite(SeleniumBrowser browser) {
		return new TestRunnerBeforeSuiteSupport() {
			@Override
			public void beforeSuite(TestRunner runner) {
				GlobalVariables.htmlReporter = new ExtentHtmlReporter("J:/QAAutomation/SmokeSuiteExecutionReports/" 
						+ systemUnderTest + "/" + execEnvironment + "/"
						+ suiteType 
						+ " Execution Results" + "_" + System.currentTimeMillis() + ".html");
				GlobalVariables.reportScreenshots = "J:\\QAAutomation/SmokeSuiteExecutionReports\\"
						+ testProp.getProperty("system.under.test") + "\\" + testProp.getProperty("test.execution.env")
					    + "\\Screenshots\\";
				GlobalVariables.reports = new ExtentReports();
				GlobalVariables.reports.attachReporter(GlobalVariables.htmlReporter);
				GlobalVariables.htmlReporter.loadXMLConfig(System.getProperty("user.dir") + "\\extent-config.xml");
				GlobalVariables.reports.setSystemInfo("Execution Environment", execEnvironment);
				GlobalVariables.reports.setSystemInfo("Suite Type", suiteType);
				GlobalVariables.reports.setSystemInfo("Application Under Test", systemUnderTest);
				GlobalVariables.reports.setSystemInfo("User Name", System.getProperty("user.name"));
				GlobalVariables.testInfo = GlobalVariables.reports
						.createTest(systemUnderTest + "-" + execEnvironment + "-" + suiteType + "-" + "Execution");
				GlobalVariables.testInfo.assignAuthor(" Authors :: Sajana Krishnan, Gulzhas Mailybayeva");
				TestDataAdapter.getTestDataLocation(execEnvironment, systemUnderTest, suiteType);
				System.out.println(GlobalVariables.testDataLocation);
				TestDataAdapter.connectDataDriver();
				GlobalVariables.execEnvironment = execEnvironment.toLowerCase();
			}
		};
	}

	@Bean
	@DependsOn("browser")
	public SequenceAfterSuite afterSuite(SeleniumBrowser browser) {
		return new TestRunnerAfterSuiteSupport() {
			@Override
			public void afterSuite(TestRunner runner) {
		    //runner.selenium(action -> action.browser(browser).stop());
			}
		};
	}

	@Bean
	@DependsOn("browser")
	public SequenceAfterTest afterTest(SeleniumBrowser browser) {
		return new TestRunnerAfterTestSupport() {
			@Override
			public void afterTest(TestRunner runner) {
				GlobalVariables.reports.flush();
			}
		};
	}

	public static void waitFor(int sec) throws InterruptedException {
		Thread.sleep(sec * 1000);
	}

	public static void takeScreenShot(SeleniumBrowser browser, String testModule) throws IOException {
		Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1500))
				.takeScreenshot(browser.getWebDriver());
		String dest = GlobalVariables.reportScreenshots + GlobalVariables.execTestCase + "_" + testModule + "_"
				+ System.currentTimeMillis() + ".png";
		BufferedImage image = screenshot.getImage();
		ImageIO.write(image, "PNG", new File(dest));
		MediaEntityModelProvider mediaModel = MediaEntityBuilder.createScreenCaptureFromPath(dest).build();
		GlobalVariables.testInfo.info(testModule + " Snapshot below:", mediaModel);
	}

	public static void jsExecutor(SeleniumBrowser browser, WebElement element, String jsType) {
		JavascriptExecutor js = (JavascriptExecutor) browser.getWebDriver();
		if(jsType == "visible"){
		js.executeScript("arguments[0].click();", element);
		} else if (jsType == "datePickerInput"){
			js.executeScript("arguments[0].type = arguments[1]", element, "text"); 
		}
	}
	
	public static void selectDropDown(WebElement element, String text, String bySelectType) {
		Select select = new Select(element);
		if(bySelectType == "Text"){
		select.selectByVisibleText(text); }
		else if (bySelectType == "Value"){
			select.selectByValue(text); }
	}	
}