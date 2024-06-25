
/* 유저의 레벨를 갖고와 버튼 여부 활성화 */
/*===  Main Game : Level === */
document.addEventListener("DOMContentLoaded",function(){
   var userIdInput = document.getElementById("gameId");
   var LevelInput = document.getElementById("gameLevel");

   function getUserLevel(userId) {
      $.ajax({
         type: "GET",
         url: "/getuserlevel",
         data: {
            userId: userId
         },
         dataType:"json",
         success: function(response) {
            var userLevel = response.g_level;
            var imageUrl = response.imageUrl;
            LevelInput.value = userLevel;
            enableButtonLevel(userLevel);
            
            updateImage(imageUrl);  // 이미지 업데이트 함수 호출하여 이미지 변경

            // location.reload();
         },
         
         error: function(xhr, status, error) {
            console.error("레벨을 가져오는중 오류가 있습니다.",error);
         }
      });
   }
  function updateImage(imageUrl){
       var timestamp = new Date().getTime(); // 현재 시간의 타임스탬프 생성
       imageUrl += "?timestamp=" + timestamp; // 이미지 URL에 타임스탬프 추가
       $('#characterImage').attr('src', imageUrl);
       console.log("URL:", imageUrl)
   }

  // 레벨에 따라 버튼 활성화
  function enableButtonLevel(userLevel, imageUrl){
     var allButtons = ["water-button", "love-button", "sun-button", "poo-button", "bug-button", "pot-button", "music-button", "nutrition-button"];
       
       // 모든 버튼 비활성화
       allButtons.forEach(function(buttonId){
           var button = document.getElementById(buttonId);
           if(button){
               button.disabled = true;
           }
       });

       if(userLevel >= 5){
           return;
       }

       var buttonTable = [];
       var imageUrl;

       if(userLevel >= 1){
            
           buttonTable.push("water-button", "love-button");
       }
       if(userLevel >= 2){
         
           buttonTable.push("sun-button", "poo-button");
       }
       if(userLevel >= 3){
         buttonTable.push("bug-button", "pot-button");
       }
       if(userLevel >= 4){
           buttonTable.push("music-button", "nutrition-button");
       }

       // 버튼 활성화
       buttonTable.forEach(function(buttonId){
           var button = document.getElementById(buttonId);
           if(button){
               button.disabled = false;
           }
       });
       updateImage(imageUrl);  // 이미지 업데이트 함수 호출하여 이미지 변경
   }

   if(userIdInput && userIdInput.value){
       getUserLevel(userIdInput.value);
   } else {
       console.error("User ID not found.");
   }
});





// ----------------------------------------------------------------------------------------




/* 물주기 버튼 기능() */
/* === water === */
// water
document.addEventListener("DOMContentLoaded",function(){
var waterTime = 1;    // 초기에 물 주는 시간
var waterCount = 5;    // 초기 물 주기 횟수
var loveCount = 5;
var sunCount = 5;
var pooCount = 5;
var bugCount = 10;
var potCount =  10;   
var musicCount = 10;
var nutritionCount = 10;
var waterTimerInterval; // 타이머 변수
var startWaterTime;
var timerInterval;
var userIdInput = document.getElementById("water_id");
var userId = '';
var LevelInput = document.getElementById("water_level");


if (userIdInput !== null) {
   userId = userIdInput.value; // userIdInput이 null이 아닌 경우에만 value 속성을 읽음
   console.log("User ID:", userId);
}
else{
   console.error("User ID not found.");
}

function waterPlant() {
   var userLevel = parseInt(LevelInput.value, 10);
   
   if(isNaN(userLevel) || userLevel < 1){
      return;
   }
   if(userLevel >= 5){
      alert("최고 레벨에 도달하였습니다.");
      return;
   }

   disabledWaterButton();

    // 시작시간 설정
    startWaterTime = new Date().getTime();
    // 타이머 시작(1초마다 갱신)
    waterTimerInterval = setInterval(updateWaterTimer, 1000);

    if(waterCount === 0){
      // 레벨이 올랐을 때 알림 표시
      alert("축하합니다! 레벨이 올랐습니다.");
    }
    // 레벨이 올랐을 때 알림 표시
    enableWaterButton(userLevel, waterCount, loveCount, sunCount, pooCount, bugCount, potCount, musicCount, nutritionCount);
}

function updateImage(imageUrl){
   var timestamp = new Date().getTime(); // 현재 시간의 타임스탬프 생성
   imageUrl += "?timestamp=" + timestamp; // 이미지 URL에 타임스탬프 추가
   $('#characterImage').attr('src', imageUrl);
   console.log("URL:", imageUrl);
}


function updateWaterTimer() {
   
    // 현재시간
    var currentTime = new Date().getTime();
    // 경과시간
    var elapsedTime = Math.floor((currentTime - startWaterTime) / 1000);
    // 남은시간 표시
    document.getElementById("water-time").innerText = formatWaterTime(waterTime - elapsedTime);

    // 30초 경과후 버튼 활성화
    if(elapsedTime >= 1) {
        clearInterval(waterTimerInterval);
      var userLevel = parseInt(LevelInput.value, 10);
        enableWaterButton(userLevel, waterCount, loveCount, sunCount, pooCount, bugCount, potCount, musicCount, nutritionCount);

        $.ajax({
            type: "post",
            async:false,
            url: "/waterPlant",
            data: {
                user_id: userId
            },
            success: function(data,textStatus) {
                var response = JSON.parse(data);
                var userLevel = response.waterLevel;
                var char_no = response.char_no;
                var imageUrl = "img/game_character/seed-"+ char_no +"_lv_" + userLevel + ".png";
                updateImage(imageUrl);
               
                // 서버로부터의 응답을 받아와서 프로그레스바 업데이트
                updateProgressBar(response.progressValue);
                // 물주기 횟수를 업데이트합니다.
                updateWaterCount(response.WaterCount);
                updateLoveCount(response.LoveCount);
                updateSunCount(response.SunCount);
                updatePooCount(response.PooCount);
                updateBugCount(response.BugCount);
                updatePotCount(response.PotCount);
                updateMusicCount(response.MusicCount);
                updateNutritionCount(response.NutritionCount);
                LevelInput.value = userLevel;

                // 성공 메시지 출력
               //  alert("물주기가 성공적으로 수행되었습니다.");
            enableWaterButton(userLevel,
               response.WaterCount,
               response.LoveCount,
               response.SunCount,
               response.PooCount,
               response.BugCount,
               response.PotCount,
               response.MusicCount,
               response.NutritionCount);               
            },
            error: function(data, status, error) {
                // 오류 발생 시 메시지 출력
                console.error("물주기 오류:", error);
                alert("물주기 수행 중 오류가 발생하였습니다.");
            console.log(userLevel);
            },
        });

        }
}

function disabledWaterButton(){
   document.getElementById("water-button").disabled = true;
   document.getElementById("love-button").disabled = true;
   document.getElementById("sun-button").disabled = true;
   document.getElementById("poo-button").disabled = true;
   document.getElementById("bug-button").disabled = true;
   document.getElementById("pot-button").disabled = true;
   document.getElementById("music-button").disabled = true;
   document.getElementById("nutrition-button").disabled = true;
}

function enableWaterButton(userLevel, waterCount, loveCount, sunCount, pooCount, 
   bugCount, potCount, musicCount, nutritionCount){
   console.log("활성화 레벨:",userLevel,"물주기 횟수:",waterCount);
	
    // 레벨이 1 이상이고 물주기 횟수가 5보다 작을 때 물주기 버튼을 활성화
    if(userLevel >= 1 && waterCount < 5 && userLevel < 5){
        document.getElementById("water-button").disabled = false;
    } else {
        document.getElementById("water-button").disabled = true;
    }
    // 레벨이 1 이상일 때 사랑주기 버튼을 활성화
    if(userLevel >= 1 && loveCount < 5 && userLevel < 5){
        document.getElementById("love-button").disabled = false;
    } else {
        document.getElementById("love-button").disabled = true;
    }
   //레벨이 2 이상일 때 햇빛주기 버튼을 활성화
   if(userLevel >= 2 && sunCount < 5 && userLevel < 5){
      document.getElementById("sun-button").disabled = false;
   }
   else{
      document.getElementById("sun-button").disabled = true;
   }
   //레벨이 2 이상일 때 비료주기 버튼을 활성화
   if(userLevel >= 2 && pooCount < 5 && userLevel < 5){
      document.getElementById("poo-button").disabled = false;
   }
   else{
      document.getElementById("poo-button").disabled = true;
   }
   if(userLevel >= 3 && bugCount < 10 && userLevel < 5){
      document.getElementById("bug-button").disabled = false;
   }
   else{
      document.getElementById("bug-button").disabled = true;
   }
   if(userLevel >= 3 && potCount < 10 && userLevel < 5){
      document.getElementById("pot-button").disabled = false;
   }
   else{
      document.getElementById("pot-button").disabled = true;
   }
   if(userLevel >= 4 && musicCount < 10 && userLevel < 5){
      document.getElementById("music-button").disabled = false;
   }
   else{
      document.getElementById("music-button").disabled = true;
   }
   if(userLevel >= 4 && nutritionCount < 10 && userLevel < 5){
      document.getElementById("nutrition-button").disabled = false;
   }
   else{
      document.getElementById("nutrition-button").disabled = true;
   }

   if(userLevel >= 5){
      alert("축하합니다! 최고레벨에 도달하였습니다.");
      return;
   }
}
function formatWaterTime(seconds) {
    // 주어진 초를 분으로 변환하여 정수 부분을 구함
    var minutes = Math.floor(seconds / 60);
    // 초를 60으로 나눈 나머지를 구하여 남은 초를 계산
    var remainingSeconds = seconds % 60;
    // 변환된 시간을 "00:00" 형식으로 반환하기 위해 패딩 함수를 사용하여 처리
    return padWater(minutes) + ":" + padWater(remainingSeconds);
}

function padWater(number) {
    // 주어진 숫자가 10보다 작으면 숫자 앞에 0을 붙여 반환하고, 그렇지 않으면 반환
    return (number < 10 ? "0" : "") + number;
}

// 시간을 1초마다 감소시키고 업데이트하는 함수
function decreaseWaterTime() {
   // console.log("decreaseTime 함수가 호출되었습니다"); // 콘솔에 함수 호출 메시지 출력
    clearInterval(timerInterval); // 이전 타이머가 있으면 제거하여 중복 실행 방지
    // setInterval 함수를 사용하여 1초마다 실행되는 타이머를 설정
    timerInterval = setInterval(function() {
        // waterTime 변수를 1씩 감소시킴
        waterTime--;
        // 시간을 업데이트하는 함수를 호출하여 화면에 남은 시간 표시
        updateWaterTime();
     //   console.log("남은 시간:", waterTime); // 콘솔에 남은 시간 출력
        // 시간이 0 이하가 되면 타이머를 중지
        if (waterTime <= 0) {
            clearInterval(timerInterval);
        }
    }, 1000); // 1초마다 실행
}

// 화면에 남은 시간을 업데이트하는 함수
function updateWaterTime() {
    // 'water-time' id를 가진 요소를 찾음
    var WaterTimeElement = document.getElementById('water-time');
    
    // 요소가 존재하는지 확인
    if (WaterTimeElement) {
        // 남은 시간을 분과 초로 변환하여 계산
        var minutes = Math.floor(waterTime / 60); // 분
        var seconds = waterTime % 60; // 초

        // 시간을 화면에 표시
        WaterTimeElement.innerText = "물주기 " + (minutes < 10 ? '0' : '') +
            minutes + ":" + (seconds < 10 ? '0' : '') + seconds + " 횟수:" + waterTime;

        // 남은 시간이 0 이하이면 시간 요소를 숨김
        if (waterTime <= 0) {
            WaterTimeElement.style.display = 'none';
        } else {
            WaterTimeElement.style.display = ''; // 시간 요소를 표시
        }
    }
}

function startWaterTimer(time){
    waterTime = time; //서버에서 받은 물주기 시간으로 설정
    decreaseWaterTime(); //타이머 시작
}

//물주기 횟수 업데이트
function updateWaterCount(count){
    var waterButton = document.getElementById("water-button");
   // 물주기 횟수를 화면에 업데이트
    var waterCountElement = document.getElementById("water-count");

    if(waterCountElement){
        waterCountElement.innerText = "횟수: " + count;
        if(count >= 5){

            if(waterButton){
                waterButton.disabled = true;
            }
            alert("물주기 횟수가 다 되었습니다.");
        }
        else{
            var waterButton = document.getElementById("water-button");
            if(waterButton){
                waterButton.disabled = false;
            }
        }
    }
    console.log("물주기 횟수:",count);
}

function updateLoveCount(count){
   var loveCountElement = document.getElementById("love-count");
   if(loveCountElement){
      loveCountElement.innerText = "횟수: " + count;
   }
}

function updateSunCount(count){
   var sunCountElement = document.getElementById("sun-count");
   if(sunCountElement){
      sunCountElement.innerText = "횟수: " + count;
   }
}

function updatePooCount(count){
   var pooCountElement = document.getElementById("poo-count");
   if(pooCountElement){
      pooCountElement.innerText = "횟수: " + count;
   } 
}

function updateBugCount(count){
   var bugCountElement = document.getElementById("bug-count");
   if(bugCountElement){
      bugCountElement.innerText = "횟수: " + count;
   }
}

function updatePotCount(count){
   var potCountElement = document.getElementById("pot-count");
   if(potCountElement){
      potCountElement.innerText = "횟수: " + count;
   }
}

function updateMusicCount(count){
   var musicCountElement = document.getElementById("music-count");
   if(musicCountElement){
      musicCountElement.innerText = "횟수: " + count;
   }
}

function updateNutritionCount(count){
   var nutritionCountElement = document.getElementById("nutrition-count");
   if(nutritionCountElement){
      nutritionCountElement.innerText = "횟수: " + count;
   }
}

function updateProgressBar(progressValue) {
    var progressBar = document.querySelector('#bar');
   
    if (progressBar) {
        // progressValue 값에 따라 프로그레스 바의 너비를 조정
        progressBar.style.width = progressValue + "%";
    } else {
        console.error("progress bar element not found");
    }
}

document.getElementById("water-button").addEventListener("click", function(){
   waterPlant();
});
//    function updateWaterImage(userLevel){
//       var imageUrl;
     
//       if (userLevel == 1){
//          imageUrl = 'img/myplant/Lv_1.png';
//       }
//       if (userLevel == 2){
//          imageUrl = 'img/myplant/Lv_2.png';
//       }
//       if(userLevel == 3){
//          imageUrl = 'img/myplant/Lv_3.png';
//       }
//       if(userLevel == 4){
//          imageUrl = 'img/myplant/Lv_4.png';
//       }
//    // 이미지 업데이트
//     var timestamp = new Date().getTime();
//     imageUrl += "?timestamp=" + timestamp;
//     document.getElementById("characterImage").src = imageUrl;

    
//   }
});



