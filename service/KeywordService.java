package com.greenfinal.service;

import java.util.List;

import com.greenfinal.dto.*;

public interface KeywordService {

	List<KeywordDto> getKeyword();

	List<KeywordDto> getAllKeyword1();

	List<KeywordDto> getAllKeyword2();

	// admin
	// 삭제
	void deleteKeywords(List<Integer> keywordNos);

	// 검색
	List<KeywordDto> getSearchKeyword(String keyword);

	List<KeywordDto> getSearchKeyword1(String keyword);

	List<KeywordDto> getSearchKeyword2(String keyword);

	// 수정 전 정보
	KeywordDto getKeywordByKeywordNo(int keyword_no);
	// 수정
	void editKeywords(KeywordDto keywordDto);

	void editKeyword(String keyword, int keywordNo);
	void editCategory_no(int category_no, int keywordNo);
	void editKeygory_no(int keygory_no, int keywordNo);

	// 추가
	int getKeywordNextNo();
	void saveKeyword(KeywordDto keywordDto);

	// --- Keygory ---

	// admin keygory
	List<KeygoryDto> getKeygory();
	// keygory 삭제
	void deleteKeygory(int keygory_no);
	// keygory 추가
	void addKeygory(KeygoryDto keygoryDto);


	// 수정 전 정보
	KeygoryDto getKeygoryByKeygoryNo(int boakeygoryDtord_no);
	// 수정
	void editKeygory(KeygoryDto keygoryDto);

	void editKeygoryName(String keygory_name, int keygoryNo);
}