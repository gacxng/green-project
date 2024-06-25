// 로그인
function checkLogin() {
    var userId = document.login.username.value;
    var password = document.login.password.value;
    if(userId == ""){
        var messageElement = document.getElementById("messageId");
        messageElement.innerText = "아이디를 입력하세요.";
        login.username.focus();
    }
    else if(password == ""){
        var messageElement = document.getElementById("messagePw");
        messageElement.innerText = "비밀번호를 입력하세요.";
        login.password.focus();
    }
    else {
        login.submit();
    }
}

document.addEventListener('DOMContentLoaded', (event) => {
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            checkLogin();
        }
    });
});

var isIdChecked = false;
// 회원가입 아이디 중복확인
function checkDuplicate() {
    var userId = document.getElementById("user_id").value;
    var xhr = new XMLHttpRequest();
    var messageElement = document.getElementById("duplicateMessage");
	if(userId.trim() === ""){
		messageElement.innerText = "아이디를 입력해주세요";
	}
	else{
	    xhr.open("GET", "/login/checkDuplicate?user_id=" + userId, true);
	    xhr.onreadystatechange = function () {
	        if (xhr.readyState === 4 && xhr.status === 200) {
	            var response = xhr.responseText;
	            
	            if (response === "true") {
	                messageElement.innerText = "사용 가능한 아이디입니다.";
	                isIdChecked = true;
	                console.log("isId : " + isIdChecked);
	                document.getElementById("regist").onsubmit = function(){
	                    return true;
	                }
	            }
	            else {
	                messageElement.innerText = "이미 사용 중인 아이디입니다.";
	                isIdChecked = false;
	                console.log("isId : " + isIdChecked);
	                document.getElementById("regist").onsubmit = function(){
	                    alert("이미 사용중인 아이디입니다.");
	                    return false;
	                }
	            }
	        }
	    };
	    xhr.send();
	    document.getElementById("registButton").style.display="block";
	    return xhr.onreadystatechange;
	}
}

var isNickChecked = false;
// 닉네임 중복 체크
function checkDuplicateNick(){
    var user_nickname = document.getElementById("user_nickname").value;
    var xhr = new XMLHttpRequest();

    xhr.open("GET", "/login/checkDuplicateNick?user_nickname=" + user_nickname, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var response = xhr.responseText;
            var messageElement = document.getElementById("messageNick");
            if (response === "true") {
                isNickChecked = true;
                console.log("isNick : " + isNickChecked);
                messageElement.innerText = "사용 가능한 닉네임입니다";
            }
            else {
                messageElement.innerText = "이미 사용 중인 닉네임입니다.";
                isNickChecked = false;
                console.log("isNick : " + isNickChecked);
                document.getElementById("regist").onsubmit = function(){
                    alert("이미 사용 중인 닉네임입니다..");
                    return false;
                }
            }
        }
    };
    xhr.send();
    return xhr.onreadystatechange;
}

// 비밀번호찾기 정보 확인
function checkUserInfo(){
    var userId = document.getElementById("user_id").value;
    var userName = document.getElementById("user_name").value;
    var email = document.getElementById("email").value;
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/checkUserInfo?user_id=" + userId + "&user_name=" + userName + "&email=" + email, true);
    xhr.onreadystatechange = function(){
        if(xhr.readyState === 4 && xhr.status === 200){
            var response = xhr.responseText;
            var messageElement = document.getElementById("duplicateMessage");
            if(response === "true"){
                messageElement.innerText = "";
                document.getElementById("checkEmail").onsubmit = function(){
                    return true;
                }
            }
        }
        else{
            messageElement.innerText = "사용자 정보가 없습니다.";
            document.getElementById("checkEmail").onsubmit = function(){
                alert("입력한 정보가 일치하지 않습니다.");
                return false;
            }
        }
    };
    xhr.send();
    return xhr.onreadystatechange;
}

// 이메일 형식 확인
function isEmailValid(email){
    var emailRegex = /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/;
    return emailRegex.test(email);
}

