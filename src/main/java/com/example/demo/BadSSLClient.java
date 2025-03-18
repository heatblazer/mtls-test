package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping(value="/")
public class BadSSLClient {

    private final RestTemplate restTemplate ;

    @Autowired
    public BadSSLClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        String url = "https://client.badssl.com/";
        ResponseEntity<String> response = null;
        try {
        	response = restTemplate.getForEntity(url, String.class);
        } catch (Exception ex) {
        	
        }
        
    }


    @GetMapping(value ="/get")
    public ResponseEntity<String> sendRequest() {
        String url = "https://client.badssl.com/";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response;
    }
}