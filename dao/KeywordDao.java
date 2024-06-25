package com.greenfinal.dao;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.springframework.dao.DataAccessException;

import com.greenfinal.dto.*;


@Mapper
public interface KeywordDao {

	@Select("select * from keyword order by keyword_no Desc")
	public List<KeywordDto> selectKeyword() throws DataAccessException;

	@Select("select * from keyword where keygory_no='1' order by keyword_no Desc")
	public List<KeywordDto> selectAllKeyword1() throws DataAccessException;

	@Select("select * from keyword where keygory_no='2' order by keyword_no Desc")
	public List<KeywordDto> selectAllKeyword2() throws DataAccessException;

	// keyword
	@Insert("INSERT INTO userkeyword (user_no, keyword_no, keyword, user_keyword) " +
			"VALUES (#{user_no}, #{keyword_no}, #{keyword}, #{user_keyword})")
	void insertKeywords(
			@Param("user_no") int user_no,
			@Param("keyword_no") int keyword_no,
			@Param("keyword") String keyword,
			@Param("user_keyword") String user_keyword
	) throws DataAccessException;

	// admin
	// 삭제
	@Delete({
			"<script>",
			"DELETE FROM keyword WHERE keyword_no IN ",
			"<foreach item='keywordNo' collection='keywordNos' open='(' separator=',' close=')'>",
			"#{keywordNo}",
			"</foreach>",
			"</script>"
	})
	void deleteKeywords(@Param("keywordNos") List<Integer> keywordNos);

	// 검색
	@Select("SELECT * FROM keyword WHERE keyword LIKE CONCAT('%', #{keyword}, '%') OR category_no LIKE CONCAT('%', #{keyword}, '%') order by keyword_no Desc")
	public List<KeywordDto> selectSearchKeyword(@Param("keyword") String keyword) throws DataAccessException;

	@Select("SELECT * FROM keyword WHERE keygory_no like 1 and (keyword LIKE CONCAT('%', #{keyword}, '%') OR category_no LIKE CONCAT('%', #{keyword}, '%')) order by keyword_no Desc")
	public List<KeywordDto> selectSearchKeyword1(@Param("keyword") String keyword) throws DataAccessException;

	@Select("SELECT * FROM keyword WHERE keygory_no like 2 and (keyword LIKE CONCAT('%', #{keyword}, '%') OR category_no LIKE CONCAT('%', #{keyword}, '%')) order by keyword_no Desc")
	public List<KeywordDto> selectSearchKeyword2(@Param("keyword") String keyword) throws DataAccessException;

	// 수정 전 정보
	@Select("SELECT * FROM keyword WHERE keyword_no=#{keyword_no}")
	public KeywordDto selectKeywordByKeywordNo(int keyword_no) throws DataAccessException;

	// 수정
	@Update("UPDATE keyword "
			+ "SET keyword=#{keyword}, "
			+ "category_no=#{category_no}, "
			+ "keygory_no=#{keygory_no} "
			+ "WHERE keyword_no=#{keyword_no}")
	public void updateKeywords(KeywordDto keywordDto) throws DataAccessException;

	@Update("UPDATE keyword SET keyword=#{keyword} WHERE keyword_no=#{keywordNo}")
	public void updateKeyword(@Param("keyword") String keyword, @Param("keywordNo") int keywordNo) throws DataAccessException;

	@Update("UPDATE keyword SET category_no=#{category_no} WHERE keyword_no=#{keywordNo}")
	public void updateCategory_no(@Param("category_no") int category_no, @Param("keywordNo") int keywordNo) throws DataAccessException;

	@Update("UPDATE keyword SET keygory_no=#{keygory_no} WHERE keyword_no=#{keywordNo}")
	public void updateKeygory_no(@Param("keygory_no") int keygory_no, @Param("keywordNo") int keywordNo) throws DataAccessException;

	// 추가
	@Select("SELECT count(*) FROM keyword")
	public int selectKeywordLastNo() throws DataAccessException;

	@Insert("INSERT INTO keyword(keyword_no, keyword, category_no, keygory_no) VALUES(#{keyword_no}, #{keyword}, #{category_no}, #{keygory_no})")
	public void insertKeyword(KeywordDto keywordDto) throws DataAccessException;

	// --- keygory ---

	// admin keygory
	@Select("Select * from keygory")
	public List<KeygoryDto> selectKeygory() throws DataAccessException;

	// keygory 삭제
	@Delete("DELETE FROM keygory WHERE keygory_no = #{keygory_no}") // 게시판 번호로 삭제
	void deleteKeygory(@Param("keygory_no") int keygory_no) throws DataAccessException;

	// keygory 추가
	@Insert("INSERT INTO keygory (keygory_no, keygory_name) VALUES (#{keygory_no}, #{keygory_name})")
	void insertKeygory(KeygoryDto keygoryDto) throws DataAccessException;


	// 수정 전 정보
	@Select("SELECT * FROM keygory WHERE keygory_no=#{keygory_no}")
	public KeygoryDto selectKeygoryByKeygoryNo(int keygory_no) throws DataAccessException;

	// 수정
	@Update("UPDATE keygory "
			+ "SET keygory_name=#{keygory_name} "
			+ "WHERE keygory_no=#{keygory_no}")
	public void updateKeygory(KeygoryDto keygory_no) throws DataAccessException;

	@Update("UPDATE keygory SET keygory_name=#{keygory_name} WHERE keygory_no=#{keygoryNo}")
	public void updateKeygoryName(@Param("keygory_name") String board_name, @Param("keygoryNo") int keygoryNo) throws DataAccessException;


	// ----- 검색창 -----


	@Select("SELECT * FROM keyword WHERE keyword LIKE CONCAT('%', #{keyword}, '%')")
	public List<KeywordDto> SearchKeyword(@Param("keyword") String keyword) throws DataAccessException;

}