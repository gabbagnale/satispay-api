package com.satispay.satispayapi.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.satispay.satispayapi.exception.SatispayException;
import com.satispay.satispayapi.models.SignatureElement;

@Component
public class ApiService {
	
	@Value("${api.endpoint}")
	private String endpoint;
	
	@Autowired
	CryptService cryptService;
	
	@Autowired
	HeaderService headerService;
	
	@Autowired
	ApiConsumer consumer;
	
	@Value("${key.id}")
	String keyId;
	
	@Value("${algorithm}")
	String algorithm;
	
	private final String dateFormat = "EEE, dd MMM yyyy HH:mm:ss Z";
	
	public String doGet() throws SatispayException {
		
		URL url;
		try {
			url = new URL(endpoint);
		} catch (MalformedURLException e) {
			throw new SatispayException("Wrong endpoint", e.getCause());
		}
		String host = url.getHost();
		String path = url.getPath();
		List<SignatureElement> signatureElements = new ArrayList<>();
		signatureElements.add(new SignatureElement("(request-target)", "get " + path));
		signatureElements.add(new SignatureElement("host", host));
		Date date=new Date();
	    SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateFormat);
	    String dateString = dateFormat.format(date);
	    signatureElements.add(new SignatureElement("date", dateString));
		String signature = headerService.buildSignatureString(signatureElements);
		byte[] encryptedMessageBytes = cryptService.encrypt(signature);
		String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
		
		List<SignatureElement> authElements = new ArrayList<>();
		authElements.add(new SignatureElement("keyId", keyId));
		authElements.add(new SignatureElement("algorithm", algorithm));
		authElements.add(new SignatureElement("headers", headerService.buildHeadersString(signatureElements)));
		authElements.add(new SignatureElement("signature", encodedMessage));
		
		String auth = headerService.buildAuthHeader(authElements);
		return consumer.callGet(url, auth, dateString, host);		
		
		
	}

	public String doPost(String jsonBody) throws SatispayException {
		URL url;
		try {
			url = new URL(endpoint);
		} catch (MalformedURLException e) {
			throw new SatispayException("Wrong endpoint", e.getCause());
		}
		String host = url.getHost();
		String path = url.getPath();
		List<SignatureElement> signatureElements = new ArrayList<>();
		signatureElements.add(new SignatureElement("(request-target)", "post " + path));
		signatureElements.add(new SignatureElement("host", host));
		Date date=new Date();
	    SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateFormat);
	    String dateString = dateFormat.format(date);
	    signatureElements.add(new SignatureElement("date", dateString));
	    String digest = headerService.digest(jsonBody);
	    signatureElements.add(new SignatureElement("digest", "SHA-256="+digest));
	    String signature = headerService.buildSignatureString(signatureElements);
	    byte[] encryptedMessageBytes = cryptService.encrypt(signature);
		String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
		List<SignatureElement> authElements = new ArrayList<>();
		authElements.add(new SignatureElement("keyId", keyId));
		authElements.add(new SignatureElement("algorithm", algorithm));
		authElements.add(new SignatureElement("headers", headerService.buildHeadersString(signatureElements)));
		authElements.add(new SignatureElement("digest", "SHA-256="+digest));
		authElements.add(new SignatureElement("signature", encodedMessage));
		String auth = headerService.buildAuthHeader(authElements);
		return consumer.callWrite(url, auth, dateString, host, jsonBody, digest, "POST");
	}

	public String doPut(String jsonBody, String id) throws SatispayException {
		URL url;
		try {
			url = new URL(endpoint + "?id=" + id);
		} catch (MalformedURLException e) {
			throw new SatispayException("Wrong endpoint", e.getCause());
		}
		String host = url.getHost();
		String path = url.getPath();
		List<SignatureElement> signatureElements = new ArrayList<>();
		signatureElements.add(new SignatureElement("(request-target)", "put " + path + "?id=" + id));
		signatureElements.add(new SignatureElement("host", host));
		Date date=new Date();
	    SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateFormat);
	    String dateString = dateFormat.format(date);
	    signatureElements.add(new SignatureElement("date", dateString));
	    String digest = headerService.digest(jsonBody);
	    signatureElements.add(new SignatureElement("digest", "SHA-256="+digest));
	    String signature = headerService.buildSignatureString(signatureElements);
	    byte[] encryptedMessageBytes = cryptService.encrypt(signature);
		String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
		List<SignatureElement> authElements = new ArrayList<>();
		authElements.add(new SignatureElement("keyId", keyId));
		authElements.add(new SignatureElement("algorithm", algorithm));
		authElements.add(new SignatureElement("headers", headerService.buildHeadersString(signatureElements)));
		authElements.add(new SignatureElement("digest", "SHA-256="+digest));
		authElements.add(new SignatureElement("signature", encodedMessage));
		String auth = headerService.buildAuthHeader(authElements);
		return consumer.callWrite(url, auth, dateString, host, jsonBody, digest, "PUT");
		
	}

	public String doDelete(String id) throws SatispayException {
		URL url;
		try {
			url = new URL(endpoint + "?id=" + id);
		} catch (MalformedURLException e) {
			throw new SatispayException("Wrong endpoint", e.getCause());
		}
		String host = url.getHost();
		String path = url.getPath();
		List<SignatureElement> signatureElements = new ArrayList<>();
		signatureElements.add(new SignatureElement("(request-target)", "delete " + path + "?id=" + id));
		signatureElements.add(new SignatureElement("host", host));
		Date date=new Date();
	    SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateFormat);
	    String dateString = dateFormat.format(date);
	    signatureElements.add(new SignatureElement("date", dateString));
		String signature = headerService.buildSignatureString(signatureElements);
		byte[] encryptedMessageBytes = cryptService.encrypt(signature);
		String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
		
		List<SignatureElement> authElements = new ArrayList<>();
		authElements.add(new SignatureElement("keyId", keyId));
		authElements.add(new SignatureElement("algorithm", algorithm));
		authElements.add(new SignatureElement("headers", headerService.buildHeadersString(signatureElements)));
		authElements.add(new SignatureElement("signature", encodedMessage));
		
		String auth = headerService.buildAuthHeader(authElements);
		return consumer.callDelete(url, auth, dateString, host);	
	}

}
