package com.greenfinal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.greenfinal.dao.*;
import com.greenfinal.dto.*;
import com.greenfinal.service.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AiplantServiceImpl implements AiplantService {
	private final AiplantDao aiplantDao;
	
    private static final Map<String, KeywordDto> keywordMap = new HashMap<>();

    static {
        keywordMap.put("10대", new KeywordDto(1, "10대", 1, 1, 1, 0));
        keywordMap.put("20대", new KeywordDto(2, "20대", 1, 1, 1, 1));
        keywordMap.put("30대", new KeywordDto(3, "30대", 1, 1, 1, 2));
        keywordMap.put("40대", new KeywordDto(4, "40대", 1, 1, 1, 3));
        keywordMap.put("50대", new KeywordDto(5, "50대", 1, 2, 1, 4));
        keywordMap.put("60대이상", new KeywordDto(6, "60대이상", 1, 1, 1, 5));
        
        keywordMap.put("남성미", new KeywordDto(7, "남성미", 1, 1, 2, 0));
        keywordMap.put("우아함", new KeywordDto(8, "우아함", 1, 1, 2, 1));
        
        keywordMap.put("유럽", new KeywordDto(9, "유럽", 1, 1, 3, 0));
        keywordMap.put("아프리카", new KeywordDto(10, "아프리카", 1, 1, 3, 1));
        keywordMap.put("아시아", new KeywordDto(11, "아시아", 1, 1, 3, 2));
        keywordMap.put("북아메리카", new KeywordDto(12, "북아메리카", 1, 1, 3, 3));
        keywordMap.put("남아메리카", new KeywordDto(13, "남아메리카", 1, 1, 3, 4));
        keywordMap.put("오세아니아", new KeywordDto(14, "오세아니아", 1, 1, 3, 5));
        
        keywordMap.put("배수 토양", new KeywordDto(15, "배수 토양", 1, 1, 4, 0));
        keywordMap.put("부식 토양", new KeywordDto(16, "부식 토양", 1, 1, 4, 1));
        keywordMap.put("산성 토양", new KeywordDto(17, "산성 토양", 1, 1, 4, 2));
        keywordMap.put("비옥한 토양", new KeywordDto(18, "비옥한 토양", 1, 1, 4, 3));
        keywordMap.put("일반 토양", new KeywordDto(19, "일반 토양", 1, 1, 4, 4));
        
        keywordMap.put("키우기 쉬운", new KeywordDto(20, "키우기 쉬운", 1, 1, 5, 0));
        keywordMap.put("식물 아마추어", new KeywordDto(21, "식물 아마추어", 1, 1, 5, 1));
        keywordMap.put("식물 베테랑", new KeywordDto(22, "식물 베테랑", 1, 1, 5, 2));
        
        keywordMap.put("소", new KeywordDto(23, "소", 1, 1, 6, 0));
        keywordMap.put("중", new KeywordDto(24, "중", 1, 1, 6, 1));
        keywordMap.put("대", new KeywordDto(25, "대", 1, 2, 6, 2));
        
        keywordMap.put("관상용", new KeywordDto(26, "관상용", 1, 1, 7, 0));
        keywordMap.put("공기정화", new KeywordDto(27, "공기정화", 1, 1, 7, 1));
        keywordMap.put("식용", new KeywordDto(28, "식용", 1, 2, 7, 2));
        keywordMap.put("약용", new KeywordDto(29, "약용", 1, 2, 7, 3));
        keywordMap.put("전자파", new KeywordDto(30, "전자파", 1, 1, 7, 4));
        keywordMap.put("악취제거", new KeywordDto(31, "악취제거", 1, 2, 7, 5));
        keywordMap.put("심적안정", new KeywordDto(32, "심적안정", 1, 2, 7, 6));
        
        keywordMap.put("그늘", new KeywordDto(33, "그늘", 1, 1, 8, 0));
        keywordMap.put("간접광", new KeywordDto(34, "간접광", 1, 1, 8, 1));
        keywordMap.put("직사광", new KeywordDto(35, "직사광", 1, 2, 8, 2));
        
        keywordMap.put("물을 자주 찾는", new KeywordDto(36, "물을 자주 찾는", 1, 1, 9, 0));
        keywordMap.put("물 적당히!", new KeywordDto(37, "물 적당히!", 1, 2, 9, 1));
        keywordMap.put("물이 필요없는", new KeywordDto(38, "물이 필요없는", 1, 1, 9, 2));
        
        keywordMap.put("선인장", new KeywordDto(39, "선인장", 1, 2, 10, 0));
        keywordMap.put("다육", new KeywordDto(40, "다육", 1, 1, 10, 1));
        keywordMap.put("꽃", new KeywordDto(41, "꽃", 1, 2, 10, 2));
        keywordMap.put("채소", new KeywordDto(42, "채소", 1, 2, 10, 3));
        keywordMap.put("허브", new KeywordDto(43, "허브", 1, 1, 10, 4));
        keywordMap.put("나무", new KeywordDto(44, "나무", 1, 2, 10, 5));
        keywordMap.put("풀", new KeywordDto(45, "풀", 1, 2, 10, 6));
        
        keywordMap.put("열매가 자라는", new KeywordDto(46, "열매가 자라는", 1, 1, 11, 0));
        keywordMap.put("열매가 자라지 않는", new KeywordDto(47, "열매가 자라지 않는", 1, 1, 11, 1));
        
        keywordMap.put("덩쿨이 있는", new KeywordDto(48, "덩쿨이 있는", 1, 1, 12, 0));
        keywordMap.put("덩쿨이 없는", new KeywordDto(49, "덩쿨이 없는", 1, 1, 12, 1));
        
        keywordMap.put("수경 재배 가능", new KeywordDto(50, "수경 재배 가능", 1, 1, 13, 0));
        keywordMap.put("수경 재배 불가능", new KeywordDto(51, "수경 재배 불가능", 1, 1, 13, 1));
        
        keywordMap.put("통풍가 필요한", new KeywordDto(52, "통풍가 필요한", 1, 1, 14, 0));
        keywordMap.put("통풍이 필요하지 않는", new KeywordDto(53, "통풍이 필요하지 않는", 1, 1, 14, 1));
        
        keywordMap.put("독성이 있는", new KeywordDto(54, "독성이 있는", 1, 1, 15, 0));
        keywordMap.put("독성이 없는", new KeywordDto(55, "독성이 없는", 1, 1, 15, 1));
    }
	
	@Override
	public List<AiplantDto> getAiplant(String user_id) {
		List<AiplantDto> list = aiplantDao.selectAiplant(user_id);
      
		return list;
	}
   
	@Override
	public List<AiplantDto> getAiplant2(String user_id) {
		List<AiplantDto> list = aiplantDao.selectAiplant(user_id);
      
		return list;
	}
   
	// aiplant_result
	@Override
	public List<AiplantDto> getAiplantResult(String user_id) {
		List<AiplantDto> list = aiplantDao.selectAiplantResult(user_id);
      
		return list;
	}
   
	@Override
	public void savePlant(SaveplantDto saveplantDto) {
       	aiplantDao.savePlant(saveplantDto);
    }   
   
	@Override
	public List<AiplantDto> selectAiplantList(String user_id) {
		List<AiplantDto> list = aiplantDao.selectAiplantList(user_id);
      
		return list;
	}
   
	@Override
	public void deleteAiPlant(SaveplantDto saveplantDto) {
		aiplantDao.deleteAiPlant(saveplantDto);
    }   
	
    @Override
    public int[] processUserKeywords(List<String> userKeywords) {
        int[] array = new int[15];
        for (String keyword : userKeywords) {
            KeywordDto keywordDto = keywordMap.get(keyword);
            if (keywordDto != null && keywordDto.getCol() > 0 && keywordDto.getCol() <= array.length) {
                array[keywordDto.getCol() - 1] = keywordDto.getVal();
            }
        }
        
        System.out.println("array : " + array);
        return array; // 완성된 배열 반환
    }
	   
}