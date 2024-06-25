package com.greenfinal.dto;

import java.sql.Date;
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
public class UserinfoDto {
	private int user_no;
	   private String user_name;
	   private String user_id;
	   private String user_pw;
	   private String tel;
	   private String email;
	   private String user_nickname;
	   private String character_no;
	   private Date regist_date;
	   private String user_img;
	   private String img_path;
	   private List<String> user_keyword;
	   private String nextPw;
	   private String nextPwChk;
	   
	   //추가 - 05.28
	   private int plant_no;
	   
}
