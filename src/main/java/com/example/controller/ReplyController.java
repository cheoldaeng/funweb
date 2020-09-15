package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Criteria;
import com.example.domain.ReplyPageDto;
import com.example.domain.ReplyVo;
import com.example.service.ReplyService;

import lombok.extern.java.Log;


/*

REST 컨트롤러의 HTTP method 매핑 방식
Create - POST 방식
Read - GET 방식
Update - PUT 또는 PATCH 방식
Delete - DELETE 방식

Get 방식 - SELECT 

*/

@RestController // 이 클래스의 모든 메서드가 JSON 또는 XML 응답으로 동작함 //@ResponseBody이런거 메서드 마다 귀찮게 안달아도 됨
@RequestMapping("/replies/*")
@Log
public class ReplyController {

	@Autowired
	private ReplyService replyService;
	
								//consumes : 추가적인 데이터를 json으로 넘긴다면 필요한 것
	@PostMapping(value = "/new", consumes = "application/json", produces = MediaType.TEXT_PLAIN_VALUE) //produces는 최종 return과 관련
	public ResponseEntity<String> create(@RequestBody ReplyVo replyVo) { //클라이언트에서 json으로 데이터를 송신하면
																		//서버에서는 @ReqeustBody를 사용해서 데이터를 받도록 작성하면
																		//Json 데이터를 송수신 할 수 있는 상태가 된다. 
		// 파라미터가 아닌 json을 쓰는 이유 : 데이터 단위가 클때(대규모 데이터) - Vo여러개를 하나의 배열 List에 묶어서 보낼 수 있다. / 바로 파싱해서 사용
		log.info("replyVo : " + replyVo);	
		
		int count = replyService.register(replyVo);
		log.info("reply INSERT count : " + count);
		
		ResponseEntity<String> entity = null;
		if (count > 0) {
			entity = new ResponseEntity<String>("success", HttpStatus.OK);
		} else {
			entity = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return entity;
	}	//create()
	
	//요청주소 뒤에 .json을 붙이면 JSON응답, .xml을 붙이면 XML응답 제출
	@GetMapping(value = "/pages/{bno}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })//여기 bno가
	public ResponseEntity<List<ReplyVo>> getList(@PathVariable("bno") int bno) { //@PathVariable bno를 찾는다
		
		List<ReplyVo> list = replyService.getList(bno);
		
		return new ResponseEntity<List<ReplyVo>>(list, HttpStatus.OK);
	}//getList()
	
	@GetMapping(value = "/pages/{bno}/{pageNum}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<ReplyPageDto> getListWithPage(@PathVariable("bno") int bno,
			@PathVariable("pageNum") int pageNum) {
		
		Criteria cri = new Criteria(pageNum, 10);
		
		ReplyPageDto replyPageDto = replyService.getListWithPaging(bno, cri);
		
		return new ResponseEntity<ReplyPageDto>(replyPageDto, HttpStatus.OK);
	}//getListWithPage()
	
	@GetMapping(value = "/{rno}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<ReplyVo> get(@PathVariable("rno") int rno) {
		ReplyVo replyVo = replyService.get(rno);
		return new ResponseEntity<ReplyVo>(replyVo, HttpStatus.OK);
	} // get()
	//다양한 클라이언트와 서버 환경에서도 JSON으로만 통신한다. view 쪽에서 데이터를 ajax로 요청해서 가져옴
	//ajax로 jsp를 만든다면 jsp의 역할이 축소된다.
	
	@DeleteMapping(value = "/{rno}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> remove(@PathVariable("rno") int rno) {
		int count = replyService.remove(rno);
		
		return (count > 0) 
				? new ResponseEntity<String>("success", HttpStatus.OK) 
				: new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
				
	} // remove()
	@PutMapping(
			value = "/{rno}",
			consumes = "application/json",
			produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> modify(@RequestBody ReplyVo replyVo, 
			@PathVariable("rno") int rno) {
		
		replyVo.setRno(rno);
		
		replyService.modify(replyVo);
		
		return new ResponseEntity<String>("success", HttpStatus.OK);
	} // modify()
	
//	@RequestMapping(method = { RequestMethod.PUT, RequestMethod.PATCH },
//			value = "/{rno}",
//			consumes = "application/json")
//	public ResponseEntity<String> modify(@RequestBody ReplyVo replyVo, 
//			@PathVariable("rno") int rno) {
//		
//		replyVo.setRno(rno);
//		
//		replyService.modify(replyVo);
//		
//		return new ResponseEntity<String>("success", HttpStatus.OK);
//	} // modify()
}