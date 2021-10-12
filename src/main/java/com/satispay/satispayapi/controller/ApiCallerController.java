package com.satispay.satispayapi.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.satispay.satispayapi.exception.SatispayException;
import com.satispay.satispayapi.models.Response;
import com.satispay.satispayapi.service.ApiService;

@RestController
@RequestMapping("/satispay")
public class ApiCallerController {
	
	@Autowired
	ApiService service;
	
	@Value("${api.endpoint}")
	private String endpoint;
	
	
	@GetMapping("/get")
	public Response get() {
		String result = "";
		try {
			result = service.doGet();
		} catch (SatispayException e) {
			return new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return new Response(HttpStatus.OK, result);
	}
	
	@PostMapping("/post")
	public Response post(@RequestBody String jsonBody) {
		String result = "";
		try {
			new JSONObject(jsonBody);
			result = service.doPost(jsonBody);
		} catch (SatispayException e) {
			return new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (JSONException e) {
			return new Response(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return new Response(HttpStatus.OK, result);
	}
	
	@PutMapping("/put")
	public Response put(@RequestBody String jsonBody, @RequestParam String id) {
		String result = "";
		try {
			new JSONObject(jsonBody);
			result = service.doPut(jsonBody, id);
		} catch (SatispayException e) {
			return new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (JSONException e) {
			return new Response(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return new Response(HttpStatus.OK, result);
	}
	
	@DeleteMapping("/delete")
	public Response delete(@RequestParam String id) {
		String result = "";
		try {
			result = service.doDelete(id);
		} catch (SatispayException e) {
			return new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (JSONException e) {
			return new Response(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return new Response(HttpStatus.OK, result);
	}
	

}
