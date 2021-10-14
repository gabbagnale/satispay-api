package com.satispay.satispayapi.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.satispay.satispayapi.exception.SatispayException;

@Component
public class CryptService {

	@Value("${api.publicKeyPath}")
	private String publicKeyPath;

	@Value("${api.privateKeyPath}")
	private String privateKeyPath;

	/**
	 * Encrypt string to byte[] with SHA256withRSA
	 * **/
	byte[] encrypt(String message) throws SatispayException {
		String key;
		Signature privateSignature;
		try {
			key = new String(Files.readAllBytes(new File(privateKeyPath).toPath()), Charset.defaultCharset());
			String privateKeyPEM = key.replace("-----BEGIN PRIVATE KEY-----", "").replaceAll(System.lineSeparator(), "")
					.replaceAll("\\R", "").replace("-----END PRIVATE KEY-----", "");
			byte[] keyBytes =  Base64.getDecoder().decode(privateKeyPEM);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			
			privateSignature = Signature.getInstance("SHA256withRSA");
			privateSignature.initSign(keyFactory.generatePrivate(keySpec));
	        privateSignature.update(message.getBytes("UTF-8"));
		} catch (IOException e) {
			throw new SatispayException("Cannot read key", e.getCause());
		} catch (InvalidKeyException e) {
			throw new SatispayException("Invalid key", e.getCause());
		} catch (InvalidKeySpecException e) {
			throw new SatispayException("Invalid spec", e.getCause());
		} catch (NoSuchAlgorithmException e) {
			throw new SatispayException("Wrong algorithm", e.getCause());
		} catch (SignatureException e) {
			throw new SatispayException("Wrong instance", e.getCause());
		}

		try {
			return  privateSignature.sign();
		} catch (SignatureException e) {
			throw new SatispayException("error in sign", e.getCause());
		}
	}
	
	/**
	 * Does the hash of the body
	 * **/
	public String digest(String body) throws SatispayException {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedhash = digest.digest(
					body.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(encodedhash);
		} catch (NoSuchAlgorithmException e) {
			throw new SatispayException("Wrong algorithm", e.getCause());
		}
	}

}
