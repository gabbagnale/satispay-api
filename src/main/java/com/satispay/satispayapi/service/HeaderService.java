package com.satispay.satispayapi.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.satispay.satispayapi.models.SignatureElement;

@Component
public class HeaderService {

	/**
	 * Builds the signature string to encrypt from a given list of elements 
	 * **/
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

	/**
	 * Builds the authentication header string to encrypt from a given list of elements 
	 * **/
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


	/**
	 * Builds the headers header string from a given list of elements 
	 * **/
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
