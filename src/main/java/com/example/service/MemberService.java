package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.MemberVo;
import com.example.mapper.MemberMapper;

@Service
@Transactional //Service 애노테이션을 사용하는 궁극적인 목적, 모든 메소드 각각이 자동으로 한개의 트랜잭션 단위로 수행됨
public class MemberService { 
	//많이 교체해야한다면 class보단 interface로 만드는게 좋다.
	
	@Autowired
	private MemberMapper memberMapper;
	
	public void insert(MemberVo vo) {
		memberMapper.insert(vo);
	}
	
	public boolean isIdDuplicated(String id) {
		boolean isIdDup = false;
		
		int count = memberMapper.countMemberById(id);
		if (count == 1) {
			isIdDup = true; // 아이디중복
		} else { // count == 0
			isIdDup = false; // 아이디중복아님
		}
		
		return isIdDup;
	}
	
	public int userCheck(String id, String passwd) {
		int check = -1; // -1: 아이디 없음, 0: 비밀번호 틀림, 1: 아이디 비밀번호 일치
		
		String dbPasswd = memberMapper.getPasswdById(id);
		System.out.println("dbPasswd = " + dbPasswd);
		
		if (dbPasswd != null) {
			if (dbPasswd.equals(passwd)) {
				check = 1;
			} else {
				check = 0;
			}
		} else { // dbPasswd == null
			check = -1;
		}
		
		return check;
	}
}
