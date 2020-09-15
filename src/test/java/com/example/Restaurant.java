package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/*
	spring web MVC 모듈에서
	@Component를 상속받는 (대표적인)애노테이션 종류
	- @Comtroller	컨트롤러 역할을 하는 클래스에 사용함
	- @Service	트랜잭션 처리를 수행하는 클래스에 사용함
	- @Repository (저장소) DAO클래스에 사용함
	C->S->R 의 의존관계를 띄게된다.
*/
@Component
public class Restaurant {

	@Autowired
	private Chef chef;
	//Component : 클래스를 다 탐색하여 필요한 모든 객체를 만들어줌
	//Autowired : 자동적으로 의존관계를 넣어줌
	//spring이 java문법을 명시하지 않아도 관리를 해준다.
	public void makeDish() {
		chef.doCook();
	}
	
}
