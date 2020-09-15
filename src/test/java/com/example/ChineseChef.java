package com.example;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
//@Primary : Component가 두개 이상이되어 bean이 유니크 하지 않을때 primary를 사용함으로 기본을 지정할 수 있다.
@Component
public class ChineseChef implements Chef {

	@Override
	public void doCook() {
		System.out.println("중식 요리합니다.");
	}

}
