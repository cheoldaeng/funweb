package com.example.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.service.MemberService;

import lombok.extern.java.Log;

// 소켓 서버 역할 클래스
@Log
@Component
public class MyTextWebSocketHandler extends TextWebSocketHandler {

	@Autowired
	private MemberService memberService;
	
	private JSONParser jsonParser = new JSONParser();
	
	private Map<String, WebSocketSession> sessionMap = new HashMap<>(); // 웹소켓 세션을 담아둘 맵
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("웹소캣 클라이언트와 연결됨");
		
		// HttpSession에 있는 attributes 값 가져오기
		Map<String, Object> attrsMap = session.getAttributes();
		String loginId = (String) attrsMap.get("id");
		log.info("loginId : " + loginId);
		//=================================================
		
		// 웹소켓 세션이 연결되면 호출됨
		sessionMap.put(session.getId(), session);//사용자를 구분하는데 사용가능
		
		//세션아이디를 클라이언트로 보내기
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", "getId"); //세션아이디를 데이터로 가진다는 의미
		jsonObject.put("sessionId", session.getId());
		log.info("jsonObject : "+ jsonObject.toJSONString());
		
		session.sendMessage(new TextMessage(jsonObject.toJSONString()));
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		
		String jsonStr = message.getPayload();
		
		JSONObject jsonobj = (JSONObject)jsonParser.parse(jsonStr);
		
		Set<String> keySet = sessionMap.keySet();
		
		// 모든 채팅참여자에게 브로드캐스트하기
		for(String key : keySet) {
			WebSocketSession wss = sessionMap.get(key);
			wss.sendMessage(new TextMessage(jsonobj.toJSONString()));
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("웹소캣 클라이언트와 연결이 끊어짐");
		// 웹소켓 세션 연결이 종료되면 호출됨
		sessionMap.remove(session.getId());
		
		
		
	}
	
	

}