/* 사랑주기 버튼 기능 */
/*===  Main Game : Love === */
//Love
document.addEventListener("DOMContentLoaded",function(){
var loveTime = 1;   //초기에 사랑을 주는 시간
var waterCount = 5;
var loveCount = 5;   //초기 사랑 주기 횟수
var sunCount = 5;
var pooCount = 5;
var bugCount = 10;
var potCount = 10;
var musicCount = 10;
var nutritionCount = 10;
var loveTimerInterval;   //타이머 변수
var startLoveTime;
var timerInterval;   //타이머 변수 추가
var userIdInput = document.getElementById("love_id");
var userId = '';
var LevelInput = document.getElementById("love_level");


if (userIdInput !== null) {
   userId = userIdInput.value; // userIdInput이 null이 아닌 경우에만 value 속성을 읽음
   console.log("User ID:", userId);
}
else{
   console.error("User ID not found.");
}


function lovePlant(){ 
   var userLevel = parseInt(LevelInput.value, 10);
   if(isNaN(userLevel) || userLevel < 1){
      return;
   }
   if(userLevel >= 5){
      alert("최고 레벨에 도달하였습니다.")
   }

   //시작시간 설정
   startLoveTime = new Date().getTime();
   //타이머 시작(1초마다 갱신)
   loveTimerInterval = setInterval(updateLoveTimer, 1000);

   if(loveCount === 0){
      alert("축하합니다! 레벨이 올랐습니다.");
           
      disabledLoveButton();   
    }
    //레벨이 올랐을때 알림 표시
    enableLoveButton(userLevel,waterCount, loveCount, sunCount, pooCount, bugCount, potCount, musicCount, nutritionCount);

}

function updateImage(imageUrl){
   var timestamp = new Date().getTime(); // 현재 시간의 타임스탬프 생성
   imageUrl += "?timestamp=" + timestamp; // 이미지 URL에 타임스탬프 추가
   $('#characterImage').attr('src', imageUrl);
   console.log("URL:", imageUrl)
}


function updateLoveTimer(){
   
   // 현재시간
   var currentTime = new Date().getTime();
   // 경과시간
   var elapsedtime = Math.floor((currentTime - startLoveTime) / 1000);
   // 남은시간 표시
   document.getElementById("love-time").innerText = formatLoveTime(loveTime - elapsedtime);
   
   // 1분 경과후 버튼 활성화
   if(elapsedtime >= 1){
      clearInterval(loveTimerInterval);
      var userLevel = parseInt(LevelInput.value, 10);
      enableLoveButton(userLevel,waterCount, loveCount, sunCount, pooCount, bugCount, potCount, musicCount, nutritionCount);
   $.ajax({
   type: "POST",
   async:false,
    url: "/lovePlant",
    data:{
       user_id: userId
    },
    success: function(data,textStatus){
      var response = JSON.parse(data);
      var userLevel = response.loveLevel;
      var char_no = response.char_no;
      var imageUrl = "img/game_character/seed-"+ char_no +"_lv_" + userLevel + ".png";
       updateImage(imageUrl);
       // 서버로부터의 응답을 받아와서 프로그레스바 업데이트
       updateProgressBar(response.progressValue);
       // 사랑주기 횟수를 업데이트합니다.
       updateLoveCount(response.LoveCount);
       LevelInput.value = userLevel;
       updateWaterCount(response.WaterCount);
       updateSunCount(response.SunCount);
       updatePooCount(response.PooCount);
       updateBugCount(response.BugCount);
       updatePotCount(response.PotCount);
       updateMusicCount(response.MusicCount);
       updateNutritionCount(response.NutritionCount);


        //성공 메시지 출력
      //   alert("사랑주기가 성공적으로 수행되었습니다.");
       enableLoveButton(userLevel,
                    response.WaterCount,
                    response.LoveCount,
                    response.SunCount,
                    response.PooCount,
                    response.BugCount,
                    response.PotCount,
                    response.MusicCount,
                    response.NutritionCount);
    },
    error: function(data, status, error){
       // 오류 발생 시 메시지 출력
       console.error("사랑주기 오류:", error);
       alert("사랑주기 수행 중 오류가 발생했습니다.");
       console.log(LevelInput.value);
       console.log(userId);
    },
 });
}   
}

function disabledLoveButton(){
   document.getElementById("water-button").disabled = true;
   document.getElementById("love-button").disabled = true;
   document.getElementById("sun-button").disabled = true;
   document.getElementById("poo-button").disabled = true;
   document.getElementById("bug-button").disabled = true;
   document.getElementById("pot-button").disabled = true;
   document.getElementById("music-button").disabled = true;
   document.getElementById("nutrition-button").disabled = true;
}
//버튼 활성화
function enableLoveButton(userLevel,waterCount, loveCount, sunCount, pooCount, 
   bugCount, potCount, musicCount, nutritionCount){
   console.log("활성화 레벨:",userLevel,"사랑주기 횟수:",loveCount);
   
    
    // 레벨이 1 이상이고 물주기 횟수가 5보다 작을 때 물주기 버튼을 활성화
    if(userLevel >= 1 && waterCount < 5 && userLevel < 5){
        document.getElementById("water-button").disabled = false;
    } else {
        document.getElementById("water-button").disabled = true;
    }
    // 레벨이 1 이상일 때 사랑주기 버튼을 활성화
    if(userLevel >= 1 && loveCount < 5 && userLevel < 5){
        document.getElementById("love-button").disabled = false;
    } else {
        document.getElementById("love-button").disabled = true;
    }
   //레벨이 2 이상일 때 햇빛주기 버튼을 활성화
   if(userLevel >= 2 && sunCount < 5 && userLevel < 5){
      document.getElementById("sun-button").disabled = false;
   }
   else{
      document.getElementById("sun-button").disabled = true;
   }
   //레벨이 2 이상일 때 비료주기 버튼을 활성화
   if(userLevel >= 2 && pooCount < 5 && userLevel < 5){
      document.getElementById("poo-button").disabled = false;
   }
   else{
      document.getElementById("poo-button").disabled = true;
   }
   if(userLevel >= 3 && bugCount < 10 && userLevel < 5){
      document.getElementById("bug-button").disabled = false;
   }
   else{
      document.getElementById("bug-button").disabled = true;
   }

   if(userLevel >= 3 && potCount < 10 && userLevel < 5){
      document.getElementById("pot-button").disabled = false;
   }
   else{
      document.getElementById("pot-button").disabled = true;
   }
   
   if(userLevel >= 4 && musicCount < 10 && userLevel < 5){
      document.getElementById("music-button").disabled = false;
   }
   else{
      document.getElementById("music-button").disabled = true;
   }
   if(userLevel >= 4 && nutritionCount < 10 && userLevel < 5){
      document.getElementById("nutrition-button").disabled = false;
   }
   else{
      document.getElementById("nutrition-button").disabled = true;
   }

   if(userLevel >= 5){
      alert("축하합니다! 최고레벨에 도달하였습니다.");
      return;
   }
}
function formatLoveTime(seconds){
   // 주어진 초를 분으로 변환하여 정수 부분을 구함
   var minutes = Math.floor(seconds / 60);
   // 초를 60으로 나눈 나머지를 구하여 남은 초를 계산
   var remainingSeconds = seconds % 60;
   // 변환된 시간을 00:00 형식으로 반환하기 위해 패딩 함수를 사용하여 처리
   return padLove(minutes) + ":" + padLove(remainingSeconds);
}

function padLove(number){
   // 주어진 숫자가 10보다 작으면 숫자 앞에 0을 붙여 반환하고, 그렇지 않으면 반환
   return (number < 10 ? "0" : "") + number;
}

// 시간을 1초마다 감소시키고 업데이트 함수
function decreaseLoveTime(){
   clearInterval(timerInterval) //이전 타이머가 있으면 제거하여 중복 실행 방지
   //setInterval 함수를 사용하여 1초마다 실행되는 타이머를 설정
   timerInterval = setInterval(function(){
      // loveTime 변수를 1씩 감소시킴
      loveTime --;
      // 시간을 업데이트하는 함수를 호출하여 화면에 남은 시간 표시
      updateLoveTime();
      // 시간이 0 이하가 되면 타이머를 중지
      if(loveTime <= 0){
         clearInterval(timerInterval);
      }
   }, 1000); //1초마다 실행
}

// 화면에 남은 시간을 업데이트하는 함수
function updateLoveTime(){
   var LoveTimeElement = document.getElementById('love-time');

   //요소가 존재하는지 확인
   if(LoveTimeElement){
      //남은 시간을 분과 초로 변환하여 계산
      var minutes = Math.floor(loveTime / 60); //분
      var seconds = loveTime % 60; //초

      //시간을 화면에 표시
      LoveTimeElement.innerText = "사랑주기 " + (minutes < 10 ? '0' : '') +
         minutes + ":" + (seconds < 10 ? '0' : '') + seconds + "횟수:" + loveTime;
      // 남은 시간이 0 이하이면 시간 요소를 숨김
      if(loveTime <= 0){
         LoveTimeElement.style.display = 'none';
      }   
      else{
         LoveTimeElement.style.display = ''; 
      }
   }
}

function startLoveTime(time){
   loveTime = time * 60;
   //서버에서 받은 사랑주기 시간으로 변경
   decreaseLoveTime();
}

//사랑주기 횟수 업데이트
function updateLoveCount(count){
   // 사랑주기 횟수를 화면에 업데이트
   var loveCountElement = document.getElementById("love-count");


   //console.log("사랑 초기값: " + LoveCount)
   if(loveCountElement){
      loveCountElement.innerText = "횟수: " + count;
      if(count >= 5){
         var loveButton = document.getElementById("love-button");
         if(loveButton){
            loveButton.disabled = true; // 버튼 비활성화
         }
          alert("사랑주기 횟수가 다 되었습니다.");
      }
      else{
		if (loveButton) {
			loveButton.disabled = false;
		}
	}
}
console.log("사랑주기 횟수:", count);
}

function updateWaterCount(count){
   var waterCountElement = document.getElementById("water-count");
   if(waterCountElement){
      waterCountElement.innerText = "횟수: " + count;
   }
}

function updateSunCount(count){
   var sunCountElement = document.getElementById("sun-count");
   if(sunCountElement){
      sunCountElement.innerText = "횟수: " + count;
   }
}

function updatePooCount(count){
   var pooCountElement = document.getElementById("poo-count");
   if(pooCountElement){
      pooCountElement.innerText = "횟수: " + count;
   } 
}

function updateBugCount(count){
   var bugCountElement = document.getElementById("bug-count");
   if(bugCountElement){
      bugCountElement.innerText = "횟수: " + count;
   }
}

function updatePotCount(count){
   var potCountElement = document.getElementById("pot-count");
   if(potCountElement){
      potCountElement.innerText = "횟수: " + count;
   }
}

function updateMusicCount(count){
   var musicCountElement = document.getElementById("music-count");
   if(musicCountElement){
      musicCountElement.innerText = "횟수: " + count;
   }
}

function updateNutritionCount(count){
   var nutritionCountElement = document.getElementById("nutrition-count");
   if(nutritionCountElement){
      nutritionCountElement.innerText = "횟수: " + count;
   }
}


function updateProgressBar(progressValue){
   var progressBar = document.querySelector('#bar');
   
   //console.log(progressBar)
   
   if(progressBar){
      progressBar.style.width = progressValue + "%";
   }
   else{
      console.error("progress bar element not found");
   }
}
//사랑주기 버튼 클릭 이벤트 리스너 추가
document.getElementById("love-button").addEventListener("click", function(){
   lovePlant();
});
// function updateLoveImage(userLevel){
//    var imageUrl;

//       if (userLevel >= 1){
//          imageUrl = 'img/myplant/Lv_1.png';
//       }
//       if (userLevel >= 2){
//          imageUrl = 'img/myplant/Lv_2.png';
//       }
//       if(userLevel >= 3){
//          imageUrl = 'img/myplant/Lv_3.png';
//       }
//       if(userLevel >= 4){
//          imageUrl = 'img/myplant/Lv_4.png';
//       }
// // 이미지 업데이트
//     var timestamp = new Date().getTime();
//     imageUrl += "?timestamp=" + timestamp;
//     document.getElementById("characterImage").src = imageUrl;
//    }
   
});

