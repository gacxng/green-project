package com.greenfinal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfinal.dao.GameDao;
import com.greenfinal.dto.CharacterDto;
import com.greenfinal.dto.GameDto;
import com.greenfinal.service.GameService;

@Service
public class GameServiceImpl implements GameService {
   private final GameDao gameDao; //게임 정보에 접근하는 Dao

   @Autowired
   public GameServiceImpl(GameDao gameDao) {
      this.gameDao = gameDao;
   }
   
   public void updateChar(CharacterDto charDto){
       gameDao.updateChar(charDto);
   }
   
   @Override
   public void addUser(GameDto gameDto) {
      // 이미 해당 유저에 대한 게임 데이터가 존재하는지 확인
      GameDto existingGame = gameDao.GamefindByUserId(gameDto.getUser_id());
      if (existingGame != null) {
         // 이미 게임 데이터가 존재한다면 해당 데이터를 업데이트
         gameDto.setUser_id(existingGame.getUser_id()); // 기존 데이터의 ID를 새 데이터에 설정하여 업데이트 수행
         gameDao.GameUpdate(gameDto);
      } else {
         // 새로운 게임 데이터를 추가
         gameDao.Gameinsert(gameDto);
      }
   }
   
   @Override
   public String selectCharNo(String user_id) {
	   return gameDao.selectCharNo(user_id);
   }

   @Override
   public void waterPlant(String user_id) { //물주기 기능
      int waterTime = 1; // 물주기 쿨타임
      int waterExp = 1; // 물주기 경험치
      int waterCount = 5;

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      // 게임 정보가 존재하고, 사랑주기 횟수와 쿨타임을 만족하는 경우에만 사랑주기 수
      if (gameDto != null && gameDto.getWatercnt() < waterCount &&
            (System.currentTimeMillis() - gameDto.getPushTime()) >= waterTime * 1000) {

         // 물주기 수행
         gameDto.setWatercnt(gameDto.getWatercnt() + 1); // 물 주기 횟수 증가
         int newExp = gameDto.getExp() + waterExp; // 새로운 경험치 값을 계산


         // 최대 경험치를 넘어가면 레벨 업 및 경험치 초기화
         if(newExp >= LevelMaxExp(gameDto.getG_level())) {
            // 레벨 업
            gameDto.setG_level(gameDto.getG_level() + 1);
            // 경험치 초기화
            gameDto.setExp(0);
            // 물주기 횟수 초기화
            gameDto.setWatercnt(0);
            // 사랑주기 횟수 초기화
            gameDto.setLovecnt(0);
            // 햇빛주기 횟수 초기화
            gameDto.setSuncnt(0);
            // 비료주기 횟수 초기화
            gameDto.setPoocnt(0);
            //벌레주기 횟수 초기화
            gameDto.setBugcnt(0);
            //화분갈이 횟수 초기화
            gameDto.setPotcnt(0);
            //음악틀어주기 횟수 초기화
            gameDto.setMusiccnt(0);
            //영양제주기 횟수 초기화
            gameDto.setNutritioncnt(0);

         } else {
            gameDto.setExp(newExp); // 최대 경험치를 넘지 않는 경우, 새로운 경험치 값을 현재 경험치를 설정합니다.
         }

         // 물주기 쿨타임 초기화
         gameDto.setPushTime(System.currentTimeMillis()); // 현재 시간으로 설정

         // 업데이트된 게임 정보를 저장
         gameDao.GameUpdate(gameDto);
      }
   }

   // 물주기 시간을 반환하는 메서드
   public int waterTime(String user_id) {
      int defaultWaterTime = 1;    // 기본 물주기 시간
      int waterTime = defaultWaterTime;    // 초기값은 기본 물주기 시간으로 설정

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      // 게임 정보가 존재하고, 물주기 횟수와 쿨타임을 만족하는 경우에만 물주기 시간을 업데이트
      if(gameDto != null && gameDto.getWatercnt() < 5 && (System.currentTimeMillis() - gameDto.getPushTime())
            >= defaultWaterTime * 1000) {
         // 현재 시간과 마지막 물주기 시간의 차이를 계산하여 경과 시간을 얻는다.
         long elapsedTime = System.currentTimeMillis() - gameDto.getPushTime();
         // 기본 물주기 시간에서 경과 시간을 빼서 남은 시간을 계산한다
         // 최소값을 0으로 설정하여 음수 값이 되지 않도록 한다.
         waterTime = (int) Math.max(0, defaultWaterTime - (elapsedTime / 1000)); // 남은 시간 계산

         // 게임 정보 업데이트: 남은 시간과 물주기 시간을 업데이트한다.
         gameDao.GameUpdate(gameDto);
      }
      return waterTime;
   }

