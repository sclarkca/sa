package com.mr.sa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mr.sa.service.common.ThreadSaveService;

@Configuration
public class ThreadJpaConfiguration {

	@Bean
	public ThreadSaveService threadSaveServiceInitiator() {
		return new ThreadSaveService();
	}

}
