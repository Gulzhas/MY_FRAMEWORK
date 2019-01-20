package com.swift.fis.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.runner.TestRunner;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.http.client.HttpClient;
import com.swift.secure.utils.CryptionAlgorithm;

public class GetPersonAPI extends TestNGCitrusTestRunner {
	@Autowired(required = true)
	public HttpClient fisClient;
	@Autowired
	Environment testProp;

	public void getPersonAPI(TestContext testContext, TestRunner testRunner, String newToken) throws Exception{
		testContext.setVariable("clientUniqueId", newToken +"_00");
		testRunner.http(action -> {
			try {
				action
						.client(fisClient)
						.send()
						.post("/CO_GetPersonInfo.asp")
						.fork(true)			
						.queryParam("userid", CryptionAlgorithm.decryptString(testProp.getProperty("api.fis.userId")))			
						.queryParam("pwd", CryptionAlgorithm.decryptString(testProp.getProperty("api.fis.password")))	
						.queryParam("sourceid", CryptionAlgorithm.decryptString(testProp.getProperty("api.fis.sourceId")))				
						.queryParam("ClientUniqueID", "${clientUniqueId}" )
						.queryParam("ClientID", "262428")
						.queryParam("SkipCreateRec", "1")
						.queryParam("CardStatus_List", "1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9");
			} catch (Exception e) {			
				e.printStackTrace();
			}
		});

		testRunner.http(action -> action.client(fisClient).receive().response(HttpStatus.OK)
				.extractFromHeader("citrus_http_status_code", "status_code")
				.extractFromHeader("citrus_http_reason_phrase", "status_phrase")
				.validationCallback((message, context) -> {
					System.out.println(message.getPayload(String.class));
					System.out.println(context.getVariable("status_code"));
					System.out.println(context.getVariable("status_phrase"));
				}));
	}
}
