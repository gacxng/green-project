document.addEventListener("DOMContentLoaded", function () {
    initializeMyKeywords(); // 페이지 로드 시 초기화

    // 핫 키워드 클릭 이벤트 리스너 추가
    document.querySelectorAll(".hot-keyowrd-section .tag-txt").forEach(function (element) {
        element.addEventListener("click", function (event) {
            const keyword = event.target.textContent;
            toggleKeywordInMyKeywords(keyword); // "마이 키워드"에 추가 또는 제거
        });
    });

    // 초기화 버튼 클릭 이벤트 리스너
    const resetButton = document.querySelector(".btn-reset");
    if (resetButton) {
        resetButton.addEventListener("click", function () {
            resetMyKeywords(); // "마이 키워드" 초기화
        });
    }

    // "내 식물로 등록" 버튼 클릭 이벤트 리스너 추가
    document.querySelectorAll('.btn-submit').forEach(function(button) {
        button.addEventListener('click', function() {
            const plant_no = this.getAttribute('data-plant-no'); // plant_no 값 가져오기
            sendPlant(plant_no);
        });
    });
});

// 페이지 로드 시 초기화
function initializeMyKeywords() {
    resetMyKeywords(); // "마이 키워드" 초기화
}

// 핫 키워드 배경색 업데이트
function updateHotKeywordBackground(keyword) {
    const hotKeywords = document.querySelectorAll(".hot-keyowrd-section .tag-txt");
    hotKeywords.forEach(function (element) {
        if (element.textContent === keyword) {
            element.parentElement.style.outline = "1px solid var(--primary-green)";
            element.style.backgroundColor = "var(--primary-green)"; // 배경색을 회색으로 변경
            element.style.color = "var(--white)";
        }
    });
}

// 핫 키워드 배경색 복원
function restoreHotKeywordBackground(keyword) {
    const hotKeywords = document.querySelectorAll(".hot-keyowrd-section .tag-txt");
    hotKeywords.forEach(function (element) {
        if (element.textContent === keyword) {
            element.parentElement.style.outline = "1px solid var(--mid-gray)";
            element.style.backgroundColor = ""; // 배경색 복원
            element.style.color = "var(--primary-green)";
        }
    });
}

// 모든 핫 키워드 배경색 복원
function restoreAllHotKeywordBackgrounds() {
    const hotKeywords = document.querySelectorAll(".hot-keyowrd-section .tag-txt");
    hotKeywords.forEach(function (element) {
        element.parentElement.style.outline = "1px solid var(--mid-gray)";
        element.style.backgroundColor = ""; // 배경색 복원
        element.style.color = "var(--primary-green)";
    });
}

// "마이 키워드"에서 키워드 제거
function removeKeywordFromMyKeywords(keyword) {
    const myKeywords = loadMyKeywords();
    const updatedKeywords = myKeywords.filter((item) => item !== keyword);
    saveMyKeywords(updatedKeywords); // 수정된 목록을 로컬 스토리지에 저장
    updateMyKeywordsDisplay(); // "마이 키워드" 영역 업데이트
    restoreHotKeywordBackground(keyword); // "핫 키워드" 배경색 복원
}

// "마이 키워드" 초기화
function resetMyKeywords() {
    saveMyKeywords([]); // "마이 키워드" 목록을 초기화
    restoreAllHotKeywordBackgrounds(); // 모든 "핫 키워드" 배경색 복원
    updateMyKeywordsDisplay(); // "마이 키워드" 영역 업데이트
}

// 로컬 스토리지에서 "마이 키워드" 로드
function loadMyKeywords() {
    const storedKeywords = localStorage.getItem("myKeywords");
    return storedKeywords ? JSON.parse(storedKeywords) : [];
}

// 로컬 스토리지에 "마이 키워드" 저장
function saveMyKeywords(keywords) {
    localStorage.setItem("myKeywords", JSON.stringify(keywords));
}

