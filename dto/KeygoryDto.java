package com.greenfinal.dto;

import java.util.List;

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
public class KeygoryDto {
	private int keygory_no;
	private String keygory_name;
	private List<KeywordDto> keywords;
}
