package com.wanderlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Main entry point for WanderLink Spring Boot backend.
 */
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
        basePackages = "com.wanderlink",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "com\\.wanderlink\\.(accommodation|ai|connection|food|group|matching|trip|user)(\\..*)?|com\\.wanderlink\\.config\\.DataInitializer"
        )
)
@EntityScan(basePackages = "com.wanderlink.entity")
@EnableJpaRepositories(basePackages = "com.wanderlink.repository")
public class WanderLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(WanderLinkApplication.class, args);
    }
}
