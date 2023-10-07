package com.sp5blue.shopshare;

import com.sp5blue.shopshare.repositories.ShopperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.sp5blue.shopshare.repositories")
public class ShopshareApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ShopshareApplication.class, args);
	}
	final ShopperRepository shopperRepository;

	private final Environment environment;

	@Autowired
	public ShopshareApplication(ShopperRepository shopperRepository, Environment environment) {
		this.shopperRepository = shopperRepository;
		this.environment = environment;
	}

	@Override
	public void run(String... args) {
//		System.out.println(environment.getProperty("DB_CONNECTION_STRING"));
	}
}
