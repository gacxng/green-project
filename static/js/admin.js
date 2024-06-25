   // 회원
function toggleSelectAll(checkbox) {
    var checkboxes = document.querySelectorAll('.table-user input[type="checkbox"]'); // 모든 체크박스를 선택
    checkboxes.forEach(function(chk) {
        chk.checked = checkbox.checked; // 전체 체크박스 상태를 변경
    });
}

$(document).ready(function() {
    $('#data-select-all').on('click', function() {
        console.log("Toggle select all clicked."); // 이벤트 처리 확인
    });
});


function updateSelectAll() {
    var allCheckbox = document.querySelector('.table-user input[type="checkbox"][data-select-all]');
    var checkboxes = Array.from(document.querySelectorAll('.table-user input[type="checkbox"]')).slice(1); // Ignore the first checkbox (select all)
    var allChecked = checkboxes.every(chk => chk.checked);
    var someChecked = checkboxes.some(chk => chk.checked);

    if (allChecked) {
        allCheckbox.indeterminate = false;
        allCheckbox.checked = true;
    } else if (someChecked) {
        allCheckbox.indeterminate = true;
        allCheckbox.checked = false;
    } else {
        allCheckbox.indeterminate = false;
        allCheckbox.checked = false;
    }
}


    // 회원 삭제
function delListUser() {
    // 선택된 회원 번호 수집
    var selectedUserNos = [];
    var checkboxes = document.querySelectorAll('.table-user input[type="checkbox"]');

    checkboxes.forEach(function (chk, index) {
        if (chk.checked && index > 0) { // 첫 번째 체크박스는 (전체 선택) 무시
            var userNo = chk.closest('tr').querySelector('td:nth-child(2)').textContent; // 두 번째 컬럼을 기준으로 user_no 가져오기
            selectedUserNos.push(userNo);
        }
    });

    if (selectedUserNos.length > 0) {
        // AJAX 요청으로 선택된 회원 삭제
        $.ajax({
            url: '/manage/deleteUsers', // 회원 삭제를 위한 백엔드 엔드포인트
            type: 'POST',
            data: JSON.stringify({ userNos: selectedUserNos }), // user_no를 JSON 객체로 전송
            contentType: 'application/json',
            success: function (response) {
                alert('선택된 회원이 성공적으로 삭제되었습니다.');
                location.reload(); // 페이지 새로고침
            },
            error: function (xhr, status, error) {
                alert('선택된 회원을 삭제하는 데 실패했습니다.');
                console.error(error);
            },
        });
    } else {
        alert('삭제할 회원을 선택해주세요.');
    }
}



// ----------------------------------------------------------------------------------------------------------------------------------------------------------



// 전체 선택/해제
function toggleSelectAllPosts(checkbox) {
    var checkboxes = document.querySelectorAll('.table-post input[type="checkbox"]');
    checkboxes.forEach(function(chk) {
        chk.checked = checkbox.checked;
    });
}

// 전체 선택/해제 업데이트
function updateSelectAllPosts() {
    var allCheckbox = document.querySelector('.table-post input[type="checkbox"][data-select-all2]');
    var checkboxes = Array.from(document.querySelectorAll('.table-post input[type="checkbox"]')).slice(1);
    var allChecked = checkboxes.every(chk => chk.checked);
    var someChecked = checkboxes.some(chk => chk.checked);

    if (allChecked) {
        allCheckbox.indeterminate = false;
        allCheckbox.checked = true;
    } else if (someChecked) {
        allCheckbox.indeterminate = true;
        allCheckbox.checked = false;
    } else {
        allCheckbox.indeterminate = false;
        allCheckbox.checked = false;
    }
}

// 게시물 삭제
function delListPosts() {
    var selectedPostNos = [];
    var checkboxes = document.querySelectorAll('.table-post input[type="checkbox"]');

    checkboxes.forEach(function(chk, index) {
        if (chk.checked && index > 0) { // 첫 번째 체크박스는 전체 선택
            var postNo = chk.closest('tr').querySelector('td:nth-child(2)').textContent; // 게시물 번호 추출
            selectedPostNos.push(postNo);
        }
    });

    if (selectedPostNos.length > 0) {
        // AJAX 요청으로 선택된 게시물 삭제
        $.ajax({
            url: '/manage/deletePosts', // 게시물 삭제를 위한 엔드포인트
            type: 'POST',
            data: JSON.stringify({ postNos: selectedPostNos }),
            contentType: 'application/json',
            success: function (response) {
                alert('선택된 게시물이 성공적으로 삭제되었습니다.');
                location.reload(); // 페이지 새로고침
            },
            error: function (xhr, status, error) {
                alert('선택된 게시물을 삭제하는 데 실패했습니다.');
                console.error(error);
            },
        });
    } else {
        alert('삭제할 게시물을 선택해주세요.');
    }
}

