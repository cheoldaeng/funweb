package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.domain.Criteria;
import com.example.domain.ReplyVo;

public interface ReplyMapper {
	
	@Insert("INSERT INTO reply (bno, reply, replyer) "
			+ "VALUES (#{bno}, #{reply}, #{replyer}) ")
	int insert(ReplyVo replyVo);
	
	@Select("SELECT * FROM reply WHERE rno = #{rno}")
	ReplyVo read(int rno);
	
	
	@Delete("DELETE FROM reply WHERE rno = #{rno}")
	int delete(int rno);
	
	@Update("UPDATE reply "
			+ "SET reply = #{reply}, update_date = CURRENT_TIMESTAMP "
			+ "WHERE rno = #{rno}")
	void update(ReplyVo replyVo);
	
	@Select("SELECT COUNT(rno) FROM reply WHERE bno = #{bno}")
	int getCountByBno(int bno);
	
	@Select("SELECT * "
			+ "FROM reply "
			+ "WHERE bno = #{bno} "
			+ "ORDER BY rno DESC ")
	List<ReplyVo> getList(int bno);
//	DESC : 최신부터 (내림차순) , ASC : 오래된것부터 (오름차순)
	@Select("SELECT * "
			+ "FROM reply "
			+ "WHERE bno = #{bno} "
			+ "ORDER BY rno DESC "
			+ "LIMIT #{cri.startRow}, #{cri.amount} ")
	List<ReplyVo> getListWithPaging(@Param("bno") int bno,@Param("cri") Criteria cri); //mybatis에서 사용하기 위한 Param설정
	
	
}
