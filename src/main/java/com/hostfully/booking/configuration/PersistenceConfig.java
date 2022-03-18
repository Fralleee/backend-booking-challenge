package com.hostfully.booking.configuration;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.hostfully.spring.data.persistence.repository")
public class PersistenceConfig {}
