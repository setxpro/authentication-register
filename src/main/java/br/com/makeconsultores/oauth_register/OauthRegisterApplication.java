package br.com.makeconsultores.oauth_register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class OauthRegisterApplication {
	public static void main(String[] args) {
		SpringApplication.run(OauthRegisterApplication.class, args);
	}
}
