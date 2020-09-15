package com.example.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.domain.MemberVo;
import com.example.service.MemberService;

import lombok.extern.java.Log;

@Log
@Controller
//제일 앞에서 서버로 들어오는 모든 요청을 처리하는 
//*프론트 컨트롤러를 Spring에서 정의하였고, 이를 Dispatcher-Servlet이라고 합니다
//공통처리 작업을 Dispatcher 서블릿이 처리한 후, 적절한 세부 컨트롤러로 작업을 위임해줍니다. 
@RequestMapping("/member/*") //mapping의 공통부분을 먼저 체크하게 할 수 있다.
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	//@RequestMapping(value="/join", method = RequestMethod.GET) : 옛날버전에선 @RequestMapping을 이렇게 지원했다.
	@GetMapping("/join") //member/join
	public void join() {
		log.info("join()호출됨");
//		return "/member/join"; //메소드 리턴타입이 String일때, void는 retrun하지 않는다.
	}
	
	@PostMapping("/join")
	public String join(MemberVo memberVo) { //모델1의 action의 usebean, setproperty의 기능을 spring이 제공한다
								//useBean처럼 vo객체를 만듬, setproperty까지 해서 완성된(다 채워진) Vo객체를 제공한다.
		log.info(memberVo.toString());
		
		// regDate 가입날짜 생성해서 넣기
		memberVo.setRegDate(LocalDateTime.now());
		
		// 회원가입처리
		memberService.insert(memberVo);
		
		return "redirect:/member/login";
	}
	
	@RequestMapping("/joinIdDupCheck") //get, post모든 방식의 요청을 받으려면 RequestMapping
	public String joinIdDupCheck(String id, Model model) { //model 객체는 jsp까지의 배달용이라고 봐야함
		log.info("id : " + id);
		
		// 아이디 중복여부 값 구하기
		boolean isIdDup = memberService.isIdDuplicated(id);
		
		//model 타입 객체에 뷰(JSP)에서 사용할 데이터를 저장하기(싣기)
		model.addAttribute("isIdDup", isIdDup);
		model.addAttribute("id", id);
		//프론트컨트롤러가 model에 추가된 내용을 request로 보내준다.(HttpServletRequest)
		
		return "member/join_IDCheck";
	}
	
	@GetMapping("/ajaxJoinIdDupCheck")
	//@ResponseBody 애노테이션을 통해서 리턴값을 JSON형식으로 응답해준다.
	public @ResponseBody Map<String, Object> ajaxJoinIdDupCheck(String id) {
		log.info("id : " + id);
		
		if (id == null || id.length() == 0) {
			return null;
		}
		// 아이디 중복여부 값 구하기
		boolean isIdDup = memberService.isIdDuplicated(id);
		
		Map<String, Object> map = new HashMap<>();
		map.put("isIdDup", isIdDup);
		map.put("name", "홍길동");
		map.put("age", 22);
		
//		Gson gson = new Gson();
//		String strJson = gson.toJson(map); //@ResponseBody가 이 역할을 해준다.
		//프론트컨트롤러가 gson을 이용해서 json으로 변환해줌
		
		return map;
	}
	@GetMapping("/login")
	public void login() {
		log.info("login() 호출됨");
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(String id, String passwd, 
			@RequestParam(defaultValue = "false") boolean keepLogin, HttpSession session,
			HttpServletResponse response) { //프론트 컨트롤러를 염두해두고 코딩해야함
											//많은 데이터들을 요청만 해도 알아서 넣어준다.
											//최대한 사용해선 안되지만 HttpServletRquest도 넣을 수 있다.
		
		// -1: 아이디 없음, 0: 비밀번호 틀림, 1: 아이디 비밀번호 일치
		int check = memberService.userCheck(id, passwd);
		
		// 로그인 실패시
		if (check != 1) {
			String message = "";
			if (check == 0) {
				message = "비밀번호 틀림";
			} else if (check == -1) {
				message = "아이디 없음";
			}
			
			HttpHeaders headers = new HttpHeaders();
			//HttpHeaders : spring전용
			headers.add("Content-Type", "text/html; charset=UTF-8");
			
			StringBuilder sb = new StringBuilder();
			sb.append("<script>");
			sb.append("alert('" + message + "');");
			sb.append("history.back();");
			sb.append("</script>");
			
			return new ResponseEntity<String>(sb.toString(), headers, HttpStatus.OK);
			//OK : 정상적인 응답
		}
		
		//로그인 성공시
		//세션에 아이디 저장(로그인 인증)
		session.setAttribute("id", id);
		
		// 로그인 상태유지 원하면 쿠키 생성 후 응답주기
		if (keepLogin) { // keepLogin == true
			Cookie idCookie = new Cookie("id", id);
			idCookie.setMaxAge(60*10); // (초단위 설정) 10분
			idCookie.setPath("/"); // 쿠키경로 설정
			response.addCookie(idCookie); // 응답객체에 추가
		}
		
		//return "redirect:/";
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/"); // redirect 경로 위치 지정
		//리다이렉트일 경우 HttpStatus.FOUND 지정해야함
		return new ResponseEntity<>(headers,HttpStatus.FOUND); //FOUND : 새로 요청해야할 경로를 찾았다.
		//프론트 컨트롤러에게 줘야하는 정보(OK, FOUND)
		
	} // login()
	
	@GetMapping("/logout")
	public ResponseEntity<String> logout(HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response) {
		//세션값 초기화
		session.invalidate();
		
		// 로그인 상태유지용 쿠키가 존재하면 삭제
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("id")) {
					cookie.setMaxAge(0); // 유효기간 0 설정
					cookie.setPath("/"); // 경로 설정
					response.addCookie(cookie);
				}
			}
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=UTF-8");
		
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('로그아웃됨');");
		sb.append("location.href='/';");
		sb.append("</script>");
		
		return new ResponseEntity<String>(sb.toString(), headers, HttpStatus.OK);
		
		
	} // logout()
	
}