/* 햇빛주기버튼 기능 */
/* === Sun === */
// sun
document.addEventListener("DOMContentLoaded",function(){
var sunTime = 1;   //초기에 햇빛을 주는 시간
var waterCount = 5;
var loveCount = 5;
var sunCount = 5;   // 초기 햇빛 주기 횟수
var pooCount = 5;
var bugCount = 10;
var potCount = 10;
var musicCount = 10;
var nutritionCount = 10;
var sunTimerInterval; // 타이머 변수
var startSunTime;
var timerInterval;
var userIdInput = document.getElementById("sun_id");
var userId = '';
var LevelInput = document.getElementById("sun_level");

if(userIdInput !== null){
   userId = userIdInput.value;   //null이 아닌 경우에만 value값 보여줌
   console.log("User ID:",userId);
}
else{
   console.log("User ID not found");
}


function sunPlant(){
   var userLevel = parseInt(LevelInput.value, 10);
   if(isNaN(userLevel) || userLevel < 1){
      return;
   }
   if(userLevel >= 5){
      alert("최고 레벨에 도달하였습니다.")
   }
   disabledSunButton();
   // 시작시간 설정
   startSunTime = new Date().getTime();
   //타이머 시작(1초마다 갱신)
   sunTimerInterval = setInterval(updateSunTimer, 1000);
}

function updateImage(imageUrl){
   var timestamp = new Date().getTime(); // 현재 시간의 타임스탬프 생성
   imageUrl += "?timestamp=" + timestamp; // 이미지 URL에 타임스탬프 추가
   $('#characterImage').attr('src', imageUrl);
   console.log("URL:", imageUrl)
}


function updateSunTimer(){
   
   //현재시간
   var currentTime = new Date().getTime();
   // 경과시간
   var elapsedtime = Math.floor((currentTime - startSunTime) / 1000);
  
   // 남은시간 표시
   document.getElementById("sun-time").innerText = formatSunTime(sunTime - elapsedtime);
   
   //1분 30초 경과후 버튼 활성화
   if(elapsedtime >= 1){
      clearInterval(sunTimerInterval);
      var userLevel = parseInt(LevelInput.value, 10);
      enableSunButton(userLevel, waterCount, loveCount, sunCount, pooCount, bugCount, potCount, musicCount, nutritionCount);
   $.ajax({
      type: "POST",
      async: false,
      url: "/sunPlant",
      data:{
         user_id: userId
      },
      success: function(data,textStatus){
         var response = JSON.parse(data);
         var userLevel = response.sunLevel;
         var char_no = response.char_no;
      	 var imageUrl = "img/game_character/seed-"+ char_no +"_lv_" + userLevel + ".png";
         updateImage(imageUrl);

         // 서버로부터의 응답을 받아와서 프로그레스바 업데이트
         updateProgressBar(response.progressValue);
         // 햇빛주기 횟수를 업데이트합니다.
         updateSunCount(response.SunCount);
         LevelInput.value = userLevel;
         updateLoveCount(response.LoveCount);
       updateWaterCount(response.WaterCount);
       updatePooCount(response.PooCount);
       updateBugCount(response.BugCount);
       updatePotCount(response.PotCount);
       updateMusicCount(response.MusicCount);
       updateNutritionCount(response.NutritionCount);

         // 성공 메시지 출력
         //alert("햇빛주기가 성공적으로 수행되었습니다.");
         enableSunButton(userLevel,
            response.WaterCount,
            response.LoveCount,
            response.SunCount,
            response.PooCount,
            response.BugCount,
            response.PotCount,
            response.MusicCount,
            response.NutritionCount);
			console.log("햇빛주기 함수 호출");
      },
      error: function(data, status, error){
         // 오류 발생 시 메시지 출력
         console.error("햇빛주기 오류:", error);
         alert("햇빛주기 수행 중 오류가 발생하였습니다.");
      },
   });
  }
}
function disabledSunButton(){
   document.getElementById("water-button").disabled = true;
   document.getElementById("love-button").disabled = true;
   document.getElementById("sun-button").disabled = true;
   document.getElementById("poo-button").disabled = true;
   document.getElementById("bug-button").disabled = true;
   document.getElementById("pot-button").disabled = true;
   document.getElementById("music-button").disabled = true;
   document.getElementById("nutrition-button").disabled = true;
}
//버튼 활성화
function enableSunButton(userLevel,waterCount, loveCount, sunCount, pooCount, 
   bugCount, potCount, musicCount, nutritionCount){
   console.log("활성화 레벨:",userLevel,"햇빛주기 횟수:", sunCount);

    // 레벨이 1 이상이고 물주기 횟수가 5보다 작을 때 물주기 버튼을 활성화
    if(userLevel >= 1 && waterCount < 5 && userLevel < 5){
        document.getElementById("water-button").disabled = false;
    } else {
        document.getElementById("water-button").disabled = true;
    }
    // 레벨이 1 이상일 때 사랑주기 버튼을 활성화
    if(userLevel >= 1 && loveCount < 5 && userLevel < 5){
        document.getElementById("love-button").disabled = false;
    } else {
        document.getElementById("love-button").disabled = true;
    }
   //레벨이 2 이상일 때 햇빛주기 버튼을 활성화
   if(userLevel >= 2 && sunCount < 5 && userLevel < 5){
      document.getElementById("sun-button").disabled = false;
   }
   else{
      document.getElementById("sun-button").disabled = true;
   }
   //레벨이 2 이상일 때 비료주기 버튼을 활성화
   if(userLevel >= 2 && pooCount < 5 && userLevel < 5){
      document.getElementById("poo-button").disabled = false;
   }
   else{
      document.getElementById("poo-button").disabled = true;
   }

   if(userLevel >= 3 && bugCount < 10 && userLevel < 5){
      document.getElementById("bug-button").disabled = false;
   }
   else{
      document.getElementById("bug-button").disabled = true;
   }

   if(userLevel >= 3 && potCount < 10 && userLevel < 5){
      document.getElementById("pot-button").disabled = false;
   }
   else{
      document.getElementById("pot-button").disabled = true;
   }

   if(userLevel >= 4 && musicCount < 10 && userLevel < 5){
      document.getElementById("music-button").disabled = false;
   }
   else{
      document.getElementById("music-button").disabled = true;
   }
   if(userLevel >= 4 && nutritionCount < 10 && userLevel < 5){
      document.getElementById("nutrition-button").disabled = false;
   }
   else{
      document.getElementById("nutrition-button").disabled = true;
   }

   if(userLevel >= 5){
      alert("축하합니다! 최고레벨에 도달하였습니다.");
      return;
   }
}
function formatSunTime(seconds){
   //주어진 초를 분으로 변환하여 정수 부분을 구함
   var minutes = Math.floor(seconds / 60);
   // 초를 60으로 나눈 나머지를 구하여 남은 초를 계산
   var remainingseconds = seconds % 60;
   // 변환된 시간을 00:00 형식으로 반환하기 위해 패딩 함수를 사용하여 처리
   return padSun(minutes) + ":" + padSun(remainingseconds);
}

function padSun(number){
   // 주어진 숫자가 10보다 작으면 숫자 앞에 0을 붙여 반환하고, 그렇지 않으면 반환
   return (number < 10 ? "0" : "") + number;
}

// 시간을 1초마다 감소시키고 업데이트 함수
function decreaseSunTime(){
   clearInterval(timerInterval) //이전 타이머가 있으면 제거하여 중복 실행 방지
   //setInterVal 함수를 사용하여 1초마다 실행되는 타이머를 설정
   timerInterval = setInterval(function(){
      // sunTime 변수를 1씩 감소
      sunTime --;
      // 시간을 업데이트하는 함수를 호출하여 화면에 남은 시간 표시
      updateSunTimer();
      //시간이 0 이하가 되면 타이머를 중지
      if(sunTime <= 0){
         clearInterval(timerInterval);
      }
   }, 1000); //1초마다 실행
}
// 화면에 남은 시간을 업데이트하는 함수
function updateSunTime(){
   var SunTimeElement = document.getElementById('sun-time');

   //요소가 존재하는지 확인
   if(SunTimeElement){
      //남은 시간을 분과 초로 변환하여 계산
      var minutes = Math.floor(sunTime / 60); //분
      var seconds = sunTime % 60; //초

      //시간을 화면에 표시
      SunTimeElement.innerText = "햇빛주기" + (minutes < 10 ? '0' : '') +
         minutes + ":" + (seconds < 10 ? '0' : '') + seconds + "횟수" + sunTime;
      // 남은 시간이 0 이하이면 시간 요소를 숨김
      if(sunTime <= 0){
         SunTimeElement.style.display = 'none';
      }   
      else{
         SunTimeElement.style.display = '';
      }
   }
}

function startSunTime(time){
   sunTime = time;
   // 서버에서 받은 햇빛주기 시간으로 변경
   decreaseSunTime();
}


//햇빛주기 횟수 업데이트
function updateSunCount(count){
   // 햇빛주기 횟수를 화면에 업데이트
   sunCountElement = document.getElementById("sun-count");
   
   //console.log("햇빛 초기값: " + sunCount)
   if(sunCountElement){
      sunCountElement.innerText = "횟수: " + count;
      if(count >= 5){
         var sunButton = document.getElementById("sun-button");
         if(sunButton){
            sunButton.disabled = true; //버튼 비활성화
         }
         alert("햇빛주기 횟수가 다 되었습니다.");
      }
      else{
         document.getElementById("sun-button").disabled = false;
      }
   }
}

function updateWaterCount(count){
   var waterCountElement = document.getElementById("water-count");
   if(waterCountElement){
      waterCountElement.innerText = "횟수: " + count;
   }
}

function updateLoveCount(count){
   var loveCountElement = document.getElementById("love-count");
   if(loveCountElement){
      loveCountElement.innerText = "횟수: " + count;
   }
}

function updatePooCount(count){
   var pooCountElement = document.getElementById("poo-count");
   if(pooCountElement){
      pooCountElement.innerText = "횟수: " + count;
   } 
}

function updateBugCount(count){
   var bugCountElement = document.getElementById("bug-count");
   if(bugCountElement){
      bugCountElement.innerText = "횟수: " + count;
   }
}

function updatePotCount(count){
   var potCountElement = document.getElementById("pot-count");
   if(potCountElement){
      potCountElement.innerText = "횟수: " + count;
   }
}

function updateMusicCount(count){
   var musicCountElement = document.getElementById("music-count");
   if(musicCountElement){
      musicCountElement.innerText = "횟수: " + count;
   }
}

function updateNutritionCount(count){
   var nutritionCountElement = document.getElementById("nutrition-count");
   if(nutritionCountElement){
      nutritionCountElement.innerText = "횟수: " + count;
   }
}

function updateProgressBar(progressValue){
   var progressBar = document.querySelector('#bar');
   
   //console.log(progressBar)
   if(progressBar){
      
      progressBar.style.width = progressValue + "%";
   }
   else{
      console.error("progress bar element not found");
   }
}
// 햇빛주기 버튼 클릭 이벤트 리스너 추가
document.getElementById("sun-button").addEventListener("click",sunPlant);
});