$(document).ready(function() {
    $('.table-post input[data-select-all2]').on('click', function() {
        toggleSelectAllPosts(this);
    });
    $('.table-post input[type="checkbox"]').on('change', updateSelectAllPosts);
});

   // 게시판 삭제
function delBoard(button) {
    var boardNo = button.closest('tr').querySelector('td').textContent.trim(); // 게시판 번호 추출
    console.log("Deleting board:", boardNo); // 추출된 게시판 번호 로그

    if (isNaN(parseInt(boardNo))) { // 게시판 번호가 유효한지 확인
        alert("유효한 게시판 번호를 찾을 수 없습니다."); // 오류 메시지
        return;
    }

    var confirmation = confirm("선택된 게시판을 삭제하시겠습니까?");
    if (confirmation) {
        $.ajax({
            url: `/manage/board/${parseInt(boardNo)}`, // 엔드포인트 문자열 보간
            type: 'DELETE', // HTTP DELETE 메서드
            contentType: 'application/json', // 콘텐츠 유형
            success: function() {
                alert('선택된 게시판이 삭제되었습니다.'); // 성공 메시지
                location.reload(); // 성공 시 페이지 새로고침
            },
            error: function(xhr, status, error) {
                alert('게시판 삭제에 실패했습니다.'); // 오류 메시지
                console.error("Error:", error); // 오류 정보 로그
            }
        });
    }
}




   // 게시판 추가
document.addEventListener('DOMContentLoaded', function() {
    // 게시판 생성 함수
    function createBoard() {
        var boardNameInput = document.querySelector('#newBoardName'); // 입력 필드 선택
        if (!boardNameInput) {
            alert("게시판 이름 입력 필드를 찾을 수 없습니다."); // 필드가 없는 경우 경고
            return; // 함수 종료
        }

        var boardName = boardNameInput.value.trim(); // 공백 제거 후 게시판 이름 추출
        if (boardName === '') {
            alert('게시판 이름을 입력해주세요.'); // 입력이 빈 문자열인 경우
            return; // 함수 종료
        }

        if (boardName.length > 16) {
            alert('게시판 이름은 16자 이하여야 합니다.'); // 입력이 16자를 초과한 경우
            boardNameInput.focus(); // 입력 필드에 포커스 설정
            return; // 함수 종료
        }

        var boardDto = {
            board_name: boardName
        };

        // AJAX 요청으로 게시판 생성
        $.ajax({
            url: '/manage/addBoard',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(boardDto),
            success: function() {
                alert('게시판이 생성되었습니다.');
                boardNameInput.value = ''; // 입력 필드 초기화
                location.reload(); // 페이지 새로고침
            },
            error: function(xhr, status, error) {
                alert('게시판 생성에 실패했습니다.');
                console.error('Error:', error);
            }
        });
    }

    // 생성 버튼에 클릭 이벤트 추가
    var createBoardButton = document.querySelector('button[onclick="createBoard()"]');
    if (createBoardButton) {
        createBoardButton.addEventListener('click', createBoard);
    }
});

// ----------------------------------------------------------------------------------------------------------------------------------------------------------

   // 상품
// 전체 선택/선택 해제 기능
function toggleSelectAll2(checkbox) {
    var checkboxes = document.querySelectorAll('.table-product input[type="checkbox"]'); // 모든 체크박스 선택
    checkboxes.forEach(function(chk) {
        chk.checked = checkbox.checked; // 전체 선택/선택 해제 상태를 설정
    });
}

