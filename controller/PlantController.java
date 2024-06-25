package com.greenfinal.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.greenfinal.dto.*;
import com.greenfinal.service.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class PlantController {
	private final AiplantService aiplantService;
	private final UserinfoService userinfoService;
	private final KeywordService keywordService;
	
	
	// id test
    @GetMapping(path="/aiplant/aiplant.do")
    public ModelAndView getKeyword(Principal principal) {
       String user_id = principal.getName();
       
          System.out.println("aiplant-user_id : " + user_id);
       
       List<KeywordDto> list = keywordService.getKeyword();
       List<UserinfoDto> list2 = userinfoService.viewUserinfo(user_id);
       List<KeygoryDto> list3 = keywordService.getKeygory();
       
       // 키워드 분류
       for (KeygoryDto keygory : list3) {
          // list의 모든 KeygoryDto객체에 stream생성
             List<KeywordDto> matchedKeywords = list.stream()
                         // 아래 조건에 해당하는 것들만 필터(keyword)
                     .filter(keyword -> keyword.getKeygory_no() == keygory.getKeygory_no())
                     // 필터링된 결과를 List<KeywordDto>로 수집 -> 수집된 List<KeywordDto>가 matchedKeywords 변수로 할당
                     .collect(Collectors.toList());
             // matchedKeywords를 Keywords필드에 설정
             keygory.setKeywords(matchedKeywords);
         }
          
          System.out.println("keyword : " + list);
       
       ModelAndView mav = new ModelAndView("/aiplant/aiplant");
             mav.addObject("nickname",userinfoService.findNickName(user_id));
             mav.addObject("list", list);
             mav.addObject("list2", list2);
             mav.addObject("list3", list3);
       
       return mav;
    }


    @PostMapping("/aiplant/save_user_keywords")
     public ResponseEntity<int[]> saveUserKeywords(@RequestBody Map<String, Object> requestBody) {
         try {
             List<String> userKeywords = (List<String>) requestBody.get("user_keywords");
             System.out.println("키워드222222: " + userKeywords);

             // Service를 호출하여 처리된 배열을 받음
             int[] resultArray = aiplantService.processUserKeywords(userKeywords);

                System.out.println("resultArray : " + resultArray);
             
             // 처리된 배열을 응답으로 반환
             return ResponseEntity.ok(resultArray);
         } catch (Exception e) {
             e.printStackTrace(); // 전체 스택 트레이스 출력
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
         }
     }
    
    @GetMapping(path="/aiplant/aiplant_result.do")
    public ModelAndView getAiplant2(Principal principal) {
    	String user_id = principal.getName();
       
    	System.out.println("aiplant_result-user_id : " + user_id);
       
    	List<AiplantDto> list = aiplantService.getAiplantResult(user_id);
    	List<UserinfoDto> list2 = userinfoService.viewUserinfo(user_id);
       
    	System.out.println("list : " + list);
        System.out.println("list2 : " + list2);
       
        ModelAndView mav = new ModelAndView("/aiplant/aiplant_result");
       	mav.addObject("nickname",userinfoService.findNickName(user_id));
       	mav.addObject("list", list);
       	mav.addObject("list2", list2);
       
       return mav;
    }
    
    
    @PostMapping("/aiplant/saveplant")
    @ResponseBody
    public ResponseEntity<String> savePlant(@RequestBody SaveplantDto saveplantDto) {
        try {
            aiplantService.savePlant(saveplantDto);
            return ResponseEntity.ok("내 식물로 등록되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("내 식물로 등록하는 데 실패했습니다.");
        }
    }
    

    @GetMapping(path="/aiplant/aiplant_list.do")
    public ModelAndView getAiplant(Principal principal) {
       String user_id = principal.getName();
       
          System.out.println("aiplant_list-user_id : " + user_id);
       
       List<AiplantDto> list = aiplantService.selectAiplantList(user_id);
       List<UserinfoDto> list2 = userinfoService.viewUserinfo(user_id);
       
       ModelAndView mav = new ModelAndView("/aiplant/aiplant_list");
             mav.addObject("list", list);
             mav.addObject("list2", list2);
       
       return mav;
    }
    
    @PostMapping("/aiplant/deleteplant")
    @ResponseBody
    public ResponseEntity<String> deletePlant(@RequestBody SaveplantDto saveplantDto) {
        try {
            aiplantService.deleteAiPlant(saveplantDto);
            return ResponseEntity.ok("내 식물로 삭제되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("내 식물로 삭제하는 데 실패했습니다.");
        }
    }
    

 // ---------------------------------------------------------------------------   


    /* 
       *해결*
       1. '내 식물로 등록'버튼을 누르면 input.do라는 행위를 하고 aiplant_list화면을 불러와주는데 
          불러와줄 때 aiplant_result에서 작성한 정보를 aiplant_list에 삽입한 상태로 불러와주기
          
       2. 이제 plant_no 기준으로 정보 가져와주기
       
       3. aiplant_list 정보 스택 쌓기
    */
    
    @PostMapping("/aiplant/savePrediction")
    public ResponseEntity<String> savePrediction(Principal principal, @RequestBody Map<String, Object> requestBody) {
       System.out.println("==== requestBody: " + requestBody);
       
       try {
            
            String user_id = principal.getName();
            
            // requestBody에서 prediction 키로 값을 가져옴
            int prediction = (int) requestBody.get("prediction");
            
            System.out.println("plant_no : " + prediction);
            System.out.println("ai : " + user_id);
            
            userinfoService.updatePrediction(user_id, prediction);
            
            return ResponseEntity.ok("Prediction saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save prediction");
        }
    }

}
