package com.example.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.service.SampleService;

import lombok.extern.java.Log;

@Component
@Log
public class MyTask {
	
	@Autowired
	private SampleService sampleService;
	
	@Scheduled(fixedRate = 1000) // 1000*60*60*24 -> 24시간 주기로 반복실행
	public void schedule1() {
		long now = System.currentTimeMillis() /1000;
		log.info("스케줄링 작업 1 : 1초마다 실행 - " + now);
	}
	
	@Scheduled(fixedRate = 2000)
	public void schedule2() {
		long now = System.currentTimeMillis() /1000;
		log.info("스케줄링 작업 2 : 2초마다 실행 - " + now);
	}
	
	@Scheduled(fixedRate = 3000) 
	public void schedule3() {
		long now = System.currentTimeMillis() /1000;
		log.info("스케줄링 작업 3 : 3초마다 실행 - " + now);
	}
	
	@Scheduled(cron = "0 * * * * *") //예) * 0,30  * * * ? *    [의미] 매시 정각 및 30분에 Job을 수행합니다. https://blog.naver.com/estern/110010101624
									/*각 필드는 숫자 또는 다음 표에서와 같은 각 특수 문자들을 이용한 값들을 가질 수 있습니다.
									특수 문자를 사용할 경우 “,”와 “-“는 동시에 사용할 수 있습니다. 예를 들어, “* 1-5,7,8 * * * ?” 은 매 1,2,3,4,5,7,8분 마다 Job을 수행합니다.
									 */
	public void schedule4() {
		long now = System.currentTimeMillis() /1000;
		log.info("스케줄링 작업 4 : 매분 0초마다 실행 - " + now);
	}
}
