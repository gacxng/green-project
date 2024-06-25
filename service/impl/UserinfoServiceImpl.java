package com.greenfinal.service.impl;

import java.sql.Date;
import java.util.List;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greenfinal.dao.*;
import com.greenfinal.dto.*;
import com.greenfinal.service.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserinfoServiceImpl implements UserinfoService {
	private final UserinfoDao userinfoDao;
	private final PasswordEncoder passwordEncoder;
	private final JavaMailSender mailSender;
	
	@Override
    public String findUserId(String name, String email){
        return userinfoDao.findUserId(name, email);
    }

    @Override
    public void updatePassword(UserinfoDto userDto){
        if(userDto.getNextPw() != null && !userDto.getNextPw().isEmpty()){
        	userinfoDao.updatePassword(userDto);
        }
    }

    @Override
    public void updateUserInfo(UserinfoDto userDto){
    	userinfoDao.updateUserInfo(userDto);
    }

    @Override
    public void dropUser(String user_id){
    	userinfoDao.dropUser(user_id);
    }

    @Override
    public boolean findUserPw(String user_name, String user_id, String email){
        return userinfoDao.findUserPw(user_name, user_id, email);
    }
    @Override
    public Date GetRegist_date(String user_id){
        return userinfoDao.GetRegist_date(user_id);
    }
    @Override
    public String findNickName(String user_id){
        return userinfoDao.findNickName(user_id);
    }
    @Override
    public String findUserImg(String user_id){
        return userinfoDao.findUserImg(user_id);
    }
    
    @Override
    public String getUserImg(String user_id){
        return userinfoDao.getUserImg(user_id);
    }
    @Override
    public String getImgpath(String user_id){
        return userinfoDao.getUserImg(user_id);
    }
    
	@Override
	public List<UserinfoDto> getUserinfo() {
		List<UserinfoDto> list = userinfoDao.selectUserinfo();
		
		return list;
	}
	
    @Override
    public boolean validateUser(String email, String name, String userId){
        return userinfoDao.findUser(email, name, userId) != null;
    }

    @Transactional
    @Override
    public void updateNewPassword(String userId, String newPassword){
        String encodedPassword = passwordEncoder.encode(newPassword);
        System.out.println("encode : "+ encodedPassword);
        System.out.println("userId : " + userId);
        userinfoDao.updateNewPassword(encodedPassword, userId);
    }

    @Override
    public String generateAndSendNewPassword(String email){
        String newPassword = generateRandomPassword();
        sendNewPassword(email,newPassword);
        return newPassword;
    }

    private String generateRandomPassword() {
        int length  = 16;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for(int i=0;i<length;i++){
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    @Override
    public void sendNewPassword(String to, String newPassword){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("초록초록 임시비밀번호입니다");
        String text = "임시비밀번호를 발급해드립니다 \n\n";
        text += "임시비밀번호는 " + newPassword + "입니다. \n";
        text += "로그인후 비밀번호를 변경해주시기 바랍니다.";
        message.setText(text);
        mailSender.send(message);
    }

    // id
    @Override
    public List<UserinfoDto> viewUserinfo(String user_id) {
        return userinfoDao.viewUserinfo(user_id);
    }

    // 키워드
    @Override
    public void saveUserKeywords(String user_id, String Keywords) {
        // 클라이언트로부터 전달받은 하나의 문자열 키워드와 사용자 ID를 DAO를 통해 처리
        userinfoDao.updateUserKeywords(user_id, Keywords);
    }

    // -----------------------------------------------------------------------------------------------------

    // 관리자 페이지
    @Override
    public void deleteUsers(List<Integer> userNos) { // 사용자 삭제
        if (userNos != null && !userNos.isEmpty()) {
            userinfoDao.deleteUsers(userNos); // DAO를 통해 사용자 목록 삭제
        }
    }

    // 회원 검색
    @Override
    public List<UserinfoDto> getSearchUser(String keyword) {
        return userinfoDao.selectSearchUser(keyword);
    }
    
    // 훈련 값 집어넣기
    public void updatePrediction(String user_id, int prediction) {
    	userinfoDao.updatePrediction(user_id, prediction);
	}

    // -----------------------------------------------------------------------------------------------------

    // admin 페이징네이션
//    @Override
//    public List<UserinfoDto> getPagedUserinfo(SearchDto search) {
//        // 페이징을 위해 SQL의 LIMIT과 OFFSET을 계산
//        return userinfoDao.getPagedUserinfo(search);
//    }
//
//    @Override
//    public int getTotalUserCount() {
//        return userinfoDao.getTotalUserCount();
//    }
    @Override
    public List<UserinfoDto> getUserList(SearchDto search) {
        search.calcPage(userinfoDao.selectDataCnt());
        int page = search.getOffset();
        int cnt = search.getRecordSize();
        return userinfoDao.selectUser(page, cnt);
    }

    @Override
    public int getTotalUserCount() {
        return userinfoDao.selectDataCnt(); // 전체 사용자 수를 반환
    }


    // 샵
    @Override
    public UserinfoDto getUserinfoById(String userId) {
        return userinfoDao.selectUserById(userId);
    }
    
    
    // 유저 캐릭터 닉네임 추가 : 06-06
    @Override
    public void putCharacterNickname(String nick, String user_id) {
    	System.out.println("putCharacterNickname service: " + nick + ", " + user_id);
    	userinfoDao.updateCharacterNicknameByUserId(nick, user_id);
    };
    
    @Override
    public String getUserCharacterByUserId(String user_id) {
    	return userinfoDao.selectUserCharacterNickNameByUserId(user_id);
    };
}
