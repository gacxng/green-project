package com.greenfinal.dao;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.dao.DataAccessException;

import com.greenfinal.dto.*;

@Mapper
public interface UserinfoDao {
	
	// 호성
	@Insert("insert into userinfo(user_name, user_id, user_pw, tel, email, user_nickname, character_no,  regist_date, user_img, img_path) values(#{user_name}, #{user_id}, #{user_pw}, #{tel}, #{email},#{user_nickname}, 0, NOW(),#{user_img}, #{img_path})")
    public void insertUser(UserinfoDto userDto) throws DataAccessException;
    
	@Select("select * from userinfo where user_id=#{user_id}")
    public UserinfoDto getByUserId(String user_id) throws DataAccessException;
    
	@Select("select count(*) from userinfo where user_id=#{user_id}")
    int countByUserId(@Param("user_id") String user_id) throws DataAccessException;
	
	@Select("select regist_date from userinfo where user_id=#{user_id}")
	Date GetRegist_date(@Param("user_id") String user_id) throws DataAccessException;
    
	@Select("select count(*) from userinfo where user_nickname=#{user_nickname}")
    int countByNickName(@Param("user_nickname") String user_nickname) throws DataAccessException;
	
	@Select("select user_id from userinfo where user_name=#{user_name} and email=#{email}")
    String findUserId(@Param("user_name") String user_name, @Param("email") String email) throws DataAccessException;
    
	@Select("select count(*) from userinfo where user_name=#{user_name} and user_id=#{user_id} and email=#{email}")
    int booleanUser(@Param("user_name") String user_name, @Param("user_id") String user_id, @Param("email") String email) throws DataAccessException;
    
	@Select("select * from userinfo where user_id=#{user_id}")
    public UserinfoDto viewUserInfo(String user_id) throws DataAccessException;
    
    @Select("select user_pw from userinfo where user_id=#{user_id}")
    public String selectPw(@Param("user_id") String user_id) throws DataAccessException;
    
    @Update("update userinfo set user_pw = #{nextPw} where user_id = #{user_id}")
    public void updatePassword(UserinfoDto userDto) throws DataAccessException;
   
    @Update("update userinfo set user_name=#{user_name}, user_nickname=#{user_nickname}, tel=#{tel}, email=#{email} where user_id=#{user_id}")
    public void updateUserInfo(UserinfoDto userDto) throws DataAccessException;
    
    @Delete("delete from userinfo where user_id=#{user_id}")
    void dropUser(@Param("user_id") String user_id) throws DataAccessException;
    
    @Select("select user_nickname from userinfo where user_id=#{user_id}")
    String findNickName(@Param("user_id") String user_id) throws DataAccessException;
    
    @Select("select user_img from userinfo where user_id=#{user_id}")
    String findUserImg(@Param("user_id") String user_id) throws DataAccessException;
    
    @Select("select count(*) from userinfo where user_name=#{user_name} and user_id=#{user_id} and email=#{email}")
    boolean findUserPw(@Param("user_name") String user_name, @Param("user_id") String user_id, @Param("email") String email) throws DataAccessException;

    @Select("select user_img from userinfo where user_id=#{user_id}")
    String getUserImg(@Param("user_id")String user_id) throws DataAccessException;
    @Select("select img_path from userinfo where user_id=#{user_id}")
    String getImgpath(@Param("user_id")String user_id) throws DataAccessException;
    
    @Select("select * from userinfo where email = #{email} and user_name=#{user_name} and user_id=#{user_id}")
    UserinfoDto findUser(@Param("email")String email, @Param("user_name")String name, @Param("user_id")String userId) throws DataAccessException;

    @Update("update userinfo set user_pw=#{user_pw} where user_id = #{user_id}")
    void updateNewPassword(@Param("user_pw")String encodedPassword, @Param("user_id")String userId);
    
	// -----------------------------------------------------------------------------------------------------
	@Select("select * from userinfo")
	public List<UserinfoDto> selectUserinfo() throws DataAccessException;

	// id
	@Select("select * from userinfo where user_id=#{user_id}")
	public List<UserinfoDto> viewUserinfo(String user_id) throws DataAccessException;
	
	// keyword
	@Update("UPDATE userinfo SET user_keyword = #{Keywords} WHERE user_id = #{user_id}")
    void updateUserKeywords(@Param("user_id") String user_id, @Param("Keywords") String Keywords);

    // 관리자 페이지
	// 회원 삭제
	@Delete({
        "<script>",
        "DELETE FROM userinfo WHERE user_no IN ",
        "<foreach item='userNo' collection='userNos' open='(' separator=',' close=')'>",
        "#{userNo}",
        "</foreach>",
        "</script>"
    })
    void deleteUsers(@Param("userNos") List<Integer> userNos);
   
   	// 회원 검색
   	@Select("select * from userinfo where user_name like concat('%', #{keyword}, '%') or user_nickname like concat('%', #{keyword}, '%') or user_id like concat('%', #{keyword}, '%') or email like concat('%', #{keyword}, '%') or tel like concat('%', #{keyword}, '%')")
   	public List<UserinfoDto> selectSearchUser(@Param("keyword") String keyword) throws DataAccessException;

   	// 훈련 값 집어넣기
    @Update("update userinfo set plant_no = #{prediction} where user_id = #{user_id}")
    public void updatePrediction(@Param("user_id") String user_id, @Param("prediction") int prediction);
   	
	@Select("select count(*) from userinfo")
	public int selectDataCnt();
	
	@Select("select * from userinfo limit #{offset}, #{count}")
	public List<UserinfoDto> selectUser(@Param("offset") int offset, @Param("count") int count);
	
	@Select("select count(*) from userinfo where email=#{email}")
    int countByEmail(@Param("email") String email) throws DataAccessException;

	// 샵
	@Select("SELECT * FROM userinfo WHERE user_id=#{user_id}")
	public UserinfoDto selectUserById(@Param("user_id") String userId);

	UserinfoDto getUserinfoById(String user_id);
	
	// 유저 캐럭터 추가 : 06-06
	@Update("update userinfo set character_nickname=#{character_nickname} where user_id=#{user_id}")
	public void updateCharacterNicknameByUserId(@Param("character_nickname") String nick, @Param("user_id") String user_id) throws DataAccessException;

	@Select("select character_nickname from userinfo where user_id=#{user_id}")
	public String selectUserCharacterNickNameByUserId(@Param("user_id") String user_id);

}