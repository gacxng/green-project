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
public class MyplantDto {
	private int plant_no;
	private String plant_name;
	private String plant_nickname;
	private String first_date;
	private String last_water;
	private String user_memo;
	private int img_id; // 추가
	private String img_name;
	// private int keyword_no;
	// private String scientific_name; // 추가
	private String user_id; // 추가
	private String org_img_name; // 추가
	private int favorite; // 추가
}