   // 물주기 횟수를 반환하는 메서드
   public int waterCount(String user_id) {
      int waterExp = 1;

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      // 게임 정보가 존재하고, 물주기 횟수와 쿨타임을 만족하는 경우에만 물주기 횟수를 업데이트
      if(gameDto != null  && gameDto.getWatercnt() < 5 && (System.currentTimeMillis() - gameDto.getPushTime())
            >= 3 * 1000) {
         // 레벨 업 조건 확인
         int newExp = gameDto.getExp() + waterExp; // 새로운 경험치 계산
         int maxExp = LevelMaxExp(gameDto.getG_level()); // 현재 레벨의 최대 경험치 가져오기

         if (newExp >= maxExp) {
            // 레벨 업 조건을 만족할 때의 처리
            gameDto.setG_level(gameDto.getG_level() + 1); // 레벨 업
            gameDto.setExp(0); // 경험치 초기화
            gameDto.setWatercnt(0); // 물주기 횟수 초기화

            // 물주기 횟수가 초기화되므로 데이터베이스에 업데이트
            gameDao.GameUpdate(gameDto);
         } else {
            gameDto.setExp(newExp); // 경험치 업데이트
            gameDto.setWatercnt(gameDto.getWatercnt() + 0); // 물주기 횟수 증가
         }
      }
      return gameDto.getWatercnt(); // 화면에 업데이트되는 물주기 횟수 반환
   }

   @Override
   public void lovePlant(String user_id) {  //사랑주기 기능
      int loveCount = 5;      //사랑주기 횟수
      int loveTime = 1;       //사랑주기 시간
      int loveExp = 2;       //사랑주기 경험치

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      // 게임 정보가 존재하고, 사랑주기 횟수와 쿨타임을 만족하는 경우에만 사랑주기 수
      if (gameDto != null && gameDto.getLovecnt() < loveCount &&
            (System.currentTimeMillis() - gameDto.getPushTime()) >= loveTime * 1000) {
         // 사랑 주기 수행
         gameDto.setLovecnt(gameDto.getLovecnt() + 1); // 사랑 주기 횟수 증가
         int newExp = gameDto.getExp() + loveExp; // 새로운 경험치 값을 계산

         //최대 경험치를 넘어가면 레벨 업 및 경험치 초기화
         if(newExp >= LevelMaxExp(gameDto.getG_level())) {
            //레벨 업
            gameDto.setG_level(gameDto.getG_level() + 1);
            //경험치 초기화
            gameDto.setExp(0);
            // 물주기 횟수 초기화
            gameDto.setWatercnt(0);
            // 사랑주기 횟수 초기화
            gameDto.setLovecnt(0);
            // 햇빛주기 횟수 초기화
            gameDto.setSuncnt(0);
            // 비료주기 횟수 초기화
            gameDto.setPoocnt(0);
            //벌레주기 횟수 초기화
            gameDto.setBugcnt(0);
            //화분갈이 횟수 초기화
            gameDto.setPotcnt(0);
            //음악틀어주기 횟수 초기화
            gameDto.setMusiccnt(0);
            //영양제주기 횟수 초기화
            gameDto.setNutritioncnt(0);
         }
         else {
            gameDto.setExp(newExp); // 최대 경험치를 넘지 않는 경우, 새로운 경험치 값을 현재 경험치를 설정합니다.
         }

         // 사랑 주기 쿨타임 초기화
         gameDto.setPushTime(System.currentTimeMillis()); // 현재 시간으로 설정

         // 업데이트된 게임 정보를 저장
         gameDao.GameUpdate(gameDto);
      }
   }
   public int loveTime(String user_id){
      int defaultLoveTime = 1;
      int loveTime = defaultLoveTime;

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      // 게임 정보가 존재하고, 사랑주기 횟수와 쿨타임을 만족하는 경우에만 사랑주기 시간을 업데이트
      if(gameDto != null && gameDto.getLovecnt() < 5 && (System.currentTimeMillis() - gameDto.getPushTime())
            >= defaultLoveTime * 1000) {
         // 현재 시간과 마지막 사랑주기 시간의 차이를 계산하여 경과 시간을 얻는다.
         long elapsedTime = System.currentTimeMillis() - gameDto.getPushTime();
         // 기본 물주기 시간에서 경과 시간을 빼서 남은 시간을 계산한다
         // 최소값을 0으로 설정하여 음수 값이 되지 않도록 한다.
         loveTime = (int) Math.max(0, defaultLoveTime - (elapsedTime / 1000)); // 남은 시간 계산

         // 게임 정보 업데이트: 남은 시간과 사랑주기 시간을 업데이트한다.
         gameDao.GameUpdate(gameDto);
      }
      return loveTime;
   }
   public int loveCount(String user_id){
      int loveCount = 5;
      int loveExp = 2;

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      // 게임 정보가 존재하고, 사랑주기 횟수와 쿨타임을 만족하는 경우에만 물주기 횟수를 업데이트
      if(gameDto != null && gameDto.getLovecnt() < 5 && (System.currentTimeMillis() - gameDto.getPushTime())
            >= 1 * 1000) {
         // 레벨 업 조건 확인
         int newExp = gameDto.getExp() + loveExp; // 새로운 경험치 계산
         int maxExp = LevelMaxExp(gameDto.getG_level()); // 현재 레벨의 최대 경험치 가져오기

         if (newExp >= maxExp) {
            // 레벨 업 조건을 만족할 때의 처리
            gameDto.setG_level(gameDto.getG_level() + 1); // 레벨 업
            gameDto.setExp(0); // 경험치 초기화
            gameDto.setLovecnt(0); // 사랑주기 횟수 초기화

            // 사랑주기 횟수가 초기화되므로 데이터베이스에 업데이트
            gameDao.GameUpdate(gameDto);
         } else {
            gameDto.setExp(newExp); // 경험치 업데이트
            gameDto.setLovecnt(gameDto.getLovecnt() + 0); // 사랑주기 횟수 증가

         }
      }
      return gameDto.getLovecnt(); // 화면에 업데이트되는 사랑주기 횟수 반환
   }

