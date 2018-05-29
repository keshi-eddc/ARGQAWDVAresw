package com.edmi;

import com.edmi.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ManageApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(ManageApplication.class, args);
	}
}
