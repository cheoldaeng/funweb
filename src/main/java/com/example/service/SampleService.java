package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.aop.LogAdvice;

@Service
public class SampleService {

	public Integer doAdd(String str1, String str2) throws NumberFormatException {
		
		return Integer.parseInt(str1) + Integer.parseInt(str2);
	}
}

//Proxy(대리자) 클래스가 실행중에 자동으로 생성되어 SampleService타입 객체로 대신 등록됨
/*
class SampleService$$EnhancerBySpringCGLIB$$3c5455ad{
	
	@Autowired
	private SampleService sampleService;
	@Autowired
	private LogAdvice logAdvice;
	
	public Integer doAdd(String str1, String str2) { //코드를 분리해서 만드는 패턴 : 프록시 패턴
		
		logAdvice.logBefore();
		
		int result = sampleService.doAdd(str1, str2);
		return result;
	}
}
*/