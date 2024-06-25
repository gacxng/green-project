package com.greenfinal.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.greenfinal.dao.*;
import com.greenfinal.dto.*;
import com.greenfinal.service.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class KeywordServiceImpl implements KeywordService {
	private final KeywordDao keywordDao;

	@Override
	public List<KeywordDto> getKeyword() {
		List<KeywordDto> list = keywordDao.selectKeyword();

		return list;
	}

	@Override
	public List<KeywordDto> getAllKeyword1() {
		List<KeywordDto> list = keywordDao.selectAllKeyword1();

		return list;
	}

	@Override
	public List<KeywordDto> getAllKeyword2() {
		List<KeywordDto> list = keywordDao.selectAllKeyword2();

		return list;
	}

	// admin
	// 삭제
	@Override
	public void deleteKeywords(List<Integer> keywordNos) { // 게시물 삭제
		if (keywordNos != null && !keywordNos.isEmpty()) {
			keywordDao.deleteKeywords(keywordNos); // DAO를 통해 사용자 목록 삭제
		}
	}

	// 검색
	@Override
	public List<KeywordDto> getSearchKeyword(String keyword) {
		return keywordDao.selectSearchKeyword(keyword);
	}

	@Override
	public List<KeywordDto> getSearchKeyword1(String keyword) {
		return keywordDao.selectSearchKeyword1(keyword);
	}

	@Override
	public List<KeywordDto> getSearchKeyword2(String keyword) {
		return keywordDao.selectSearchKeyword2(keyword);
	}

	// 수정 전 정보
	@Override
	public KeywordDto getKeywordByKeywordNo(int keyword_no) {
		return keywordDao.selectKeywordByKeywordNo(keyword_no);
	}

	// 수정
	@Override
	public void editKeywords(KeywordDto keywordDto) {
		keywordDao.updateKeywords(keywordDto);
	}

	@Override
	public void editKeyword(String keyword, int keywordNo) {
		keywordDao.updateKeyword(keyword, keywordNo);
	};

	@Override
	public void editCategory_no(int category_no, int keywordNo) {
		keywordDao.updateCategory_no(category_no, keywordNo);
	};

	@Override
	public void editKeygory_no(int keygory_no, int keywordNo) {
		keywordDao.updateKeygory_no(keygory_no, keywordNo);
	};

	// 추가
	@Override
	public int getKeywordNextNo() {
		return keywordDao.selectKeywordLastNo() + 1;
	}

	@Override
	public void saveKeyword(KeywordDto keywordDto) {
		keywordDao.insertKeyword(keywordDto);
	}

	// --- keygory ---

	// admin keygory
	@Override
	public List<KeygoryDto> getKeygory() {
		List<KeygoryDto> list = keywordDao.selectKeygory();

		return list;
	}

	// keygory 삭제
	@Override
	public void deleteKeygory(int keygory_no) {
		keywordDao.deleteKeygory(keygory_no); // DAO를 통해 게시판 삭제
	}

	// keygory 추가
	@Override
	public void addKeygory(KeygoryDto keygoryDto) {
		keywordDao.insertKeygory(keygoryDto);
	}


	// 수정 전 정보
	@Override
	public KeygoryDto getKeygoryByKeygoryNo(int keygory_no) {
		return keywordDao.selectKeygoryByKeygoryNo(keygory_no);
	}

	// 수정
	@Override
	public void editKeygory(KeygoryDto keygoryDto) {
		keywordDao.updateKeygory(keygoryDto);
	}

	@Override
	public void editKeygoryName(String keygory_name, int keygoryNo) {
		keywordDao.updateKeygoryName(keygory_name, keygoryNo);
	};
}