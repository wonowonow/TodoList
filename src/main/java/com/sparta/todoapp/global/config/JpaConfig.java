package com.sparta.todoapp.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@RequiredArgsConstructor
@EnableJpaAuditing
@Configuration
public class JpaConfig {

    private final EntityManager em;

    @Bean
    JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }
}
