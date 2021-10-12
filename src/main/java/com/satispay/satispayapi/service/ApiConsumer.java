package com.satispay.satispayapi.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Component;

import com.satispay.satispayapi.exception.SatispayException;

@Component
public class ApiConsumer {

	final static Logger logger = Logger.getLogger(ApiConsumer.class.getName());

	public String callGet(URL endpoint, String auth, String date, String host) throws SatispayException {
		HttpsURLConnection conn = null;
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			conn = (HttpsURLConnection) endpoint.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Host", host);
			conn.setRequestProperty("Date", date);
			conn.setRequestProperty("Authorization", auth);
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}

		} catch (MalformedURLException e) {
			throw new SatispayException("Wrong endpoint", e.getCause());
		} catch (IOException e) {
			throw new SatispayException("Cannot connect to endpoint", e.getCause());
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				throw new SatispayException("Cannot close buffer", e.getCause());
			}
			conn.disconnect();
		}

		logger.info(sb.toString());
		return sb.toString();
	}

	public String callWrite(URL endpoint, String auth, String date, String host, String body, String digest, String method) throws SatispayException {
		HttpsURLConnection conn = null;
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			conn = (HttpsURLConnection) endpoint.openConnection();
			conn.setRequestMethod(method);
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Host", host);
			conn.setRequestProperty("Date", date);
			conn.setRequestProperty("Authorization", auth);
			conn.setRequestProperty("Digest", "SHA-256="+digest);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = body.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

			sb = new StringBuilder();
			for (int c; (c = br.read()) >= 0;)
				sb.append((char) c);

		} catch (MalformedURLException e) {
			throw new SatispayException("Wrong endpoint", e.getCause());
		} catch (IOException e) {
			throw new SatispayException("Cannot connect to endpoint", e.getCause());
		} finally {
			conn.disconnect();
		}

		logger.info(sb.toString());
		return sb.toString();

	}

	public String callDelete(URL endpoint, String auth, String date, String host) throws SatispayException {
		HttpsURLConnection conn = null;
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			conn = (HttpsURLConnection) endpoint.openConnection();
			conn.setRequestMethod("DELETE");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Host", host);
			conn.setRequestProperty("Date", date);
			conn.setRequestProperty("Authorization", auth);
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}

		} catch (MalformedURLException e) {
			throw new SatispayException("Wrong endpoint", e.getCause());
		} catch (IOException e) {
			throw new SatispayException("Cannot connect to endpoint", e.getCause());
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				throw new SatispayException("Cannot close buffer", e.getCause());
			}
			conn.disconnect();
		}

		logger.info(sb.toString());
		return sb.toString();
	}

}
