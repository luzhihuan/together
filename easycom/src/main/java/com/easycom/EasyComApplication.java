package com.easycom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.easycom"})
@MapperScan(basePackages = "com.easycom.Mapper")
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class EasyComApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyComApplication.class, args);
	}

}
