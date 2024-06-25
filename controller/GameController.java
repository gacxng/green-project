package com.greenfinal.controller;

import com.greenfinal.dto.GameDto;
import com.greenfinal.service.*;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GameController {
    private final GameService gameService;

    // 생성자를 통해 GameService 빈을 주입받도록 설정
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    
    @PostMapping("/waterPlant") // POST 요청 처리
    public ResponseEntity<String> waterPlant(@RequestParam("user_id") String user_id) {
        // 물주기 기능 호출
        gameService.waterPlant(user_id);
        int progressValue = gameService.ProgressValue(user_id);
        int waterTime = gameService.waterTime(user_id);
        int WaterCount = gameService.waterCount(user_id);
        int waterLevel = gameService.userLevel(user_id);
        int LoveCount = gameService.loveCount(user_id);
        int SunCount = gameService.sunCount(user_id);
        int PooCount = gameService.pooCount(user_id);
        int BugCount = gameService.bugCount(user_id); // 벌레주기 횟수 가져오기
        int PotCount = gameService.potCount(user_id); // 화분 갈아주기 횟수 가져오기
        int MusicCount = gameService.musicCount(user_id); // 음악 틀어주기 횟수 가져오기
        int NutritionCount = gameService.nutritionCount(user_id); // 영양제 주기 횟수 가져오기
        
        String char_no = gameService.selectCharNo(user_id);
                
     // JSON 응답 생성
        String json = "{\"progressValue\":\"" + progressValue + "\",\"WaterCount\":\"" + WaterCount + "\",\"waterLevel\":\"" + waterLevel + "\",\"LoveCount\":\"" + LoveCount + "\",\"SunCount\":\"" + SunCount + "\",\"PooCount\":\"" + PooCount + "\",\"BugCount\":\"" + BugCount + "\",\"PotCount\":\"" + PotCount + "\",\"MusicCount\":\"" + MusicCount + "\",\"NutritionCount\":\"" + NutritionCount + "\",\"char_no\":\"" + char_no + "\"}";
        return ResponseEntity.ok(json);
    }
   @PostMapping("/lovePlant")
   public ResponseEntity<String> lovePlant(@RequestParam("user_id") String user_id) {
      gameService.lovePlant(user_id);
       int progressValue = gameService.ProgressValue(user_id);
        int loveTime = gameService.loveTime(user_id);
        int LoveCount = gameService.loveCount(user_id);
        int WaterCount = gameService.waterCount(user_id);
        int SunCount = gameService.sunCount(user_id);
        int PooCount = gameService.pooCount(user_id);
        int BugCount = gameService.bugCount(user_id); // 벌레주기 횟수 가져오기
        int PotCount = gameService.potCount(user_id); // 화분 갈아주기 횟수 가져오기
        int MusicCount = gameService.musicCount(user_id); // 음악 틀어주기 횟수 가져오기
        int NutritionCount = gameService.nutritionCount(user_id); // 영양제 주기 횟수 가져오기
        int loveLevel = gameService.userLevel(user_id);
        
        String char_no = gameService.selectCharNo(user_id);
        
        // JSON 응답 생성
        String json = "{\"progressValue\":\"" + progressValue + "\",\"WaterCount\":\"" + WaterCount + "\",\"loveLevel\":\"" + loveLevel + "\",\"LoveCount\":\"" + LoveCount + "\",\"SunCount\":\"" + SunCount + "\",\"PooCount\":\"" + PooCount + "\",\"BugCount\":\"" + BugCount + "\",\"PotCount\":\"" + PotCount + "\",\"MusicCount\":\"" + MusicCount + "\",\"NutritionCount\":\"" + NutritionCount + "\",\"char_no\":\"" + char_no + "\"}";

       return ResponseEntity.ok(json);
   }
   
   @PostMapping("/sunPlant")
   public ResponseEntity<String> sunPlant(@RequestParam("user_id") String user_id) {
   gameService.sunPlant(user_id);
   int progressValue = gameService.ProgressValue(user_id);
    int sunTime = gameService.sunTime(user_id);
    int WaterCount = gameService.waterCount(user_id);
    int LoveCount = gameService.loveCount(user_id);
    int SunCount = gameService.sunCount(user_id);
    int PooCount = gameService.pooCount(user_id);
    int BugCount = gameService.bugCount(user_id); // 벌레주기 횟수 가져오기
    int PotCount = gameService.potCount(user_id); // 화분 갈아주기 횟수 가져오기
    int MusicCount = gameService.musicCount(user_id); // 음악 틀어주기 횟수 가져오기
    int NutritionCount = gameService.nutritionCount(user_id); // 영양제 주기 횟수 가져오기
    int sunLevel = gameService.userLevel(user_id);
    String char_no = gameService.selectCharNo(user_id);
    
    // JSON 응답 생성
    String json = "{\"progressValue\":\"" + progressValue + "\",\"WaterCount\":\"" + WaterCount + "\",\"sunLevel\":\"" + sunLevel + "\",\"LoveCount\":\"" + LoveCount + "\",\"SunCount\":\"" + SunCount + "\",\"PooCount\":\"" + PooCount + "\",\"BugCount\":\"" + BugCount + "\",\"PotCount\":\"" + PotCount + "\",\"MusicCount\":\"" + MusicCount + "\",\"NutritionCount\":\"" + NutritionCount + "\",\"char_no\":\"" + char_no + "\"}";
    return ResponseEntity.ok(json);

   }
   
   @PostMapping("/pooPlant")
   public ResponseEntity<String> pooPlant(@RequestParam("user_id") String user_id) {
      gameService.pooPlant(user_id);
      int progressValue = gameService.ProgressValue(user_id);
       int pooTime = gameService.pooTime(user_id);
       int WaterCount = gameService.waterCount(user_id);
       int LoveCount = gameService.loveCount(user_id);
       int SunCount = gameService.sunCount(user_id);
       int PooCount = gameService.pooCount(user_id);
       int BugCount = gameService.bugCount(user_id); // 벌레주기 횟수 가져오기
       int PotCount = gameService.potCount(user_id); // 화분 갈아주기 횟수 가져오기
       int MusicCount = gameService.musicCount(user_id); // 음악 틀어주기 횟수 가져오기
       int NutritionCount = gameService.nutritionCount(user_id); // 영양제 주기 횟수 가져오기
       int pooLevel = gameService.userLevel(user_id);
      
       String char_no = gameService.selectCharNo(user_id);
       
       // JSON 응답 생성
       String json = "{\"progressValue\":\"" + progressValue + "\",\"WaterCount\":\"" + WaterCount + "\",\"pooLevel\":\"" + pooLevel + "\",\"LoveCount\":\"" + LoveCount + "\",\"SunCount\":\"" + SunCount + "\",\"PooCount\":\"" + PooCount + "\",\"BugCount\":\"" + BugCount + "\",\"PotCount\":\"" + PotCount + "\",\"MusicCount\":\"" + MusicCount + "\",\"NutritionCount\":\"" + NutritionCount + "\",\"char_no\":\"" + char_no + "\"}";

       return ResponseEntity.ok(json);
   }
   @PostMapping("/bugPlant")
   public ResponseEntity<String> bugPlant(@RequestParam("user_id") String user_id) {
      gameService.bugPlant(user_id);
      int progressValue = gameService.ProgressValue(user_id);
       int bugTime = gameService.bugTime(user_id);
       int WaterCount = gameService.waterCount(user_id);
       int LoveCount = gameService.loveCount(user_id);
       int SunCount = gameService.sunCount(user_id);
       int PooCount = gameService.pooCount(user_id);
       int BugCount = gameService.bugCount(user_id); // 벌레주기 횟수 가져오기
       int PotCount = gameService.potCount(user_id); // 화분 갈아주기 횟수 가져오기
       int MusicCount = gameService.musicCount(user_id); // 음악 틀어주기 횟수 가져오기
       int NutritionCount = gameService.nutritionCount(user_id); // 영양제 주기 횟수 가져오기 
       int bugLevel = gameService.userLevel(user_id);
       
       String char_no = gameService.selectCharNo(user_id);
       
       // JSON 응답 생성
       String json = "{\"progressValue\":\"" + progressValue + "\",\"WaterCount\":\"" + WaterCount + "\",\"bugLevel\":\"" + bugLevel + "\",\"LoveCount\":\"" + LoveCount + "\",\"SunCount\":\"" + SunCount + "\",\"PooCount\":\"" + PooCount + "\",\"BugCount\":\"" + BugCount + "\",\"PotCount\":\"" + PotCount + "\",\"MusicCount\":\"" + MusicCount + "\",\"NutritionCount\":\"" + NutritionCount + "\",\"char_no\":\"" + char_no + "\"}";
       return ResponseEntity.ok(json);
   }
   @PostMapping("/potPlant")
   public ResponseEntity<String> potPlant(@RequestParam("user_id") String user_id) {
      gameService.potPlant(user_id);
      int progressValue = gameService.ProgressValue(user_id);
      int potTime = gameService.potTime(user_id);
       int WaterCount = gameService.waterCount(user_id);
       int LoveCount = gameService.loveCount(user_id);
       int SunCount = gameService.sunCount(user_id);
       int PooCount = gameService.pooCount(user_id);
       int BugCount = gameService.bugCount(user_id); // 벌레주기 횟수 가져오기
       int PotCount = gameService.potCount(user_id); // 화분 갈아주기 횟수 가져오기
       int MusicCount = gameService.musicCount(user_id); // 음악 틀어주기 횟수 가져오기
       int NutritionCount = gameService.nutritionCount(user_id); // 영양제 주기 횟수 가져오기
       int potLevel = gameService.userLevel(user_id);
       
       String char_no = gameService.selectCharNo(user_id);
       
       // JSON 응답 생성
       String json = "{\"progressValue\":\"" + progressValue + "\",\"WaterCount\":\"" + WaterCount + "\",\"potLevel\":\"" + potLevel + "\",\"LoveCount\":\"" + LoveCount + "\",\"SunCount\":\"" + SunCount + "\",\"PooCount\":\"" + PooCount + "\",\"BugCount\":\"" + BugCount + "\",\"PotCount\":\"" + PotCount + "\",\"MusicCount\":\"" + MusicCount + "\",\"NutritionCount\":\"" + NutritionCount + "\",\"char_no\":\"" + char_no + "\"}";
       return ResponseEntity.ok(json);
   }
   @PostMapping("/musicPlant")
   public ResponseEntity<String> musicPlant(@RequestParam("user_id") String user_id) {
      gameService.musicPlant(user_id);
      int progressValue = gameService.ProgressValue(user_id);
       int musicTime = gameService.musicTime(user_id);
       int WaterCount = gameService.waterCount(user_id);
       int LoveCount = gameService.loveCount(user_id);
       int SunCount = gameService.sunCount(user_id);
       int PooCount = gameService.pooCount(user_id);
       int BugCount = gameService.bugCount(user_id); // 벌레주기 횟수 가져오기
       int PotCount = gameService.potCount(user_id); // 화분 갈아주기 횟수 가져오기
       int MusicCount = gameService.musicCount(user_id); // 음악 틀어주기 횟수 가져오기
       int NutritionCount = gameService.nutritionCount(user_id); // 영양제 주기 횟수 가져오기
       int musicLevel = gameService.userLevel(user_id);
       
       String char_no = gameService.selectCharNo(user_id);
       
       // JSON 응답 생성
       String json = "{\"progressValue\":\"" + progressValue + "\",\"WaterCount\":\"" + WaterCount + "\",\"musicLevel\":\"" + musicLevel + "\",\"LoveCount\":\"" + LoveCount + "\",\"SunCount\":\"" + SunCount + "\",\"PooCount\":\"" + PooCount + "\",\"BugCount\":\"" + BugCount + "\",\"PotCount\":\"" + PotCount + "\",\"MusicCount\":\"" + MusicCount + "\",\"NutritionCount\":\"" + NutritionCount + "\",\"char_no\":\"" + char_no + "\"}";
       return ResponseEntity.ok(json);
   }
   @PostMapping("/nutritionPlant")
   public ResponseEntity<String> nutritionPlant(@RequestParam("user_id") String user_id) {
      gameService.nutritionPlant(user_id);
      int progressValue = gameService.ProgressValue(user_id);
       int nutritionTime = gameService.nutritionTime(user_id);
       int WaterCount = gameService.waterCount(user_id);
       int LoveCount = gameService.loveCount(user_id);
       int SunCount = gameService.sunCount(user_id);
       int PooCount = gameService.pooCount(user_id);
       int BugCount = gameService.bugCount(user_id); // 벌레주기 횟수 가져오기
       int PotCount = gameService.potCount(user_id); // 화분 갈아주기 횟수 가져오기
       int MusicCount = gameService.musicCount(user_id); // 음악 틀어주기 횟수 가져오기
       int NutritionCount = gameService.nutritionCount(user_id); // 영양제 주기 횟수 가져오기
       int nutritionLevel = gameService.userLevel(user_id);
       String char_no = gameService.selectCharNo(user_id);
       
       // JSON 응답 생성
       String json = "{\"progressValue\":\"" + progressValue + "\",\"WaterCount\":\"" + WaterCount + "\",\"nutritionLevel\":\"" + nutritionLevel + "\",\"LoveCount\":\"" + LoveCount + "\",\"SunCount\":\"" + SunCount + "\",\"PooCount\":\"" + PooCount + "\",\"BugCount\":\"" + BugCount + "\",\"PotCount\":\"" + PotCount + "\",\"MusicCount\":\"" + MusicCount + "\",\"NutritionCount\":\"" + NutritionCount + "\",\"char_no\":\"" + char_no + "\"}";
       return ResponseEntity.ok(json);
   }
  @GetMapping("/getuserlevel")
   public ResponseEntity<String> getUserlevel(@RequestParam("userId") String userId, @AuthenticationPrincipal User user){
    
    int g_level = gameService.userLevel(userId);
    
    GameDto characterInfo = gameService.getCharacterNoInfo(userId);
    //String characterNo = characterInfo.getCharacter_no();
    String char_no = gameService.selectCharNo(userId);
    String imageUrl = GameImageUrl(char_no, g_level, user);

        // JSON 형식의 문자열 생성
    String jsonResponse = "{\"user_id\":\"" + userId + "\",\"char_no\":\"" + char_no + "\",\"g_level\":" + g_level + ",\"imageUrl\":\"" + imageUrl + "\"}";

    return ResponseEntity.ok(jsonResponse);
   }
   public String GameImageUrl(String characterNo, int level, @AuthenticationPrincipal User user){
	   String user_id = user.getUsername();
	   String char_no = gameService.selectCharNo(user_id);
    String imageUrl = "img/game_character/seed-"+ char_no +"_lv_" + level + ".png";
    System.out.println(imageUrl);
    return imageUrl;
   }
}
   