   public void sunPlant(String user_id) {      //햇빛주기 기능
      int sunCount = 5;     //햇빛주기 횟수
      int sunTime = 1;    //햇빛주기 시간
      int sunExp = 3;      //햇빛주기 경험치

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      // 게임 정보가 존재하고, 햇빛주기 횟수와 쿨타임을 만족하는 경우에만 햇빛주기 수
      if(gameDto != null && gameDto.getSuncnt() < sunCount &&
            (System.currentTimeMillis() - gameDto.getPushTime()) >= sunTime * 1000) {
         //햇빛 주기 수행
         gameDto.setSuncnt(gameDto.getSuncnt() + 1); //햇빛 주기 횟수 증가
         int newExp = gameDto.getExp() + sunExp; //새로운 경험치 값을 계산

         //최대 경험치를 넘어가면 레벨 업 및 경험치 초기화
         if(newExp >= LevelMaxExp(gameDto.getG_level())) {
            //레벨 업
            gameDto.setG_level(gameDto.getG_level() + 1);
            //경험치 초기화
            gameDto.setExp(0);
            // 물주기 횟수 초기화
            gameDto.setWatercnt(0);
            // 사랑주기 횟수 초기화
            gameDto.setLovecnt(0);
            //햇빛횟수 초기화
            gameDto.setSuncnt(0);
            //비료주기 횟수 초기화
            gameDto.setPoocnt(0);
            //벌레주기 횟수 초기화
            gameDto.setBugcnt(0);
            //화분갈이 횟수 초기화
            gameDto.setPotcnt(0);
            //음악틀어주기 횟수 초기화
            gameDto.setMusiccnt(0);
            //영양제주기 횟수 초기화
            gameDto.setNutritioncnt(0);
         }
         else {
            gameDto.setExp(newExp);   // 최대 경험치를 넘지 않는 경우, 새로운 경험치 값을 현재 경험치를 설정합니다.
         }
         // 햇빛 주기 쿨타임 초기화
         gameDto.setPushTime(System.currentTimeMillis());    // 현재 시간으로 설정
         // 업데이트된 게임 정보를 저장
         gameDao.GameUpdate(gameDto);
      }
   }

