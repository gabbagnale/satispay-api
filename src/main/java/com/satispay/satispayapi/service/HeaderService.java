package com.satispay.satispayapi.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Component;

import com.satispay.satispayapi.exception.SatispayException;
import com.satispay.satispayapi.models.SignatureElement;

@Component
public class HeaderService {

	public String buildSignatureString(List<SignatureElement> signatureElements) {
		StringBuffer writer = new StringBuffer();
		for (int i = 0; i < signatureElements.size(); i++) {
			writer.append(signatureElements.get(i).getKey());
			writer.append(": ");
			writer.append(signatureElements.get(i).getValue());
			if (i < signatureElements.size() - 1) {
				writer.append("\n");
			}
		}

		return writer.toString();
	}

	public String buildAuthHeader(List<SignatureElement> authElements) {
		StringBuffer writer = new StringBuffer();
		
		writer.append("Signature ");
		
		for (int i = 0; i < authElements.size(); i++) {
			writer.append(authElements.get(i).getKey());
			writer.append("=");
			writer.append(authElements.get(i).getValue());
			if (i < authElements.size() - 1) {
				writer.append(", ");
			}
		}
		return writer.toString();
	}

	public String digest(String bodyExample) throws SatispayException {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedhash = digest.digest(
					bodyExample.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(encodedhash);
		} catch (NoSuchAlgorithmException e) {
			throw new SatispayException("Wrong algorithm", e.getCause());
		}
	}

	public String buildHeadersString(List<SignatureElement> signatureElements) {
		StringBuffer writer = new StringBuffer();
		for (int i = 0; i < signatureElements.size(); i++) {
			writer.append(signatureElements.get(i).getKey());
			if (i < signatureElements.size() - 1) {
				writer.append(" ");
			}
		}
		return writer.toString();
	}

}