function validateForm() {
    var password1 = document.getElementById("user_pw").value;
    var password2 = document.getElementById("user_pwCheck").value;
    var email = document.getElementById("email").value;

    if (regist.user_id.value === "") {
        window.alert("유저아이디를 입력하세요");
        regist.user_id.focus();
        return;
    } else if (document.getElementById("duplicateMessage").innerText === "이미 사용 중인 아이디입니다.") {
        window.alert("이미 사용중인 아이디입니다.");
        regist.user_id.focus();
        return;
    } else if (regist.user_pw.value === "") {
        var messageElement = document.getElementById("messagePw");
        messageElement.innerText = "비밀번호를 입력하세요.";
        regist.user_pw.focus();
        return;
    } else if (regist.user_pwCheck.value === "") {
        var messageElement = document.getElementById("messagePwChk");
        messageElement.innerText = "비밀번호확인을 입력하세요.";
        regist.user_pwCheck.focus();
        return;
    } else if (password1 !== password2) {
        window.alert("비밀번호가 일치하지 않습니다.");
        return false;
    } else if (regist.user_nickname.value === "") {
        var messageElement = document.getElementById("messageNick");
        messageElement.innerText = "닉네임을 입력하세요.";
        regist.user_nickname.focus();
        return;
    } else if (document.getElementById("messageNick").innerText === "이미 사용 중인 닉네임입니다.") {
        window.alert("이미 사용 중인 닉네임입니다.");
        regist.user_nickname.focus();
        return;
    } else if (!isEmailValid(email)) {
        var messageElement = document.getElementById("messageEmail");
        messageElement.innerText = "올바른 이메일 형식이 아닙니다.";
        regist.email.focus();
        return;
    } else if (document.getElementById("messageEmail").innerText === "이미 사용 중인 이메일입니다.") {
        window.alert("이미 사용 중인 이메일입니다.");
        regist.email.focus();
        return;
    } else if (!isIdChecked) {
        window.alert("아이디 중복확인 버튼을 눌러주세요");
        return;
    } else if (!isNickChecked) {
        window.alert("닉네임 중복확인 버튼을 눌러주세요");
        return;
    } else if (!isEmailChecked) {
        window.alert("이메일 중복확인 버튼을 눌러주세요");
        return;
    } else {
        regist.submit();
    }
}

// 아이디 찾기
function findId(){
    window.location.href="/find_id.do"
}


function chkFindPw(){
    var userName = document.find_pw.user_name.value;
    var userId = document.find_pw.user_id.value;
    var email = document.find_pw.email.value;
    if(userName == ""){
        var messageElement = document.getElementById("messageName");
        messageElement.innerText = "이름을 입력하세요.";
        find_pw.user_name.focus();
    }
    else if(userId == ""){
        var messageElement = document.getElementById("messageId");
        messageElement.innerText = "아이디를 입력하세요.";
        find_pw.user_id.focus();
    }
    else if(email == ""){
        var messageElement = document.getElementById("messageEmail");
        messageElement.innerText = "이메일을 입력하세요.";
        find_pw.email.focus();
    }
    else if (!isEmailValid(email)) {
        var messageElement = document.getElementById("messageEmail");
        messageElement.innerText = "올바른 이메일 형식이 아닙니다.";
        find_pw.email.focus();
    }
    else {
        find_pw.submit();
    }
}

// 비밀번호 변경팝업 띄우기
function ChgPwPopup(){
    var url = "/mypage/chgPw.do";
    var dec = "width=800px, height=700px";
    window.open(url, "비밀번호변경", dec);
}