   public int sunTime(String user_id){
      int defaultSunTime = 1;
      int sunTime = defaultSunTime;

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      //게임 정보가 존재하고, 햇빛주기 횟수와 쿨타임을 만족하는 경우에만 햇빛주기 시간을 업데이트
      if(gameDto != null && gameDto.getSuncnt() < 5 && (System.currentTimeMillis() - gameDto.getPushTime())
            >= defaultSunTime * 1000) {
         // 현재 시간과 마지막 햇빛주기 시간의 차이를 계산하여 경과 시간을 얻는다
         long elapsedTime = System.currentTimeMillis() - gameDto.getPushTime();
         // 기본 햇빛주기 시간에서 경과 시간을 빼서 남은 시간을 계산한다.
         // 최소값을 0으로 설정하여 음수 값이 되지 않도록 한다.
         sunTime = (int)Math.max(0, defaultSunTime - (elapsedTime / 1000));
         // 게임 정보 업데이트
         gameDao.GameUpdate(gameDto);
      }
      return sunTime;
   }
   public int sunCount(String user_id){
      int sunCount = 5;
      int sunExp = 3;

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      //게임 정보가 존재하고, 햇빛주기 횟수와 쿨타임을 만족하는 경우에마 햇빛주기 수행
      if(gameDto != null && gameDto.getSuncnt() < 5 && (System.currentTimeMillis() - gameDto.getPushTime())
            >= 1 * 1000){
         // 레벨 업 조건 확인
         int newExp = gameDto.getExp() + sunExp;   //새로운 경험치 계산
         int maxExp = LevelMaxExp(gameDto.getG_level());   // 현재 레벨의 최대 경험치 가져오기

         if(newExp >= maxExp){
            // 레벨 업 조건을 만족할 때 의 처리
            gameDto.setG_level(gameDto.getG_level() + 1); //레벨 업
            gameDto.setExp(0);   // 경험치 초기화
            gameDto.setSuncnt(0);   // 햇빛주기 초기화

            //햇빛주기 횟수가 초기화되므로 데이터베이스에 업데이트
            gameDao.GameUpdate(gameDto);
         }
         else{
            gameDto.setExp(newExp); //경험치 업데이트
            gameDto.setSuncnt(gameDto.getSuncnt() + 0); //햇빛주기 횟수 증가
         }
      }
      return gameDto.getSuncnt();
   }

