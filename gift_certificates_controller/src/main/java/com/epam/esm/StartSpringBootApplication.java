package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
public class StartSpringBootApplication {

  // If put this bean in SecurityConfig class, circular dependency will occur
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public static void main(String[] args) {
    SpringApplication.run(StartSpringBootApplication.class, args);

    //    ConfigurableEnvironment environment = new StandardEnvironment();
    //    environment.setActiveProfiles("dev");
    //
    //    SpringApplication application = new SpringApplication(StartSpringBootApplication.class);
    //    application.setEnvironment(environment);
    //    application.setAdditionalProfiles("test");
    //    application.run(args);
  }
}
