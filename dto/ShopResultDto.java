package com.greenfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopResultDto {
	private int no;
	private String address_name;
    private String category_group_code;
    private String category_group_name;
    private String category_name;
    private String distance;
    private String id;
    private String phone;
    private String place_name;
    private String place_url;
    private String road_address_name;
    private String x;
    private String y;

	/*
    @Override
    public String toString() {
        return "SearchResult{" +
                "key1='" + key1 + '\'' +
                ", key2='" + key2 + '\'' +
                // 다른 필드들 출력
                '}';
    } */
}
