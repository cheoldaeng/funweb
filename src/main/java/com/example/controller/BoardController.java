package com.example.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.BoardVo;
import com.example.domain.PageDto;
import com.example.service.BoardService;

import lombok.extern.java.Log;

@Log
@RequestMapping("/board/*")
@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping("/list")
	public String list(@RequestParam(defaultValue = "1") int pageNum,
			@RequestParam(defaultValue = "") String category,
			@RequestParam(defaultValue = "") String search,
			Model model) {
		log.info("list() 호출됨");
		//noticeAction 옮겨옴

		// 전체 글갯수
		int totalCount = boardService.getTotalCount(category, search);
		
		
		// ========================================
		// 한페이지에 해당하는 글목록 구하기 작업
		// ========================================
		
		// 한 페이지에 15개씩 가져오기
		int pageSize = 15;
		// 시작행 인덱스번호 구하기(수식)
		int startRow = (pageNum-1) * pageSize;
			
		// 원하는 페이지의 글을 가져오는 메소드
		List<BoardVo> list = null;
		if (totalCount > 0) {
			list = boardService.getBoards(startRow, pageSize, category, search);
		}
		
		
		
		// ========================================
		// 페이지블록 관련정보 구하기 작업
		// ========================================
		
		// 총 페이지 수 구하기
		// 글50개. 한화면에보여줄글 10개 -> 50/10 = 5페이지
		// 글55개. 한화면에보여줄글 10개 -> 55/10 = 5 + 1페이지(나머지 있으면) -> 6페이지
		//int pageCount = totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1);
		int pageCount = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			pageCount += 1;
		}
		
		// 화면에 보여줄 페이지번호의 갯수 설정
		int pageBlock = 10;
		
		// 페이지블록의 시작페이지 구하기!
		// 1~10   11~20   21~30
		
		//  1~10 ->  1
		// 11~20 -> 11
		// 21~30 -> 21
		// 31~40
		
		// 페이지 블록의 시작페이지
		int startPage = ((pageNum / pageBlock) - (pageNum % pageBlock == 0 ? 1 : 0)) * pageBlock + 1;
		// 페이지 블록의 끝페이지
		int endPage = startPage + pageBlock - 1;
		if (endPage > pageCount) {
			endPage = pageCount;
		}
		
		
		// 페이지블록 관련 정보를 PageDTO에 저장(Map 컬렉션 사용 가능)
		PageDto pageDto = new PageDto();
		pageDto.setTotalCount(totalCount);
		pageDto.setPageCount(pageCount);
		pageDto.setPageBlock(pageBlock);
		pageDto.setStartPage(startPage);
		pageDto.setEndPage(endPage);
		pageDto.setCategory(category);
		pageDto.setSearch(search);
		
//		Map<String, Object> pageMap = new HashMap<>();
//		pageMap.put("totalCount", totalCount);
//		pageMap.put("pageCount", pageCount);
//		pageMap.put("pageBlock", pageBlock);
//		pageMap.put("startPage", startPage);
//		pageMap.put("endPage", endPage);
//		pageMap.put("category", category);
//		pageMap.put("search", search);
		
		
		// 뷰(jsp)에서 사용할 데이터를 model 객체에 저장해놓으면
		// 스프링 프론트컨트롤러가 request 영역객체로 옮겨줌
		model.addAttribute("boardList", list);
		model.addAttribute("pageDto", pageDto);
		//request.setAttribute("pageMap", pageMap);
		model.addAttribute("pageNum", pageNum);
		
		return "center/notice";
	} // list()
	
	@GetMapping("/write")
	public String write() {
		return "center/writeForm";
	}
	
	@PostMapping("/write")
	public String write(BoardVo boardVo, HttpServletRequest request) {
		
		//ip주소 작성일자 값 저장
		boardVo.setIp(request.getRemoteAddr());
		boardVo.setRegDate(LocalDateTime.now());
		
		//주글 한개 등록
		boardService.insert(boardVo);
		
		return "redirect:/board/list";
	}
	
	@GetMapping("/content")
	public String content(int num, @ModelAttribute("pageNum") String pageNum, Model model) {
		
		// 조회수 1증가
		boardService.updateReadcount(num);
		// 글 한개 가져오기
		BoardVo vo = boardService.getBoardByNum(num);
		// 내용에서 엔터키 줄바꿈 \r\n -> <br> 바꾸기
		String content = "";
		if (vo.getContent() != null) {
			content = vo.getContent().replace("\r\n", "<br>");
			vo.setContent(content);
		}
		
		//jsp에서 사용할 데이터를 영역객체에 저장
		model.addAttribute("boardVo", vo);
//		model.addAttribute("pageNum", pageNum);
		
		//실행할 특정 jsp 경로정보 리턴
		return "center/content";
	}
	
	@GetMapping("/reply")
	public String reply(BoardVo boardVo, 
			@ModelAttribute("pageNum") String pageNum) {
		//BoardVo에 reRef, reLev, reSeq 파라미터 설정됨
		//스프링 프론트컨트롤러는 우리메소드 파라미터에 VO형식 클래스가 선언되어 있으면
		//VO 객체를 항상 model객체에 추가해주기 때문에 뷰(JSP)에서 사용가능하다. : @ModelAttribute() 생략가능,
		//@ModelAttribute()를 VO객체에 사용하는 경우는 이름을 직접 주고 싶은 경우 : ex)@ModelAttribute("b")
		
		return "center/replyWriteForm";
	}
	
	@PostMapping("reply")
	public String reply(BoardVo boardVo, @ModelAttribute("pageNum") String pageNum, HttpServletRequest request,
			RedirectAttributes rttr) {
		
		// ip주소  작성일자 값 저장
		boardVo.setIp(request.getRemoteAddr());
		boardVo.setRegDate(LocalDateTime.now());
		
		// 답글 insert하기
		boardService.replyInsert(boardVo);
		
		//return "redirect:/board/list?pageNum=" + pageNum;
		rttr.addAttribute("pageNum", pageNum); // ? 아래의 코드를 spring에게 대신 맡김
		return "redirect:/board/list";
	}
}