// "마이 키워드" 영역을 업데이트
function updateMyKeywordsDisplay() {
    const myKeywords = loadMyKeywords(); // 로컬 스토리지에서 "마이 키워드" 로드
    const myKeywordsContainer = document.querySelector(".tag-container"); // "마이 키워드" 영역
    myKeywordsContainer.innerHTML = ""; // 기존 내용 지우기

    myKeywords.forEach(function (keyword) {
        const li = document.createElement("li");
        li.classList.add("tag");

        const span = document.createElement("span");
        span.classList.add("tag-txt");
        span.textContent = keyword;

        const closeSpan = document.createElement("span");
        closeSpan.classList.add("tag-close");
        closeSpan.textContent = "x";

        // "x" 아이콘 클릭 이벤트 리스너
        closeSpan.addEventListener("click", function () {
            removeKeywordFromMyKeywords(keyword); // "마이 키워드"에서 제거
        });

        li.appendChild(span);
        li.appendChild(closeSpan);

        myKeywordsContainer.appendChild(li);
        updateHotKeywordBackground(keyword); // 핫 키워드 배경색 업데이트
    });
}

// 키워드를 "마이 키워드"에 추가 또는 제거
function toggleKeywordInMyKeywords(keyword) {
    const myKeywords = loadMyKeywords();
    if (myKeywords.includes(keyword)) {
        removeKeywordFromMyKeywords(keyword); // 이미 존재하면 제거
    } else {
        addKeywordToMyKeywords(keyword); // 존재하지 않으면 추가
    }
}

// 키워드를 "마이 키워드"에 추가
function addKeywordToMyKeywords(keyword) {
    const myKeywords = loadMyKeywords();
    if (!myKeywords.includes(keyword)) {
        myKeywords.push(keyword);
        saveMyKeywords(myKeywords); // 로컬 스토리지에 저장
        updateMyKeywordsDisplay(); // "마이 키워드" 영역 업데이트
    }
}



// ============================================================================================================




function sendKeywords() {
    var myKeywords = loadMyKeywords(); // 로컬 스토리지에서 "마이 키워드" 로드

    // 선택한 키워드가 없는 경우 처리
    if (myKeywords.length === 0) {
        alert('키워드를 선택하세요.');
        return;
    }

    console.log('Sending keywords:', myKeywords); // 전송할 키워드 출력

    // 서버로 데이터를 보냅니다.
    fetch('/aiplant/save_user_keywords', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ user_keywords: myKeywords }) // 사용자 키워드를 JSON 배열 형식으로 전송
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        console.log('Server response:', data); // 서버 응답 출력
        /*alert('키워드가 성공적으로 전송되었습니다.');*/
        console.log('Received array:', data); // 완성된 배열 출력

        // predict 함수 호출 및 inputData로 완성된 배열 전달
        predict(data);
    })
    .catch(error => {
        console.error('Error sending keywords:', error);
        alert('키워드 전송에 실패했습니다.');
    });
}

// 예측 요청을 보내는 함수
function predict(inputData) {
    console.log("Sending input data to server:", JSON.stringify({ input_data: inputData }));  // 입력 데이터 확인 (JSON 형식)
    
    fetch('http://127.0.0.1:5000/predict', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ input_data: inputData })  // 데이터를 JSON 형식으로 변환
    })
    .then(response => response.json())
    .then(data => {
        console.log("식물 번호:", data);  // 서버 응답 확인
        const predictionResult = data.prediction;
        /*alert('식물 번호 : ' + predictionResult);*/
        // displayPrediction(predictionResult);
      
        // 예측 결과를 서버에 전송
        savePredictionToServer(predictionResult);
        console.log("predictionResult : " + predictionResult)
        
        
    })
    .catch(error => console.error('Error:', error));
}

// 예측 값을 서버로 저장하는 함수
function savePredictionToServer(prediction) {
    $.ajax({
        url: '/aiplant/savePrediction', // 예측 값을 서버로 보낼 엔드포인트
        type: 'POST', // POST 요청
        contentType: 'application/json',
        data: JSON.stringify({ prediction: prediction }), // 예측 값 전송
        success: function(response) {
            console.log("savePredictionToServer prediction: " + prediction); // 서버 응답 확인
            
            window.location.href = "/aiplant/aiplant_result.do";
        },
        error: function(xhr, status, error) {
            console.error('Error:', error);
        }
    });
}




