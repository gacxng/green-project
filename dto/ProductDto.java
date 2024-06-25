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
public class ProductDto {
	private int product_no;
	private String product_name;
	private String url;
	private int category_no;
	private String manufacturer;
	private String p_reply;
	private String img_path;
	private String category; // 추가
	private int favorite; // 추가
	private String user_id; // 추가
	private String description; // 추가
	private String workinghour; // 추가
}
