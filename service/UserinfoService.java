package com.greenfinal.service;

import java.sql.Date;
import java.util.List;

import com.greenfinal.dto.*;

public interface UserinfoService {
	
	// 호성
    String findUserId(String user_name, String email);
    void updatePassword(UserinfoDto userDto);
    void updateUserInfo(UserinfoDto userDto);
    void dropUser(String user_id);
    boolean findUserPw(String user_name, String user_id, String email);
    String findNickName(String user_id);
    String findUserImg(String user_id);
    String getUserImg(String user_id);
    String getImgpath(String user_id);
    boolean validateUser(String email, String name, String userId);
    String generateAndSendNewPassword(String email);
    void updateNewPassword(String userId, String newPassword);
    void sendNewPassword(String to, String newPassword);

    // 종현
    List<UserinfoDto> getUserinfo();

    //id
    List<UserinfoDto> viewUserinfo(String user_id);

    // 키워드
    void saveUserKeywords(String user_id, String keywords);
    
    Date GetRegist_date(String user_id);

    // -----------------------------------------------------------------------------------------------------

    // 관리자 페이지
    void deleteUsers(List<Integer> userNos); // user_no로 삭제

    // 회원 검색
    List<UserinfoDto> getSearchUser(String keyword);
    
    // 훈련 값 집어넣기
    void updatePrediction(String user_id, int prediction);

    // -----------------------------------------------------------------------------------------------------

    // admin 페이징네이션
    List<UserinfoDto> getUserList(SearchDto search);
    int getTotalUserCount(); // 전체 사용자 수

    UserinfoDto getUserinfoById(String user_id);
    
    // 유저 캐릭터 닉네임 추가 : 06-06
	void putCharacterNickname(String nick, String user_id);
	String getUserCharacterByUserId(String user_id);
}