function editInfo(){
    var email = document.getElementById("email").value;

    if (chgInfo.user_nickname.value === "") {
        var messageElement = document.getElementById("messageNick");
        messageElement.innerText = "닉네임을 입력하세요.";
        chgInfo.user_nickname.focus();
        return;
    } else if (document.getElementById("messageNick").innerText === "이미 사용 중인 닉네임입니다.") {
        window.alert("이미 사용 중인 닉네임입니다.");
        chgInfo.user_nickname.focus();
        return;
    } else if (!isEmailValid(email)) {
        var messageElement = document.getElementById("messageEmail");
        messageElement.innerText = "올바른 이메일 형식이 아닙니다.";
        chgInfo.email.focus();
        return;
    } else if (document.getElementById("messageEmail").innerText === "이미 사용 중인 이메일입니다.") {
        window.alert("이미 사용 중인 이메일입니다.");
        chgInfo.email.focus();
        return;
    } else if (!isNickChecked) {
        window.alert("닉네임 중복확인 버튼을 눌러주세요");
        return;
    } else if (!isEmailChecked) {
        window.alert("이메일 중복확인 버튼을 눌러주세요");
        return;
    } else {
        chgInfo.submit();
    }
}

// 비밀번호변경 빈칸찾기
function chgPw(){
    var errorMessage = document.getElementById("errorMessage");
    if(changePw.prePw.value==""){
        errorMessage.innerText="비밀번호를 입력해주세요";
        changePw.prePw.focus();
    }
    else if(changePw.nextPw.value==""){
        errorMessage.innerText="새 비밀번호를 입력해주세요";
        changePw.nextPw.focus();
    }
    else if(changePw.nextPwChk.value==""){
        errorMessage.innerText="새 비밀번호를 입력해주세요";
        changePw.nextPwChk.focus();
    }
    else{
        changePw.submit();
    }
}

// 비밀번호변경 취소
function cancel_del(){
    window.location.href="/mypage/editUserInfo.do";
}

// 회원탈퇴 빈칸찾기
function del_user(){
    var errorMessage = document.getElementById("errorMessage");
    if(exitService.user_id.value==""){
        errorMessage.innerText="아이디를 입력하세요";
        exitService.user_id.focus();
    }
    else if(exitService.password.value==""){
        errorMessage.innerText="비밀번호를 입력하세요";
        exitService.password.focus();
    }
    else {
        exitService.submit();
    }
}

// 비밀번호찾기 비밀번호변경
function chkBlankPw(){
    var errorMessage = document.getElementById("errorMessage");
    var nextPw = document.getElementById("nextPw").value;
    var nextPwChk = document.getElementById("nextPwChk").value;

    if(nextPw == ""){
        errorMessage.innerText="비밀번호를 입력하세요";
        chkPwForm.nextPw.focus();
    }
    else if(nextPwChk == ""){
        errorMessage.innerText="비밀번호 확인을 입력하세요";
        chkPwForm.newPwChk.focus();
    }
    else{
        chkPwForm.submit();
    }
}

var isEmailChecked = false;
// 이메일 중복확인
function checkDuplicateEmail(){
    var email = document.getElementById("email").value;
    var xhr = new XMLHttpRequest();

    xhr.open("GET", "/login/checkDuplicateEmail?email=" + email, true);
    xhr.onreadystatechange = function () {
    if(email.trim() ===""){
        var messageElement = document.getElementById("messageEmail");
        messageElement.innerText = "올바른 형식을 입력해주세요.";
        return regist.email.focus();
    }
    else if (!isEmailValid(email)) {
        var messageElement = document.getElementById("messageEmail");
        messageElement.innerText = "올바른 이메일 형식이 아닙니다.";
        regist.email.focus();
        return;
    }
    else {
            if (xhr.readyState === 4 && xhr.status === 200) {
                var response = xhr.responseText;
                var messageElement = document.getElementById("messageEmail");
                if (response === "true") {
                    messageElement.innerText = "사용 가능한 이메일입니다";
                    isEmailChecked = true;
                    console.log("isEmail : " + isEmailChecked);
                    document.getElementById("regist").onsubmit = function(){
                        return true;
                    }
                }
                else {
                    messageElement.innerText = "이미 사용 중인 이메일입니다.";
                    isEmailChecked = false;
                    console.log("isEmail : " + isEmailChecked);
                    document.getElementById("regist").onsubmit = function(){
                        alert("이미 사용 중인 이메일입니다.");
                        return false;
                    }
                }
            }
        };
    }
    xhr.send();
    return xhr.onreadystatechange;
}

