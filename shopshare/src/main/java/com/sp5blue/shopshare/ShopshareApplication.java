package com.sp5blue.shopshare;

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

  private final Environment environment;

  @Autowired
  public ShopshareApplication(Environment environment) {
    this.environment = environment;
  }

  @Override
  public void run(String... args) {}
}