/* 비료주기 버튼 기능 */
/* === poo === */
document.addEventListener("DOMContentLoaded",function(){
var pooTime = 1; //초기에 비료를 주는 시간
var waterCount = 5;
var loveCount = 5;
var sunCount = 5;
var pooCount = 5;   //초기 비료 주기 횟수
var bugCount = 10;
var potCount = 10;
var musicCount = 10;
var nutritionCount = 10;
var pooTimerInterval;   //타이머 변수
var startPooTime;
var timerInterval;
var userIdInput = document.getElementById("poo_id");
var userId = '';
var LevelInput = document.getElementById("poo_level");

if (userIdInput !== null) {
   userId = userIdInput.value; // userIdInput이 null이 아닌 경우에만 value 속성을 읽음
   console.log("User ID:", userId);
}
else{
   console.error("User ID not found.");
}

function pooPlant(){

   var userLevel = parseInt(LevelInput.value, 10);
   if(isNaN(userLevel) || userLevel < 1){
   return;
   }
   if(userLevel >= 5){
   alert("최고 레벨에 도달하였습니다.");
   }
   disabledPooButton();
   startPooTime = new Date().getTime();// 시작시간 설정
   pooTimerInterval = setInterval(updatePooTimer, 1000);// 타이머 시작(1초마다 갱신)
}

function updateImage(imageUrl){
   var timestamp = new Date().getTime(); // 현재 시간의 타임스탬프 생성
   imageUrl += "?timestamp=" + timestamp; // 이미지 URL에 타임스탬프 추가
   $('#characterImage').attr('src', imageUrl);
   console.log("URL:", imageUrl)
}


function updatePooTimer(){
   
   
   
   var currentTime = new Date().getTime();//현재시간
   
   var elapsedtime = Math.floor((currentTime - startPooTime) / 1000);//경과 시간
   
   document.getElementById("poo-time").innerText = formatPooTime(pooTime - elapsedtime);//남은시간 표시

   if(elapsedtime >= 1){// 3분 경과후 버튼 활성화
      
      clearInterval(pooTimerInterval);
      var userLevel = parseInt(LevelInput.value, 10);
      enablePooButton(userLevel, waterCount, loveCount, sunCount, pooCount, bugCount, potCount, musicCount, nutritionCount);
   $.ajax({
      type: "POST",
      async:false,
      url: "/pooPlant",
      data:{
         user_id: userId
      },
      success: function(data,textStatus){
         var response = JSON.parse(data);
         var userLevel = response.pooLevel;
         var char_no = response.char_no;
      	 var imageUrl = "img/game_character/seed-"+ char_no +"_lv_" + userLevel + ".png";
         updateImage(imageUrl);
         updateProgressBar(response.progressValue);//서버로부터의 응답을 받아와서 프로그레스바 업데이트
         updatePooCount(response.PooCount);//비료주기 횟수를 업데이트한다.
         LevelInput.value = userLevel;
         updateSunCount(response.SunCount);
         updateLoveCount(response.LoveCount);
       updateWaterCount(response.WaterCount);
       updateBugCount(response.BugCount);
       updatePotCount(response.PotCount);
       updateMusicCount(response.MusicCount);
       updateNutritionCount(response.NutritionCount);

     //  alert("비료주기가 성공적으로 수행되었습니다.");//성공 메시지 출력
         enablePooButton(userLevel,
            response.WaterCount,
            response.LoveCount,
            response.SunCount,
            response.PooCount,
            response.BugCount,
            response.PotCount,
            response.MusicCount,
            response.NutritionCount);
      },
      error: function(data, status, error){
         console.error("비료주기 오류:", error);//오류 발생 시 메시지 출력
         alert("비료주기 수행 중 오류가 발생하였습니다.");
      },
   });
  }   
}

function disabledPooButton(){
   document.getElementById("water-button").disabled = true;
   document.getElementById("love-button").disabled = true;
   document.getElementById("sun-button").disabled = true;
   document.getElementById("poo-button").disabled = true;
   document.getElementById("bug-button").disabled = true;
   document.getElementById("pot-button").disabled = true;
   document.getElementById("music-button").disabled = true;
   document.getElementById("nutrition-button").disabled = true;
}
function enablePooButton(userLevel,waterCount, loveCount, sunCount, pooCount, 
   bugCount, potCount, musicCount, nutritionCount){// 버튼 활성화
   console.log("활성화 레벨:",userLevel,"비료주기 횟수:",pooCount);
   
    // 레벨이 1 이상이고 물주기 횟수가 5보다 작을 때 물주기 버튼을 활성화
    if(userLevel >= 1 && waterCount < 5 && userLevel < 5){
        document.getElementById("water-button").disabled = false;
    } else {
        document.getElementById("water-button").disabled = true;
    }
    // 레벨이 1 이상일 때 사랑주기 버튼을 활성화
    if(userLevel >= 1 && loveCount < 5 && userLevel < 5){
        document.getElementById("love-button").disabled = false;
    } else {
        document.getElementById("love-button").disabled = true;
    }
   //레벨이 2 이상일 때 햇빛주기 버튼을 활성화
   if(userLevel >= 2 && sunCount < 5 && userLevel < 5){
      document.getElementById("sun-button").disabled = false;
   }
   else{
      document.getElementById("sun-button").disabled = true;
   }
   //레벨이 2 이상일 때 비료주기 버튼을 활성화
   if(userLevel >= 2 && pooCount < 5 && userLevel < 5){
      document.getElementById("poo-button").disabled = false;
   }
   else{
      document.getElementById("poo-button").disabled = true;
   }

   if(userLevel >= 3 && bugCount < 10 && userLevel < 5){
      document.getElementById("bug-button").disabled = false;
   }
   else{
      document.getElementById("bug-button").disabled = true;
   }

   if(userLevel >= 3 && potCount < 10 && userLevel < 5){
      document.getElementById("pot-button").disabled = false;
   }
   else{
      document.getElementById("pot-button").disabled = true;
   }

   if(userLevel >= 4 && musicCount < 10 && userLevel < 5){
      document.getElementById("music-button").disabled = false;
   }
   else{
      document.getElementById("music-button").disabled = true;
   }

   if(userLevel >= 4 && nutritionCount < 10 && userLevel < 5){
      document.getElementById("nutrition-button").disabled = false;
   }
   else{
      document.getElementById("nutrition-button").disabled = true;
   }

  // 레벨이 올랐을 때 햇빛주기 횟수 초기화
   var PooPrevLevel = parseInt(LevelInput.value, 10);
   if (userLevel > PooPrevLevel) {
       updatePooCount(0); // 비료주기 횟수 초기화
       alert("레벨이 올라갔습니다.");
   }

   if(userLevel >= 5){
   alert("축하합니다! 최고레벨에 도달하였습니다.");
   return;
   }
}

function formatPooTime(seconds){
   //주어진 초를 분으로 변환하여 정수 부분을 구함
   var minutes = Math.floor(seconds / 60);
   // 초를 60으로 나눈 나머지를 구하여 남은 초를 계산
   var remainingseconds = seconds % 60;
   // 변환된 시간을 00:00 형식으로 반환하기 위해 패딩 함수를 사용하여 처리
   return padPoo(minutes) + ":" + padPoo(remainingseconds);
}

function padPoo(number){
   // 주어진 숫자가 10보다 작으면 숫자 앞에 0을 붙여 반환하고, 그렇지 않으면 반환
   return(number < 10 ? "0" : "") + number;
}

// 시간을 1초마다 감소시키고 업데이트 함수
function decreasePooTime(){
   clearInterval(timerInterval) //이전 타이머가 있으면 제거항 중복 실행 방지
   //setInterVal 함수를 사용하여 1초마다 실행되는 타이머를 설정
   timerInterval = setInterval(function(){
      // pooTime 변수를 1씩 감소
      pooTime --;
      // 시간을 업데이트하는 함수를 호출하여 화면에 남은 시간 표시
      updatePooTimer();
      //시간이 0이하가 되면 타이머를 중지
      if(pooTime <= 0){
         clearInterval(timerInterval);
      }
   }, 1000);
}

// 화면에 남은 시간을 업데이트하는 함수
function updatePooTime(){
   var PooTimeElement = document.getElementById('poo-time');

   //요소가 존재하는지 확인
   if(PooTimeElement){
      //남은 시간을 분과 초로 변환하여 계산
      var minutes = Math.floor(pooTime / 60);
      var seconds = pooTime % 60;

      //시간을 화면에 표시
      PooTimeElement.innerText = "비료주기" + (minutes < 10 ? '0' : 0) +
       minutes + ":" + (seconds < 10 ? '0' : '') + seconds + "횟수" + pooTime;
      // 남은 시간이 0 이하이면 시간 요소를 숨김
      if(pooTime <= 0){
          PooTimeElement.style.display = 'none';
      }
      else{
         PooTimeElement.style.display = '';
      }
   }
}

function startPooTime(time){
   pooTime = true;
   // 서버에서 받은 비료주기 시간으로 변경
   decreasePooTime();
}

//비료주기 횟수 업데이트
function updatePooCount(count){
   // 비료주기 횟수를 화면에 업데이트합니다.
   var pooCountElement = document.getElementById("poo-count");
   
   //console.log("비료 초기값:" + pooCount);
   if(pooCountElement){
      pooCountElement.innerText = "횟수: " + count;
      if(count >= 5){
         var pooButton = document.getElementById("poo-button");
         if(pooButton){
            pooButton.disabled = true; //버튼 비활성화
         }
         alert("비료주기 횟수가 다 되었습니다.");
      }
      else{
         document.getElementById("poo-button").disabled = false;
      }
   }
}

function updateWaterCount(count){
   var waterCountElement = document.getElementById("water-count");
   if(waterCountElement){
      waterCountElement.innerText = "횟수: " + count;
   }
}

function updateLoveCount(count){
   var loveCountElement = document.getElementById("love-count");
   if(loveCountElement){
      loveCountElement.innerText = "횟수: " + count;
   }
}

function updateSunCount(count){
   var sunCountElement = document.getElementById("sun-count");
   if(sunCountElement){
      sunCountElement.innerText = "횟수: " + count;
   } 
}

function updateBugCount(count){
   var bugCountElement = document.getElementById("bug-count");
   if(bugCountElement){
      bugCountElement.innerText = "횟수: " + count;
   }
}

function updatePotCount(count){
   var potCountElement = document.getElementById("pot-count");
   if(potCountElement){
      potCountElement.innerText = "횟수: " + count;
   }
}

function updateMusicCount(count){
   var musicCountElement = document.getElementById("music-count");
   if(musicCountElement){
      musicCountElement.innerText = "횟수: " + count;
   }
}

function updateNutritionCount(count){
   var nutritionCountElement = document.getElementById("nutrition-count");
   if(nutritionCountElement){
      nutritionCountElement.innerText = "횟수: " + count;
   }
}

function updateProgressBar(progressValue){
   var progressBar = document.querySelector("#bar");
   
   //console.log(progressBar);
   if(progressBar){
      progressBar.style.width = progressValue + "%";
   }
   else{
      console.error("progress bar element not found");
   }
}
//비료주기 버튼 클릭 이벤트 리스너 추가
document.getElementById("poo-button").addEventListener("click",pooPlant);
});

