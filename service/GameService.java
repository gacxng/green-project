package com.greenfinal.service;

import com.greenfinal.dto.CharacterDto;
import com.greenfinal.dto.GameDto;

public interface GameService {
   
   void waterPlant(String user_id);   //물주기
   int waterTime(String user_id);      //물주기 시간
   int waterCount(String user_id);   //물주기 횟수
   
   void lovePlant(String user_id);   //사랑주기
   int loveTime(String user_id);      //사랑주기 시간
   int loveCount(String user_id);      //사랑주기 횟수

   void sunPlant(String user_id);      //햇빛주기
   int sunTime(String user_id);      //햇빛주기 시간
   int sunCount(String user_id);      //햇빛주기 횟수

   void pooPlant(String user_id);      //비료주기
   int pooTime(String user_id);      //비료주기 시간
   int pooCount(String user_id);      //비료주기 횟수

   void bugPlant(String user_id);      //벌레주기
   int bugTime(String user_id);      //벌레주기 시간
   int bugCount(String user_id);      //벌레주기 횟수
   
   void potPlant(String user_id);      //화분갈이
   int potTime(String user_id);      //화분갈이 시간
   int potCount(String user_id);      //화분갈이 횟수
   
   void musicPlant(String user_id);   //음악틀어주기
   int musicTime(String user_id);   //음악틀어주기 시간
   int musicCount(String user_id);   //음악틀어주기 횟수

   void nutritionPlant(String user_id);   //영양제주기
   int nutritionTime(String user_id);      //영양제주기 시간
   int nutritionCount(String user_id);   //영양제주기 횟수

   void addUser(GameDto newGame);
   
   int userLevel(String user_id);

   int ProgressValue(String user_id);      //경험치 바
   
   GameDto getCharacterNoInfo(String userId);

   void updateChar(CharacterDto charDto);
   
   String selectCharNo(String user_id);
   
}
