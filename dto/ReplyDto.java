package com.greenfinal.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReplyDto {
    private int reply_no;
    private int post_no;
    private String board_name;
    private String title;
    private String user_id;
    private String r_content;
    private LocalDateTime update_date;
    
    private int user_no;
    private String user_name;
    private String user_nickname;
}