   public void pooPlant(String user_id) {  //비료주기 기능
      int pooCount = 5;    //비료주기 횟수
      int pooTime = 1;   //비료주기 시간
      int pooExp = 6;      //비료주기 경험치

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      //게임 정보가 존재하고, 비료주기 횟수와 쿨타임을 만족하는 경우에만 비료주기 수
      if(gameDto != null && gameDto.getPoocnt() < pooCount &&
            (System.currentTimeMillis() - gameDto.getPushTime()) >= pooTime * 1000) {
         //비료 주기 수행
         gameDto.setPoocnt(gameDto.getPoocnt() + 1); //비료 주기 횟수 증가
         int newExp = gameDto.getExp() + pooExp; // 새로운 경험치 값을 계산

         //최대 경험치를 넘어가면 레벨 업 및 경험치 초기화
         if(newExp >= LevelMaxExp(gameDto.getG_level())) {
            //레벨 업
            gameDto.setG_level(gameDto.getG_level() + 1);
            //경험치 초기화
            gameDto.setExp(0);
            // 물주기 횟수 초기화
            gameDto.setWatercnt(0);
            // 사랑주기 횟수 초기화
            gameDto.setLovecnt(0);
            //햇빛횟수 초기화
            gameDto.setSuncnt(0);
            //비료주기 횟수 초기화
            gameDto.setPoocnt(0);
            //벌레주기 횟수 초기화
            gameDto.setBugcnt(0);
            //화분갈이 횟수 초기화
            gameDto.setPotcnt(0);
            //음악틀어주기 횟수 초기화
            gameDto.setMusiccnt(0);
            //영양제주기 횟수 초기화
            gameDto.setNutritioncnt(0);
         }
         else {
            gameDto.setExp(newExp);      // 최대 경험치를 넘지 않는 경우, 새로운 경험치 값을 현재 경험치를 설정합니다.
         }
         //비료 주기 쿨타임 초기화
         gameDto.setPushTime(System.currentTimeMillis());
         //업데이트된 게임 정보를 저장
         gameDao.GameUpdate(gameDto);
      }
   }
   public int pooTime(String user_id){
      int defaultPooTime = 1;
      int pooTime = defaultPooTime;

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      if(gameDto != null && gameDto.getPoocnt() < 5 && (System.currentTimeMillis() - gameDto.getPushTime())
            >= defaultPooTime * 1000){
         // 현재 시간과 마지막 비료주기 시간의 차이를 계산하여 경과 시간을 얻는다.
         long elapsedTime = System.currentTimeMillis() - gameDto.getPushTime();
         //기본 비료주기 시간에서 경과 시간을 빼서 남은 시간을 계산한다.
         //최소값을 0으로 설정하여 음수 값이 되지 않도록 한다.
         pooTime = (int)Math.max(0, defaultPooTime - (elapsedTime / 1000));
         //게임 정보 업데이트
         gameDao.GameUpdate(gameDto);
      }
      return pooTime;
   }
   public int pooCount(String user_id){
      int pooCount = 5;
      int pooExp = 6;

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      //게임 정보가 존재하고, 햇빛주기 횟수와 쿨타임을 만족하는 경우에마 햇빛주기 수행
      if(gameDto != null && gameDto.getPoocnt() < 5 && (System.currentTimeMillis() - gameDto.getPushTime())
            >= 1 * 1000){
         // 레벨 업 조건 확인
         int newExp = gameDto.getExp() + pooExp;   //새로운 경험치 계산
         int maxExp = LevelMaxExp(gameDto.getG_level());   // 현재 레벨의 최대 경험치 가져오기

         if(newExp >= maxExp){
            // 레벨 업 조건을 만족할 때 의 처리
            gameDto.setG_level(gameDto.getG_level() + 1); //레벨 업
            gameDto.setExp(0);   // 경험치 초기화
            gameDto.setPoocnt(0);   // 비료주기 초기화

            //햇빛주기 횟수가 초기화되므로 데이터베이스에 업데이트
            gameDao.GameUpdate(gameDto);
         }
         else{
            gameDto.setExp(newExp); //경험치 업데이트
            gameDto.setPoocnt(gameDto.getPoocnt() + 0); //햇빛주기 횟수 증가
         }
      }
      return gameDto.getPoocnt();
   }
   public void bugPlant(String user_id) {      //벌레주기 기능
      int bugCount = 10; //벌레주기 횟수
      int bugTime = 1; //벌레주기 시간
      int bugExp = 10;   //벌레주기 경험치

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      //게임 정보가 존재하고, 벌레주기 횟수와 쿨타임을 만족하는 경우에만 벌레주기 수
      if(gameDto != null && gameDto.getBugcnt() < bugCount &&
            (System.currentTimeMillis() - gameDto.getPushTime()) >= bugTime * 1000) {
         //벌레 주기 수행
         gameDto.setBugcnt(gameDto.getBugcnt() + 1); // 벌레 주기 횟수 증가
         int newExp = gameDto.getExp() + bugExp;    // 새로운 경험치 값을 계산

         //최대 경험치를 넘어가면 레벨 업 및 경험치 초기화
         if(newExp >= LevelMaxExp(gameDto.getG_level())) {
            //레벨 업
            gameDto.setG_level(gameDto.getG_level() + 1);
            //경험치 초기화
            gameDto.setExp(0);
            // 물주기 횟수 초기화
            gameDto.setWatercnt(0);
            // 사랑주기 횟수 초기화
            gameDto.setLovecnt(0);
            //햇빛횟수 초기화
            gameDto.setSuncnt(0);
            //비료주기 횟수 초기화
            gameDto.setPoocnt(0);
            //횟수 초기화
            gameDto.setBugcnt(0);
            //화분갈이 횟수 초기화
            gameDto.setPotcnt(0);
            //음악틀어주기 횟수 초기화
            gameDto.setMusiccnt(0);
            //영양제주기 횟수 초기화
            gameDto.setNutritioncnt(0);
         }
         else {
            gameDto.setExp(newExp); // 최대 경험치를 넘지 않는 경우, 새로운 경험치 값을 현재 경험치를 설정합니다.
         }
         // 벌레 주기 쿨타임 초기화
         gameDto.setPushTime(System.currentTimeMillis());
         // 업데이트된 게임 정보를 저장
         gameDao.GameUpdate(gameDto);
      }
   }
   public int bugTime(String user_id){
      int defaultBugTime = 1;
      int bugTime = defaultBugTime;

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      //게임 정보가 존재하고,햇빛주기 횟수와 쿨타임을 만족하는 경우에만 벌레주기 시간을 업데이트
      if(gameDto != null && gameDto.getBugcnt() < 10 && (System.currentTimeMillis() -gameDto.getPushTime())
            >= defaultBugTime * 1000){
         //현재 시간과 마지막 벌레주기 시간의 차이를 계산하여 경과 시간을 얻는다.
         long elapsedTime = System.currentTimeMillis() - gameDto.getPushTime();
         //기본 벌레주기 시간에서 경과 시간을 빼서 남은 시간을 계산한다.
         //최소값을 0으로 설정하여 음수 값이 되지 않도록 한다.
         bugTime = (int)Math.max(0, defaultBugTime - (elapsedTime / 1000));
         //게임 정보를 업데이트
         gameDao.GameUpdate(gameDto);
      }
      return bugTime;
   }
   public int bugCount(String user_id){
      int bugCount = 10;
      int bugExp = 10;

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      //게임 정보가 존재하고, 벌레주기 횟수와 쿨타임을 만족하는 경우에만 벌레주기 수행
      if(gameDto != null && gameDto.getBugcnt() < 5 && (System.currentTimeMillis() - gameDto.getPushTime())
            >= 1 * 1000){
         int newExp = gameDto.getExp() + bugExp;
         int maxExp = LevelMaxExp(gameDto.getG_level());

         if(newExp >= maxExp){

            gameDto.setG_level(gameDto.getG_level() + 1);
            gameDto.setExp(0);
            gameDto.setBugcnt(0);

            gameDao.GameUpdate(gameDto);
         }
         else{
            gameDto.setExp(newExp);
            gameDto.setBugcnt(gameDto.getBugcnt() + 0);;
         }
      }
      return gameDto.getBugcnt();
   }
   public void potPlant(String user_id) {      //화분주기 기능
      int potCount = 10; //화분주기 횟수
      int potTime = 1; //화분주기 시간
      int potExp = 20; //화분주기 경험치

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      //게임 정보가 존재하고, 화분주기 횟수와 쿨타임을 만족하는 경우에만 화분주기 수행
      if(gameDto != null && gameDto.getPotcnt() < potCount &&
            (System.currentTimeMillis() - gameDto.getPushTime()) >= potTime * 1000) {
         //화분 주기 수행
         gameDto.setPotcnt(gameDto.getPotcnt() + 1); //화분 주기 횟수 증가
         int newExp = gameDto.getExp() + potExp;      // 새로운 경험치 값을 계산

         //최대 경험치를 넘어가면 레벨 업 및 경험치 초기화
         if(newExp >= LevelMaxExp(gameDto.getG_level())) {
            //레벨 업
            gameDto.setG_level(gameDto.getG_level() + 1);
            //경험치 초기화
            gameDto.setExp(0);
            // 물주기 횟수 초기화
            gameDto.setWatercnt(0);
            // 사랑주기 횟수 초기화
            gameDto.setLovecnt(0);
            //햇빛횟수 초기화
            gameDto.setSuncnt(0);
            //비료주기 횟수 초기화
            gameDto.setPoocnt(0);
            //벌레주기 횟수 초기화
            gameDto.setBugcnt(0);
            //화분갈이 횟수 초기화
            gameDto.setPotcnt(0);
            //음악틀어주기 횟수 초기화
            gameDto.setMusiccnt(0);
            //영양제주기 횟수 초기화
            gameDto.setNutritioncnt(0);
         }
         else {
            gameDto.setExp(newExp);   //최대 경험치를 넘지 않는 경우, 새로운 경험치 값을 현재 경험치를 설정합니다.
         }
         // 화분주기 쿨타임 초기화
         gameDto.setPushTime(System.currentTimeMillis());
         // 업데이트된 게임 정보를 저장
         gameDao.GameUpdate(gameDto);
      }
   }
   public int potTime(String user_id){
      int defaultPotTIme = 1;
      int potTime = defaultPotTIme;

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      //게임 정보가 존재하고, 화분갈이 횟수와 쿨타임을 만족하는 경우에만 화분갈이 시간을 업데이트
      if(gameDto != null && gameDto.getPotcnt() < 10 && (System.currentTimeMillis() - gameDto.getPushTime())
            >= defaultPotTIme * 1000){
         //현재 시간과 마지막 화분갈이 시간의 차이를 계산하여 경과 시간을 얻는다.
         long elapsedTime = System.currentTimeMillis() - gameDto.getPushTime();
         // 기본 화분갈이 시간에서 경과 시간을 빼서 남은 시간을 계산한다.
         // 최소값을 0으로 설정하여 음수 값이 되지 않도록 한다.
         potTime =(int)Math.max(0, defaultPotTIme - (elapsedTime / 1000));
         //게임정보 업데이트
         gameDao.GameUpdate(gameDto);
      }
      return potTime;
   }
   public int potCount(String user_id){
      int potCount = 10;
      int potExp = 20;

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      //게임 정보가 존재하고,화분갈이 횟수와 쿨타임을 만족하는 경우에만 화분갈이 수행
      if(gameDto != null && gameDto.getPotcnt() < 10 && (System.currentTimeMillis() - gameDto.getPushTime())
            >= 1 * 1000){
         //레벨 업 조건 확인
         int newExp = gameDto.getExp() + potExp;   //새로운 경험치 계산
         int maxExp = LevelMaxExp(gameDto.getG_level()); //현재 레벨의 최대 경험치

         if(newExp >= maxExp){
            //레벨 업 조건을 만족할 때 의 처리
            gameDto.setG_level(gameDto.getG_level() + 1); //레벨 업
            gameDto.setExp(0);   //경험치 초기화
            gameDto.setPotcnt(0);   //화분갈이 초기화

            //화분갈이 횟수가 초기화되므로 데이터베이스에 업데이트
            gameDao.GameUpdate(gameDto);
         }
         else{
            gameDto.setExp(newExp);  //경험치 업데이트
            gameDto.setPotcnt(gameDto.getPotcnt() + 0);   //화분갈이 횟수 증가
         }
      }
      return gameDto.getPotcnt();
   }
   public void musicPlant(String user_id) {
      int musicCount = 10;   //음악듣기 횟수
      int musicTime = 1;   //음악듣기 시간
      int musicExp = 60;      //음악듣기 경험치

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      //게임 정보가 존재하고, 음악틀어주기 횟수와 쿨타임을 만족하는 경우에만 음악틀어주기 수행
      if(gameDto != null && gameDto.getMusiccnt() < musicCount &&
            (System.currentTimeMillis() - gameDto.getPushTime()) >= musicTime * 1000) {
         // 음악 틀어주기 수행
         gameDto.setMusiccnt(gameDto.getMusiccnt() + 1);   //음악틀어주기 횟수 증가
         int newExp = gameDto.getExp() + musicExp; //새로운 경험치 값을 계산

         //최대 경험치를 넘어가면 레벨 업 및 경험치 초기화
         if(newExp >= LevelMaxExp(gameDto.getG_level())) {
            //레벨 업
            gameDto.setG_level(gameDto.getG_level() + 1);
            //경험치 초기화
            gameDto.setExp(0);
            // 물주기 횟수 초기화
            gameDto.setWatercnt(0);
            // 사랑주기 횟수 초기화
            gameDto.setLovecnt(0);
            //햇빛횟수 초기화
            gameDto.setSuncnt(0);
            //비료주기 횟수 초기화
            gameDto.setPoocnt(0);
            //벌레주기 횟수 초기화
            gameDto.setBugcnt(0);
            //화분갈이 횟수 초기화
            gameDto.setPotcnt(0);
            //음악틀어주기 횟수 초기화
            gameDto.setMusiccnt(0);
            //영양제주기 횟수 초기화
            gameDto.setNutritioncnt(0);
         }
         else {
            gameDto.setExp(newExp);
         }
         //음악틀어주기 쿨타임 초기화
         gameDto.setPushTime(System.currentTimeMillis());
         // 업데이트된 게임 정보를 저장
         gameDao.GameUpdate(gameDto);
      }
   }
   public int musicTime(String user_id){
      int defaultMusicTime =   1;
      int musicTime = defaultMusicTime;

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      //게임 정보가 존재하고,음악틀어주기 횟수와 쿨타임을 만족하는 경우에만 음악틀어주기 시간을 업데이트
      if(gameDto != null && gameDto.getMusiccnt() < 10 && (System.currentTimeMillis() - gameDto.getPushTime())
            >= defaultMusicTime * 1000){
         //현재 시간과 마지막 음악틀어주기 시간의 차이를 계산하여 경과 시간을 얻는다.
         long elapsedTime = System.currentTimeMillis() - gameDto.getPushTime();
         //기본 음악틀어주기 시간에서 경과 시간을 빼서 남은 시간을 계산한다.
         //최소값을 0으로 설정하여 음수 값이 되지 않도록 한다.
         musicTime =(int)Math.max(0, defaultMusicTime - (elapsedTime / 1000));
         //게임 정보 업데이트
         gameDao.GameUpdate(gameDto);
      }
      return musicTime;
   }
   public int musicCount(String user_id){
      int musicCount = 10;
      int musicExp = 60;

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      //게임 정보가 존재하고, 음악틀어주기 횟수와 쿨타임을 만족하는 경우에만 음악틀어주기 수행
      if(gameDto != null && gameDto.getMusiccnt() < 10 && (System.currentTimeMillis() - gameDto.getPushTime())
            >= 1 * 1000){
         //레벨 업 조건 확인
         int newExp = gameDto.getExp() + musicExp;
         int maxExp = LevelMaxExp(gameDto.getG_level());

         if(newExp >= maxExp){
            //레벨 업 조건을 만족할 때 의 처리
            gameDto.setG_level(gameDto.getG_level() + 1);
            gameDto.setExp(0);
            gameDto.setMusiccnt(0);

            gameDao.GameUpdate(gameDto);
         }
         else{
            gameDto.setExp(newExp);
            gameDto.setMusiccnt(gameDto.getMusiccnt() + 0);
         }
      }
      return gameDto.getMusiccnt();
   }
   public void nutritionPlant(String user_id) {
      int nutritionCount = 10;
      int nutritionTime = 1;
      int nutritionExp = 120;

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      //게임 정보가 존재하고, 영양제주기 횟수와 쿨타임을 만족하는 경우에만 영양제주기 수행
      if(gameDto != null && gameDto.getNutritioncnt() < nutritionCount &&
            (System.currentTimeMillis() - gameDto.getPushTime()) >= nutritionTime * 1000) {
         //영양제 주기 수행
         gameDto.setNutritioncnt(gameDto.getNutritioncnt() + 1); //영양제주기 횟수 증가
         int newExp = gameDto.getExp() + nutritionExp; //새로운 경험치 값을 계산

         //최대 경험치를 넘어가면 레벨 업 및 경험치 초기화
         if(newExp >= LevelMaxExp(gameDto.getG_level())) {
            //레벨 업
            gameDto.setG_level(gameDto.getG_level() + 1);
            // 경험치 초기화
            gameDto.setExp(0);
            // 물주기 횟수 초기화
            gameDto.setWatercnt(0);
            // 사랑주기 횟수 초기화
            gameDto.setLovecnt(0);
            //햇빛횟수 초기화
            gameDto.setSuncnt(0);
            //비료주기 횟수 초기화
            gameDto.setPoocnt(0);
            //벌레주기 횟수 초기화
            gameDto.setBugcnt(0);
            //화분갈이 횟수 초기화
            gameDto.setPotcnt(0);
            //음악틀어주기 횟수 초기화
            gameDto.setMusiccnt(0);
            //영양제주기 횟수 초기화
            gameDto.setNutritioncnt(0);
         }
         else {
            gameDto.setExp(newExp);
         }
         //영양제주기 쿨타임 초기화
         gameDto.setPushTime(System.currentTimeMillis());
         // 업데이트된 게임 정보를 저장
         gameDao.GameUpdate(gameDto);
      }
   }
   public int nutritionTime(String user_id){
      int defaultNutritionTime = 1;
      int nutritionTime = defaultNutritionTime;

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      //게임 정보가 존재하고, 영양제주기 횟수와 쿨타임을 만족하는 경우에만 영양제주기 시간을 업데이트
      if(gameDto != null && gameDto.getNutritioncnt() < 10 && (System.currentTimeMillis() - gameDto.getPushTime())
            >= defaultNutritionTime * 1000){
         //현재 시간과 마지막 영양제주기 사간의 차이를 계산하여 경과 시간을 얻는다.
         long elapsedTime = System.currentTimeMillis() - gameDto.getPushTime();
         // 기본 영양제주기 시간에서 경과 시간을 빼서 남은 시간을 계산한다.
         // 최소값을 0으로 설정하여 음수 값이 되지 않도록 한다.
         nutritionTime = (int)Math.max(0, defaultNutritionTime - (elapsedTime / 1000));
         //게임 정보 업데이트
         gameDao.GameUpdate(gameDto);
      }
      return nutritionTime;
   }
   public int nutritionCount(String user_id){
      int nutritionCount = 10;
      int nutritionExp = 120;

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      //게임 정보가 존재하고, 영양제주기 횟수와 쿨타임을 만족하는 경우에만 영양제주기 수행
      if(gameDto != null && gameDto.getNutritioncnt() < 10 && (System.currentTimeMillis() -  gameDto.getPushTime())
            >= 1 * 1000){
         //레벨 업 조건 확인
         int newExp =  gameDto.getExp() + nutritionExp;   //새로운 경험치 계산
         int maxExp = LevelMaxExp(gameDto.getG_level());   //현재 레벨의 최대 경험치

         if(newExp >= maxExp){
            //레벨 업 조건을 만족할 때 의 처리
            gameDto.setG_level(gameDto.getG_level() + 1);   //레벨 업
            gameDto.setExp(0);   //경험치 초기화
            gameDto.setNutritioncnt(0);   //영양제주기 초기화

            //영양제주기 횟수가 초기화되므로 데이터베이스 업데이트
            gameDao.GameUpdate(gameDto);
         }
         else{
            gameDto.setExp(newExp); //경험치 업데이트
            gameDto.setNutritioncnt(gameDto.getNutritioncnt() + 0);   //영양제주기 횟수 증가
         }
      }
      return gameDto.getNutritioncnt();
   }


