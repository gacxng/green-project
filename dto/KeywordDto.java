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
public class KeywordDto {
	private int keyword_no;
	private String keyword;
	private int category_no;
	private int keygory_no;
	private int col;
	private int val;
}
