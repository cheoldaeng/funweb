package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.domain.MailDto;
import com.example.mail.MyMailSender;

import lombok.extern.java.Log;

@Controller
@Log
public class HomeController {
	
	@Autowired
	private MyMailSender mailSender;
	
	@GetMapping("/") //@Post //하나의 메서드에 하나의 mapping정보를 넣을 수 있다. 그래서 모든 mapping이 가능하지만
							//관리가 어려울 수 있으니 주제별로 Controller class를 만들어주는게 보통이다.
	public String index() { 
		log.info("index()호출됨");
		return "index";
	}
	//spring이 기본적으로 싱글톤 객체를 관리해준다. 싱글톤 객체 예시 -> MemberDao
	
	@GetMapping("/company/welcome")
	public void welcome() {
		log.info("welcome() 호출됨");
		//return "company/welcome";
		
		// 리턴타입이 void면 url요청 주소경로를
		// jsp 경로로 사용함
	}
	
	@GetMapping("/company/history")
	public void history() {
		log.info("history() 호출됨");
	}
	
	@GetMapping("/mail")
	public String mail() {
		return "contact/mail";
	}
	
	@PostMapping("/mail")
	public String sendMail(MailDto mailDto) {
		log.info("mailDto : " + mailDto);
		
		mailSender.sendHtmlMail(mailDto);
		return "redirect:/mail";
	}

}
