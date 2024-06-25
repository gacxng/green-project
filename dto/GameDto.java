package com.greenfinal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameDto {
    private String user_id;
    private String character_no;
    private int exp;
    private int g_level;
    private long pushTime; //마지막 누른 시간
    private int watercnt; //물주기
    private int lovecnt;  //사랑주기
    private int suncnt;    //햇빛
    private int poocnt;  //비료
    private int bugcnt;  //벌레
    private int potcnt;    //분갈이
    private int musiccnt; //음악들려주기
    private int nutritioncnt; //영양제주기
    
    private String imageUrl;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}