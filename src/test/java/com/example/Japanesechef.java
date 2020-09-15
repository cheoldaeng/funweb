package com.example;

import org.springframework.stereotype.Component;

@Component
public class Japanesechef implements Chef {

	@Override
	public void doCook() {
		System.out.println("일식 요리합니다.");

	}

}
