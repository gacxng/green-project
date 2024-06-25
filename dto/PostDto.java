package com.greenfinal.dto;

import java.time.LocalDateTime;

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
public class PostDto {
	private int post_no;
	private int board_no;
	private String user_id;
	private String title;
	private String p_content;
	private LocalDateTime regist_date;
	private LocalDateTime update_date;
	private int p_like;
	private String keyword;
	private String location;
	private int hit;
	
	private String img_url;
    private int img_id;
	
	private String origin; // 추가
	private int img_no; // 추가
	private String img_location; // 추가
	private String img_name; // 추가
	private String img_path; // 추가
	private String board_name; // 추가
	
	private String user_nickname;
	private int like_cnt;
}
