package com.greenfinal.dao;

import com.greenfinal.dto.CharacterDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Mapper
public interface CharacterDao {
    @Select("select * from game_character")
    List<CharacterDto> getAllChar() throws DataAccessException;
    @Update("update userinfo set character_no=#{character_no} where user_id=#{user_id}")
    void insertChar(CharacterDto charDto) throws DataAccessException;
    @Select("select * from game_character where character_no = #{char_no}")
    List<CharacterDto> getSelectChar(@Param("char_no")int char_no) throws DataAccessException;
    @Select("select character_no from userinfo where user_id = #{user_id}")
    int getUserChar(@Param("user_id")String user_id) throws DataAccessException;
}
