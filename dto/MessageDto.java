package com.greenfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDto {
	private String message;				// 사용자에게 전달할 메시지
	private String redirectUri;			// 리다이렉트 URI
	// private RequestMethod method;		// HTTP 요청 메서드
	// private Map<String, Object> data;	// 화면(View)으로 전달할 데이터(파라미터)
}
