// 내 식물 업데이트
function updateMyplant(frm) {
    let thisFrm = frm.closest('form');
    if (thisFrm && thisFrm.name === 'form_myplant_update') {
        let msg = "내 식물을 수정 하시겠습니까?";
        if (confirm(msg)) {
            thisFrm.submit();
            return;
        }
        window.alert("업데이트에 실패했습니다.");
    	window.location.href="/main";
    }
}

// 내 식물 삭제
function deleteMyplant(plant_no) {
	let msg = "내 식물을 삭제 하시겠습니까?";
	if (confirm(msg)) {
		window.location.href="/myplant/deleteMyplant.do?plant_no=" + plant_no;
		return;
	}
}

// 내 식물 등록
function confirmMyplant() {
	let frm = form_input_myplant;
	
	if(frm.plant_name.value == "") {
		window.alert("식물명이 입력되지 않았습니다.");
		frm.plant_name.focus();
	}
	else if(frm.plant_nickname.value == "") {
		window.alert("애칭이 입력되지 않았습니다.");
		frm.plant_nickname.focus();
	}
	else if(frm.first_date.value == "") {
		window.alert("처음 만난 날이 입력되지 않았습니다.");
		frm.first_date.focus();
	}
	else if(frm.last_water.value == "") {
		window.alert("마지막으로 물 준 날이 입력되지 않았습니다.");
		frm.last_water.focus();
	}
	else {
		frm.submit();
	}
}


function uploadFile() {
    var formData = new FormData(document.getElementById('uploadForm'));
    fetch('/upload', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            var timestamp = new Date().getTime();
            document.getElementById('uploadedImage').src = data.filePath + '?' + timestamp;
        } else {
            alert('Upload failed');
        }
    })
    .catch(error => console.error('Error:', error));
}


function resetForm() {
	window.alert("정보를 지우고 처음부터 다시 입력합니다.");
	frm_myplant.reset();
	frm_myplant.plant_name.focus();
}


function confirmProduct() {
    let frm = document.forms['form_input_product'];
    let urlPattern = /^(ftp|http|https):\/\/[^ "]+$/;

    if(frm.product_name.value == "") {
        window.alert("상품명이 입력되지 않았습니다.");
        frm.product_name.focus();
        return;
    }
    if(frm.url.value == "") {
        window.alert("판매처 링크 주소가 입력되지 않았습니다.");
        frm.url.focus();
        return;
    }
    if(!urlPattern.test(frm.url.value)) {
        window.alert("유효하지 않은 URL 주소입니다.");
        frm.url.focus();
        return;
    }
    if(isNaN(frm.category_no.value) || frm.category_no.value == "") {
        window.alert("카테고리 번호는 숫자로 입력되어야 합니다.");
        frm.category_no.focus();
        return;
    }
    if(frm.manufacturer.value == "") {
        window.alert("판매처가 입력되지 않았습니다.");
        frm.manufacturer.focus();  // 'frm.manufacture' 를 'frm.manufacturer' 로 수정
        return;
    }
    if(frm.img_path.value == "") {
        window.alert("상품 이미지 주소가 입력되지 않았습니다.");
        frm.img_path.focus();
        return;
    }
    if(!urlPattern.test(frm.img_path.value)) {
        window.alert("유효하지 않은 이미지 URL 주소입니다.");
        frm.img_path.focus();
        return;
    }

    // 서버에서 다음 product_no 값을 가져와서 설정
    fetch('/getProductNextNo')
        .then(response => response.json())
        .then(data => {
            const productNo = data.productNextNo;
            document.getElementById('product_no').value = productNo;
            
            // 폼 제출
            frm.submit();
            window.alert("상품을 추가하였습니다.");
        })
        .catch(error => {
            console.error('Error fetching product number:', error);
            window.alert("상품 번호를 가져오는 데 실패했습니다.");
        });
}


// 상품 업데이트
function updateProduct(frm) {
    let thisFrm = frm.closest('form');
    if (thisFrm && thisFrm.name === 'form_product_update') {
        let msg = "상품을 수정 하시겠습니까?";
        if (confirm(msg)) {
            thisFrm.submit();
            return;
        }
        window.alert("업데이트에 실패했습니다.");
       window.location.href="/product.do";
    }
}

// 게시판 업데이트
function updateBoard(frm) {
    let thisFrm = frm.closest('form');
    if (thisFrm && thisFrm.name === 'form_board_update') {
        let msg = "게시판을 수정 하시겠습니까?";
        if (confirm(msg)) {
            thisFrm.submit();
            return;
        }
        window.alert("업데이트에 실패했습니다.");
       window.location.href="/board.do";
    }
}

// 키워드 업데이트
function updateKeyword(frm) {
    let thisFrm = frm.closest('form');
    if (thisFrm && thisFrm.name === 'form_keyword_update') {
        let msg = "키워드를 수정 하시겠습니까?";
        if (confirm(msg)) {
            thisFrm.submit();
            /*window.alert("업데이트에 성공했습니다.");*/
            return;
        }
        /*window.alert("업데이트에 실패했습니다.");*/
       window.location.href="/keyword1.do";
    }
}

// 키워드 등록
function confirmKeyword() {
    let frm = form_input_keyword;

    if(frm.keyword.value == "") {
        window.alert("키워드가 입력되지 않았습니다.");
        frm.keyword.focus();
    }
    else if(isNaN(frm.category_no.value) || frm.category_no.value == "") {
        window.alert("카테고리 번호는 숫자로 입력되어야 합니다.");
        frm.category_no.focus();
    }
    else {
        frm.submit();
        window.alert("키워드를 추가하였습니다.");
    }
}

// Keygory 업데이트
function updateKeygory(frm) {
    let thisFrm = frm.closest('form');
    if (thisFrm && thisFrm.name === 'form_keygory_update') {
        let msg = "keygory을 수정 하시겠습니까?";
        if (confirm(msg)) {
            thisFrm.submit();
            return;
        }
        window.alert("업데이트에 실패했습니다.");
       window.location.href="/keygory.do";
    }
}



// 게임캐릭터 닉네임 생성
function saveCharacterNickName() {
	// window.alert("submit")
	const frm = document.forms["form_charNickname"];
	const nick = frm.elements["character_nickname"];
	const regex = /^[a-zA-Z가-힣ㄱ-ㅎ]{1,8}$/;
	
	const resultMsg = document.querySelector('.result-msg-container');
	if(nick.value == "") {
		window.alert("닉네임이 입력되지 않았습니다.");
        nick.focus();
	}
	else if(!regex.test(nick.value)) {
		window.alert("한/영문 8글자 이내로 입력해 주세요.");
		nick.focus();
	}
	else {
		let msg = "닉네임을 저장하시겠습니까?";
		if (confirm(msg)) {
			frm.submit();
		}
	}
}