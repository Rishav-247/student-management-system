package com.example.sms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA and Database Transaction Configuration.
 * Enables JPA Repositories, Auditing (createdAt, updatedAt), and declarative Transaction Management.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.example.sms.repository")
public class JpaConfig {
}
