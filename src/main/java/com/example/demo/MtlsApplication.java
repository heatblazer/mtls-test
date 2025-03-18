package com.example.demo;
import com.example.demo.BadSSLClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MtlsApplication {


	public static void main(String[] args) {
		SpringApplication.run(MtlsApplication.class, args);
	}

}
