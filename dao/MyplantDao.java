package com.greenfinal.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.dao.DataAccessException;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import com.greenfinal.dto.*;
import com.greenfinal.dao.*;

@Transactional
@Mapper
public interface MyplantDao {
	@Select("SELECT * FROM myplant")
	public List<MyplantDto> selectAllMyplants() throws DataAccessException;
	
	@Select("SELECT * FROM myplant WHERE user_id=#{userId}")
	public List<MyplantDto> selectMyplantListByUserId(@Param("userId") String userId) throws DataAccessException;
	
	@Select("SELECT * FROM myplant WHERE user_id=#{userId} AND favorite=1")
	public List<MyplantDto> selectFavoriteMyplants(@Param("userId") String userId) throws DataAccessException;

	@Insert("INSERT INTO myplant("
			+ "plant_no, "
			+ "plant_name, "
			+ "plant_nickname, "
			+ "first_date, "
			+ "last_water, "
			+ "user_memo, "
			+ "user_id, "
			+ "img_id, "
			+ "img_name"
			+ ") VALUES ("
			+ "#{plant_no}, #{plant_name}, #{plant_nickname}, #{first_date}, #{last_water}, "
			+ "#{user_memo}, #{user_id}, #{img_id}, #{img_name})")
	public void insertMyplant(MyplantDto myplantDto) throws DataAccessException;

	@Select("SELECT count(*) FROM myplant")
	public int selectTotalMyplantCnt() throws DataAccessException;
	
	@Select("SELECT count(*) FROM myplant WHERE user_id=#{userId}")
	public int selectTotalMyplantCntByUserId(@Param("userId") String userId) throws DataAccessException;

	@Update("UPDATE myplant "
	        + "SET plant_nickname=#{plant_nickname}, "
	        + "first_date=#{first_date}, "
	        + "last_water=#{last_water}, "
	        + "user_memo=#{user_memo}, "
	        + "img_id=#{img_id}, "
	        + "img_name=#{img_name} "
	        + "WHERE plant_no=#{plant_no}")
	public void updateMyplant(MyplantDto myplantDto) throws DataAccessException;
	
	// 내 식물 이미지 등록
	@Insert("INSERT INTO file (user_id, file_name, file_path, org_file_name) "
	        + "VALUES (#{user_id}, #{file_name}, #{file_path}, #{org_file_name})")
	@Options(useGeneratedKeys = true, keyProperty = "img_id")
	public void insertMyplantImg(FileDto imgDto) throws DataAccessException;
	
	@Update("UPDATE myplant SET plant_name=#{plantName} WHERE plant_no=#{plantNo}")
	public void updateMyplantName(@Param("plantName") String plantName, @Param("plantNo") int plantNo) throws DataAccessException;

	@Update("UPDATE myplant SET plant_nickname=#{plantNickName} WHERE plant_no=#{plantNo}")
	public void updateMyplantNickname(@Param("plantNickName") String plantNickName, @Param("plantNo") int plantNo) throws DataAccessException;

	@Update("UPDATE myplant SET user_memo=#{userMemo} WHERE plant_no=#{plantNo}")
	public void updateMyplantUserMemo(@Param("userMemo") String userMemo, @Param("plantNo") int plantNo) throws DataAccessException;

	@Select("SELECT * FROM myplant LIMIT #{offset}, #{count}")
	public List<MyplantDto> searchMyplantList(@Param("offset") int offset, @Param("count") int count);
	
	@Select("SELECT * FROM myplant WHERE user_id=#{userId} LIMIT #{offset}, #{count}")
	public List<MyplantDto> searchMyplantListByUserId(@Param("userId") String userId, @Param("offset") int offset, @Param("count") int count);
	
	@Select("SELECT * FROM myplant WHERE plant_no = #{plantNo} LIMIT 1")
	public MyplantDto selectMyplantByPlantNo(@Param("plantNo") int plantNo) throws DataAccessException;

	@Select("SELECT count(*) FROM myplant")
	public int selectMyPlantLastNo() throws DataAccessException;

	@Delete("DELETE FROM myplant WHERE plant_no=#{plantNo} AND user_id=#{userId}")
	void deleteMyplant(@Param("plantNo") int plantNo, @Param("userId") String userId);

	// 내 식물 즐겨찾기 추가
   @Update("UPDATE myplant SET favorite=1 WHERE plant_no=#{plantNo}")
   int putFavoriteByPlantNo(@Param("plantNo") int plantNo) throws DataAccessException;

   // 내 식물 즐겨찾기 제거
   @Update("UPDATE myplant SET favorite=0 WHERE plant_no=#{plantNo}")
   int removeFavoriteByPlantNo(@Param("plantNo") int plantNo) throws DataAccessException;

   // 내 식물 이미지 가져오기 추가
   @Select("SELECT * FROM file WHERE img_id=#{imgId}")
   public FileDto selectFileNameByid(@Param("imgId") int imgId) throws DataAccessException;

   // 내 식물 파일이름으로 가져오기 추가
   @Select("SELECT img_id FROM file WHERE file_name=#{imgName}")
   public int selectImgIdByImgName(@Param("imgName") String imgName) throws DataAccessException;

   // 내 식물 이미지아이디로 가져오기 추가
   @Select("SELECT * FROM file WHERE img_id=#{imgId}")
   public FileDto selectImgByImgId(int imgId) throws DataAccessException;
   
	
}