// 전체 선택 상태 업데이트
function updateSelectAll2() {
    var allCheckbox = document.querySelector('.table-product input[type="checkbox"]'); // 전체 선택 체크박스
    var checkboxes = Array.from(document.querySelectorAll('.table-product input[type="checkbox"]')).slice(1); // 첫 번째 체크박스는 전체 선택
    var allChecked = checkboxes.every(chk => chk.checked); // 모두 선택된 경우
    var someChecked = checkboxes.some(chk => chk.checked); // 일부만 선택된 경우

    if (allChecked) {
        allCheckbox.indeterminate = false; // 모든 체크박스가 선택된 경우
        allCheckbox.checked = true;
    } else if (someChecked) {
        allCheckbox.indeterminate = true; // 일부만 선택된 경우
        allCheckbox.checked = false;
    } else {
        allCheckbox.indeterminate = false; // 모두 선택되지 않은 경우
        allCheckbox.checked = false;
    }
}

// 선택된 상품 삭제
function delList() {
    var selectedProductNos = []; // 선택된 상품 번호 수집
    var checkboxes = document.querySelectorAll('.table-product input[type="checkbox"]'); // 모든 체크박스 선택

    checkboxes.forEach(function (chk, index) {
        if (chk.checked && index > 0) { // 첫 번째 체크박스(전체 선택) 무시
            var productNo = chk.closest('tr').querySelector('td:nth-child(2)').textContent.trim(); // 상품 번호 추출
            selectedProductNos.push(productNo);
        }
    });

    if (selectedProductNos.length > 0) { // 선택된 상품이 하나 이상인 경우
        $.ajax({
            url: '/manage/deleteProducts', // 상품 삭제 엔드포인트
            type: 'POST', // HTTP 메서드
            contentType: 'application/json', // 콘텐츠 유형
            data: JSON.stringify({ productNos: selectedProductNos }), // JSON 객체로 변환하여 전송
            success: function() {
                alert('선택된 상품이 성공적으로 삭제되었습니다.');
                location.reload(); // 성공 시 페이지 새로고침
            },
            error: function(xhr, status, error) {
                alert('선택된 상품을 삭제하는 데 실패했습니다.');
                console.error("Error:", error); // 오류 정보 로그
            }
        });
    } else {
        alert('삭제할 상품을 선택해주세요.'); // 선택된 상품이 없는 경우
    }
}

// 페이지 로드 시 이벤트 처리
$(document).ready(function() {
    $('#data-select-all').on('click', function() {
        console.log("Toggle select all clicked."); // 이벤트 처리 확인
    });
});


// --------------------------------------------------------------------------

// 검색 결과 없을 시 검색창에 focus
$(document).ready(function() {
    $('#data-select-all').on('click', function() {
        console.log("Toggle select all clicked."); // 이벤트 처리 확인
    });

    // 검색 결과 없을 시 검색창에 focus 설정
    if (document.getElementById('noResultsMessage')) {
        document.getElementById('searchKeyword').focus();
    }
});

// ----------------------------------------------------------------------------

   // 키워드
// 전체 선택/선택 해제 기능
function toggleSelectAll3(checkbox) {
    var checkboxes = document.querySelectorAll('.table-keyword input[type="checkbox"]'); // 모든 체크박스 선택
    checkboxes.forEach(function(chk) {
        chk.checked = checkbox.checked; // 전체 선택/선택 해제 상태를 설정
    });
}

// 전체 선택 상태 업데이트
function updateSelectAll3() {
    var allCheckbox = document.querySelector('.table-keyword input[type="checkbox"]'); // 전체 선택 체크박스
    var checkboxes = Array.from(document.querySelectorAll('.table-keyword input[type="checkbox"]')).slice(1); // 첫 번째 체크박스는 전체 선택
    var allChecked = checkboxes.every(chk => chk.checked); // 모두 선택된 경우
    var someChecked = checkboxes.some(chk => chk.checked); // 일부만 선택된 경우

    if (allChecked) {
        allCheckbox.indeterminate = false; // 모든 체크박스가 선택된 경우
        allCheckbox.checked = true;
    } else if (someChecked) {
        allCheckbox.indeterminate = true; // 일부만 선택된 경우
        allCheckbox.checked = false;
    } else {
        allCheckbox.indeterminate = false; // 모두 선택되지 않은 경우
        allCheckbox.checked = false;
    }
}

