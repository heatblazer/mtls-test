package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import io.micrometer.common.lang.Nullable;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

import java.util.logging.Logger;

@Configuration
public class RestClientConfig {

	@Value("${mtls.keystore.path}")
	private  String pkcs12Path;
	
	@Value("${mtls.keystore.password}")
	private  String pkcs12Password;
	
	@Value("${mtls.keystore.jks}")
	private  String jksPath;
	
	@Value("${mtls.keystore.jks.password}")
	private  String jksPassword;
	
    
	private static final Logger LOGGER = Logger.getLogger("RestClientConfig.java");

    @Bean
    @Nullable
    public RestTemplate restTemplate()  {
        SSLContext sslContext = configureSSLContext();
        return new RestTemplate(createRequestFactory(sslContext));
    }

    private SSLContext configureSSLContext()  {
    	SSLContext sslContext = null;
    	try {
        	LOGGER.info("STARTING CONFIG!!!");
        	
            sslContext = SSLContext.getInstance("TLS");

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            char[] keyStorePassword = pkcs12Password.toCharArray();
            keyStore.load(new FileInputStream(pkcs12Path), keyStorePassword);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            try {
				keyManagerFactory.init(keyStore, keyStorePassword);
			} catch (UnrecoverableKeyException e) {
				// TODO Auto-generated catch block
				LOGGER.info("ERROR: " + e.toString());
			}

            KeyStore trustStore = KeyStore.getInstance("JKS");
            char[] trustStorePassword = jksPassword.toCharArray();
            trustStore.load(new FileInputStream(jksPath), trustStorePassword);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            try {
            sslContext.init(keyManagerFactory.getKeyManagers(), null/*trustManagerFactory.getTrustManagers()*/, null);
            } catch (Exception ex) {
            	return sslContext;
            }            
            LOGGER.info("END CONFIG");
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException e) {
        	return sslContext;
        } 
    }

    private ClientHttpRequestFactory createRequestFactory(SSLContext sslContext) {
        return new CustomRequestFactory(sslContext);
    }

    private static class CustomRequestFactory extends SimpleClientHttpRequestFactory {

        private final SSLContext sslContext;

        public CustomRequestFactory(SSLContext sslContext) {
            this.sslContext = sslContext;
        }

        @Override
        protected void prepareConnection(java.net.HttpURLConnection connection, String httpMethod) throws IOException {
            if (connection instanceof javax.net.ssl.HttpsURLConnection) {
                ((javax.net.ssl.HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
            }
            super.prepareConnection(connection, httpMethod);
        }
    }
}
