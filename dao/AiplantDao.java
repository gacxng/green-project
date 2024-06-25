package com.greenfinal.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.dao.DataAccessException;

import com.greenfinal.dto.AiplantDto;
import com.greenfinal.dto.SaveplantDto;

@Mapper
public interface AiplantDao {
	
	@Select("select * from aiplant where user_id = #{user_id}")
	public List<AiplantDto> selectAiplant(String user_id) throws DataAccessException;
   
	// aiplant_result 
	@Select("select * from aiplant a join userinfo u on a.plant_no = u.plant_no where user_id = #{user_id}")
	public List<AiplantDto> selectAiplantResult(String user_id) throws DataAccessException;
   
	@Insert("INSERT INTO saveplant (plant_no, user_id) VALUES (#{plant_no}, #{user_id})")
	public void savePlant(SaveplantDto saveplantDto) throws DataAccessException;
   
	@Select("select * from aiplant a join saveplant s on a.plant_no = s.plant_no where user_id = #{user_id}")
	public List<AiplantDto> selectAiplantList(String user_id) throws DataAccessException;

	@Delete("DELETE from saveplant where plant_no = #{plant_no}")
	public void deleteAiPlant(SaveplantDto saveplantDto) throws DataAccessException;
	   
}