/* 벌레주기 버튼 기능 */
/*===  Main Game : Bug === */
document.addEventListener("DOMContentLoaded",function(){
var bugTime = 1; //초기에 벌레를 주는 시간
var waterCount = 5;
var loveCount = 5;
var sunCount = 5;
var pooCount = 5;
var bugCount = 10;   //초기 벌레 주기 횟수
var potCount = 10;
var musicCount = 10;
var nutritionCount = 10;
var bugTimerInterval;
var startBugTime;
var timerInterval;
var userIdInput = document.getElementById("bug_id");
var userId = '';
var LevelInput = document.getElementById("bug_level");

if (userIdInput !== null) {
   userId = userIdInput.value; // userIdInput이 null이 아닌 경우에만 value 속성을 읽음
   console.log("User ID:", userId);
}
else{
   console.error("User ID not found.");
}

function bugPlant(){
   var userLevel = parseInt(LevelInput.value, 10);
   if(isNaN(userLevel) || userLevel < 1){
      return;
   }
   if(userLevel >= 5){
      alert("최고 레벨에 도달하였습니다.")
   }
   disabledBugButton();
   startBugTime = new Date().getTime(); // 시작시간 설정
   bugTimerInterval = setInterval(updateBugTimer, 1000); // 타이머 시작(1초마다 갱신)
}

function updateImage(imageUrl){
   var timestamp = new Date().getTime(); // 현재 시간의 타임스탬프 생성
   imageUrl += "?timestamp=" + timestamp; // 이미지 URL에 타임스탬프 추가
   $('#characterImage').attr('src', imageUrl);
   console.log("URL:", imageUrl)
}


function updateBugTimer(){   //서버에 벌레주기 요청을 한다
      var currentTime = new Date().getTime(); //현재 시간
   var elapsedTime = Math.floor((currentTime - startBugTime) / 1000); // 경과 시간
   document.getElementById("bug-time").innerText = formatBugTime(bugTime - elapsedTime); // 남은시간 표시

   // 5분 경과후 버튼 활성화
   if(elapsedTime >= 1){
      clearInterval(bugTimerInterval);
      var userLevel = parseInt(LevelInput.value,10);
      enableBugButton(userLevel, waterCount, loveCount, sunCount, pooCount, bugCount, potCount, musicCount, nutritionCount);
   $.ajax({
      type:"POST",
      async:false,
      url:"/bugPlant",
      data:{
         user_id: userId
      },
      success: function(data,textStatus){
         var response = JSON.parse(data); // 서버로부터의 응답을 받아와서 프로그레스바 업데이트
         var userLevel = response.bugLevel;
         var char_no = response.char_no;
      	 var imageUrl = "img/game_character/seed-"+ char_no +"_lv_" + userLevel + ".png";
         updateImage(imageUrl);
         updateProgressBar(response.progressValue); // 벌레주기 횟수를 업데이트합니다.
         updateBugCount(response.BugCount); //성공 메시지 출력
         LevelInput.value = userLevel;
         updatePooCount(response.PooCount);//비료주기 횟수를 업데이트한다.
         updateSunCount(response.SunCount);
         updateLoveCount(response.LoveCount);
       updateWaterCount(response.WaterCount);
       updatePotCount(response.PotCount);
       updateMusicCount(response.MusicCount);
       updateNutritionCount(response.NutritionCount);

   //      alert("벌레주기가 성공적으로 수행되었습니다."); 
         enableBugButton(userLevel,
            response.WaterCount,
            response.LoveCount,
            response.SunCount,
            response.PooCount,
            response.BugCount,
            response.PotCount,
            response.MusicCount,
            response.NutritionCount);
      },
      error: function(data, status, error){
         console.error("벌레주기 오류:", error); // 오류 발생 시 메시지 출력
         alert("벌레주기 수행 중 오류가 발생했습니다.");
      },
      complete:function(){
      }
   });
  }
}
function disabledBugButton(){
   document.getElementById("water-button").disabled = true;
   document.getElementById("love-button").disabled = true;
   document.getElementById("sun-button").disabled = true;
   document.getElementById("poo-button").disabled = true;
   document.getElementById("bug-button").disabled = true;
   document.getElementById("pot-button").disabled = true;
   document.getElementById("music-button").disabled = true;
   document.getElementById("nutrition-button").disabled = true;
}
// 버튼 활성화
function enableBugButton(userLevel,waterCount, loveCount, sunCount, pooCount, 
   bugCount, potCount, musicCount, nutritionCount){
   console.log("활성화 레벨:",userLevel,"벌레주기 횟수:",bugCount);
   
    
    // 레벨이 1 이상이고 물주기 횟수가 5보다 작을 때 물주기 버튼을 활성화
    if(userLevel >= 1 && waterCount < 5 && userLevel < 5){
        document.getElementById("water-button").disabled = false;
    } else {
        document.getElementById("water-button").disabled = true;
    }
    // 레벨이 1 이상일 때 사랑주기 버튼을 활성화
    if(userLevel >= 1 && loveCount < 5 && userLevel < 5){
        document.getElementById("love-button").disabled = false;
    } else {
        document.getElementById("love-button").disabled = true;
    }
   //레벨이 2 이상일 때 햇빛주기 버튼을 활성화
   if(userLevel >= 2 && sunCount < 5 && userLevel < 5){
      document.getElementById("sun-button").disabled = false;
   }
   else{
      document.getElementById("sun-button").disabled = true;
   }
   //레벨이 2 이상일 때 비료주기 버튼을 활성화
   if(userLevel >= 2 && pooCount < 5 && userLevel < 5){
      document.getElementById("poo-button").disabled = false;
   }
   else{
      document.getElementById("poo-button").disabled = true;
   }
   if(userLevel >= 3 && bugCount < 10 && userLevel < 5){
      document.getElementById("bug-button").disabled = false;
   }
   else{
      document.getElementById("bug-button").disabled = true;
   }
   if(userLevel >= 3 && potCount < 10 && userLevel < 5){
      document.getElementById("pot-button").disabled = false;
   }
   else{
      document.getElementById("pot-button").disabled = true;
   }

   if(userLevel >= 4 && musicCount < 10 && userLevel < 5){
      document.getElementById("music-button").disabled = false;
   }
   else{
      document.getElementById("music-button").disabled = true;
   }
   if(userLevel >= 4 && nutritionCount < 10 && userLevel < 5){
      document.getElementById("nutrition-button").disabled = false;
   }
   else{
      document.getElementById("nutrition-button").disabled = true;
   }
     // 레벨이 올랐을 때 햇빛주기 횟수 초기화
       var BugPrevLevel = parseInt(LevelInput.value, 10);
       if (userLevel > BugPrevLevel) {
           updatePooCount(0); // 비료주기 횟수 초기화
           alert("레벨이 올라갔습니다.");
       }
        if(userLevel >= 5){
          alert("축하합니다! 최고레벨에 도달하였습니다.");
          return;
        }
}

function formatBugTime(seconds){
   var minutes = Math.floor(seconds / 60); //주어진 초를 분으로 변환하여 정수 부분을 구함
   var remainingseconds = seconds % 60; // 초를 60으로 나눈 나머지를 구하여 남은 초를 계산
   return padBug(minutes) + ":" + padBug(remainingseconds); // 변환된 시간을 00:00 형식으로 반환하기 위해 패딩 함수를 사용하여 처리
}

function padBug(number){
   return(number < 10 ? "0" : "") + number; //주어진 숫자가 10보다 작으면 숫자 앞에 0을 붙여 반복하고, 그렇지 않으면 반환
}

// 시간을 1초마다 감소시키고 업데이트 함수
function decreaseBugTime(){
   clearInterval(timerInterval) //이전 타이머가 있으면 제거하고 중복 실행 방지
   timerInterval = setInterval(function(){
      bugTime --; // bugTime 변수를 1씩 감소
      updateBugTimer(); //시간을 업데이트하는 함수를 호출하여 화면에 남은 시간 표시
      if(bugTime <= 0){
         clearInterval(timerInterval); //시간이 0이하가 되면 타이머를 중지
      }
   }, 1000);
}
// 화면에 남은 시간을 업데이트하는 함수
function updateBugTime(){
   var BugTimeElement = document.getElementById('bug-time');

   //요소가 존재하는지 확인
   if(BugTimeElement){
      //남은 시간을 분과 초로 변환하여 계산
      var minutes = Math.floor(bugTime / 60);
      var seconds = bugTime % 60;

      //시간을 화면에 표시
      BugTimeElement.innerText = "벌레주기" + (minutes < 10 ? '0' : 0) +
       minutes + ":" + (seconds < 10 ? '0' : '') + seconds + "횟수" + bugTime;
      //남은 시간이 0 이하이면 시간 요소를 숨김
      if(bugTime <= 0){
         BugTimeElement.style.display = 'none';
      }
      else{
         BugTimeElement.style.display = '';
      } 
   }
}

function startBugTime(time){
   bugTime = true; //서버에서 받은 벌레주기 시간으로 변경
   decreaseBugTime();
}

//벌레주기 횟수 업데이트
function updateBugCount(count){
   bugCountElement = document.getElementById("bug-count");
   
   //console.log("벌레 초기값:" + bugCount);
   if(bugCountElement){
      bugCountElement.innerText = "횟수: " + count;
      if(count >= 10){
         var bugButton = document.getElementById("bug-button");
         if(bugButton){
            bugButton.disabled = true; //버튼 비활성화
         }
         alert("벌레주기 횟수가 다 되었습니다.")   
      }
   }
}

function updateWaterCount(count){
   var waterCountElement = document.getElementById("water-count");
   if(waterCountElement){
      waterCountElement.innerText = "횟수: " + count;
   }
}

function updateLoveCount(count){
   var loveCountElement = document.getElementById("love-count");
   if(loveCountElement){
      loveCountElement.innerText = "횟수: " + count;
   }
}

function updateSunCount(count){
   var sunCountElement = document.getElementById("sun-count");
   if(sunCountElement){
      sunCountElement.innerText = "횟수: " + count;
   } 
}

function updatePooCount(count){
   var pooCountElement = document.getElementById("poo-count");
   if(pooCountElement){
      pooCountElement.innerText = "횟수: " + count;
   }
}

function updatePotCount(count){
   var potCountElement = document.getElementById("pot-count");
   if(potCountElement){
      potCountElement.innerText = "횟수: " + count;
   }
}

function updateMusicCount(count){
   var musicCountElement = document.getElementById("music-count");
   if(musicCountElement){
      musicCountElement.innerText = "횟수: " + count;
   }
}

function updateNutritionCount(count){
   var nutritionCountElement = document.getElementById("nutrition-count");
   if(nutritionCountElement){
      nutritionCountElement.innerText = "횟수: " + count;
   }
}

function updateProgressBar(progressValue){
   var progressBar = document.querySelector('#bar');
   
   //console.log(progressBar);
   if(progressBar){
      progressBar.style.width = progressValue + "%";
   }
   else{
      console.error("progress bar element not found");
   }
}
//벌레주기 버튼 클릭 이벤트 리스너 추가
document.getElementById("bug-button").addEventListener("click",bugPlant);
});