// 선택된 키워드 삭제
function delKeyword() {
    var selectedKeywordNos = []; // 선택된 상품 번호 수집
    var checkboxes = document.querySelectorAll('.table-keyword input[type="checkbox"]'); // 모든 체크박스 선택

    checkboxes.forEach(function (chk, index) {
        if (chk.checked && index > 0) { // 첫 번째 체크박스(전체 선택) 무시
            var keywordNo = chk.closest('tr').querySelector('td:nth-child(2)').textContent.trim(); // 상품 번호 추출
            selectedKeywordNos.push(keywordNo);
        }
    });

    if (selectedKeywordNos.length > 0) { // 선택된 상품이 하나 이상인 경우
        $.ajax({
            url: '/manage/deleteKeywords', // 상품 삭제 엔드포인트
            type: 'POST', // HTTP 메서드
            contentType: 'application/json', // 콘텐츠 유형
            data: JSON.stringify({ keywordNos: selectedKeywordNos }), // JSON 객체로 변환하여 전송
            success: function() {
                alert('선택된 상품이 성공적으로 삭제되었습니다.');
                location.reload(); // 성공 시 페이지 새로고침
            },
            error: function(xhr, status, error) {
                alert('선택된 상품을 삭제하는 데 실패했습니다.');
                console.error("Error:", error); // 오류 정보 로그
            }
        });
    } else {
        alert('삭제할 키워드를 선택해주세요.'); // 선택된 상품이 없는 경우
    }
}

// 페이지 로드 시 이벤트 처리
$(document).ready(function() {
    $('#data-select-all').on('click', function() {
        console.log("Toggle select all clicked."); // 이벤트 처리 확인
    });
});



// --------------------------------------------------------------------------------------------

   // 게시판 삭제
function delKeygory(button) {
    var keygoryNo = button.closest('tr').querySelector('td').textContent.trim(); // 게시판 번호 추출
    console.log("Deleting board:", keygoryNo); // 추출된 게시판 번호 로그

    if (isNaN(parseInt(keygoryNo))) { // 게시판 번호가 유효한지 확인
        alert("유효한 keygory 번호를 찾을 수 없습니다."); // 오류 메시지
        return;
    }

    var confirmation = confirm("선택된 keygory 삭제하시겠습니까?");
    if (confirmation) {
        $.ajax({
            url: `/manage/keygory/${parseInt(keygoryNo)}`, // 엔드포인트 문자열 보간
            type: 'DELETE', // HTTP DELETE 메서드
            contentType: 'application/json', // 콘텐츠 유형
            success: function() {
                alert('선택된 keygory 삭제되었습니다.'); // 성공 메시지
                location.reload(); // 성공 시 페이지 새로고침
            },
            error: function(xhr, status, error) {
                alert('게시판 삭제에 실패했습니다.'); // 오류 메시지
                console.error("Error:", error); // 오류 정보 로그
            }
        });
    }
}




   // 게시판 추가
document.addEventListener('DOMContentLoaded', function() {
    // 게시판 생성 함수
    function createKeygory() {
        var keygoryNameInput = document.querySelector('#newKeygoryName'); // 입력 필드 선택
        if (!keygoryNameInput) {
            alert("keygory 이름 입력 필드를 찾을 수 없습니다."); // 필드가 없는 경우 경고
            return; // 함수 종료
        }

        var keygoryName = keygoryNameInput.value.trim(); // 공백 제거 후 게시판 이름 추출
        if (keygoryName === '') {
            alert('keygory 이름을 입력해주세요.'); // 입력이 빈 문자열인 경우
            return; // 함수 종료
        }

        if (keygoryName.length > 16) {
            alert('keygory 이름은 16자 이하여야 합니다.'); // 입력이 16자를 초과한 경우
            keygoryNameInput.focus();
            return; // 함수 종료
        }

        var keygoryDto = {
            keygory_name: keygoryName
        };

        // AJAX 요청으로 게시판 생성
        $.ajax({
            url: '/manage/addKeygory',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(keygoryDto),
            success: function() {
                alert('게시판이 생성되었습니다.');
                keygoryNameInput.value = ''; // 입력 필드 초기화
                location.reload(); // 페이지 새로고침
            },
            error: function(xhr, status, error) {
                alert('keygory 생성에 실패했습니다.');
                console.error('Error:', error);
            }
        });
    }

    // 생성 버튼에 클릭 이벤트 추가
    var createKeygoryButton = document.querySelector('button[onclick="createKeygory()"]');
    if (createKeygoryButton) {
        createKeygoryButton.addEventListener('click', createKeygory);
    }
});




// -------------------------------------------------------------------------------------------------------------





