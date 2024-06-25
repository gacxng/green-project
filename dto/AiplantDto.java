package com.greenfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AiplantDto {
	private int plant_no;
	private String plant_name; // 식물명
	private String watering; // 물주기
	private String temperature; // 온도
	private String height; // 성장
	private String feature; // 특징
	private String age; // 수명
	private String scientific_name; // 학명
	private String plant_img; 
	
	// userinfo
	private String user_id;
	
	// keyword
//	private int keyword_no;
	private String keyword; // 식물에 따라 다닐 키워드
	
}