/* 화분갈이 */
/* === pot ===*/
document.addEventListener("DOMContentLoaded",function(){
var potTime = 1;
var waterCount = 5;
var loveCount = 5;
var sunCount = 5;
var pooCount = 5;
var bugCount = 5;
var potCount = 10;
var musicCount = 10;
var nutritionCount = 10;
var potTimerInterval;
var startPotTime;
var timerInterval;
var userIdInput = document.getElementById("pot_id");
var userId = '';
var LevelInput = document.getElementById("pot_level");

if (userIdInput !== null) {
   userId = userIdInput.value; // userIdInput이 null이 아닌 경우에만 value 속성을 읽음
   console.log("User ID:", userId);
}
else{
   console.error("User ID not found.");
}

function potPlant(){
   var userLevel = parseInt(LevelInput.value);
   if(isNaN(userLevel) || userLevel < 1){
      return;
   }
   if(userLevel >= 5){
      alert("최고 레벨에 도달하였습니다.");
   }
   disabledPotButton();
   //시작시간 설정
   startPotTime = new Date().getTime();
   //타이머 시작(1초마다 갱신)
   potTimerInterval = setInterval(updatePotTimer, 1000);
}

function updateImage(imageUrl){
   var timestamp = new Date().getTime(); // 현재 시간의 타임스탬프 생성
   imageUrl += "?timestamp=" + timestamp; // 이미지 URL에 타임스탬프 추가
   $('#characterImage').attr('src', imageUrl);
   console.log("URL:", imageUrl)
}

function updatePotTimer(){
   
   //현재 시간
   var currentTime = new Date().getTime();
   //경과 시간
   var elapsedTime = Math.floor((currentTime - startPotTime) / 1000);
   //남은시간 표시
   document.getElementById("pot-time").innerText = formatPotTime(potTime - elapsedTime);

   //10분 경과후 버튼 활성화
   if(elapsedTime >= 1){
      clearInterval(potTimerInterval);
      var userLevel = parseInt(LevelInput.value,10);
      enablePotButton(userLevel, waterCount, loveCount, sunCount, pooCount, bugCount, potCount, musicCount, nutritionCount);
   $.ajax({
      type: "POST",
      async:false,
      url: "/potPlant",
      data:{
         user_id: userId
      },
      success: function(data,textStatus){
         var response = JSON.parse(data);
         var userLevel = response.potLevel;
         var char_no = response.char_no;
      	 var imageUrl = "img/game_character/seed-"+ char_no +"_lv_" + userLevel + ".png";
         updateImage(imageUrl);
         // 서버로부터의 응답을 받아와서 프로그레스바 업데이트
         updateProgressBar(response.progressValue);
         // 화분주기 횟수를 업데이트합니다.
         updatePotCount(response.PotCount);
         LevelInput.value = userLevel;
         updateBugCount(response.BugCount); //성공 메시지 출력
         updatePooCount(response.PooCount);//비료주기 횟수를 업데이트한다.
         updateSunCount(response.SunCount);
         updateLoveCount(response.LoveCount);
       updateWaterCount(response.WaterCount);
       updateMusicCount(response.MusicCount);
       updateNutritionCount(response.NutritionCount);

         // 성공 메시지 출력
      //   alert("화분갈이가 성공적으로 수행되었습니다.");
         enablePotButton(userLevel,
            response.WaterCount,
            response.LoveCount,
            response.SunCount,
            response.PooCount,
            response.BugCount,
            response.PotCount,
            response.MusicCount,
            response.NutritionCount)
      //   console.log(response);
      },
      error: function(data, status, error){
         // 오류 발생 시 메시지 출력
         console.error("화분주기 오류:", error);
         alert("화분갈이 수행 중 오류가 발생하였습니다.");
      },
   });
  }
}
function disabledPotButton(){
   document.getElementById("water-button").disabled = true;
   document.getElementById("love-button").disabled = true;
   document.getElementById("sun-button").disabled = true;
   document.getElementById("poo-button").disabled = true;
   document.getElementById("bug-button").disabled = true;
   document.getElementById("pot-button").disabled = true;
   document.getElementById("music-button").disabled = true;
   document.getElementById("nutrition-button").disabled = true;
}
//버튼 활성화
function enablePotButton(userLevel, waterCount, loveCount, sunCount, pooCount, 
   bugCount, potCount, musicCount, nutritionCount){
      console.log("활성화 레벨:",userLevel,"화분갈이 횟수:",potCount);

      // 레벨이 1 이상이고 물주기 횟수가 5보다 작을 때 물주기 버튼을 활성화
      if(userLevel >= 1 && waterCount < 5 && userLevel < 5){
         document.getElementById("water-button").disabled = false;
      } else {
         document.getElementById("water-button").disabled = true;
      }
      // 레벨이 1 이상일 때 사랑주기 버튼을 활성화
      if(userLevel >= 1 && loveCount < 5 && userLevel < 5){
         document.getElementById("love-button").disabled = false;
      } else {
         document.getElementById("love-button").disabled = true;
      }
      //레벨이 2 이상일 때 햇빛주기 버튼을 활성화
      if(userLevel >= 2 && sunCount < 5 && userLevel < 5){
         document.getElementById("sun-button").disabled = false;
      }
      else{
         document.getElementById("sun-button").disabled = true;
      }
      //레벨이 2 이상일 때 비료주기 버튼을 활성화
      if(userLevel >= 2 && pooCount < 5 && userLevel < 5){
         document.getElementById("poo-button").disabled = false;
      }
      else{
         document.getElementById("poo-button").disabled = true;
      }
      if(userLevel >= 3 && bugCount < 10 && userLevel < 5){
         document.getElementById("bug-button").disabled = false;
      }
      else{
         document.getElementById("bug-button").disabled = true;
      }
      if(userLevel >= 3 && potCount < 10 && userLevel < 5){
         document.getElementById("pot-button").disabled = false;
      }
      else{
         document.getElementById("pot-button").disabled = true;
      }
      if(userLevel >= 4 && musicCount < 10 && userLevel < 5){
         document.getElementById("music-button").disabled = false;
      }
      else{
         document.getElementById("music-button").disabled = true;
      }
      if(userLevel >= 4 && nutritionCount < 10 && userLevel < 5){
         document.getElementById("nutrition-button").disabled = false;
      }
      else{
         document.getElementById("nutrition-button").disabled = true;
      }
       // 레벨이 올랐을 때 햇빛주기 횟수 초기화
       var PotPrevLevel = parseInt(LevelInput.value, 10);
       if (userLevel > PotPrevLevel) {
              updatePotCount(0); // 비료주기 횟수 초기화
              alert("레벨이 올라갔습니다.");
       }
       if(userLevel >= 5){
         alert("축하합니다! 최고레벨에 도달하였습니다.");
            return;
       }
}

function formatPotTime(seconds){
   //주어진 초를 분으로 변환하여 정수 부분을 구함
   var minutes = Math.floor(seconds / 60);
   //초를 60으로 나눈 나머지를 구하여 남은 초를 계산
   var remainingseconds = seconds % 60;
   //변환된 시간을 00:00 형식으로 반환하기 위해 패딩 함수를 사용하여 처리
   return padPot(minutes) + ":" + padPot(remainingseconds);
}

function padPot(number){
   //주어진 숫자가 10보다 작으면 숫자 앞에 0을 붙여 반복하고, 그렇지 않으면 반환
   return(number < 10 ? "0" : "") + number;
}

// 시간을 1초마다 감소시키고 업데이트 함수
function decreasePotTime(){
   clearInterval(timerInterval) //이전 타이머가 있으면 제거하고 중복 실행 방지
   //setInterval 함수를 사용하여 1초마다 실행되는 타이머를 설정
   timerInterval = setInterval(function(){
      //potTime 변수를 1씩 감소
      potTime--;
      //시간을 업데이트하는 함수를 호출하여 화면에 남은 시간 표시
      updatePotTimer();
      //시간이 0이하가 되면 타이머를 중지
      if(potTime <= 0){
         clearInterval(timerInterval);
      }
   }, 1000);
}
//화면에 남은 시간을 업데이트하는 함수
function updatePotTime(){
   var PotTimeElement = document.getElementById('pot-time');

   //요소가 존재하는지 확인
   if(PotTimeElement){
      //남은 시간을 분과 초로 변환하여 계산
      var minutes = Math.floor(potTime / 60);
      var seconds = potTime % 60;

      //시간을 화면에 표시
      PotTimeElement.innerText = "화분갈이" + (minutes < 10 ? '0' : 0) +
         minutes + ":" + (seconds < 10 ? '0' : '') + seconds + "횟수" + potTime;
      //남은 시간이 0 이하이면 시간 요소를 숨김
      if(potTime <= 0){
         PotTimeElement.style.display = 'none';
      }
      else{
         PotTimeElement.style.display = '';
      }   
   }
}
function startPotTime(time){
   potTime = true;
   //서버에서 받은 화분갈이 시간으로 변경
   decreasePotTime();
}

//화분주기 횟수 업데이트
function updatePotCount(count){
   //화분주기 횟수를 화면에 업데이트
   potCountElement = document.getElementById("pot-count");
   
   //console.log("화분 초기값: " + potCount);
   if(potCountElement){
      potCountElement.innerText = "횟수: " + count;
      if(count >= 10){
         var potButton = document.getElementById("pot-button");
         if(potButton){
            potButton.disabled = true; //버튼 비활성화
         }
         alert("화분갈이 횟수가 다 되었습니다.");
      }
   }
}

function updateWaterCount(count){
   var waterCountElement = document.getElementById("water-count");
   if(waterCountElement){
      waterCountElement.innerText = "횟수: " + count;
   }
}

function updateLoveCount(count){
   var loveCountElement = document.getElementById("love-count");
   if(loveCountElement){
      loveCountElement.innerText = "횟수: " + count;
   }
}

function updateSunCount(count){
   var sunCountElement = document.getElementById("sun-count");
   if(sunCountElement){
      sunCountElement.innerText = "횟수: " + count;
   } 
}

function updatePooCount(count){
   var pooCountElement = document.getElementById("poo-count");
   if(pooCountElement){
      pooCountElement.innerText = "횟수: " + count;
   }
}

function updateBugCount(count){
   var bugCountElement = document.getElementById("bug-count");
   if(bugCountElement){
      bugCountElement.innerText = "횟수: " + count;
   }
}

function updateMusicCount(count){
   var musicCountElement = document.getElementById("music-count");
   if(musicCountElement){
      musicCountElement.innerText = "횟수: " + count;
   }
}

function updateNutritionCount(count){
   var nutritionCountElement = document.getElementById("nutrition-count");
   if(nutritionCountElement){
      nutritionCountElement.innerText = "횟수: " + count;
   }
}

function updateProgressBar(progressValue){
   var progressBar = document.querySelector('#bar');
   
   //console.log(progressBar);
   if(progressBar){
      progressBar.style.width = progressValue + "%";
   }
   else{
      console.error("progress bar element not found");
   }
}
//화분갈이 버튼 클릭 이벤트 리스너 추가
document.getElementById("pot-button").addEventListener("click",potPlant);
});

