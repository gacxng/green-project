package com.greenfinal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {
	private int page;            // 현재페이지
	private int listSizePerTime;   // 한번에 보여줄 리스트 개수
	private int pageGroupSize;      // 페이지 그룹 개수. 총 리스트 개수 / 그룹 수
	private String keyword;         // 검색 키워드 저장
	private String searchType;      // 검색 종류
	private int totDataCnt;         // 전체 데이터(리스트) 수
	private int totPageCnt;         // 전체 페이지 수
	private int startPage;         // 페이지 리스트 시작 번호
	private int endPage;         // 페이지 리스트 끝 번호
	private boolean existPrevPage;   // 이전 페이지 존재 여부
	private boolean existNextPage;   // 다음 페이지 존재 여부

	private int recordSize;         // 페이지당 보여줄 레코드 수 == 튜플
	public SearchDto() {
		this.page = 1;         // 기본값 지정. 1페이지부터 5개의 페이지
		this.listSizePerTime = 9;
		this.pageGroupSize = 5;      // 페이지 그룹 개수
	}

	public SearchDto(int listSize) {
		this.page = 1;
		this.listSizePerTime = listSize;
	}

	public SearchDto(int listSize, int pageGroup) {
		this.page = 1;
		this.listSizePerTime = listSize;
		this.pageGroupSize = pageGroup;
	}

	public int getOffset() {      // sql limit 0,5
		return (page - 1) * listSizePerTime;
	}

	public void calcPage(int totDataCnt) { // 전체 페이지 수 계산
		this.totDataCnt = totDataCnt;
		totPageCnt = ((totDataCnt - 1) / listSizePerTime) + 1;
		startPage = ((page - 1) / pageGroupSize) * pageGroupSize + 1;
		endPage = startPage + pageGroupSize - 1;
		if(endPage > totPageCnt) endPage = totPageCnt;
		existPrevPage = startPage != 1;
		existNextPage = endPage != totPageCnt;
	}
}