function insertCharacter(){
    window.location.href="/login/insertChar.do?character_no="+character_no;
}

function maxLengthCheck(object) {
    if (object.value.length > object.maxLength) {
        object.value = object.value.slice(0, object.maxLength);
    }
}

function previewImage(event){
    var reader = new FileReader();
    reader.onload = function() {
        var output = document.getElementById('profileImg');
        output.src = reader.result;
    }
    reder.readAsDataURL(event.target.files[0]);
}

// 게시물 삭제
function deletePost() {
    var selectedPostNos = [];
    var checkboxes = document.querySelectorAll('.table-post input[type="checkbox"]');

    checkboxes.forEach(function(chk, index) {
        if (chk.checked && index > 0) { // 첫 번째 체크박스는 전체 선택
            var postNo = chk.closest('tr').querySelector('td.post_no').textContent.trim(); // 게시물 번호 추출
            selectedPostNos.push(postNo);
        }
    });

    if (selectedPostNos.length > 0) {
        // AJAX 요청으로 선택된 게시물 삭제
        $.ajax({
            url: '/mypage/deletePost.do', // 게시물 삭제를 위한 엔드포인트
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

// 댓글 삭제
function deleteReply() {
    var selectedReplyNos = [];
    var checkboxes = document.querySelectorAll('.table-reply input[type="checkbox"]');

    checkboxes.forEach(function(chk, index) {
        if (chk.checked && index > 0) { // 첫 번째 체크박스는 전체 선택
            var replyNo = chk.closest('tr').querySelector('td.reply_no').textContent.trim(); // 게시물 번호 추출
            selectedReplyNos.push(replyNo);
        }
    });

    if (selectedReplyNos.length > 0) {
        // AJAX 요청으로 선택된 게시물 삭제
        $.ajax({
            url: '/mypage/deleteReply.do', // 게시물 삭제를 위한 엔드포인트
            type: 'POST',
            data: JSON.stringify({ replyNos: selectedReplyNos }),
            contentType: 'application/json',
            success: function (response) {
                alert('선택된 댓글이 성공적으로 삭제되었습니다.');
                location.reload(); // 페이지 새로고침
            },
            error: function (xhr, status, error) {
                alert('선택된 댓글을 삭제하는 데 실패했습니다.');
                console.error(error);
            },
        });
    } else {
        alert('삭제할 댓글을 선택해주세요.');
    }
}

// 게시물 전체 선택/해제
function SelectAllPosts(checkbox) {
    var checkboxes = document.querySelectorAll('.table-post input[type="checkbox"]');
    checkboxes.forEach(function(chk) {
        chk.checked = checkbox.checked;
    });
}
// 댓글 전체 선택/해제
 function SelectAllReplies(checkbox) {
     var checkboxes = document.querySelectorAll('.table-reply input[type="checkbox"]');
     checkboxes.forEach(function(chk) {
         chk.checked = checkbox.checked;
     });
 }
 
  function checkIdForm(){
    var userName = document.find_id.user_name.value;
    var email = document.find_id.email.value;
    if(userName == "") {
        var messageElement = document.getElementById("messageName");
        messageElement.innerText = "이름을 입력하세요";
        find_id.userName.focus();
    }
    else if (email == "") {
        var messageElement = document.getElementById("messageEmail");
        messageElement.innerText = "이메일을 입력하세요";
        find_id.email.focus();
    }
    else if (!isEmailValid(email)) {
        var messageElement = document.getElementById("messageEmail");
        messageElement.innerText = "올바른 이메일 형식이 아닙니다.";
        find_id.email.focus();
        return;
    }
    else {
        find_id.submit();
    }
 }