/* 음악틀어주기 */
/*===  Main Game : Music === */
document.addEventListener("DOMContentLoaded",function(){
var musicTime = 1;
var waterCount = 5;
var loveCount = 5;
var sunCount = 5;
var pooCount = 5;
var bugCount = 10;
var potCount = 10;
var musicCount = 10;
var nutritionCount = 10;
var musicTimerInterval;
var startMusicTime;
var timerInterval;
var userIdInput = document.getElementById("music_id");
var userId = '';
var LevelInput = document.getElementById("music_level");

if (userIdInput !== null) {
   userId = userIdInput.value; // userIdInput이 null이 아닌 경우에만 value 속성을 읽음
   console.log("User ID:", userId);
}
else{
   console.error("User ID not found.");
}


function musicPlant(){
   var userLevel = parseInt(LevelInput.value, 10);
   if(isNaN(userLevel) || userLevel < 1){
      return;
   }
   if(userLevel >= 5){
      alert("최고 레벨에 도달하였습니다.")
   }
   disabledMusicButton();
   //시작시간 설정
   startMusicTime = new Date().getTime();
   //타이머 시작(1초마다 갱신)
   musicTimerInterval = setInterval(updateMusicTimer, 1000);
}

function updateImage(imageUrl){
   var timestamp = new Date().getTime(); // 현재 시간의 타임스탬프 생성
   imageUrl += "?timestamp=" + timestamp; // 이미지 URL에 타임스탬프 추가
   $('#characterImage').attr('src', imageUrl);
   console.log("URL:", imageUrl)
}


function updateMusicTimer(){
   
   //현재 시간
   var currentTime = new Date().getTime();
   //경과 시간
   var elapsedTime = Math.floor((currentTime - startMusicTime) / 1000);
   //남은시간 표시
   document.getElementById("music-time").innerText = formatMusicTime(musicTime - elapsedTime);
   
   //30분 경과후 버튼 활성화
   if(elapsedTime >= 1){
      clearInterval(musicTimerInterval);
      var userLevel = parseInt(LevelInput.value, 10);
      enableMusicButton(userLevel, waterCount, loveCount, sunCount, pooCount, bugCount, potCount, musicCount, nutritionCount);
   $.ajax({
      type: "POST",
      async:false,
      url: "/musicPlant",
      data:{
         user_id: userId
      },
      success: function(data,textStatus){
         var response = JSON.parse(data);
         var userLevel = response.musicLevel;
         var char_no = response.char_no;
      	 var imageUrl = "img/game_character/seed-"+ char_no +"_lv_" + userLevel + ".png";
         updateImage(imageUrl);
         //서버로부터의 응답을 받아와서 프로그레스바 업데이트
         updateProgressBar(response.progressValue);
         //음악틀어주기 횟수를 업데이트합니다.
         updateMusicCount(response.MusicCount);
         LevelInput.value = userLevel;
         updatePotCount(response.PotCount);
         updateBugCount(response.BugCount); //성공 메시지 출력
         updatePooCount(response.PooCount);//비료주기 횟수를 업데이트한다.
         updateSunCount(response.SunCount);
         updateLoveCount(response.LoveCount);
       updateWaterCount(response.WaterCount);
       updateNutritionCount(response.NutritionCount);

         //성공 메시지 출력
       //  alert("음악틀어주기를 성공적으로 수행되었습니다.");
         enableMusicButton(userLevel,
            response.WaterCount,
            response.LoveCount,
            response.SunCount,
            response.PooCount,
            response.BugCount,
            response.PotCount,
            response.MusicCount,
            response.NutritionCount);
      },
      error:function(data, status, error){
         //오류 발생 시 메시지 출력
         console.error("음악틀어주기 오류: ", error);
         alert("음악틀어주기 수행 중 오류가 발생하였습니다.");
      },
      complete: function(){
      }
   });
  }
}
function disabledMusicButton(){
   document.getElementById("water-button").disabled = true;
   document.getElementById("love-button").disabled = true;
   document.getElementById("sun-button").disabled = true;
   document.getElementById("poo-button").disabled = true;
   document.getElementById("bug-button").disabled = true;
   document.getElementById("pot-button").disabled = true;
   document.getElementById("music-button").disabled = true;
   document.getElementById("nutrition-button").disabled = true;
}
//버튼 활성화
function enableMusicButton(userLevel, waterCount, loveCount, sunCount, pooCount, 
   bugCount, potCount, musicCount, nutritionCount){
      // 레벨이 1 이상이고 물주기 횟수가 5보다 작을 때 물주기 버튼을 활성화
      if(userLevel >= 1 && waterCount < 5 && userLevel < 5){
         document.getElementById("water-button").disabled = false;
      } else {
         document.getElementById("water-button").disabled = true;
      }
      // 레벨이 1 이상일 때 사랑주기 버튼을 활성화
      if(userLevel >= 1 && loveCount < 5 && userLevel < 5){
         document.getElementById("love-button").disabled = false;
      } else {
         document.getElementById("love-button").disabled = true;
      }
      //레벨이 2 이상일 때 햇빛주기 버튼을 활성화
      if(userLevel >= 2 && sunCount < 5 && userLevel < 5){
         document.getElementById("sun-button").disabled = false;
      }
      else{
         document.getElementById("sun-button").disabled = true;
      }
      //레벨이 2 이상일 때 비료주기 버튼을 활성화
      if(userLevel >= 2 && pooCount < 5 && userLevel < 5){
         document.getElementById("poo-button").disabled = false;
      }
      else{
         document.getElementById("poo-button").disabled = true;
      }
      if(userLevel >= 3 && bugCount < 10 && userLevel < 5){
         document.getElementById("bug-button").disabled = false;
      }
      else{
         document.getElementById("bug-button").disabled = true;
      }
      if(userLevel >= 3 && potCount < 10 && userLevel < 5){
         document.getElementById("pot-button").disabled = false;
      }
      else{
         document.getElementById("pot-button").disabled = true;
      }
      if(userLevel >= 4 && musicCount < 10 && userLevel < 5){
         document.getElementById("music-button").disabled = false;
      }
      else{
         document.getElementById("music-button").disabled = true;
      }
      if(userLevel >= 4 && nutritionCount < 10 && userLevel < 5){
         document.getElementById("nutrition-button").disabled = false;
      }
      else{
         document.getElementById("nutrition-button").disabled = true;
      }
      // 레벨이 올랐을 때 햇빛주기 횟수 초기화
             var MusicPrevLevel = parseInt(LevelInput.value, 10);
             if (userLevel > MusicPrevLevel) {
                    updateMusicCount(0); // 비료주기 횟수 초기화
                    alert("레벨이 올라갔습니다.");
             }
             if(userLevel >= 5){
               alert("축하합니다! 최고레벨에 도달하였습니다.");
                  return;
             }
}

function formatMusicTime(seconds){
   //주어진 초를 분으로 변환하여 정수 부분을 구함
   var minutes = Math.floor(seconds / 60);
   //초를 60으로 나눈 나머지를 구하여 남은 초를 계산
   var remainingseconds = seconds % 60;
   //변환된 시간을 00:00 형식으로 반환하기 위해 패딩 함수를 사용하여 처리
   return padMusic(minutes) + ":" + padMusic(remainingseconds);

}

function padMusic(number){
   //주어진 숫자가 10보다 작으면 숫자 앞에 0을 붙여 반복하고, 그렇지 않으면 반환
   return(number < 10 ? "0" : "") + number;
}

//시간을 1초마다 감소시키고 업데이트 함수
function decreaseMusicTime(){
   clearInterval(timerInterval) //이전 타이머가 있으면 제거하고 중복 실행 방지
   //setInterval 함수를 사용하여 1초마다 실행되는 타이머를 설정
   timerInterval = setInterval(function(){
      //musicTime 변수를 1씩 감소
      musicTime--;
      //시간을 업데이트하는 함수를 호출하여 화면에 남은 시간 표시
      updateMusicTimer();
      //시간이 0이하가 되면 타이머를 중지
      if(musicTime <= 0){
         clearInterval(timerInterval);
      }
   }, 1000);
}

//화면에 남은 시간을 업데이트하는 함수
function updateMusicTime(){
   var MusicTimeElement = document.getElementById('music-time');

   //요소가 존재하는지 확인
   if(MusicTimeElement){
      //남은 시간을 분과 초로 변환하여 계산
      var minutes = Math.floor(musicTime / 60);
      var seconds = musicTime % 60;

      //시간에 화면에 표시
      MusicTimeElement.innerText = "음악틀어주기" + (minutes < 10 ? '0' : 0) +
         minutes + ":" + (seconds < 10 ? '0' : '') + seconds + "횟수" + musicTime;
      //남은 시간이 0 이하이면 시간 요소를 숨김
      if(musicTime <= 0){
         MusicTimeElement.style.display = 'none';
      }
      else{
         MusicTimeElement.style.display = '';
      }   
   }
}

function startMusicTime(time){
   musicTime = true;
   //서버에서 받은 음악틀어주기 시간으로 변경
   decreaseMusicTime();
}

// 음악틀어주기 횟수 업데이트
function updateMusicCount(count){
   //음악틀어주기 횟수를 화면에 업데이트
   musicCountElement = document.getElementById("music-count");
   
   //console.log("음악 초기값: " + musicCount);
   if(musicCountElement){
      musicCountElement.innerText = "횟수: " + count;
      if(count >= 10){
         var musicButton = document.getElementById("music-button");
         if(musicButton){
            musicButton.disabled = true; //버튼 비활성화
         }
         alert("음악틀어주기 횟수가 다 되었습니다.");
      }
   }
}
function updateWaterCount(count){
   var waterCountElement = document.getElementById("water-count");
   if(waterCountElement){
      waterCountElement.innerText = "횟수: " + count;
   }
}

function updateLoveCount(count){
   var loveCountElement = document.getElementById("love-count");
   if(loveCountElement){
      loveCountElement.innerText = "횟수: " + count;
   }
}

function updateSunCount(count){
   var sunCountElement = document.getElementById("sun-count");
   if(sunCountElement){
      sunCountElement.innerText = "횟수: " + count;
   } 
}

function updatePooCount(count){
   var pooCountElement = document.getElementById("poo-count");
   if(pooCountElement){
      pooCountElement.innerText = "횟수: " + count;
   }
}

function updateBugCount(count){
   var bugCountElement = document.getElementById("bug-count");
   if(bugCountElement){
      bugCountElement.innerText = "횟수: " + count;
   }
}

function updatePotCount(count){
   var potCountElement = document.getElementById("pot-count");
   if(potCountElement){
      potCountElement.innerText = "횟수: " + count;
   }
}

function updateNutritionCount(count){
   var nutritionCountElement = document.getElementById("nutrition-count");
   if(nutritionCountElement){
      nutritionCountElement.innerText = "횟수: " + count;
   }
}

function updateProgressBar(progressValue){
   var progressBar = document.querySelector('#bar');
   
   console.log(progressBar);
   if(progressBar){
      progressBar.style.width = progressValue + "%";
   }
   else{
      console.error("progress bar element not found");
   }
}
//음악틀어주기 버튼 클릭 이벤트 리스너 추가
document.getElementById("music-button").addEventListener("click",musicPlant);
});

