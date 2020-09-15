package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.example.websocket.MyTextWebSocketHandler;

@Configuration
@EnableWebSocket //웹소캣 기능 활성화 하기
public class MyWebSocketConfig implements WebSocketConfigurer {

	@Autowired
	private MyTextWebSocketHandler webSocketHandler;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		//"/chatting" 는 소캣연결을 위한 주소로서 HTTP 연결이 아닌 ws(websocket) 연결이 되어야함
		registry.addHandler(webSocketHandler, "/chatting")
				.addInterceptors(new HttpSessionHandshakeInterceptor())
				//HttpSessionHandshakeInterceptor : httpsession(클라이언트와 서버가 http통신을 하며 저장해둔 세션(정보))에 
				//있는 attributes들을 WebSocketSession으로 복사해줌 Handshake를 Interceptor - 가로챔
				.setAllowedOrigins("*");
	}
	
	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

	    scheduler.setPoolSize(2);
	    scheduler.setThreadNamePrefix("scheduled-task-");
	    scheduler.setDaemon(true);

	    return scheduler;
	}
	//spring bean을 위해 준비 해야하는 코드들 : @Configuration-@bean / @Component

}
