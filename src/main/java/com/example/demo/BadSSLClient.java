package com.example.demo;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/api")
public class BadSSLClient {

    private final RestTemplate restTemplate ;
	private static final Logger LOGGER = Logger.getLogger("RestClientConfig.java");

    @Autowired
    public BadSSLClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Scheduled(fixedRate=1000)
    @GetMapping("/get")
    public ResponseEntity<String> sendRequest() {
        String url = "https://client.badssl.com/";
        ResponseEntity<String> response = null;
        try {
        	response = restTemplate.getForEntity(url, String.class);
        	LOGGER.info(response.toString());
        } catch (Exception ex) {
        	LOGGER.info("ERRRRRROOOOOR");
        }
        return response;
    }
}