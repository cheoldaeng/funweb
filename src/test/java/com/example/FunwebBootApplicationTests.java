package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.service.SampleService;

import lombok.extern.java.Log;

@SpringBootTest
@Log
class FunwebBootApplicationTests {

	@Autowired
	private Restaurant restaurant;
	
	@Autowired
	private SampleService sampleService;
	
	@Test
	void testSampleServiceClass() {
		log.info(sampleService.getClass().getName()); //getClass : 클래스 타입 객체 리턴, getName : 클래스 이름
		System.out.println(sampleService);
	}
	
	@Test
	void testAdd() {
		int result = sampleService.doAdd("123", "456");
		log.info("결과: "+result);
		assertEquals(579,result);
	}
	
	@Test
	void testAddError() {
		
		//예외객체 타입검사
		
		assertThrows(NumberFormatException.class, new Executable() {
			
			@Override
			public void execute() throws Throwable {
				
				sampleService.doAdd("123", "ABC");
			}
		});
	}
	
	@Disabled
	@Test
	void contextLoads() {
	}
	
	@Disabled
	@Test
	void testRestaurant() {
		log.info("@Test - testRestaurant() 호출됨");
		
		// 스프링 프레임워크를 사용하지 않고 
		// Restaurant을 사용하는 경우
		// Restaurant이 Check를 직접 생성해서 사용함.
//		Restaurant restaurant = new Restaurant();
		
		//Chef chef = new ChineseChef();
//		Chef chef = new Japanesechef();
//		// 레스토랑 클래스는 건들이지 않은체 chef만 바꿀 수 있도록
//		// 어떤 클래스가 변경될때 다른 클래스는 변경되지 않도록 하는 것
//		// 객체간의 의존관계 주입(Dependency Injection, DI)
//		restaurant.setChef(chef); 
//		
//		restaurant.makeDish();
		
		restaurant.makeDish();
	}
	
	

}