   @Override
   public int ProgressValue(String user_id) {

      // 게임 정보를 가져옴
      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      gameDto = gameDao.GamefindByUserId(user_id);   // 사용자의 경험치를 증가시켰으므로 해당 사용자의 최신 정보를 가져옴

      // 사용자의 경험치와 최대 경험치를 계산하여 진행도 값을 반환
      int exp = gameDto.getExp(); //사용자 경험치
      int maxExp = LevelMaxExp(gameDto.getG_level()); //해당 레벨의 최대 경험치를 계산
      int progressValue = (int) Math.floor((double)exp/maxExp * 100); //경험치 비율을 백분율로 계산

      return progressValue;
   }
   public int userLevel(String user_id){

      GameDto gameDto = gameDao.GamefindByUserId(user_id);

      return gameDto.getG_level();
   }
   public GameDto getCharacterNoInfo(String userId){
      GameDto characterInfo = new GameDto();
      characterInfo.setCharacter_no(userId);

      return characterInfo;
   }
   private int LevelMaxExp(int level) { // 해당 레벨의 최대 경험치를 계산하는 메서드
      switch(level) {
         case 1:
            return 15;
         case 2:
            return 60;
         case 3:
            return 360;
         case 4:
            return 2160;
         default:
            return 0;
      }
   }
}