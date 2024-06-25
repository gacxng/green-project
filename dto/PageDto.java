package com.greenfinal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDto {
	private int page;				// 현재 페이지
	private int recordSize;			// 페이지당 보여줄 레코드 수 == 튜플	<이전 1 2 3 4 5 다음>
	private int pageSize;			// 페이지의 크기를 보여줌 크기가 5일 시 1~5까지 표시 / 한 번에 표시할 페이지 개수
	private String keyword;			// 검색 키워드 저장 목적
	private String searchType;		// 검색 종류
	private int totDataCnt;			// 전체 데이터 수
	private int totPageCnt;			// 전체 페이지 수
	private boolean existPrevPage;	// 이전 페이지 존재 여부
	private boolean existNextPage;	// 다음 페이지 존재 여부
	private int startPage;			// 페이지 리스트 시작 번호
	private int endPage;			// 페이지 리스트 끝 번호
	
	public void SearchDto() {		// 기본값 지정 1페이지/ 5개의 페이지/ 
		this.page = 1;
		this.recordSize = 5;
		this.pageSize = 5;
	}
	
	public int getOffset() {		// sql- limit 0, 5; 
		return (page - 1) * recordSize;
	}
	
	public void calcPage(int totDataCnt) {	// 전체 페이지 수 계산
		this.totDataCnt = totDataCnt;
		totPageCnt = ((totDataCnt - 1)/ recordSize) + 1;
		startPage = ((page - 1) / pageSize) * pageSize + 1;
		endPage = startPage + pageSize - 1;		// 현재 페이지가 12일때 11~20까지 표시
		if(endPage > totPageCnt) endPage = totPageCnt;	
		existPrevPage = startPage != 1;
		existNextPage = endPage != totPageCnt; 
	}
}