/* 영양제주기 버튼 기능 */
/*===  Main Game : Nutrition === */
document.addEventListener("DOMContentLoaded",function(){
var nutritionTime = 1;
var waterCount = 5;    // 초기 물 주기 횟수
var loveCount = 5;
var sunCount = 5;
var pooCount = 5;
var bugCount = 10;
var potCount =  10;   
var musicCount = 10;
var nutritionCount = 10;
var nutritionTimerInterval;
var startNutritionTime;
var timerInterval;
var userIdInput = document.getElementById("nutrition_id");
var userId = '';
var LevelInput = document.getElementById("nutrition_level");

if (userIdInput !== null) {
   userId = userIdInput.value; // userIdInput이 null이 아닌 경우에만 value 속성을 읽음
   console.log("User ID:", userId);
}
else{
   console.error("User ID not found.");
}

function nutritionPlant(){   
   var userLevel = parseInt(LevelInput.value, 10);
   if(isNaN(userLevel) || userLevel < 1){
      return;
   }
   if(userLevel >= 5){
      alert("최고 레벨에 도달하였습니다.");
   }
   disabledNutritionButton();
   startNutritionTime = new Date().getTime();//시작시간 설정
   nutritionTimerInterval = setInterval(updateNutritionTimer, 1000);//타이머 시작(1초마다 갱신)
}

function updateImage(imageUrl){
   var timestamp = new Date().getTime(); // 현재 시간의 타임스탬프 생성
   imageUrl += "?timestamp=" + timestamp; // 이미지 URL에 타임스탬프 추가
   $('#characterImage').attr('src', imageUrl);
   console.log("URL:", imageUrl)
}


function updateNutritionTimer(){
   
   var currentTime = new Date().getTime();//현재 시간
   var elapsedTime = Math.floor((currentTime - startNutritionTime) / 1000);//경과 시간
   document.getElementById("nutrition-time").innerText = formatNutritionTime(nutritionTime - elapsedTime);//남은시간 표시
   
   if(elapsedTime >= 1){//1시간 경과후 버튼 활성화
      clearInterval(nutritionTimerInterval);
      var userLevel = parseInt(LevelInput.value, 10);
      enableNutritionButton(userLevel, waterCount, loveCount, sunCount, pooCount, bugCount, potCount, musicCount, nutritionCount);
   $.ajax({
      type:"POST",
      async:false,
      url:"/nutritionPlant",
      data:{
         user_id: userId
      },
      success:function(data,textStatus){
         var response = JSON.parse(data);
         var userLevel = response.nutritionLevel;
         var char_no = response.char_no;
      	 var imageUrl = "img/game_character/seed-"+ char_no +"_lv_" + userLevel + ".png";
         updateImage(imageUrl);
         updateProgressBar(response.progressValue);// 서버로부터의 응답을 받아와서 프로그레스바 업데이트
         updateNutritionCount(response.NutritionCount);//영양제주기 횟수를 업데이트합니다.
         LevelInput.value = userLevel;
         updateMusicCount(response.MusicCount);
         updatePotCount(response.PotCount);
         updateBugCount(response.BugCount); //성공 메시지 출력
         updatePooCount(response.PooCount);//비료주기 횟수를 업데이트한다.
         updateSunCount(response.SunCount);
         updateLoveCount(response.LoveCount);
       updateWaterCount(response.WaterCount);
       

        // alert("영양제주기가 성공적으로 수행되었습니다.");// 성공 메시지 출력
         enableNutritionButton(userLevel,
            response.WaterCount,
            response.LoveCount,
            response.SunCount,
            response.PooCount,
            response.BugCount,
            response.PotCount,
            response.MusicCount,
            response.NutritionCount);
      },
      error: function(data, status, error){
         console.error("영양제주기 오류:", error);// 오류 발생 시 메시지 출력
         alert("영양제주기 수행 중 오류가 발생하였습니다.");
      },
   });
  }   
}
function disabledNutritionButton(){
   document.getElementById("water-button").disabled = true;
   document.getElementById("love-button").disabled = true;
   document.getElementById("sun-button").disabled = true;
   document.getElementById("poo-button").disabled = true;
   document.getElementById("bug-button").disabled = true;
   document.getElementById("pot-button").disabled = true;
   document.getElementById("music-button").disabled = true;
   document.getElementById("nutrition-button").disabled = true;
}
//버튼 활성화
function enableNutritionButton(userLevel, waterCount, loveCount, sunCount, pooCount, 
   bugCount, potCount, musicCount, nutritionCount){

   console.log("활성화 레벨:",userLevel,"영양제주기 횟수:",nutritionCount);

    // 레벨이 1 이상이고 물주기 횟수가 5보다 작을 때 물주기 버튼을 활성화
    if(userLevel >= 1 && waterCount < 5 && userLevel < 5){
        document.getElementById("water-button").disabled = false;
    } else {
        document.getElementById("water-button").disabled = true;
    }
    // 레벨이 1 이상일 때 사랑주기 버튼을 활성화
    if(userLevel >= 1 && loveCount < 5 && userLevel < 5){
        document.getElementById("love-button").disabled = false;
    } else {
        document.getElementById("love-button").disabled = true;
    }
   //레벨이 2 이상일 때 햇빛주기 버튼을 활성화
   if(userLevel >= 2 && sunCount < 5 && userLevel < 5){
      document.getElementById("sun-button").disabled = false;
   }
   else{
      document.getElementById("sun-button").disabled = true;
   }
   //레벨이 2 이상일 때 비료주기 버튼을 활성화
   if(userLevel >= 2 && pooCount < 5 && userLevel < 5){
      document.getElementById("poo-button").disabled = false;
   }
   else{
      document.getElementById("poo-button").disabled = true;
   }
   if(userLevel >= 3 && bugCount < 10 && userLevel < 5){
      document.getElementById("bug-button").disabled = false;
   }
   else{
      document.getElementById("bug-button").disabled = true;
   }
   if(userLevel >= 3 && potCount < 10 && userLevel < 5){
      document.getElementById("pot-button").disabled = false;
   }
   else{
      document.getElementById("pot-button").disabled = true;
   }
   if(userLevel >= 4 && musicCount < 10 && userLevel < 5){
      document.getElementById("music-button").disabled = false;
   }
   else{
      document.getElementById("music-button").disabled = true;
   }
   if(userLevel >= 4 && nutritionCount < 10 && userLevel < 5){
      document.getElementById("nutrition-button").disabled = false;
   }
   else{
      document.getElementById("nutrition-button").disabled = true;
   }
   // 레벨이 올랐을 때 햇빛주기 횟수 초기화
          var NutritionPrevLevel = parseInt(LevelInput.value, 10);
          if (userLevel > NutritionPrevLevel) {
                 updateNutritionCount(0); // 비료주기 횟수 초기화
                 alert("레벨이 올라갔습니다.");
          }
          if(userLevel >= 5){
            alert("축하합니다! 최고레벨에 도달하였습니다.");
               return;
          }
}

function formatNutritionTime(seconds){
   var minutes = Math.floor(seconds / 60);//주어진 초를 분으로 변환하여 정수 부분을 구함
   var remainingSeconds = seconds % 60;// 초를 60으로 나눈 나머지를 구하여 남은 초를 계산
   return padNutrition(minutes) + ":" + padNutrition(remainingSeconds);// 변환된 시간을 00:00 형식으로 반환하기 위해 패딩 함수를 사용하여 처리
}

function padNutrition(number){
   return(number < 10 ? "0" : "") + number;//주어진 숫자가 10보다 작으면 숫자 앞에 0을 붙여 반복하고, 그렇지 않으면 반환
}

function decreaseNutritionTime(){//시간을 1초마다 감소시키고 업데이트 함수
   clearInterval(timerInterval) //이전 타이머가 있으면 제거하고 중복 실행 방지
   timerInterval = setInterval(function(){//setInterval 함수를 사용하여 1초마다 실행되는 타이머를 설정
      
      nutritionTime--;//nutritionTime 변수를 1씩 감소
      updateNutritionTimer();//시간을 업데이트하는 함수를 호출하여 화면에 남은 시간 표시
      if(nutritionTime <= 0){//시간이 0이하가 되면 타이머를 중지
         clearInterval(timerInterval);
      }
   }, 1000);
}


function updateNutritionTime(){//화면에 남은 시간을 업데이트 하는 함수
   var NutritionTimeElement = document.getElementById('nutrition-time');

   if(NutritionTimeElement){//요소가 존재하는지 확인
      var minutes = Math.floor(nutritionTime / 60);//남은 시간을 분과 초를 변환하여 계산
      var seconds = nutritionTime % 60;
      
      NutritionTimeElement.innerText = "영양제주기 " + (minutes < 10 ? '0' : '') +
          minutes + ":" + (seconds < 10 ? '0' : '') + seconds + " 횟수:" + nutritionTime;//시간에 화면에 표시
      
      if(nutritionTime <= 0){
         NutritionTimeElement.style.display = 'none';//남은 시간이 0 이하이면 시간 요소를 숨김
      }
      else{
         NutritionTimeElement.style.display = '';
      }   
   }
}

function startNutritionTime(time){
   nutritionTime = true;
   decreaseNutritionTime();//서버에서 받은 영양제주기 시간으로 변경
}


function updateNutritionCount(count){//영양제주기 횟수 업데이트
   nutritionCountElement = document.getElementById("nutrition-count");//영양제주기 횟수를 화면에 업데이트
   
   if(nutritionCountElement){//console.log("영양제 초기값: " + nutritionCount);
      nutritionCountElement.innerText = "횟수: " + count;
      if(count >= 10){
         var nutritionButton = document.getElementById("nutrition-button")
         if(nutritionButton){
            nutritionButton.disabled = true; //버튼 비활성화
         }
         alert("영양제 횟수가 다 되었습니다.");
      }
   }
}

function updateWaterCount(count){
   var waterCountElement = document.getElementById("water-count");
   if(waterCountElement){
      waterCountElement.innerText = "횟수: " + count;
   }
}

function updateLoveCount(count){
   var loveCountElement = document.getElementById("love-count");
   if(loveCountElement){
      loveCountElement.innerText = "횟수: " + count;
   }
}

function updateSunCount(count){
   var sunCountElement = document.getElementById("sun-count");
   if(sunCountElement){
      sunCountElement.innerText = "횟수: " + count;
   } 
}

function updatePooCount(count){
   var pooCountElement = document.getElementById("poo-count");
   if(pooCountElement){
      pooCountElement.innerText = "횟수: " + count;
   }
}

function updateBugCount(count){
   var bugCountElement = document.getElementById("bug-count");
   if(bugCountElement){
      bugCountElement.innerText = "횟수: " + count;
   }
}

function updatePotCount(count){
   var potCountElement = document.getElementById("pot-count");
   if(potCountElement){
      potCountElement.innerText = "횟수: " + count;
   }
}

function updateMusicCount(count){
   var musicCountElement = document.getElementById("music-count");
   if(musicCountElement){
      musicCountElement.innerText = "횟수: " + count;
   }
}

function updateProgressBar(progressValue){
   var progressBar = document.querySelector('#bar');
   
   if(progressBar){
      progressBar.style.width = progressValue + "%";//console.log(progressBar);
   }
   else{
      console.error("progress bar element not found");
   }
}
//영양제주지 버튼 클릭 이벤트 리스너 추가
document.getElementById("nutrition-button").addEventListener("click", nutritionPlant);
});