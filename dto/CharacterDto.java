package com.greenfinal.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CharacterDto {
    private String user_id;
    private String character_name;
    private String join_keyword;
    private String char_img;
    private int character_no;
}
