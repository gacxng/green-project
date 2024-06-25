package com.greenfinal.dao;

import com.greenfinal.dto.CharacterDto;
import com.greenfinal.dto.GameDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Mapper
@Repository("gameDao")
public interface GameDao {
    // 사용자 아이디에 해당하는 사용자 정보를 DB에 조회하는 메서드
    @Select("select * from game where user_id = #{user_id}")
    public GameDto GamefindByUserId(String user_id);

    // 사용자 정보를 업데이트하는 메서드
    @Update("update game " +
            "set exp = #{exp}, g_level = #{g_level}, pushTime = #{pushTime}, watercnt = #{watercnt}, " +
            "lovecnt = #{lovecnt}, suncnt = #{suncnt}, poocnt = #{poocnt}, bugcnt = #{bugcnt}, " +
            "potcnt = #{potcnt}, musiccnt = #{musiccnt}, nutritioncnt = #{nutritioncnt} " +
            "where user_id = #{user_id} ")
    public void GameUpdate(GameDto gameDto);

    @Insert("insert into game " +
            "(user_id, character_no, exp, g_level, pushTime, watercnt, lovecnt, suncnt, poocnt, bugcnt, potcnt, musiccnt, nutritioncnt) " +
            "values (#{user_id}, #{character_no},  #{exp}, #{g_level}, #{pushTime}, #{watercnt}, #{lovecnt}, #{suncnt}, #{poocnt}, #{bugcnt}, #{potcnt}, #{musiccnt}, #{nutritioncnt})")
    public void Gameinsert(GameDto gameDto) throws DataAccessException;
    
    @Update("update game set character_no=#{character_no} where user_id=#{user_id}")
    void updateChar(CharacterDto charDto) throws DataAccessException;
    
    @Select("select character_no from game where user_id = #{user_id}")
    String selectCharNo(@Param("user_id")String user_id) throws DataAccessException;
}