// 예측 결과를 HTML에 표시하는 함수
/*function displayPrediction(prediction) {
   console.log("prediction : " + prediction)
    document.getElementById('predictionResult').innerText = `추천된 식물 ID: ${prediction}`;
    // 추가적으로 추천된 식물의 정보를 표시하는 로직을 여기에 추가합니다.
}*/
// 페이지 로드 시 이벤트 리스너 등록
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('recommendButton').addEventListener('click', function() {
        sendKeywords();
    });
});




// ============================================================================================================



// ---------------------------------------------------------------------------------------------------

// 내 식물로 등록
function sendPlant(plant_no, user_id) {
    $.ajax({
        type: "POST",
        url: "/aiplant/saveplant",
        contentType: "application/json",
        data: JSON.stringify({
            user_id: user_id,
            plant_no: plant_no
        }),
        success: function(response) {
            console.log("내 식물로 등록되었습니다.");
            alert("내 식물로 등록되었습니다.");
            window.location.href = "/aiplant/aiplant_list.do"; // 페이지 리다이렉션
        },
        error: function(xhr, status, error) {
            console.error(xhr.responseText);
        }
    });
}

// ai 식물 삭제
function sendPlant2(plant_no, user_id) {
    $.ajax({
        type: "POST",
        url: "/aiplant/deleteplant",
        contentType: "application/json",
        data: JSON.stringify({
            user_id: user_id,
            plant_no: plant_no
        }),
        success: function(response) {
            console.log("식물이 삭제되었습니다.");
            alert("식물이 삭제되었습니다.");
            window.location.href = "/aiplant/aiplant_list.do"; // 페이지 리다이렉션
        },
        error: function(xhr, status, error) {
            console.error(xhr.responseText);
        }
    });
}


// ----- 검색창 -----


document.addEventListener("DOMContentLoaded", function () {
    // 기존 코드는 여기에 있음

    // 검색창 이벤트 리스너 추가
    const searchBar = document.querySelector(".search-bar");
    searchBar.addEventListener("keypress", function (event) {
        if (event.key === "Enter") {
            const keyword = searchBar.value.trim(); // 입력된 키워드 가져오기
            if (keyword !== "") {
                addKeywordToMyKeywords(keyword); // 마이 키워드에 추가
                searchBar.value = ""; // 검색창 비우기
            }
        }
    });

    // 검색 버튼 클릭 이벤트 리스너 추가
    const searchButton = document.querySelector(".ic-search");
    searchButton.addEventListener("click", function () {
        const keyword = searchBar.value.trim(); // 입력된 키워드 가져오기
        if (keyword !== "") {
            addKeywordToMyKeywords(keyword); // 마이 키워드에 추가
            searchBar.value = ""; // 검색창 비우기
        }
    });

    // 기존 코드는 여기에 있음
});

// 키워드를 "마이 키워드"에 추가
function addKeywordToMyKeywords(keyword) {
    const myKeywords = loadMyKeywords();
    const hotKeywords = document.querySelectorAll(".hot-keyowrd-section .tag-txt");
    let isHotKeyword = false;
    hotKeywords.forEach(function (element) {
        if (element.textContent === keyword) {
            isHotKeyword = true;
            return;
        }
    });
    if (!isHotKeyword || myKeywords.includes(keyword)) {
        // 핫 키워드가 아니거나 이미 추가된 키워드인 경우 메시지 표시
        document.querySelector(".search-error").style.display = "block";
        setTimeout(function() {
            document.querySelector(".search-error").style.display = "none";
        }, 3000); // 3초 후 메시지 숨기기
        return;
    }
    myKeywords.push(keyword);
    saveMyKeywords(myKeywords); // 로컬 스토리지에 저장
    updateMyKeywordsDisplay(); // "마이 키워드" 영역 업데이트
    updateHotKeywordBackground(keyword); // 핫 키워드 배경색 업데이트
}

