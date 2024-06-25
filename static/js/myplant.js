function getPage(page) {
	var sndurl = '/myplant/' + page;
	// window.alert(sndurl);
	var _jsonInfo = '{"plant_no":"1", "plant_name":"다육식물", "plant_nickname":"다육이", "img_name":"no-img.png", "org_img_name":"no-img.png"}';

		$.ajax({
	    type: 'get',
	    async: false,
	    url: sndurl,
	    contentType: 'application/json',
	    data: { jsonInfo: _jsonInfo },
	    success: function(data, textStatus) {
	        var responseData = JSON.parse(data);
	        var temp = '<div class="page-list-container">';
	        responseData.list.forEach(function(dto) {
				if(dto === null) {
					temp += '<p class="error-msg-container">표시할 식물이 없습니다...</p>';
				}
	            temp += '<div class="page">';
	            temp += '<div class="card type-2" onclick="sendId(' + '"' + dto.myplant_id + '"' + ')">';
	            temp += '<div class="card-img img-container">';
	            temp += '<img src="/img/myplant/' + dto.img_name + '" alt="' + dto.org_img_name + '" />';
	            temp += '</div>';
	            temp += '<p><span class="keyword">#오늘의 가든</span><span class="keyword">#홍콩야자</span></p>';
	            temp += '<p class="plant-nickname">' + dto.plant_nickname + '</p>';
	            temp += '<p class="plant-name">' + dto.plant_name + '</p>';
	            temp += '<input type="hidden" value="' + dto.plant_no + '">';
	            temp += '</div>';
	            temp += '</div>';
	        });
	        temp += '</div>';
	        temp += '<div class="page-container">';
	        temp += '<ul class="page">';
	        
	        if (responseData.page && responseData.page.existPrevPage) {
	            temp += '<li class="btn-prev"><span class="btn-prev active" onclick="getPage(' + (responseData.page.startPage - 1) + ')">이전</span></li>';
	        }
	        
	        for (var number = responseData.page.startPage; number <= responseData.page.endPage; number++) {
	            if (responseData.page.page == number) {
	                temp += '<li><span class="btn-page" onclick="getPage(' + number + ')">' + number + '</span></li>';
	            } else {
	                temp += '<li><span class="btn-page" onclick="getPage(' + number + ')">' + number + '</span></li>';
	            }
	        }
	        if (responseData.page && responseData.page.existNextPage) {
	            temp += '<li class="btn-next active"><span class="btn-next active" onclick="getPage(' + (responseData.page.endPage + 1) + ')">다음</a></li>';
	        } else {
	            temp += '<li class="btn-next">다음</li>';
	        }
	        
	        temp += '</ul>';
	        temp += '</div>';
	        // window.alert(temp);
	       	$("#myplant_json").html(temp);
	    },
		error:function(data,textStatus) { // 에러 발생 시
			alert("에러가 발생했습니다.");
		},
		complete:function(data,textStatus) {
			console.log(data);
			location.reload(true);
			// $('#myplant_json').load(location.href+'#myplant_json');
		} // 전송 성공 시
	});
}

function sendFetchUrl(url) {
	fetch(url)
		.then(response => {
			if (!response.ok) {
	            throw new Error('Network response was NOT OK ' + response.statusText);
	        }
	        console.log(response);
	        return response.json();
	    })
	    .then(data => {
	        // 응답 처리 로직
	        console.log(data);
	    })
	    .catch(error => {
	        // 에러 처리 로직
	        console.error('Fetch error:', error);
	    });
}

// 내 식물 찜하기 (fetch 방식 사용))
function addMyplantFavorite(plant_no) {
	// window.alert(plant_no);
	const favoriteBtn = document.querySelector('#btn-' + plant_no);
	let favorite = favoriteBtn.classList.contains('active') ? 1 : 0;
    const newFavorite = favorite === 0 ? 1 : 0;
    
	if(newFavorite === 1) {
		favoriteBtn.classList.add('active');
		favoriteBtn.innerHTML = '찜 해제';
	} else {
		favoriteBtn.classList.remove('active');
		favoriteBtn.innerHTML = '찜 하기';
	}
	sendFetchUrl(`/myplant/myplant_list/${plant_no}/${newFavorite}`);
	sendFetchUrl(`/myplant/myplant_list/?plant_no=${plant_no}&favorite=${newFavorite}`);
}

// myplant 특정 섹션으로 이동
function scrollToMyplant(plantNo) {
	const url = `/myplant/myplant_list?id=myplant_${plantNo}`;
	// window.alert(url);
    window.location.href = url;
}

document.addEventListener('DOMContentLoaded', function() {
	// 내 식물 등록하기
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); // January is 0!
    var yyyy = today.getFullYear();
    today = yyyy + '-' + mm + '-' + dd;

    // input 요소의 max 속성 설정
    document.getElementById('myplant_first_date').setAttribute('max', today);
    document.getElementById('myplant_lastwater_date').setAttribute('max', today);
});