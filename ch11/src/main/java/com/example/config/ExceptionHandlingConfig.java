package com.example.config;

import org.springframework.ai.tool.execution.DefaultToolExecutionExceptionProcessor;
import org.springframework.ai.tool.execution.ToolExecutionExceptionProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 교재 p366
@Configuration
public class ExceptionHandlingConfig {
	
	@Bean
	public ToolExecutionExceptionProcessor toolExecutionExceptionProcessor() {
		// true를 하면 예외 메시지를 LLM으로 전달하지 않고 예외가 프로그램으로 처리
		return new DefaultToolExecutionExceptionProcessor(true);
	}
}