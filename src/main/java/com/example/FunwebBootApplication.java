package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy // 프로젝트 내의 @Aspect 붙은 클래스들을 스캔해서 처리
//@Component 계열 애노테이션 이름: @Component, @Configuration, @Controller, @RestController, @Service, @Repository 등
@SpringBootApplication //@Component 계열 애노테이션이 붙은 클래스들을 스캔해서 스프링 빈으로 등록
//- @EnableAutoConfiguration : Spring Boot의 자동화 기능(Spring 설정)을 활성화시켜줍니다.
//- @ComponentScan : 패키지내 application 컴포넌트가 어디에 위치해있는지 검사합니다. (빈 검색)
//- @Configuration : 빈에 대해서 Context에 추가하거나 특정 클래스를 참조해올 수 있습니다.
//@EnableScheduling  // 프로젝트 내의 @Scheduled 붙은 클래스들을 스캔해서 처리
@MapperScan("com.example.mapper") // 해당 패키지에 있는 매퍼 인터페이스를 구현한 객체를 스프링 빈으로 등록
//mapper.xml파일들이 바라볼 기본 패키지 위치를 지정
public class FunwebBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(FunwebBootApplication.class, args);
	}

}
